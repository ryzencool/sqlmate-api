package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.CodeTemplateEditReq;
import com.marsh.sqlmateapi.controller.request.CodeTemplateFileEditReq;
import com.marsh.sqlmateapi.controller.request.CodeTemplateFileQueryReq;
import com.marsh.sqlmateapi.controller.request.TemplateQueryReq;
import com.marsh.sqlmateapi.domain.CodeTemplate;
import com.marsh.sqlmateapi.domain.CodeTemplateFile;
import com.marsh.sqlmateapi.service.CodeTemplateService;
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
    public BaseResponse<List<CodeTemplate>> listTemplate(TemplateQueryReq req) {
        return BaseResponse.success(codeTemplateService.listTemplate(req));
    }

    @PostMapping("/add")
    public BaseResponse<Object> addTemplate(@RequestBody CodeTemplateEditReq req) {
        codeTemplateService.createTemplate(req);
        return BaseResponse.success();
    }

    @PostMapping("/file/add")
    public BaseResponse<Object> addFile(@RequestBody CodeTemplateFileEditReq req) {
        codeTemplateService.addFile(req);
        return BaseResponse.success();
    }

    @GetMapping("/file/list")
    public BaseResponse<List<CodeTemplateFile>> listTemplateFile(CodeTemplateFileQueryReq req) {
        return BaseResponse.success(codeTemplateService.listFile(req));
    }

    @GetMapping("/file/get")
    public BaseResponse<List<CodeTemplateFile>> getTemplateFile(CodeTemplateFileQueryReq req) {
        return BaseResponse.success(codeTemplateService.getFile(req));
    }
}