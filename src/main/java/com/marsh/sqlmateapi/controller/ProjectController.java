package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.AddProjectReq;
import com.marsh.sqlmateapi.controller.request.ProjectDetailQueryReq;
import com.marsh.sqlmateapi.controller.request.ProjectQueryReq;
import com.marsh.sqlmateapi.controller.response.ProjectStatResp;
import com.marsh.sqlmateapi.domain.ProjectInfo;
import com.marsh.sqlmateapi.service.ProjectService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/add")
    public BaseResponse<Void> addProject(@RequestBody AddProjectReq req) {
        projectService.AddProject(req);
        return BaseResponse.success();
    }

    @GetMapping("/list")
    public BaseResponse<List<ProjectInfo>> listProject(ProjectQueryReq req) {
       return BaseResponse.success( projectService.listProject(req));
    }

    @GetMapping("/get")
    public BaseResponse<ProjectStatResp> projectDetail(ProjectDetailQueryReq req) {
        return BaseResponse.success(projectService.getProject(req.getProjectId()));
    }

    @PostMapping("/del")
    public void delProject() {

    }
}
