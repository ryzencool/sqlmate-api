package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.TableEditReq;
import com.marsh.sqlmateapi.controller.request.TableQueryReq;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.service.TableService;
import com.marsh.sqlmateapi.service.dto.TableWithColumnsDto;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<List<TableWithColumnsDto>> listAll(TableQueryReq req) {
        return BaseResponse.success(tableService.listAll(req));
    }

    // 表从那几个构建 ，然后认清关系，所有的都是去先构建1

    @GetMapping("/get")
    public BaseResponse<TableInfo> getTable(TableQueryReq req) {
        return BaseResponse.success(tableService.getTable(req.getTableId()));
    }

    @PostMapping("/update")
    public BaseResponse<Object> updateTable(@RequestBody TableEditReq req) {
        tableService.updateTable(req);
        return BaseResponse.success();
    }

    @PostMapping("/create")
    public BaseResponse<Object> createTable(@RequestBody TableEditReq req) {
        tableService.createTable(req);
        return BaseResponse.success();
    }

}
