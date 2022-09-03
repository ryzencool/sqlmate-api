package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.*;
import com.marsh.sqlmateapi.domain.TeamInfo;
import com.marsh.sqlmateapi.domain.TeamUser;
import com.marsh.sqlmateapi.mapper.TeamInfoMapper;
import com.marsh.sqlmateapi.mapper.TeamUserMapper;
import com.marsh.sqlmateapi.mapper.result.TeamUserResult;
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


    public List<TeamInfo> listUserTeam(TeamQueryReq req, Integer userId) {
        var myTeams = teamInfoMapper.selectList(new QueryWrapper<TeamInfo>().lambda().eq(TeamInfo::getMasterId, userId));



        return teamInfoMapper.selectList(new QueryWrapper<TeamInfo>().lambda()
                .eq(req.getMasterId() != null, TeamInfo::getMasterId,
                        req.getMasterId()).eq(TeamInfo::getMasterId, userId));
    }

    public List<TeamUserResult> listTeamMember(TeamUserQueryReq req, Integer userId) {
        return teamUserMapper.listUser(req.getTeamId());


    }

    public TeamInfo addTeam(TeamEditReq req, Integer userId) {
        var team = BeanUtil.transfer(req, TeamInfo.class);
        team.setMasterId(userId);
        teamInfoMapper.insert(team);

        return teamInfoMapper.selectOne(new QueryWrapper<TeamInfo>()
                .lambda().eq(TeamInfo::getMasterId, userId)
                .eq(TeamInfo::getName, req.getName()));

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
