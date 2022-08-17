package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.TableRelQueryReq;
import com.marsh.sqlmateapi.domain.TableRel;
import com.marsh.sqlmateapi.service.TableRelService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tableRel")
public class TableRelController {

    private final TableRelService tableRelService;

    public TableRelController(TableRelService tableRelService) {
        this.tableRelService = tableRelService;
    }

    @GetMapping("/list")
    public BaseResponse<List<TableRel>> list(TableRelQueryReq req, UserIdentity identity) {

        return BaseResponse.success(
                tableRelService.listRel(req, identity.getUserId())
        );
    }
}
