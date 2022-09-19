package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.ColumnQueryReq;
import com.marsh.sqlmateapi.controller.request.ColumnUpdateReq;
import com.marsh.sqlmateapi.domain.TableColumn;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.domain.TableRel;
import com.marsh.sqlmateapi.mapper.TableColumnMapper;
import com.marsh.sqlmateapi.mapper.TableInfoMapper;
import com.marsh.sqlmateapi.mapper.TableRelMapper;
import com.marsh.sqlmateapi.service.dto.ColumnRelationShip;
import com.marsh.sqlmateapi.service.dto.FullTableColumnDto;
import com.marsh.sqlmateapi.service.dto.FullTableRelDto;
import com.marsh.zutils.util.BeanUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableColumnService {

    private final TableInfoMapper tableInfoMapper;

    private final TableRelMapper tableRelMapper;
    private final TableColumnMapper tableColumnMapper;

    public TableColumnService(TableInfoMapper tableInfoMapper,
                              TableRelMapper tableRelMapper,
                              TableColumnMapper tableColumnMapper) {
        this.tableInfoMapper = tableInfoMapper;
        this.tableRelMapper = tableRelMapper;
        this.tableColumnMapper = tableColumnMapper;
    }


    public List<TableColumn> list(ColumnQueryReq req) {
        return tableColumnMapper.selectList(new QueryWrapper<TableColumn>().lambda().eq(TableColumn::getTableId, req.getTableId()).orderByAsc(TableColumn::getCreateTime));
    }

    public List<FullTableColumnDto> listColumn(ColumnQueryReq req) {
        var tableId = req.getTableId();
        var curColumns = tableColumnMapper.selectList(new QueryWrapper<TableColumn>()
                .lambda()
                .eq(req.getTableId() != null, TableColumn::getTableId, req.getTableId()));
        var rels = tableRelMapper.selectList(new QueryWrapper<TableRel>().lambda()
                .eq(TableRel::getLeftTableId, tableId)
                .or()
                .eq(TableRel::getRightTableId, tableId));
        List<FullTableColumnDto> fullColumns;
        if (CollectionUtils.isNotEmpty(rels)) {
            var tableIdSet = new HashSet<Integer>();
            var columnIdSet = new HashSet<Long>();
            rels.forEach(it -> {
                tableIdSet.add(it.getLeftTableId());
                tableIdSet.add(it.getRightTableId());
                columnIdSet.add(it.getLeftColumnId());
                columnIdSet.add(it.getRightColumnId());
            });
            var tables = tableInfoMapper.selectList(new QueryWrapper<TableInfo>().lambda().in(TableInfo::getId, tableIdSet));
            var columns = tableColumnMapper.selectList(new QueryWrapper<TableColumn>().lambda().in(TableColumn::getId, columnIdSet));
            var fullRels = rels.stream().map(rel -> {
                var leftTable = tables.stream().filter(table -> Objects.equals(table.getId(), rel.getLeftTableId())).findFirst().get();
                var rightTable = tables.stream().filter(table -> Objects.equals(table.getId(), rel.getRightTableId())).findFirst().get();
                var leftColumn = columns.stream().filter(column -> Objects.equals(column.getId(), rel.getLeftColumnId())).findFirst().get();
                var rightColumn = columns.stream().filter(column -> Objects.equals(column.getId(), rel.getRightColumnId())).findFirst().get();
                var id = rel.getId();
                return FullTableRelDto.builder()
                        .leftTableId(leftTable.getId())
                        .leftTableName(leftTable.getName())
                        .rightTableId(rightTable.getId())
                        .rightTableName(rightTable.getName())
                        .leftColumnId(leftColumn.getId())
                        .leftColumnName(leftColumn.getName())
                        .rightColumnId(rightColumn.getId())
                        .rightColumnName(rightColumn.getName())
                        .id(id).build();
            }).collect(Collectors.toList());

            fullColumns = curColumns.stream().map(column -> {
                var leftColumns = fullRels.stream()
                        .filter(it -> Objects.equals(it.getLeftColumnId(), column.getId())).collect(Collectors.toList());
                var rightColumns = fullRels.stream()
                        .filter(it -> Objects.equals(it.getRightColumnId(), column.getId())).collect(Collectors.toList());
                var fullColumn = BeanUtil.transfer(column, FullTableColumnDto.class);
                fullColumn.setColumnRelationShip(ColumnRelationShip.builder()
                        .leftColumns(leftColumns)
                        .rightColumns(rightColumns)
                        .build());
                return fullColumn;
            }).collect(Collectors.toList());
        } else {
            fullColumns = curColumns.stream().map(column -> BeanUtil.transfer(column, FullTableColumnDto.class)).collect(Collectors.toList());
        }
        return fullColumns;
    }

    public void delColumn(Integer columnId) {
        tableColumnMapper.deleteById(columnId);
    }

    @Transactional
    public void addColumn(ColumnUpdateReq req, Integer userId) {
        var tableInfo = tableInfoMapper.selectById(req.getTableId());
        var col = BeanUtil.transfer(req, TableColumn.class);
        col.setCreateId(userId);
        col.setProjectId(tableInfo.getProjectId());
        col.setCreateTime(LocalDateTime.now());
        tableColumnMapper.insert(col);
    }

    public void delColumns(Integer[] columnIds, Integer userId) {
        Arrays.stream(columnIds).forEach(tableColumnMapper::deleteById);
    }

    public void updateColumn(ColumnUpdateReq req, Integer userId) {
        var column = BeanUtil.transfer(req, TableColumn.class);
        tableColumnMapper.updateById(column);
    }
}
