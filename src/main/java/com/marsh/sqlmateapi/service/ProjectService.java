package com.marsh.sqlmateapi.service;

import com.marsh.sqlmateapi.controller.request.AddProjectReq;
import com.marsh.sqlmateapi.controller.request.ProjectQueryReq;
import com.marsh.sqlmateapi.domain.ProjectInfo;
import com.marsh.sqlmateapi.mapper.ProjectInfoMapper;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectInfoMapper projectInfoMapper;

    public ProjectService(ProjectInfoMapper projectInfoMapper) {
        this.projectInfoMapper = projectInfoMapper;
    }

    public void AddProject(AddProjectReq req) {
        var project = BeanUtil.transfer(req, ProjectInfo.class);
        projectInfoMapper.insert(project);

    }

    public List<ProjectInfo> listProject(ProjectQueryReq req) {
        return null;
    }

    public ProjectInfo getProject(Integer projectId ) {
        return projectInfoMapper.selectById(projectId);
    }
}
