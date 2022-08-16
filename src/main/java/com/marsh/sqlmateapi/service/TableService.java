package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.marsh.sqlmateapi.controller.request.TableEditReq;
import com.marsh.sqlmateapi.controller.request.TableQueryReq;
import com.marsh.sqlmateapi.domain.TableColumn;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.domain.TableRel;
import com.marsh.sqlmateapi.mapper.TableColumnMapper;
import com.marsh.sqlmateapi.mapper.TableInfoMapper;
import com.marsh.sqlmateapi.mapper.TableRelMapper;
import com.marsh.sqlmateapi.service.dto.FullTableRelDto;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final TableInfoMapper tableInfoMapper;

    private final TableRelMapper tableRelMapper;

    private final TableColumnMapper tableColumnMapper;

    public TableService(TableInfoMapper tableInfoMapper, TableRelMapper tableRelMapper, TableColumnMapper tableColumnMapper) {
        this.tableInfoMapper = tableInfoMapper;
        this.tableRelMapper = tableRelMapper;
        this.tableColumnMapper = tableColumnMapper;
    }

    public List<TableInfo> listTable(TableQueryReq req) {
        return tableInfoMapper.selectList(new QueryWrapper<TableInfo>()
                .lambda()
                .like(req.getTableName() != null, TableInfo::getName, req.getTableName())
                .eq(req.getProjectId() != null, TableInfo::getProjectId, req.getProjectId())
                .orderByDesc(true, TableInfo::getCreateTime));
    }

    public TableInfo getTable(Integer tableId) {
        return tableInfoMapper.selectById(tableId);
    }

    public Object listAll(TableQueryReq req) {
        return null;
    }

    public void updateTable(TableEditReq req) {
        var table = BeanUtil.transfer(req, TableInfo.class);
        tableInfoMapper.updateById(table);
    }

    public void createTable(TableEditReq req) {
        var table = BeanUtil.transfer(req, TableInfo.class);
        tableInfoMapper.insert(table);
    }
}
