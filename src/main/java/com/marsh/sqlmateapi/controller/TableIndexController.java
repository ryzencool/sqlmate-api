package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.TableIndexDelReq;
import com.marsh.sqlmateapi.controller.request.TableIndexEditReq;
import com.marsh.sqlmateapi.controller.request.TableIndexQueryReq;
import com.marsh.sqlmateapi.domain.TableIndex;
import com.marsh.sqlmateapi.service.TableIndexService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tableIndex")
public class TableIndexController {

    private final TableIndexService tableIndexService;

    public TableIndexController(TableIndexService tableIndexService) {
        this.tableIndexService = tableIndexService;
    }

    @GetMapping("/list")
    public BaseResponse<List<TableIndex>> listIndex(TableIndexQueryReq req) {
        return BaseResponse.success(tableIndexService.listIndex(req));
    }

    @PostMapping("/update")
    public BaseResponse<Object> updateIndex(@RequestBody TableIndexEditReq req, UserIdentity identity) {
        tableIndexService.updateIndex(req, identity.getUserId());
        return BaseResponse.success();
    }

    @PostMapping("/add")
    public BaseResponse<Object> addIndex(@RequestBody TableIndexEditReq req, UserIdentity identity) {
        tableIndexService.addIndex(req, identity.getUserId());
        return BaseResponse.success();
    }

    @PostMapping("/delete")
    public BaseResponse<Object> deleteIndex(@RequestBody TableIndexDelReq req, UserIdentity identity) {
        tableIndexService.deleteIndex(req, identity);
        return BaseResponse.success();
    }

}
