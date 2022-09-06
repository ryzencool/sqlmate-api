package com.marsh.sqlmateapi.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.SyncReq;
import com.marsh.sqlmateapi.domain.ProjectDataSource;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.mapper.ProjectDataSourceMapper;
import com.marsh.sqlmateapi.mapper.TableInfoMapper;
import com.marsh.sqlmateapi.utils.RemoteCallUtil;
import com.marsh.sqlmateapi.utils.SqlExecutor;
import com.marsh.zutils.exception.BaseBizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SyncService {

    private final SqlExecutor sqlExecutor;

    private final ProjectDataSourceMapper projectDataSourceMapper;

    private final TableInfoMapper tableInfoMapper;

    public SyncService(SqlExecutor sqlExecutor, ProjectDataSourceMapper projectDataSourceMapper, TableInfoMapper tableInfoMapper) {
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


}
