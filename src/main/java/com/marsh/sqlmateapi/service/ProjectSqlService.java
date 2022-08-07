package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.ProjectSqlAddReq;
import com.marsh.sqlmateapi.controller.request.ProjectSqlQueryReq;
import com.marsh.sqlmateapi.domain.ProjectSql;
import com.marsh.sqlmateapi.mapper.ProjectSqlMapper;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectSqlService {

    private final ProjectSqlMapper projectSqlMapper;

    public ProjectSqlService(ProjectSqlMapper projectSqlMapper) {
        this.projectSqlMapper = projectSqlMapper;
    }

    public void getSql() {

    }

    public List<ProjectSql> listSql(ProjectSqlQueryReq req) {
        return projectSqlMapper.selectList(new QueryWrapper<ProjectSql>().lambda()
                .eq(ProjectSql::getProjectId, req.getProjectId()));
    }

    public void addSql(ProjectSqlAddReq req) {
        var sql = BeanUtil.transfer(req, ProjectSql.class);
        projectSqlMapper.insert(sql);
    }
}
