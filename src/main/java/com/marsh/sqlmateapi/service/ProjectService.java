package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marsh.sqlmateapi.config.ExeDBProperties;
import com.marsh.sqlmateapi.constant.DBTypeConstant;
import com.marsh.sqlmateapi.controller.request.AddProjectReq;
import com.marsh.sqlmateapi.controller.request.ProjectEditReq;
import com.marsh.sqlmateapi.controller.request.ProjectQueryReq;
import com.marsh.sqlmateapi.controller.request.PublicProjectQueryReq;
import com.marsh.sqlmateapi.controller.response.ProjectStatResp;
import com.marsh.sqlmateapi.domain.*;
import com.marsh.sqlmateapi.mapper.*;
import com.marsh.sqlmateapi.mapper.param.UserInfoGetParam;
import com.marsh.sqlmateapi.utils.RemoteCallUtil;
import com.marsh.sqlmateapi.utils.SqlExecutor;
import com.marsh.zutils.util.BeanUtil;
import com.marsh.zutils.util.UUIDUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ProjectService {

    private final ExeDBProperties exeDBProperties;
    private final ProjectInfoMapper projectInfoMapper;

    private final ProjectSqlMapper projectSqlMapper;

    private final TableInfoMapper tableInfoMapper;

    private final TableColumnMapper tableColumnMapper;

    private final UserInfoMapper userInfoMapper;

    private final ProjectDataSourceMapper projectDataSourceMapper;

    private final DatabaseUserMapper databaseUserMapper;


    private final SqlExecutor sqlExecutor;

    private final TableIndexMapper tableIndexMapper;

    public ProjectService(ExeDBProperties exeDBProperties,
                          ProjectInfoMapper projectInfoMapper,
                          ProjectSqlMapper projectSqlMapper,
                          TableInfoMapper tableInfoMapper,
                          TableColumnMapper tableColumnMapper,
                          UserInfoMapper userInfoMapper,
                          ProjectDataSourceMapper projectDataSourceMapper,
                          DatabaseUserMapper databaseUserMapper, SqlExecutor sqlExecutor, TableIndexMapper tableIndexMapper) {
        this.exeDBProperties = exeDBProperties;
        this.projectInfoMapper = projectInfoMapper;
        this.projectSqlMapper = projectSqlMapper;
        this.tableInfoMapper = tableInfoMapper;
        this.tableColumnMapper = tableColumnMapper;
        this.userInfoMapper = userInfoMapper;
        this.projectDataSourceMapper = projectDataSourceMapper;
        this.databaseUserMapper = databaseUserMapper;
        this.sqlExecutor = sqlExecutor;
        this.tableIndexMapper = tableIndexMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addProject(AddProjectReq req, Integer userId) {
        var project = BeanUtil.transfer(req, ProjectInfo.class);
        project.setOwnerId(userId);
        project.setCreateTime(LocalDateTime.now());
        project.setUpdateTime(LocalDateTime.now());
        project.setUpdateId(userId);
        projectInfoMapper.insert(project);

        var pj = projectInfoMapper
                .selectOne(new QueryWrapper<ProjectInfo>().lambda()
                        .eq(ProjectInfo::getName, req.getName())
                        .eq(ProjectInfo::getOwnerId, userId));
        createProjectDatabase(userId, pj);
    }

    private void createProjectDatabase(Integer userId, ProjectInfo pj) {
        var dbs = databaseUserMapper.selectList(new QueryWrapper<DatabaseUser>().lambda().eq(DatabaseUser::getUserId, userId));

        for (DatabaseUser db : dbs) {
            var dbName = "db_" + UUIDUtil.cleanLowerUUID();
            if (Objects.equals(db.getDbType(), DBTypeConstant.POSTGRES)) {
                createPostgres(pj, db, dbName);

            } else if (Objects.equals(db.getDbType(), DBTypeConstant.MYSQL)) {
                createMysql(pj, db, dbName);
            }
        }
    }

    private void createMysql(ProjectInfo pj, DatabaseUser db, String dbName) {
        var url = String.format("jdbc:mysql://%s:%d/%s",
                exeDBProperties.getMysql().getHost(),
                exeDBProperties.getMysql().getPort(),
                dbName);
        var createDb = String.format("create database if not exists %s character set utf8", dbName);
        try (var createDbRes = sqlExecutor.sendMysqlMainSql(createDb)) {
            RemoteCallUtil.handleResponse(createDbRes);
        }
        var grantPermission = String.format("GRANT select, insert, create, alter, drop, delete, update, execute on %s.* to '%s'@'%%' identified by '%s' with grant option", dbName, db.getUsername(), db.getPassword());
        try (var grantRes = sqlExecutor.sendMysqlMainSql(grantPermission)) {
            RemoteCallUtil.handleResponse(grantRes);
        }
        projectDataSourceMapper.insert(ProjectDataSource.builder()
                .projectId(pj.getId())
                .dbType(db.getDbType())
                .name(dbName)
                .password(db.getPassword())
                .username(db.getUsername())
                .url(url)
                .host(exeDBProperties.getMysql().getHost())
                .port(exeDBProperties.getMysql().getPort())
                .build());
    }

    private void createPostgres(ProjectInfo pj, DatabaseUser db, String dbName) {
        var url = String.format("jdbc:postgresql://%s:%d/%s?currentSchema=%s&useSSL=false",
                exeDBProperties.getPg().getHost(),
                exeDBProperties.getPg().getPort(),
                exeDBProperties.getPg().getDatabase(),
                dbName);
        var schemaSql = String.format("CREATE SCHEMA IF NOT EXISTS %s AUTHORIZATION %s", dbName, db.getUsername());
        try (var pgRes = sqlExecutor.sendPgMainSql(schemaSql)) {
            RemoteCallUtil.handleResponse(pgRes);
        }
        var grantSql = String.format("GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA %s TO %s", dbName, db.getUsername());
        try (var grantRes = sqlExecutor.sendPgMainSql(grantSql)) {
            RemoteCallUtil.handleResponse(grantRes);
        }

        projectDataSourceMapper.insert(ProjectDataSource.builder()
                .projectId(pj.getId())
                .dbType(db.getDbType())
                .name(dbName)
                .password(db.getPassword())
                .username(db.getUsername())
                .url(url)
                .host(exeDBProperties.getPg().getHost())
                .port(exeDBProperties.getPg().getPort())
                .build());
    }

    public List<ProjectInfo> listProject(ProjectQueryReq req) {
        return projectInfoMapper.selectList(new QueryWrapper<ProjectInfo>().lambda().eq(req.getUserId() != null, ProjectInfo::getOwnerId, req.getUserId()));
    }

    public ProjectInfo getProject(ProjectQueryReq req) {
        var project = projectInfoMapper.selectOne(new QueryWrapper<ProjectInfo>().lambda().eq(ProjectInfo::getId, req.getId()));
        return project;
    }

    public ProjectStatResp getProjectDetail(Integer projectId) {
        var project = projectInfoMapper.selectById(projectId);
        var createUser = userInfoMapper.getUserInfo(UserInfoGetParam.builder().id(project.getCreateId()).build());
        var updateUser = userInfoMapper.getUserInfo(UserInfoGetParam.builder().id(project.getUpdateId()).build());

        var sqlCount = projectSqlMapper.selectCount(new QueryWrapper<ProjectSql>()
                .lambda().eq(ProjectSql::getProjectId, projectId));
        var tableCount = tableInfoMapper.selectCount(new QueryWrapper<TableInfo>().lambda()
                .eq(TableInfo::getProjectId, projectId));
        var columnCount = tableColumnMapper.selectCount(new QueryWrapper<TableColumn>().lambda().eq(TableColumn::getProjectId, projectId));
        var indexCount = tableIndexMapper.selectCount(new QueryWrapper<TableIndex>().lambda().eq(TableIndex::getProjectId, projectId));
        return ProjectStatResp.builder()
                .createUser(createUser)
                .updateUser(updateUser)
                .projectInfo(project)
                .sqlCount(sqlCount)
                .tableCount(tableCount)
                .columnCount(columnCount)
                .indexCount(indexCount)
                .build();

    }

    public Page<ProjectInfo> pagePublic(PublicProjectQueryReq req) {
        return projectInfoMapper.selectPage(req.page(), new QueryWrapper<ProjectInfo>()
                .lambda()
                .eq(ProjectInfo::getIsPublic, true)
                .like(req.getName() != null, ProjectInfo::getName, req.getName()));
    }

    public List<ProjectInfo> listFavoriteProject(ProjectQueryReq req, Integer userId) {
        return projectInfoMapper.selectList(new QueryWrapper<ProjectInfo>().lambda().eq(ProjectInfo::getOwnerId, userId));
    }

    public void updateProject(ProjectEditReq req, Integer userId) {
        var project = BeanUtil.transfer(req, ProjectInfo.class);
        project.setUpdateId(userId);
        project.setUpdateTime(LocalDateTime.now());
        projectInfoMapper.updateById(project);
    }
}
