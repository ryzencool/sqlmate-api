package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.DeleteTableReq;
import com.marsh.sqlmateapi.controller.request.TableEditReq;
import com.marsh.sqlmateapi.controller.request.TableQueryReq;
import com.marsh.sqlmateapi.domain.*;
import com.marsh.sqlmateapi.mapper.*;
import com.marsh.sqlmateapi.mapper.param.TableDetailParam;
import com.marsh.sqlmateapi.mapper.result.TableDetailResult;
import com.marsh.sqlmateapi.service.dto.ColumnSimpleDto;
import com.marsh.sqlmateapi.service.dto.TableSimpleDto;
import com.marsh.sqlmateapi.service.dto.TableWithColumnsDto;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final TableInfoMapper tableInfoMapper;

    private final TableRelMapper tableRelMapper;

    private final TableColumnMapper tableColumnMapper;

    private final TableIndexMapper tableIndexMapper;

    private final DefaultColumnDetailMapper defaultColumnTemplateMapper;

    public TableService(TableInfoMapper tableInfoMapper, TableRelMapper tableRelMapper, TableColumnMapper tableColumnMapper, TableIndexMapper tableIndexMapper, DefaultColumnDetailMapper defaultColumnTemplateMapper) {
        this.tableInfoMapper = tableInfoMapper;
        this.tableRelMapper = tableRelMapper;
        this.tableColumnMapper = tableColumnMapper;
        this.tableIndexMapper = tableIndexMapper;
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
                    .isNotNull(it.getIsNotNull())
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

    @Transactional
    public void updateTable(TableEditReq req, Integer userId) {
        var table = BeanUtil.transfer(req, TableInfo.class);
        table.setCreateTime(LocalDateTime.now());
        table.setCreateId(userId);
        tableInfoMapper.updateById(table);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer createTable(TableEditReq req, Integer userId) {
        var table = BeanUtil.transfer(req, TableInfo.class);
        table.setCreateTime(LocalDateTime.now());
        table.setCreateId(userId);
        var tableId = tableInfoMapper.insertReturnId(table);
        // ??????????????????
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
                        .projectId(req.getProjectId())
                        .isAutoIncrement(detail.getIsAutoIncrement())
                        .isNotNull(detail.getIsNotNull())
                        .defaultValue(detail.getDefaultValue())
                        .isPrimaryKey(detail.getIsPrimaryKey())
                        .isUniqueKey(detail.getIsUniqueKey())
                        .createTime(LocalDateTime.now())
                        .createId(userId)
                        .tableId(tableId)
                        .build();
                tableColumnMapper.insert(newColumn);
            }
        }
        return tableId;
    }


    public void deleteTable(DeleteTableReq req, Integer userId) {
        tableInfoMapper.deleteById(req.getTableId());
        var conColumn = new QueryWrapper<TableColumn>().lambda().eq(TableColumn::getTableId, req.getTableId());
        var conIndex = new QueryWrapper<TableIndex>().lambda().eq(TableIndex::getTableId, req.getTableId());
        tableColumnMapper.delete(conColumn);

        tableIndexMapper.delete(conIndex);

        tableRelMapper.delete(new QueryWrapper<TableRel>().lambda().eq(TableRel::getLeftTableId, req.getTableId()).or().eq(TableRel::getRightTableId, req.getTableId()));

    }
}
