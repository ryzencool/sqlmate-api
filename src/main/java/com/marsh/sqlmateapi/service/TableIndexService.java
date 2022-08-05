package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.TableIndexQueryReq;
import com.marsh.sqlmateapi.domain.TableIndex;
import com.marsh.sqlmateapi.mapper.TableIndexMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableIndexService {

    private final TableIndexMapper tableIndexMapper;

    public TableIndexService(TableIndexMapper tableIndexMapper) {
        this.tableIndexMapper = tableIndexMapper;
    }

    public List<TableIndex> listIndex(TableIndexQueryReq req) {
        return tableIndexMapper.selectList(new QueryWrapper<TableIndex>()
                .lambda()
                .eq(req.getTableId() != null, TableIndex::getTableId, req.getTableId()));
    }
}
