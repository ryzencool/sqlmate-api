package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marsh.sqlmateapi.controller.request.AddProjectReq;
import com.marsh.sqlmateapi.controller.request.ProjectEditReq;
import com.marsh.sqlmateapi.controller.request.ProjectQueryReq;
import com.marsh.sqlmateapi.controller.request.PublicProjectQueryReq;
import com.marsh.sqlmateapi.controller.response.ProjectStatResp;
import com.marsh.sqlmateapi.domain.*;
import com.marsh.sqlmateapi.helper.RoutingDataSource;
import com.marsh.sqlmateapi.mapper.*;
import com.marsh.zutils.util.BeanUtil;
import com.marsh.zutils.util.UUIDUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectInfoMapper projectInfoMapper;

    private final ProjectSqlMapper projectSqlMapper;

    private final TableInfoMapper tableInfoMapper;

    private final TableColumnMapper tableColumnMapper;

    private final UserInfoMapper userInfoMapper;

    private final ProjectDataSourceMapper projectDataSourceMapper;

    private final DatabaseUserMapper databaseUserMapper;

    private final JdbcTemplate jdbcTemplate;

    private final RoutingDataSource routingDataSource;


    public ProjectService(ProjectInfoMapper projectInfoMapper, ProjectSqlMapper projectSqlMapper, TableInfoMapper tableInfoMapper, TableColumnMapper tableColumnMapper, UserInfoMapper userInfoMapper, ProjectDataSourceMapper projectDataSourceMapper, DatabaseUserMapper databaseUserMapper, JdbcTemplate jdbcTemplate, RoutingDataSource routingDataSource) {
        this.projectInfoMapper = projectInfoMapper;
        this.projectSqlMapper = projectSqlMapper;
        this.tableInfoMapper = tableInfoMapper;
        this.tableColumnMapper = tableColumnMapper;
        this.userInfoMapper = userInfoMapper;
        this.projectDataSourceMapper = projectDataSourceMapper;
        this.databaseUserMapper = databaseUserMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.routingDataSource = routingDataSource;
    }

    @Transactional
    public void addProject(AddProjectReq req, Integer userId) {
        var project = BeanUtil.transfer(req, ProjectInfo.class);
        project.setOwnerId(userId);
        projectInfoMapper.insert(project);

        var pj = projectInfoMapper.selectOne(new QueryWrapper<ProjectInfo>().lambda().eq(ProjectInfo::getName, req.getName()).eq(ProjectInfo::getOwnerId, userId));
        var dbs = databaseUserMapper.selectList(new QueryWrapper<DatabaseUser>().lambda().eq(DatabaseUser::getUserId, userId));
        for (DatabaseUser db : dbs) {
            if (db.getDbType() == 2) {
                var dbName = "db_" + UUIDUtil.cleanLowerUUID();
                var schemaSql = String.format("CREATE SCHEMA IF NOT EXISTS %s AUTHORIZATION %s", dbName, db.getUsername());
                var url = String.format("jdbc:postgresql://localhost:55435/sqlmate?currentSchema=%s&useSSL=false", dbName);
                jdbcTemplate.execute(schemaSql);
                projectDataSourceMapper.insert(ProjectDataSource.builder()
                                .projectId(pj.getId())
                                .dbType(db.getDbType())
                                .name(dbName)
                                .password(db.getPassword())
                                .username(db.getUsername())
                                .url(url)
                        .build());

            }
        }
    }

    public List<ProjectInfo> listProject(ProjectQueryReq req) {
        return projectInfoMapper.selectList(new QueryWrapper<ProjectInfo>().lambda().eq(req.getUserId() != null, ProjectInfo::getOwnerId, req.getUserId()));
    }

    public ProjectInfo getProject(ProjectQueryReq req) {
        var project = projectInfoMapper.selectOne(new QueryWrapper<ProjectInfo>().lambda().eq(ProjectInfo::getId, req.getId()));
        var ds = projectDataSourceMapper
                .selectOne(new QueryWrapper<ProjectDataSource>().lambda()
                        .eq(ProjectDataSource::getProjectId, req.getId())
                        .eq(ProjectDataSource::getDbType, 2));
        routingDataSource.createAndSaveDataSource(ds.getName());
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
