package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.TemplateQueryReq;
import com.marsh.sqlmateapi.domain.CodeTemplate;
import com.marsh.sqlmateapi.service.CodeTemplateService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
