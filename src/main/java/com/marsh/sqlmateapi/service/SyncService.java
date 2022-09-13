package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.CleanTableReq;
import com.marsh.sqlmateapi.controller.request.ConnectIsLiveReq;
import com.marsh.sqlmateapi.controller.request.ConnectReq;
import com.marsh.sqlmateapi.controller.request.SyncReq;
import com.marsh.sqlmateapi.domain.ProjectDataSource;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.mapper.ProjectDataSourceMapper;
import com.marsh.sqlmateapi.mapper.TableInfoMapper;
import com.marsh.sqlmateapi.utils.RemoteCallUtil;
import com.marsh.sqlmateapi.utils.SqlExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
public class SyncService {

    private final SqlExecutor sqlExecutor;

    private final ProjectDataSourceMapper projectDataSourceMapper;

    private final TableInfoMapper tableInfoMapper;

    public SyncService(SqlExecutor sqlExecutor,
                       ProjectDataSourceMapper projectDataSourceMapper,
                       TableInfoMapper tableInfoMapper) {
        this.sqlExecutor = sqlExecutor;
        this.projectDataSourceMapper = projectDataSourceMapper;
        this.tableInfoMapper = tableInfoMapper;
    }

    public Object sync(SyncReq req, Integer userId) {
        var ds = projectDataSourceMapper.selectOne(new QueryWrapper<ProjectDataSource>()
                .lambda()
                .eq(ProjectDataSource::getDbType, req.getDbType())
                .eq(ProjectDataSource::getProjectId, req.getProjectId()));

        var tables = tableInfoMapper.selectList(new QueryWrapper<TableInfo>().lambda()
                .eq(TableInfo::getProjectId, req.getProjectId()));

        var dropSql = tables.stream().map(item -> String.format("drop table if exists %s;", item.getName())).collect(Collectors.joining("\n"));

        var sql = dropSql + "\n\n" + req.getSql();

        try (var response = sqlExecutor.sendSql(sql, ds.getName(), ds.getDbType())) {
            return RemoteCallUtil.handleResponse(response);
        }


    }

    public void connect(ConnectReq req) {
        var ds = projectDataSourceMapper.selectOne(new QueryWrapper<ProjectDataSource>().lambda().eq(ProjectDataSource::getProjectId, req.getProjectId()).eq(ProjectDataSource::getDbType, req.getDbType()));
        try (var resp = sqlExecutor.connect(ds.getName())) {
            RemoteCallUtil.handleResponse(resp);
        }

    }

    public void cleanTable(CleanTableReq req, Integer userId) {

        var ds = findDatasource(req.getProjectId(), req.getDbType());
        var table = tableInfoMapper.selectById(req.getTableId());
        var sql = String.format("TRUNCATE TABLE %s;", table.getName());
        try (var resp = sqlExecutor.sendSql(sql, ds.getName(), req.getDbType())) {
            RemoteCallUtil.handleResponse(resp);
        }

    }

    private ProjectDataSource findDatasource(Integer projectId, Integer dbType) {
        return  projectDataSourceMapper
                .selectOne(new QueryWrapper<ProjectDataSource>()
                        .lambda()
                        .eq(ProjectDataSource::getProjectId, projectId)
                        .eq(ProjectDataSource::getDbType, dbType));
    }

    public Object connectIsLive(ConnectIsLiveReq req) {
        var ds = findDatasource(req.getProjectId(),req.getDbType());

        try (var resp = sqlExecutor.isLive(ds.getName())) {
            return RemoteCallUtil.handleResponse(resp);
        }

    }
}