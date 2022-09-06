package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.*;
import com.marsh.sqlmateapi.domain.CodeTemplate;
import com.marsh.sqlmateapi.domain.CodeTemplateFile;
import com.marsh.sqlmateapi.service.CodeTemplateService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/codeTemplate")
public class CodeTemplateController {

    private final CodeTemplateService codeTemplateService;

    public CodeTemplateController(CodeTemplateService codeTemplateService) {
        this.codeTemplateService = codeTemplateService;
    }

    @GetMapping("/get")
    public BaseResponse<CodeTemplate> getTemplate(TemplateQueryReq req) {
        return BaseResponse.success(codeTemplateService.getTemplate(req));
    }

    @GetMapping("/list")
    public BaseResponse<List<CodeTemplate>> listTemplate(TemplateQueryReq req, UserIdentity identity) {
        return BaseResponse.success(codeTemplateService.listTemplate(req, identity.getUserId()));
    }

    @PostMapping("/add")
    public BaseResponse<Object> addTemplate(@RequestBody CodeTemplateEditReq req, UserIdentity identity) {
        codeTemplateService.createTemplate(req, identity.getUserId());
        return BaseResponse.success();
    }

    @PostMapping("/update")
    public BaseResponse<Object> updateTemplate(@RequestBody CodeTemplateEditReq req, UserIdentity identity) {
        codeTemplateService.updateTemplate(req, identity.getUserId());
        return BaseResponse.success();
    }

    @PostMapping("/file/add")
    public BaseResponse<Object> addFile(@RequestBody CodeTemplateFileEditReq req, UserIdentity identity) {
        codeTemplateService.addFile(req, identity.getUserId());
        return BaseResponse.success();
    }

    @GetMapping("/file/list")
    public BaseResponse<List<CodeTemplateFile>> listTemplateFile(CodeTemplateFileQueryReq req, UserIdentity identity) {
        return BaseResponse.success(codeTemplateService.listFile(req));
    }

    @GetMapping("/file/get")
    public BaseResponse<List<CodeTemplateFile>> getTemplateFile(CodeTemplateFileQueryReq req) {
        return BaseResponse.success(codeTemplateService.getFile(req));
    }

    @PostMapping("/file/update")
    public BaseResponse<Object> updateTemplateFile(@RequestBody CodeTemplateFileEditReq req, UserIdentity identity) {
        codeTemplateService.updateFile(req, identity.getUserId());
        return BaseResponse.success();
    }

    @PostMapping("/clone")
    public BaseResponse<Object> cloneTemplate(@RequestBody CloneCodeTemplateReq req, UserIdentity identity) {
        codeTemplateService.clone(req, identity.getUserId());
        return BaseResponse.success();
    }

}
