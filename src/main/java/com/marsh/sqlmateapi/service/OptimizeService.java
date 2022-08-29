package com.marsh.sqlmateapi.service;

import com.marsh.sqlmateapi.controller.request.QueryOptimizerReq;
import com.marsh.sqlmateapi.utils.CommandLineUtil;
import org.springframework.stereotype.Service;

@Service
public class OptimizeService {

    public String optimize(QueryOptimizerReq req) {
        var result = CommandLineUtil.execute(req.getSql());
        return result;
    }
}
