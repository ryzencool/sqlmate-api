package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.CleanTableReq;
import com.marsh.sqlmateapi.controller.request.ConnectIsLiveReq;
import com.marsh.sqlmateapi.controller.request.ConnectReq;
import com.marsh.sqlmateapi.controller.request.SyncReq;
import com.marsh.sqlmateapi.service.SyncService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/database")
public class SyncController {

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }


    @PostMapping("/sync")
    public BaseResponse<Object> sync(@RequestBody SyncReq req, UserIdentity identity) {
        return BaseResponse.success(syncService.sync(req, identity.getUserId()));
    }

    @PostMapping("/cleanTable")
    public BaseResponse<Object> cleanTable (@RequestBody CleanTableReq req, UserIdentity identity) {
        syncService.cleanTable(req, identity.getUserId());
        return BaseResponse.success();
    }
    @PostMapping("/createConnect")
    public BaseResponse<Object> connect(@RequestBody ConnectReq req, UserIdentity identity) {
        syncService.connect(req);
        return BaseResponse.success();
    }

    @GetMapping("/connectIsLive")
    public BaseResponse<Object> connectIsLive(ConnectIsLiveReq req, UserIdentity identity) {
        return BaseResponse.success(syncService.connectIsLive(req));

    }
}
