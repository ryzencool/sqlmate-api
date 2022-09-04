package com.marsh.sqlmateapi.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.ExecuteSqlReq;
import com.marsh.sqlmateapi.domain.ProjectDataSource;
import com.marsh.sqlmateapi.mapper.ProjectDataSourceMapper;
import com.marsh.sqlmateapi.utils.SqlExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExecuteService {

    private final SqlExecutor sqlExecutor;
    private final ProjectDataSourceMapper projectDataSourceMapper;


    public ExecuteService(SqlExecutor sqlExecutor, ProjectDataSourceMapper projectDataSourceMapper) {
        this.sqlExecutor = sqlExecutor;
        this.projectDataSourceMapper = projectDataSourceMapper;
    }

    public Object execute(ExecuteSqlReq req) {
        var sql = req.getSql();
        var ds = projectDataSourceMapper
                .selectOne(new QueryWrapper<ProjectDataSource>().lambda()
                        .eq(ProjectDataSource::getProjectId, req.getProjectId())
                        .eq(ProjectDataSource::getDbType, req.getDbType()));

        sql = sql.strip();
        var res = sqlExecutor.sendSql(sql, ds.getName(), ds.getDbType());
        var resObj = JSONObject.parseObject(res.body());
        var code = (String)resObj.get("code");
        var data = resObj.getJSONObject("data");
        var result = data.get("result");
        return result;
    }
}
