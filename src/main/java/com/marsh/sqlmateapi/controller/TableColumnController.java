package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.ColumnUpdateReq;
import com.marsh.sqlmateapi.controller.request.ColumnDelReq;
import com.marsh.sqlmateapi.controller.request.ColumnQueryReq;
import com.marsh.sqlmateapi.controller.request.ColumnsDelReq;
import com.marsh.sqlmateapi.domain.TableColumn;
import com.marsh.sqlmateapi.service.TableColumnService;
import com.marsh.zutils.auth.UserIdentity;
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
    public BaseResponse<List<TableColumn>> listColumn(ColumnQueryReq req, UserIdentity identity) {
        return BaseResponse.success(tableColumnService.list(req));
    }

    @PostMapping("/del")
    public BaseResponse<Void> delColumn(@RequestBody ColumnDelReq req) {
        tableColumnService.delColumn(req.getColumnId());
        return BaseResponse.success();
    }

    @PostMapping("/multi/del")
    public BaseResponse<Void> delColumns(@RequestBody ColumnsDelReq req, UserIdentity identity) {
        tableColumnService.delColumns(req.getColumnIds(), identity.getUserId());
        return BaseResponse.success();
    }

    @PostMapping("/add")
    public BaseResponse<Object> addColumn(@RequestBody ColumnUpdateReq req, UserIdentity identity) {
        tableColumnService.addColumn(req, identity.getUserId());
        return BaseResponse.success();
    }

    @PostMapping("/update")
    public BaseResponse<Object> updateColumn(@RequestBody ColumnUpdateReq req, UserIdentity identity) {
        tableColumnService.updateColumn(req, identity.getUserId());
        return BaseResponse.success();
    }


}
