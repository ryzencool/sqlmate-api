package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marsh.sqlmateapi.controller.request.AddProjectReq;
import com.marsh.sqlmateapi.controller.request.ProjectEditReq;
import com.marsh.sqlmateapi.controller.request.ProjectQueryReq;
import com.marsh.sqlmateapi.controller.request.PublicProjectQueryReq;
import com.marsh.sqlmateapi.controller.response.ProjectStatResp;
import com.marsh.sqlmateapi.domain.ProjectInfo;
import com.marsh.sqlmateapi.domain.ProjectSql;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.mapper.*;
import com.marsh.zutils.util.BeanUtil;
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

    private final JdbcTemplate jdbcTemplate;
    public ProjectService(ProjectInfoMapper projectInfoMapper, ProjectSqlMapper projectSqlMapper, TableInfoMapper tableInfoMapper, TableColumnMapper tableColumnMapper, UserInfoMapper userInfoMapper, JdbcTemplate jdbcTemplate) {
        this.projectInfoMapper = projectInfoMapper;
        this.projectSqlMapper = projectSqlMapper;
        this.tableInfoMapper = tableInfoMapper;
        this.tableColumnMapper = tableColumnMapper;
        this.userInfoMapper = userInfoMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void addProject(AddProjectReq req, Integer userId) {
        var project = BeanUtil.transfer(req, ProjectInfo.class);
        project.setOwnerId(userId);
        projectInfoMapper.insert(project);

        var user = userInfoMapper.selectById(userId);
        var projectName = req.getName();
        var schemaSql = String.format("CREATE SCHEMA IF NOT EXISTS %s AUTHORIZATION %s", projectName + user.getPhone(), "user_" + user.getPhone());
        jdbcTemplate.execute(schemaSql);

    }

    public List<ProjectInfo> listProject(ProjectQueryReq req) {
        return projectInfoMapper.selectList(new QueryWrapper<ProjectInfo>().lambda().eq(req.getUserId() != null, ProjectInfo::getOwnerId, req.getUserId()));
    }

    public ProjectInfo getProject(ProjectQueryReq req) {
        return projectInfoMapper.selectOne(new QueryWrapper<ProjectInfo>().lambda().eq(ProjectInfo::getId, req.getId()));
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
