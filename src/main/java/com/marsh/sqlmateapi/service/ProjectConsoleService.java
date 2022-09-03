package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.SyncConsoleReq;
import com.marsh.sqlmateapi.domain.ProjectConsole;
import com.marsh.sqlmateapi.mapper.ProjectConsoleMapper;
import org.springframework.stereotype.Service;

@Service
public class ProjectConsoleService {

    private final ProjectConsoleMapper projectConsoleMapper;

    public ProjectConsoleService(ProjectConsoleMapper projectConsoleMapper) {
        this.projectConsoleMapper = projectConsoleMapper;
    }

    public void syncConsole(SyncConsoleReq req, Integer userId) {
        var console = projectConsoleMapper.selectOne(new QueryWrapper<ProjectConsole>().lambda().eq(ProjectConsole::getProjectId, req.getProjectId()));
        if (console == null) {
            projectConsoleMapper.insert(ProjectConsole.builder()
                    .projectId(req.getProjectId())
                    .content(req.getContent())
                    .build());
        } else {
            console.setContent(req.getContent());
            projectConsoleMapper.updateById(console);
        }
    }

    public ProjectConsole getConsole(Integer projectId) {
        var console = projectConsoleMapper.selectOne(new QueryWrapper<ProjectConsole>().lambda().eq(ProjectConsole::getProjectId, projectId));
        if (console == null) {
            console = ProjectConsole.builder()
                    .projectId(projectId)
                    .content("")
                    .build();
            projectConsoleMapper.insert(console);
        }
        return console;
    }
}
