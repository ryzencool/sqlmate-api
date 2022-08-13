package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.DefaultColumnDetailQueryReq;
import com.marsh.sqlmateapi.controller.request.DefaultColumnTemplateEditReq;
import com.marsh.sqlmateapi.controller.request.DefaultColumnTemplateQuery;
import com.marsh.sqlmateapi.controller.request.DefaultColumnDetailEditReq;
import com.marsh.sqlmateapi.domain.DefaultColumnDetail;
import com.marsh.sqlmateapi.domain.DefaultColumnTemplate;
import com.marsh.sqlmateapi.service.DefaultColumnService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/defaultColumn")
public class DefaultColumnController {

    private final DefaultColumnService defaultColumnService;

    public DefaultColumnController(DefaultColumnService defaultColumnService) {
        this.defaultColumnService = defaultColumnService;
    }

    @PostMapping("/template/add")
    public BaseResponse<Object> addTemplate(@RequestBody DefaultColumnTemplateEditReq req, UserIdentity identity) {
        defaultColumnService.addTemplate(req, identity.getUserId());
        return BaseResponse.success();
    }

    @PostMapping("/template/update")
    public BaseResponse<Object> updateTemplate(@RequestBody DefaultColumnTemplateEditReq req, UserIdentity identity) {
        defaultColumnService.updateTemplate(req, identity.getUserId());
        return BaseResponse.success();
    }

    @GetMapping("/template/list")
    public BaseResponse<List<DefaultColumnTemplate>> listTemplate(DefaultColumnTemplateQuery req, UserIdentity identity) {
        return BaseResponse.success(defaultColumnService.listTemplate(req, identity.getUserId()));
    }

    @GetMapping("/detail/list")
    public BaseResponse<List<DefaultColumnDetail>> listDetail(DefaultColumnDetailQueryReq req, UserIdentity identity) {
        return BaseResponse.success(defaultColumnService.listDetail(req, identity.getUserId()));
    }

    @PostMapping("/detail/add")
    public BaseResponse<Object> addDetail(@RequestBody DefaultColumnDetailEditReq req, UserIdentity identity) {
        defaultColumnService.addDetail(req, identity.getUserId());
        return BaseResponse.success();
    }

    @PostMapping("/detail/update")
    public BaseResponse<Object> updateDetail(@RequestBody DefaultColumnDetailEditReq req, UserIdentity identity){
        defaultColumnService.updateDetail(req, identity.getUserId());
        return BaseResponse.success();
    }

}
