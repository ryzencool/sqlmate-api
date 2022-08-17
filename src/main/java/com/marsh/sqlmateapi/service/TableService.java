package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.TableEditReq;
import com.marsh.sqlmateapi.controller.request.TableQueryReq;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.mapper.TableColumnMapper;
import com.marsh.sqlmateapi.mapper.TableInfoMapper;
import com.marsh.sqlmateapi.mapper.TableRelMapper;
import com.marsh.sqlmateapi.mapper.result.TableDetailResult;
import com.marsh.sqlmateapi.service.dto.ColumnSimpleDto;
import com.marsh.sqlmateapi.service.dto.TableSimpleDto;
import com.marsh.sqlmateapi.service.dto.TableWithColumnsDto;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<TableWithColumnsDto> listAll(TableQueryReq req) {
        var columns = tableColumnMapper.listProjectColumns(req.getProjectId());

        var columnGroup = columns.stream().collect(Collectors.groupingBy(TableDetailResult::getTableId)).entrySet().stream().map(obj -> {
            var firstObj = obj.getValue().get(0);
            var tableId = obj.getKey();
            var tableName = firstObj.getTableName();
            var simpleTable = TableSimpleDto.builder().name(tableName).id(tableId).build();
            var list = obj.getValue().stream().map(it -> ColumnSimpleDto.builder()
                    .id(it.getColumnId())
                    .type(it.getColumnType())
                    .name(it.getColumnName())
                    .build()).collect(Collectors.toList());
            return TableWithColumnsDto.builder()
                    .content(list)
                    .id(tableId)
                    .title(tableName)
                    .build();
        }).collect(Collectors.toList());




        return columnGroup;
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
