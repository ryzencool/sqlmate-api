package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.ExecuteSqlReq;
import com.marsh.sqlmateapi.service.ExecuteService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sql")
public class ExecuteController {

    private final ExecuteService executeService;

    public ExecuteController(ExecuteService executeService) {
        this.executeService = executeService;
    }

    @PostMapping("/execute")
    public BaseResponse<Object> execute(@RequestBody ExecuteSqlReq req) {
        return BaseResponse.success(executeService.execute(req));
    }


}
