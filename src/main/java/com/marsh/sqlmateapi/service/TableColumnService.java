package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.ColumnQueryReq;
import com.marsh.sqlmateapi.controller.request.ColumnUpdateReq;
import com.marsh.sqlmateapi.domain.TableColumn;
import com.marsh.sqlmateapi.mapper.TableColumnMapper;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TableColumnService {

    private final TableColumnMapper tableColumnMapper;

    public TableColumnService(TableColumnMapper tableColumnMapper) {
        this.tableColumnMapper = tableColumnMapper;
    }

    public List<TableColumn> listColumn(ColumnQueryReq req) {
        return tableColumnMapper.selectList(new QueryWrapper<TableColumn>()
                .lambda()
                .eq(req.getTableId() != null, TableColumn::getTableId, req.getTableId()));
    }

    public void delColumn(Integer columnId) {
        tableColumnMapper.deleteById(columnId);
    }

    public void addColumn(ColumnUpdateReq req) {
        var col = BeanUtil.transfer(req, TableColumn.class);
        tableColumnMapper.insert(col);
    }

    public void delColumns(Integer[] columnIds, Integer userId) {
        Arrays.stream(columnIds).forEach(tableColumnMapper::deleteById);

    }
}
