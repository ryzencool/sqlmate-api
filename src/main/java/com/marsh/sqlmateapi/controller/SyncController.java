package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.SyncReq;
import com.marsh.sqlmateapi.service.SyncService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
