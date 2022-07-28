package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.ColumnUpdateReq;
import com.marsh.sqlmateapi.controller.request.ColumnDelReq;
import com.marsh.sqlmateapi.controller.request.ColumnQueryReq;
import com.marsh.sqlmateapi.domain.TableColumn;
import com.marsh.sqlmateapi.service.TableColumnService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tableColumn")
public class TableColumnController {

    private final TableColumnService tableColumnService;

    public TableColumnController(TableColumnService tableColumnService) {
        this.tableColumnService = tableColumnService;
    }

    @GetMapping("/list")
    public BaseResponse<List<TableColumn>> listColumn(ColumnQueryReq req) {
        return BaseResponse.success(tableColumnService.listColumn(req));
    }

    @PostMapping("/del")
    public BaseResponse<Void> delColumn(@RequestBody ColumnDelReq req) {
        tableColumnService.delColumn(req.getColumnId());
        return BaseResponse.success();
    }

    @PostMapping("/add")
    public BaseResponse<Object> addColumn(@RequestBody ColumnUpdateReq req) {
        tableColumnService.addColumn(req);
        return BaseResponse.success();
    }



}
