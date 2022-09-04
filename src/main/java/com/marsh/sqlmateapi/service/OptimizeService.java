package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.QueryOptimizerReq;
import com.marsh.sqlmateapi.domain.ProjectDataSource;
import com.marsh.sqlmateapi.mapper.ProjectDataSourceMapper;
import com.marsh.sqlmateapi.utils.SqlExecutor;
import org.springframework.stereotype.Service;

@Service
public class OptimizeService {

    private final SqlExecutor sqlExecutor;
    private final ProjectDataSourceMapper projectDataSourceMapper;

    public OptimizeService(SqlExecutor sqlExecutor, ProjectDataSourceMapper projectDataSourceMapper) {
        this.sqlExecutor = sqlExecutor;
        this.projectDataSourceMapper = projectDataSourceMapper;
    }

    public String optimize(QueryOptimizerReq req) {
        var pds = projectDataSourceMapper.selectOne(new QueryWrapper<ProjectDataSource>()
                .lambda().eq(ProjectDataSource::getDbType, req.getDbType())
                .eq(ProjectDataSource::getProjectId, req.getProjectId()));
        var pgRes = sqlExecutor.optimize(req.getSql(), pds.getName(), pds.getDbType(), pds.getUsername(), pds.getPassword(), pds.getHost(), pds.getPort());

        return pgRes.body();

    }
}
