package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.*;
import com.marsh.sqlmateapi.domain.DefaultColumnDetail;
import com.marsh.sqlmateapi.domain.DefaultColumnTemplate;
import com.marsh.sqlmateapi.mapper.DefaultColumnDetailMapper;
import com.marsh.sqlmateapi.mapper.DefaultColumnTemplateMapper;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DefaultColumnService {

    private final DefaultColumnTemplateMapper defaultColumnTemplateMapper;

    private final DefaultColumnDetailMapper defaultColumnDetailMapper;

    public DefaultColumnService(DefaultColumnTemplateMapper defaultColumnTemplateMapper, DefaultColumnDetailMapper defaultColumnDetailMapper) {
        this.defaultColumnTemplateMapper = defaultColumnTemplateMapper;
        this.defaultColumnDetailMapper = defaultColumnDetailMapper;
    }

    public void addTemplate(DefaultColumnTemplateEditReq req, Integer userId) {
        var tpl = BeanUtil.transfer(req, DefaultColumnTemplate.class);
        tpl.setOwnerId(userId);
        defaultColumnTemplateMapper.insert(tpl);
    }

    public void updateTemplate(DefaultColumnTemplateEditReq req, Integer userId) {
        var tpl = BeanUtil.transfer(req, DefaultColumnTemplate.class);
        defaultColumnTemplateMapper.updateById(tpl);
    }


    public List<DefaultColumnTemplate> listTemplate(DefaultColumnTemplateQuery req, Integer userId) {
        return defaultColumnTemplateMapper.selectList(new QueryWrapper<DefaultColumnTemplate>().lambda().eq(req.getName() != null, DefaultColumnTemplate::getName, req.getName()));
    }

    public List<DefaultColumnDetail> listDetail(DefaultColumnDetailQueryReq req, Integer userId) {
        return defaultColumnDetailMapper.selectList(new QueryWrapper<DefaultColumnDetail>().lambda().eq(DefaultColumnDetail::getTemplateId, req.getTemplateId()));
    }

    public void addDetail(DefaultColumnDetailEditReq req, Integer userId) {
        var detail = BeanUtil.transfer(req, DefaultColumnDetail.class);

        defaultColumnDetailMapper.insert(detail);
    }

    public void updateDetail(DefaultColumnDetailEditReq req, Integer userId) {
        var detail = BeanUtil.transfer(req, DefaultColumnDetail.class);
        defaultColumnDetailMapper.updateById(detail);
    }

    @Transactional
    public void clone(CloneDefaultColumnReq req, Integer userId) {
        defaultColumnTemplateMapper.insert(DefaultColumnTemplate.builder()
                .name(req.getName())
                .note(req.getNote())
                .ownerId(userId)
                .build());

        var template = defaultColumnTemplateMapper
                .selectOne(new QueryWrapper<DefaultColumnTemplate>()
                        .lambda()
                        .eq(DefaultColumnTemplate::getName, req.getName())
                        .eq(DefaultColumnTemplate::getOwnerId, userId));

        var details = defaultColumnDetailMapper.selectList(new QueryWrapper<DefaultColumnDetail>().lambda().eq(DefaultColumnDetail::getTemplateId, req.getTemplateId()));

        for (DefaultColumnDetail detail : details) {
            defaultColumnDetailMapper.insert(DefaultColumnDetail.builder()
                            .comment(detail.getComment())
                            .defaultValue(detail.getDefaultValue())
                            .isAutoIncrement(detail.getIsAutoIncrement())
                            .isPrimaryKey(detail.getIsPrimaryKey())
                            .isNull(detail.getIsPrimaryKey())
                            .name(detail.getName())
                            .note(detail.getNote())
                            .comment(detail.getComment())
                            .isUniqueKey(detail.getIsUniqueKey())
                            .templateId(template.getId())
                            .type(detail.getType())
                    .build());
        }


    }
}
