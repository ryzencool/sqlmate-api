package com.marsh.sqlmateapi.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.QueryOptimizerReq;
import com.marsh.sqlmateapi.domain.ProjectDataSource;
import com.marsh.sqlmateapi.mapper.ProjectDataSourceMapper;
import com.marsh.sqlmateapi.service.dto.OptimizeDto;
import com.marsh.sqlmateapi.utils.SoarParser;
import com.marsh.sqlmateapi.utils.SqlExecutor;
import com.marsh.zutils.exception.BaseBizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OptimizeService {

    private final SqlExecutor sqlExecutor;
    private final ProjectDataSourceMapper projectDataSourceMapper;

    public OptimizeService(SqlExecutor sqlExecutor, ProjectDataSourceMapper projectDataSourceMapper) {
        this.sqlExecutor = sqlExecutor;
        this.projectDataSourceMapper = projectDataSourceMapper;
    }

    public OptimizeDto optimize(QueryOptimizerReq req) {
        var pds = projectDataSourceMapper.selectOne(new QueryWrapper<ProjectDataSource>()
                .lambda().eq(ProjectDataSource::getDbType, req.getDbType())
                .eq(ProjectDataSource::getProjectId, req.getProjectId()));
        var pgRes = sqlExecutor.optimize(req.getSql(), pds.getName(), pds.getDbType(), pds.getUsername(), pds.getPassword(), pds.getHost(), pds.getPort());

        var body = pgRes.body();
        var jsonBody = JSONObject.parseObject(body);
        var code = jsonBody.getString("code");

        if (!Objects.equals(code, "000000")) {
            throw new BaseBizException("执行错误");
        }

        var data = jsonBody.getString("data");
        OptimizeDto dto = new OptimizeDto();
        if (StringUtils.isNotEmpty(data)) {
            dto = SoarParser.parse(data);
        }


        return dto;

    }
}
