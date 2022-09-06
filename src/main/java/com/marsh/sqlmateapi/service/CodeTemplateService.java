package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.*;
import com.marsh.sqlmateapi.domain.CodeTemplate;
import com.marsh.sqlmateapi.domain.CodeTemplateFile;
import com.marsh.sqlmateapi.mapper.CodeTemplateFileMapper;
import com.marsh.sqlmateapi.mapper.CodeTemplateMapper;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    public List<CodeTemplate> listTemplate(TemplateQueryReq req, Integer userId) {
        return codeTemplateMapper.selectList(new QueryWrapper<CodeTemplate>().lambda()
                        .in(CodeTemplate::getOwnerId, List.of(userId, 0))
                .eq(CodeTemplate::getIsDel, false));
    }

    public void createTemplate(CodeTemplateEditReq req, Integer userId) {
        var tpl = BeanUtil.transfer(req, CodeTemplate.class);
        tpl.setOwnerId(userId);
        tpl.setCreateTime(LocalDateTime.now());
        codeTemplateMapper.insert(tpl);
    }

    public void updateTemplate(CodeTemplateEditReq req, Integer userId) {
        var tpl = BeanUtil.transfer(req, CodeTemplate.class);
        tpl.setUpdateId(userId);
        tpl.setUpdateTime(LocalDateTime.now());
        codeTemplateMapper.updateById(tpl);

    }

    public void addFile(CodeTemplateFileEditReq req, Integer userId) {
        var file = BeanUtil.transfer(req, CodeTemplateFile.class);
        file.setCreateTime(LocalDateTime.now());
        file.setCreateId(userId);
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
        file.setUpdateId(userId);
        file.setUpdateTime(LocalDateTime.now());
        codeTemplateFileMapper.updateById(file);
    }

    @Transactional
    public void clone(CloneCodeTemplateReq req, Integer userId) {
        var oldTemplate = codeTemplateMapper.selectById(req.getTemplateId());

        codeTemplateMapper.insert(CodeTemplate.builder()
                .name(req.getName())
                .lang(req.getLang())
                .transferFn(oldTemplate.getTransferFn())
                .ownerId(userId)
                .build());
        var codeTemplate = codeTemplateMapper.selectOne(new QueryWrapper<CodeTemplate>()
                .lambda().eq(CodeTemplate::getName, req.getName())
                .eq(CodeTemplate::getOwnerId, userId));

        var templateFiles = codeTemplateFileMapper.selectList(new QueryWrapper<CodeTemplateFile>().lambda().eq(CodeTemplateFile::getTemplateId, req.getTemplateId()));


        for (CodeTemplateFile templateFile : templateFiles) {
            codeTemplateFileMapper.insert(CodeTemplateFile.builder()
                    .content(templateFile.getContent())
                    .fileName(templateFile.getFileName())
                    .fileType(templateFile.getFileType())
                    .templateId(codeTemplate.getId())
                    .build());
        }
    }
}
