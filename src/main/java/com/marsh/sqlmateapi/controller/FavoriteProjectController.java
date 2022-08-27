package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.FavoriteProjectAddReq;
import com.marsh.sqlmateapi.mapper.param.QueryFavoriteProjectParam;
import com.marsh.sqlmateapi.mapper.result.FavoriteProjectDetail;
import com.marsh.sqlmateapi.service.FavoriteProjectService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/favoriteProject")
public class FavoriteProjectController {

    private final FavoriteProjectService favoriteProjectService;

    public FavoriteProjectController(FavoriteProjectService favoriteProjectService) {
        this.favoriteProjectService = favoriteProjectService;
    }

    @GetMapping("/list")
    public BaseResponse<List<FavoriteProjectDetail>> listProject(QueryFavoriteProjectParam req, UserIdentity identity) {
        req.setUserId(identity.getUserId());
        return BaseResponse.success(favoriteProjectService.listProject(req));
    }

    @GetMapping("/add")
    public BaseResponse<Object> collectProject( FavoriteProjectAddReq req, UserIdentity identity) {
        favoriteProjectService.addProject(req, identity.getUserId());
        return BaseResponse.success();
    }

}
