package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.TableIndexQueryReq;
import com.marsh.sqlmateapi.domain.TableIndex;
import com.marsh.sqlmateapi.service.TableIndexService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
