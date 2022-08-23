package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.DBMLProjectImportReq;
import com.marsh.sqlmateapi.controller.request.DBMLProjectQueryReq;
import com.marsh.sqlmateapi.controller.request.DBMLTableQueryReq;
import com.marsh.sqlmateapi.service.DBMLService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/project")
    public BaseResponse<String> exportProjectDbml (DBMLProjectQueryReq req, UserIdentity identity) {
        return BaseResponse.success(dbmlService.exportProjectDBML(req.getProjectId()));

    }

    @PostMapping("/import")
    public BaseResponse<Object> importProject(@RequestBody DBMLProjectImportReq req, UserIdentity identity) {
        dbmlService.importProject(req);
        return BaseResponse.success();
    }
}
