package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.DBMLTableQueryReq;
import com.marsh.sqlmateapi.service.DBMLService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dbml")
public class DBMLController {

    private final DBMLService dbmlService;

    public DBMLController(DBMLService dbmlService) {
        this.dbmlService = dbmlService;
    }

    @GetMapping("/table")
    public BaseResponse<String> exportDbml(DBMLTableQueryReq req) {
        return BaseResponse.success(dbmlService.exportTableDBML(req.getTableId()));
    }
}
