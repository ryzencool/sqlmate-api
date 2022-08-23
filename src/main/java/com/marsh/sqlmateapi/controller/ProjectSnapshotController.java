package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.SnapshotDelReq;
import com.marsh.sqlmateapi.controller.request.SnapshotEditReq;
import com.marsh.sqlmateapi.controller.request.SnapshotQueryReq;
import com.marsh.sqlmateapi.domain.ProjectSnapshot;
import com.marsh.sqlmateapi.service.ProjectSnapshotService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/snapshot")
public class ProjectSnapshotController {

    private final ProjectSnapshotService projectSnapshotService;

    public ProjectSnapshotController(ProjectSnapshotService projectSnapshotService) {
        this.projectSnapshotService = projectSnapshotService;
    }

    @GetMapping("/list")
    public BaseResponse<List<ProjectSnapshot>> listSnapshot(SnapshotQueryReq req, UserIdentity identity) {
        return BaseResponse.success(projectSnapshotService.listSnapshot(req, identity.getUserId()));
    }

    @PostMapping("/del")
    public BaseResponse<Object> delSnapshot(@RequestBody  SnapshotDelReq req, UserIdentity identity) {
        projectSnapshotService.deleteSnapshot(req, identity.getUserId());
        return BaseResponse.success();
    }

    @PostMapping("/create")
    public BaseResponse<Object> createSnapshot(@RequestBody SnapshotEditReq req, UserIdentity identity) {
        projectSnapshotService.createSnapshot(req, identity.getUserId());
        return BaseResponse.success();
    }
}
