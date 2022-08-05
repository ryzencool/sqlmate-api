package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.TableQueryReq;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.service.TableService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/table")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/list")
    public BaseResponse<List<TableInfo>> listTable(TableQueryReq req) {
        return BaseResponse.success(tableService.listTable(req));
    }

    @GetMapping("/listAll")
    public BaseResponse<Object> listAll(TableQueryReq req) {
        return BaseResponse.success(tableService.listAll(req));
    }




}
