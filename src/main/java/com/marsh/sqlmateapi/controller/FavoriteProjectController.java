package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.mapper.param.QueryFavoriteProjectParam;
import com.marsh.sqlmateapi.mapper.result.FavoriteProjectDetail;
import com.marsh.sqlmateapi.service.FavoriteProjectService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
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
    public BaseResponse<List<FavoriteProjectDetail>> listProject(QueryFavoriteProjectParam req) {
        return BaseResponse.success(favoriteProjectService.listProject(req));
    }
}
