package com.marsh.sqlmateapi.service;

import com.marsh.sqlmateapi.controller.request.ConnectionCloseReq;
import com.marsh.sqlmateapi.controller.request.ConnectionCreateReq;
import com.marsh.sqlmateapi.helper.RoutingDataSource;
import com.marsh.sqlmateapi.mapper.ProjectDataSourceMapper;
import com.marsh.sqlmateapi.mapper.ProjectInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class DataSourceService {

    private final RoutingDataSource routingDataSource;

    private final ProjectInfoMapper projectInfoMapper;

    private final ProjectDataSourceMapper projectDataSourceMapper;



    public DataSourceService(RoutingDataSource routingDataSource, ProjectInfoMapper projectInfoMapper, ProjectDataSourceMapper projectDataSourceMapper) {
        this.routingDataSource = routingDataSource;
        this.projectInfoMapper = projectInfoMapper;
        this.projectDataSourceMapper = projectDataSourceMapper;
    }

    public void createConnection(ConnectionCreateReq req) {
        var ds = projectDataSourceMapper.selectById(req.getProjectId());
        routingDataSource.createAndSaveDataSource(ds.getName());
    }

    public void closeConnection(ConnectionCloseReq req) {
        var ds = projectDataSourceMapper.selectById(req.getProjectId());
        routingDataSource.closeDataSource(ds.getName());
    }
}
