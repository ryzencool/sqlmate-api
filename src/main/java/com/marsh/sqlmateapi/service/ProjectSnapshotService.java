package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.SnapshotDelReq;
import com.marsh.sqlmateapi.controller.request.SnapshotEditReq;
import com.marsh.sqlmateapi.controller.request.SnapshotQueryReq;
import com.marsh.sqlmateapi.domain.ProjectSnapshot;
import com.marsh.sqlmateapi.mapper.ProjectSnapshotMapper;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectSnapshotService {

    private final ProjectSnapshotMapper projectSnapshotMapper;

    public ProjectSnapshotService(ProjectSnapshotMapper projectSnapshotMapper) {
        this.projectSnapshotMapper = projectSnapshotMapper;
    }

    public List<ProjectSnapshot> listSnapshot(SnapshotQueryReq req, Integer userId) {
        return projectSnapshotMapper.selectList(new QueryWrapper<ProjectSnapshot>().lambda()
                .eq(ProjectSnapshot::getProjectId, req.getProjectId())
                .and(req.getName() != null || req.getNote() != null, (data) -> data.like(req.getName() != null, ProjectSnapshot::getName, req.getName())
                        .or()
                        .like(req.getNote() != null, ProjectSnapshot::getNote, req.getNote()))
                .orderByDesc(ProjectSnapshot::getCreateTime)
        );
    }

    public void createSnapshot(SnapshotEditReq req, Integer userId) {
        var snap = BeanUtil.transfer(req, ProjectSnapshot.class);
        snap.setCreateTime(LocalDateTime.now());
        snap.setCreateId(userId);
        projectSnapshotMapper.insert(snap);
    }

    public void deleteSnapshot(SnapshotDelReq req, Integer userId) {
        projectSnapshotMapper.deleteById(req.getId());
    }


}
