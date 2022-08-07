package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.CodeTemplateEditReq;
import com.marsh.sqlmateapi.controller.request.TemplateQueryReq;
import com.marsh.sqlmateapi.domain.CodeTemplate;
import com.marsh.sqlmateapi.mapper.CodeTemplateMapper;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeTemplateService {

    private final CodeTemplateMapper codeTemplateMapper;

    public CodeTemplateService(CodeTemplateMapper codeTemplateMapper) {
        this.codeTemplateMapper = codeTemplateMapper;
    }


    public CodeTemplate getTemplate(TemplateQueryReq req) {
        return codeTemplateMapper.selectById(req.getId());
    }

    public List<CodeTemplate> listTemplate(TemplateQueryReq req) {
        return codeTemplateMapper.selectList(new QueryWrapper<CodeTemplate>().lambda()
                .eq(CodeTemplate::getIsDel, false));
    }

    public void createTemplate(CodeTemplateEditReq req) {
        var tpl = BeanUtil.transfer(req, CodeTemplate.class);
        codeTemplateMapper.insert(tpl);
    }

    public void updateTemplate(CodeTemplateEditReq req) {
        var tpl = BeanUtil.transfer(req, CodeTemplate.class);
    }
}
