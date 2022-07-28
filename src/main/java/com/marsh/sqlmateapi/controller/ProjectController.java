package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.AddProjectReq;
import com.marsh.sqlmateapi.controller.request.ProjectQueryReq;
import com.marsh.sqlmateapi.service.ProjectService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

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
    public void listProject(ProjectQueryReq req) {
        projectService.listProject(req);
    }

    @GetMapping("/get")
    public void projectDetail() {

    }

    @PostMapping("/del")
    public void delProject() {

    }
}
