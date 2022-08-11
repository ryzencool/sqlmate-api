package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.TeamEditReq;
import com.marsh.sqlmateapi.controller.request.TeamJoinReq;
import com.marsh.sqlmateapi.controller.request.TeamQueryReq;
import com.marsh.sqlmateapi.controller.request.TeamUrlGenerateReq;
import com.marsh.sqlmateapi.domain.TeamInfo;
import com.marsh.sqlmateapi.domain.TeamUser;
import com.marsh.sqlmateapi.mapper.TeamInfoMapper;
import com.marsh.sqlmateapi.mapper.TeamUserMapper;
import com.marsh.zutils.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamInfoMapper teamInfoMapper;

    private final TeamUserMapper teamUserMapper;

    public TeamService(TeamInfoMapper teamInfoMapper, TeamUserMapper teamUserMapper) {
        this.teamInfoMapper = teamInfoMapper;
        this.teamUserMapper = teamUserMapper;
    }


    public List<TeamInfo> listUserTeam(TeamQueryReq req) {

        return teamInfoMapper.selectList(new QueryWrapper<TeamInfo>().lambda().eq(req.getMasterId() != null, TeamInfo::getMasterId, req.getMasterId()));
    }

    public List<TeamUser> listTeamMember(TeamQueryReq req) {
        return teamUserMapper.selectList(new QueryWrapper<TeamUser>().lambda().eq(req.getId() != null, TeamUser::getId, req.getId()));
    }

    public void addTeam(TeamEditReq req) {
        var team = BeanUtil.transfer(req, TeamInfo.class);
        teamInfoMapper.insert(team);
    }

    public void joinTeam(TeamJoinReq req) {
        var tu = TeamUser.builder()
                .teamId(req.getTeamId())
                .userId(req.getUserId())
                .build();
        teamUserMapper.insert(tu);

    }

    public void generateTeamUrl(TeamUrlGenerateReq req) {

    }
}