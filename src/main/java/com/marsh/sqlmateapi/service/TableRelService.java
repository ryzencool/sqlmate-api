package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.TableRelQueryReq;
import com.marsh.sqlmateapi.domain.TableRel;
import com.marsh.sqlmateapi.mapper.TableRelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableRelService {

    private final TableRelMapper tableRelMapper;

    public TableRelService(TableRelMapper tableRelMapper) {
        this.tableRelMapper = tableRelMapper;
    }

    public List<TableRel> listRel(TableRelQueryReq req, Integer userId) {
        return tableRelMapper.selectList(new QueryWrapper<TableRel>().lambda().eq(TableRel::getProjectId, req.getProjectId()));
    }
}
