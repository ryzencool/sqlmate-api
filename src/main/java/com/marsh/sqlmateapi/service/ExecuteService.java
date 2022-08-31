package com.marsh.sqlmateapi.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.ExecuteSqlReq;
import com.marsh.sqlmateapi.domain.ProjectDataSource;
import com.marsh.sqlmateapi.helper.RoutingDataSourceContext;
import com.marsh.sqlmateapi.mapper.ProjectDataSourceMapper;
import com.marsh.zutils.exception.BaseBizException;
import com.marsh.zutils.exception.BaseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExecuteService {

    private final JdbcTemplate jdbcTemplate;

    private final ProjectDataSourceMapper projectDataSourceMapper;


    public ExecuteService(JdbcTemplate jdbcTemplate, ProjectDataSourceMapper projectDataSourceMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.projectDataSourceMapper = projectDataSourceMapper;
    }

    public Object execute(ExecuteSqlReq req) {
        var sql = req.getSql();
        var project = projectDataSourceMapper
                .selectOne(new QueryWrapper<ProjectDataSource>().lambda().eq(ProjectDataSource::getProjectId, req.getProjectId()).eq(ProjectDataSource::getDbType, 2));
        RoutingDataSourceContext.setRouteKey(project.getName());

        sql = sql.strip();
        Object result = null;
        try {
            if (sql.startsWith("select") || sql.startsWith("explain")) {
                result = jdbcTemplate.queryForList(sql);
                log.info("结果是: {}", JSONObject.toJSONString(result));
            } else if (sql.startsWith("insert") || sql.startsWith("update") || sql.startsWith("delete")) {
                result = jdbcTemplate.update(sql);
            } else {
                jdbcTemplate.execute(sql);
            }
        } catch (Exception e) {
            throw new BaseBizException(new BaseErrorCode("200000", e.getMessage()));
        }
        RoutingDataSourceContext.setRouteKey("master");

        return result;
    }
}
