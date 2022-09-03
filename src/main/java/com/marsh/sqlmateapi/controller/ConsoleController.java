package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.ConsoleGetReq;
import com.marsh.sqlmateapi.controller.request.SyncConsoleReq;
import com.marsh.sqlmateapi.domain.ProjectConsole;
import com.marsh.sqlmateapi.service.ProjectConsoleService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/console")
public class ConsoleController {

    private final ProjectConsoleService projectConsoleService;

    public ConsoleController(ProjectConsoleService projectConsoleService) {
        this.projectConsoleService = projectConsoleService;
    }

    @PostMapping("/sync")
    public BaseResponse<Object> syncConsole(@RequestBody SyncConsoleReq req, UserIdentity identity) {
        projectConsoleService.syncConsole(req, identity.getUserId());
        return BaseResponse.success();
    }

    @GetMapping("/get")
    public BaseResponse<ProjectConsole> getConsole(ConsoleGetReq req, UserIdentity identity) {
        var console = projectConsoleService.getConsole(req.getProjectId());
        return BaseResponse.success(console);

    }
}
