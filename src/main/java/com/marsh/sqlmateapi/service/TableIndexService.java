package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.TableIndexEditReq;
import com.marsh.sqlmateapi.controller.request.TableIndexQueryReq;
import com.marsh.sqlmateapi.domain.TableIndex;
import com.marsh.sqlmateapi.mapper.TableIndexMapper;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.util.BeanUtil;
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

    public void updateIndex(TableIndexEditReq req, Integer userId) {
        var index = BeanUtil.transfer(req, TableIndex.class);
        tableIndexMapper.updateById(index);
    }

    public void addIndex(TableIndexEditReq req, Integer userId) {
        var index = BeanUtil.transfer(req, TableIndex.class);
        tableIndexMapper.insert(index);
    }

    public void deleteIndex(TableIndexEditReq req, UserIdentity identity) {
        tableIndexMapper.deleteById(req.getId());
    }
}
