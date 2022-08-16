package com.marsh.sqlmateapi.controller;


import com.marsh.sqlmateapi.controller.request.ProjectSqlEditReq;
import com.marsh.sqlmateapi.controller.request.ProjectSqlQueryReq;
import com.marsh.sqlmateapi.domain.ProjectSql;
import com.marsh.sqlmateapi.service.ProjectSqlService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projectSql")
public class ProjectSqlController {

    private final ProjectSqlService projectSqlService;

    public ProjectSqlController(ProjectSqlService projectSqlService) {
        this.projectSqlService = projectSqlService;
    }

    @GetMapping("/list")
    public BaseResponse<List<ProjectSql>> listSql(ProjectSqlQueryReq req) {
        return BaseResponse.success(projectSqlService.listSql(req));
    }

    @PostMapping("/add")
    public BaseResponse<Object> addSql(@RequestBody ProjectSqlEditReq req) {
        projectSqlService.addSql(req);
        return BaseResponse.success();
    }
    @PostMapping("/update")
    public BaseResponse<Object> updateSql(@RequestBody ProjectSqlEditReq req) {
        projectSqlService.updateSql(req);
        return BaseResponse.success();
    }


    @PostMapping("/del")
    public BaseResponse<Object> deleteSql(@RequestBody ProjectSqlEditReq req, UserIdentity identity){
        projectSqlService.deleteSql(req, identity.getUserId());
        return BaseResponse.success();
    }

}
