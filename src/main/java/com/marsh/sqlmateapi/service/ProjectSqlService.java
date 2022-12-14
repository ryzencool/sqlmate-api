package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.ProjectSqlEditReq;
import com.marsh.sqlmateapi.controller.request.ProjectSqlQueryReq;
import com.marsh.sqlmateapi.domain.ProjectSql;
import com.marsh.sqlmateapi.mapper.ProjectSqlMapper;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectSqlService {

    private final ProjectSqlMapper projectSqlMapper;

    public ProjectSqlService(ProjectSqlMapper projectSqlMapper) {
        this.projectSqlMapper = projectSqlMapper;
    }

    public List<ProjectSql> listSql(ProjectSqlQueryReq req) {
        return projectSqlMapper.selectList(new QueryWrapper<ProjectSql>().lambda()
                .eq(ProjectSql::getProjectId, req.getProjectId())
                .and(req.getCondition() != null,
                        it ->
                                it.like(req.getCondition() != null, ProjectSql::getNote, req.getCondition())
                                        .like(req.getCondition() != null, ProjectSql::getSql, req.getCondition())
                                        ).orderByDesc(ProjectSql::getCreateTime)

        );
    }

    public void addSql(ProjectSqlEditReq req, Integer userId) {
        var sql = BeanUtil.transfer(req, ProjectSql.class);
        sql.setCreateTime(LocalDateTime.now());
        sql.setCreateId(userId);
        projectSqlMapper.insert(sql);
    }

    public void deleteSql(ProjectSqlEditReq req, Integer userId) {
        projectSqlMapper.deleteById(req.getId());

    }

    public void updateSql(ProjectSqlEditReq req, Integer userId) {
        var sql = BeanUtil.transfer(req, ProjectSql.class);
        sql.setUpdateTime(LocalDateTime.now());
        sql.setUpdateId(userId);
        projectSqlMapper.updateById(sql);

    }
}
