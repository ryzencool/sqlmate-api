package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marsh.sqlmateapi.controller.request.AddProjectReq;
import com.marsh.sqlmateapi.controller.request.ProjectQueryReq;
import com.marsh.sqlmateapi.controller.request.PublicProjectQueryReq;
import com.marsh.sqlmateapi.controller.response.ProjectStatResp;
import com.marsh.sqlmateapi.domain.ProjectInfo;
import com.marsh.sqlmateapi.domain.ProjectSql;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.mapper.ProjectInfoMapper;
import com.marsh.sqlmateapi.mapper.ProjectSqlMapper;
import com.marsh.sqlmateapi.mapper.TableColumnMapper;
import com.marsh.sqlmateapi.mapper.TableInfoMapper;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectInfoMapper projectInfoMapper;

    private final ProjectSqlMapper projectSqlMapper;

    private final TableInfoMapper tableInfoMapper;

    private final TableColumnMapper tableColumnMapper;

    public ProjectService(ProjectInfoMapper projectInfoMapper, ProjectSqlMapper projectSqlMapper, TableInfoMapper tableInfoMapper, TableColumnMapper tableColumnMapper) {
        this.projectInfoMapper = projectInfoMapper;
        this.projectSqlMapper = projectSqlMapper;
        this.tableInfoMapper = tableInfoMapper;
        this.tableColumnMapper = tableColumnMapper;
    }

    public void AddProject(AddProjectReq req, Integer userId) {
        var project = BeanUtil.transfer(req, ProjectInfo.class);
        project.setOwnerId(userId);
        projectInfoMapper.insert(project);

    }

    public List<ProjectInfo> listProject(ProjectQueryReq req) {
        return projectInfoMapper.selectList(new QueryWrapper<ProjectInfo>().lambda().eq(req.getUserId() != null, ProjectInfo::getOwnerId, req.getUserId()));
    }

    public ProjectStatResp getProject(Integer projectId) {
        var project = projectInfoMapper.selectById(projectId);
        var sqlCount = projectSqlMapper.selectCount(new QueryWrapper<ProjectSql>()
                .lambda().eq(ProjectSql::getProjectId, projectId));
        var tableCount = tableInfoMapper.selectCount(new QueryWrapper<TableInfo>().lambda()
                .eq(TableInfo::getProjectId, projectId));

        return ProjectStatResp.builder().projectInfo(project).sqlCount(sqlCount).tableCount(tableCount).build();

    }

    public Page<ProjectInfo> pagePublic(PublicProjectQueryReq req) {
        return projectInfoMapper.selectPage(req.page(), new QueryWrapper<ProjectInfo>().lambda().like(req.getName() != null ,ProjectInfo::getName, req.getName()));
    }

    public List<ProjectInfo> listFavoriteProject(ProjectQueryReq req, Integer userId) {
        return projectInfoMapper.selectList(new QueryWrapper<ProjectInfo>().lambda().eq(ProjectInfo::getOwnerId, userId));
    }
}
