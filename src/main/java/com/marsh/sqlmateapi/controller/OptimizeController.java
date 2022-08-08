package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.QueryOptimizerReq;
import com.marsh.sqlmateapi.controller.response.QueryOptimizerResp;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptimizeController {

    @GetMapping("/queryOptimizer")
    public BaseResponse<QueryOptimizerResp> optimize(QueryOptimizerReq req) {
        return BaseResponse.success(QueryOptimizerResp.builder().build());
    }
}
