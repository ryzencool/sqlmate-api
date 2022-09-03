package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marsh.sqlmateapi.config.ExeDBProperties;
import com.marsh.sqlmateapi.controller.request.AddProjectReq;
import com.marsh.sqlmateapi.controller.request.ProjectEditReq;
import com.marsh.sqlmateapi.controller.request.ProjectQueryReq;
import com.marsh.sqlmateapi.controller.request.PublicProjectQueryReq;
import com.marsh.sqlmateapi.controller.response.ProjectStatResp;
import com.marsh.sqlmateapi.domain.*;
import com.marsh.sqlmateapi.mapper.*;
import com.marsh.sqlmateapi.utils.SqlExecutor;
import com.marsh.zutils.util.BeanUtil;
import com.marsh.zutils.util.UUIDUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    public ProjectService(ExeDBProperties exeDBProperties,
                          ProjectInfoMapper projectInfoMapper,
                          ProjectSqlMapper projectSqlMapper,
                          TableInfoMapper tableInfoMapper,
                          TableColumnMapper tableColumnMapper,
                          UserInfoMapper userInfoMapper,
                          ProjectDataSourceMapper projectDataSourceMapper,
                          DatabaseUserMapper databaseUserMapper) {
        this.exeDBProperties = exeDBProperties;
        this.projectInfoMapper = projectInfoMapper;
        this.projectSqlMapper = projectSqlMapper;
        this.tableInfoMapper = tableInfoMapper;
        this.tableColumnMapper = tableColumnMapper;
        this.userInfoMapper = userInfoMapper;
        this.projectDataSourceMapper = projectDataSourceMapper;
        this.databaseUserMapper = databaseUserMapper;
    }

    @Transactional
    public void addProject(AddProjectReq req, Integer userId) {
        var project = BeanUtil.transfer(req, ProjectInfo.class);
        project.setOwnerId(userId);
        projectInfoMapper.insert(project);

        var pj = projectInfoMapper.selectOne(new QueryWrapper<ProjectInfo>().lambda().eq(ProjectInfo::getName, req.getName()).eq(ProjectInfo::getOwnerId, userId));
        var dbs = databaseUserMapper.selectList(new QueryWrapper<DatabaseUser>().lambda().eq(DatabaseUser::getUserId, userId));
        for (DatabaseUser db : dbs) {
            var dbName = "db_" + UUIDUtil.cleanLowerUUID();
            if (db.getDbType() == 2) {
                var url = String.format("jdbc:postgresql://%s:%d/%s?currentSchema=%s&useSSL=false",
                        exeDBProperties.getPg().getHost(),
                        exeDBProperties.getPg().getPort(),
                        exeDBProperties.getPg().getDatabase(),
                        dbName);
                var schemaSql = String.format("CREATE SCHEMA IF NOT EXISTS %s AUTHORIZATION %s", dbName, db.getUsername());
                var pgRes = SqlExecutor.sendSql(schemaSql, "pgMain", 2);
                var grantSql = String.format("grant select, insert, update, delete on all tables in schema %s public to %s", dbName, db.getUsername());
                var grantRes = SqlExecutor.sendSql(grantSql, "pgMain", 2);
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

            } else if (db.getDbType() == 1) {
                var url = String.format("jdbc:mysql://%s:%d/%s",
                        exeDBProperties.getMysql().getHost(),
                        exeDBProperties.getMysql().getPort(),
                        dbName);
                var createDb = String.format("create database if not exists %s character set utf8", dbName);
                var createDbRes = SqlExecutor.sendSql(createDb, "mysqlMain", 1);
                var grantPermission = String.format("GRANT select, insert, create, alter, drop, delete, update, execute on %s.* to '%s'@'%%' identified by '%s' with grant option", dbName, db.getUsername(), db.getPassword());
                var grantRes = SqlExecutor.sendSql(grantPermission, "mysqlMain", 1);
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
        }
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
        var sqlCount = projectSqlMapper.selectCount(new QueryWrapper<ProjectSql>()
                .lambda().eq(ProjectSql::getProjectId, projectId));
        var tableCount = tableInfoMapper.selectCount(new QueryWrapper<TableInfo>().lambda()
                .eq(TableInfo::getProjectId, projectId));

        return ProjectStatResp.builder().projectInfo(project).sqlCount(sqlCount).tableCount(tableCount).build();

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
        projectInfoMapper.updateById(project);
    }
}
