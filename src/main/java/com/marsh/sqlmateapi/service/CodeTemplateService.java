package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.CodeTemplateEditReq;
import com.marsh.sqlmateapi.controller.request.CodeTemplateFileEditReq;
import com.marsh.sqlmateapi.controller.request.CodeTemplateFileQueryReq;
import com.marsh.sqlmateapi.controller.request.TemplateQueryReq;
import com.marsh.sqlmateapi.domain.CodeTemplate;
import com.marsh.sqlmateapi.domain.CodeTemplateFile;
import com.marsh.sqlmateapi.mapper.CodeTemplateFileMapper;
import com.marsh.sqlmateapi.mapper.CodeTemplateMapper;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeTemplateService {

    private final CodeTemplateFileMapper codeTemplateFileMapper;
    private final CodeTemplateMapper codeTemplateMapper;

    public CodeTemplateService(CodeTemplateFileMapper codeTemplateFileMapper, CodeTemplateMapper codeTemplateMapper) {
        this.codeTemplateFileMapper = codeTemplateFileMapper;
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

    public void updateTemplate(CodeTemplateEditReq req, Integer userId) {
        var tpl = BeanUtil.transfer(req, CodeTemplate.class);
        codeTemplateMapper.updateById(tpl);

    }

    public void addFile(CodeTemplateFileEditReq req) {
        var file = BeanUtil.transfer(req, CodeTemplateFile.class);
        codeTemplateFileMapper.insert(file);
    }

    public List<CodeTemplateFile> listFile(CodeTemplateFileQueryReq req) {
        return codeTemplateFileMapper.selectList(new QueryWrapper<CodeTemplateFile>()
                .lambda()
                .eq(CodeTemplateFile::getTemplateId, req.getTemplateId()));
    }

    public List<CodeTemplateFile> getFile(CodeTemplateFileQueryReq req) {
        return codeTemplateFileMapper.selectList(new QueryWrapper<CodeTemplateFile>().lambda().eq(CodeTemplateFile::getTemplateId, req.getTemplateId()));
    }

    public void updateFile(CodeTemplateFileEditReq req, Integer userId) {
        var file = BeanUtil.transfer(req, CodeTemplateFile.class);
         codeTemplateFileMapper.updateById(file);
    }
}
