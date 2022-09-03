package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.QueryOptimizerReq;
import com.marsh.sqlmateapi.domain.ProjectDataSource;
import com.marsh.sqlmateapi.mapper.ProjectDataSourceMapper;
import com.marsh.sqlmateapi.utils.SqlExecutor;
import org.springframework.stereotype.Service;

@Service
public class OptimizeService {

    private final ProjectDataSourceMapper projectDataSourceMapper;

    public OptimizeService(ProjectDataSourceMapper projectDataSourceMapper) {
        this.projectDataSourceMapper = projectDataSourceMapper;
    }

    public String optimize(QueryOptimizerReq req) {
        var pds = projectDataSourceMapper.selectOne(new QueryWrapper<ProjectDataSource>()
                .lambda().eq(ProjectDataSource::getDbType, req.getDbType())
                .eq(ProjectDataSource::getProjectId, req.getProjectId()));
        var pgRes = SqlExecutor.optimize(req.getSql(), pds.getName(), pds.getDbType(), pds.getUsername(), pds.getPassword(), pds.getHost(), pds.getPort());

        return pgRes.body();

    }
}
