package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.TableEditReq;
import com.marsh.sqlmateapi.controller.request.TableQueryReq;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.mapper.TableInfoMapper;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {

    private final TableInfoMapper tableInfoMapper;

    public TableService(TableInfoMapper tableInfoMapper) {
        this.tableInfoMapper = tableInfoMapper;
    }

    public List<TableInfo> listTable(TableQueryReq req) {
        return tableInfoMapper.selectList(new QueryWrapper<TableInfo>()
                .lambda()
                .like(req.getTableName() != null, TableInfo::getName, req.getTableName())
                .eq(req.getProjectId() != null, TableInfo::getProjectId, req.getProjectId()));
    }

    public TableInfo getTable(Integer tableId) {
        return tableInfoMapper.selectById(tableId);
    }

    public Object listAll(TableQueryReq req) {
        return null;
    }

    public void updateTable(TableEditReq req) {
        var table  = BeanUtil.transfer(req, TableInfo.class);
        tableInfoMapper.updateById(table);
    }

    public void createTable(TableEditReq req) {
        var table = BeanUtil.transfer(req, TableInfo.class);
        tableInfoMapper.insert(table);
    }
}
