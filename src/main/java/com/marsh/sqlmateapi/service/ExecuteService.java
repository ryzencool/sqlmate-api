package com.marsh.sqlmateapi.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.ExecuteSqlReq;
import com.marsh.sqlmateapi.domain.ProjectDataSource;
import com.marsh.sqlmateapi.mapper.ProjectDataSourceMapper;
import com.marsh.sqlmateapi.utils.SqlExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExecuteService {


    private final ProjectDataSourceMapper projectDataSourceMapper;


    public ExecuteService( ProjectDataSourceMapper projectDataSourceMapper) {
        this.projectDataSourceMapper = projectDataSourceMapper;
    }

    public Object execute(ExecuteSqlReq req) {
        var sql = req.getSql();
        var ds = projectDataSourceMapper
                .selectOne(new QueryWrapper<ProjectDataSource>().lambda()
                        .eq(ProjectDataSource::getProjectId, req.getProjectId())
                        .eq(ProjectDataSource::getDbType, req.getDbType()));

        sql = sql.strip();
        var res = SqlExecutor.sendSql(sql, ds.getName(), ds.getDbType());
        var resObj = JSONObject.parseObject(res.body());
        var code = (String)resObj.get("code");
        var data = resObj.getJSONObject("data");
        var result = data.get("result");
        return result;
    }
}
