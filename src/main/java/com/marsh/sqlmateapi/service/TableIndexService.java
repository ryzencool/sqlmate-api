package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.TableIndexDelReq;
import com.marsh.sqlmateapi.controller.request.TableIndexEditReq;
import com.marsh.sqlmateapi.controller.request.TableIndexQueryReq;
import com.marsh.sqlmateapi.domain.TableIndex;
import com.marsh.sqlmateapi.mapper.TableIndexMapper;
import com.marsh.sqlmateapi.mapper.TableInfoMapper;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class TableIndexService {

    private final TableIndexMapper tableIndexMapper;

    private final TableInfoMapper tableInfoMapper;

    public TableIndexService(TableIndexMapper tableIndexMapper, TableInfoMapper tableInfoMapper) {
        this.tableIndexMapper = tableIndexMapper;
        this.tableInfoMapper = tableInfoMapper;
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

    @Transactional
    public void addIndex(TableIndexEditReq req, Integer userId) {
        var table = tableInfoMapper.selectById(req.getTableId());
        var index = BeanUtil.transfer(req, TableIndex.class);
        index.setCreateId(userId);
        index.setCreateTime(LocalDateTime.now());
        index.setProjectId(table.getProjectId());
        tableIndexMapper.insert(index);
    }

    public void deleteIndex(TableIndexDelReq req, UserIdentity identity) {
        Arrays.stream(req.getIndexesId()).forEach(tableIndexMapper::deleteById);
    }
}
