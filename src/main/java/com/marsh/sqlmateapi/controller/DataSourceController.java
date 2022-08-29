package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.ConnectionCloseReq;
import com.marsh.sqlmateapi.controller.request.ConnectionCreateReq;
import com.marsh.sqlmateapi.service.DataSourceService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    private final DataSourceService dataSourceService;

    public DataSourceController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @PostMapping("/connect")
    public BaseResponse<Object> openDataSource(@RequestBody ConnectionCreateReq req, UserIdentity identity) {
        dataSourceService.createConnection(req);
        return BaseResponse.success();
    }

    @PostMapping("/close")
    public BaseResponse<Object> closeDataSource(@RequestBody ConnectionCloseReq req, UserIdentity identity) {
        dataSourceService.closeConnection(req);
        return BaseResponse.success();
    }
}
