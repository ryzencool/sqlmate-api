package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.QueryOptimizerReq;
import com.marsh.sqlmateapi.controller.response.QueryOptimizerResp;
import com.marsh.sqlmateapi.service.OptimizeService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptimizeController {

    private final OptimizeService optimizeService;

    public OptimizeController(OptimizeService optimizeService) {
        this.optimizeService = optimizeService;
    }

    @PostMapping("/optimize")
    public BaseResponse<String> optimize(@RequestBody QueryOptimizerReq req) {
        return BaseResponse.success(optimizeService.optimize(req));
    }
}
