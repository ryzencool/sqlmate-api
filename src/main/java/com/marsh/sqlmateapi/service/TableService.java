package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.TableEditReq;
import com.marsh.sqlmateapi.controller.request.TableQueryReq;
import com.marsh.sqlmateapi.domain.DefaultColumnDetail;
import com.marsh.sqlmateapi.domain.TableColumn;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.mapper.*;
import com.marsh.sqlmateapi.mapper.param.TableDetailParam;
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

    private final DefaultColumnDetailMapper defaultColumnTemplateMapper;

    public TableService(TableInfoMapper tableInfoMapper, TableRelMapper tableRelMapper, TableColumnMapper tableColumnMapper, DefaultColumnDetailMapper defaultColumnTemplateMapper) {
        this.tableInfoMapper = tableInfoMapper;
        this.tableRelMapper = tableRelMapper;
        this.tableColumnMapper = tableColumnMapper;
        this.defaultColumnTemplateMapper = defaultColumnTemplateMapper;
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
        var columns = tableColumnMapper.listProjectColumns(TableDetailParam.builder()
                .projectId(req.getProjectId())
                .build());

        var columnGroup = columns.stream()
                .collect(Collectors.groupingBy(TableDetailResult::getTableId)).entrySet().stream().map(obj -> {
            var firstObj = obj.getValue().get(0);
            var tableId = obj.getKey();
            var tableName = firstObj.getTableName();
            var simpleTable = TableSimpleDto.builder().name(tableName).id(tableId).build();
            var list = obj.getValue().stream().map(it -> ColumnSimpleDto.builder()
                    .id(it.getColumnId())
                    .type(it.getColumnType())
                    .name(it.getColumnName())
                    .isAutoIncrement(it.getIsAutoIncrement())
                    .isNull(it.getIsNull())
                    .isUniqueKey(it.getIsUniqueKey())
                    .isPrimaryKey(it.getIsPrimaryKey())
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
        var tableId = tableInfoMapper.insertReturnId(table);


        // 创建预设字段
        if (req.getDefaultColumnTemplateId() != null || req.getDefaultColumnTemplateId() == 0) {

            var columnList = defaultColumnTemplateMapper.selectList(new QueryWrapper<DefaultColumnDetail>()
                    .lambda()
                    .eq(DefaultColumnDetail::getTemplateId, req.getDefaultColumnTemplateId()));
            for (DefaultColumnDetail detail : columnList) {
                var newColumn = TableColumn.builder()
                        .comment(detail.getComment())
                        .note(detail.getNote())
                        .name(detail.getName())
                        .type(detail.getType())
                        .isAutoIncrement(detail.getIsAutoIncrement())
                        .isNull(detail.getIsNull())
                        .defaultValue(detail.getDefaultValue())
                        .isPrimaryKey(detail.getIsPrimaryKey())
                        .isUniqueKey(detail.getIsUniqueKey())
                        .tableId(tableId)
                        .build();
                tableColumnMapper.insert(newColumn);
            }
        }
    }
}
