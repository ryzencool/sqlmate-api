package com.marsh.sqlmateapi.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marsh.sqlmateapi.controller.request.*;
import com.marsh.sqlmateapi.domain.TeamInfo;
import com.marsh.sqlmateapi.domain.TeamUser;
import com.marsh.sqlmateapi.mapper.TeamInfoMapper;
import com.marsh.sqlmateapi.mapper.TeamUserMapper;
import com.marsh.sqlmateapi.mapper.result.TeamUserResult;
import com.marsh.sqlmateapi.service.dto.TeamJoinDto;
import com.marsh.sqlmateapi.utils.CryptString;
import com.marsh.zutils.util.BeanUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
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

    @Transactional
    public void insertTeamUser(Integer teamId, Integer userId) {
        teamUserMapper.insert(TeamUser.builder()
                        .teamId(teamId)
                        .userId(userId)
                .build());
    }

    @Transactional
    @SneakyThrows
    public Integer joinTeam(TeamJoinReq req) {
        String password = "7e6659eb-b45b-4e28-957b-e346164112b8";
        DESKeySpec key = new DESKeySpec(password.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        CryptString crypt = new CryptString(keyFactory.generateSecret(key));

        var res = crypt.decryptBase64(req.getKey());

        var mapper = new ObjectMapper();

        var dt = mapper.readValue(res, TeamJoinDto.class);

        var tu = TeamUser.builder()
                .teamId(dt.getTeamId())
                .userId(dt.getUserId())
                .build();
        teamUserMapper.insert(tu);

        return dt.getTeamId();


    }

    @SneakyThrows
    public String generateTeamUrl(TeamUrlGenerateReq req, Integer userId) {
        String password = "7e6659eb-b45b-4e28-957b-e346164112b8";
        DESKeySpec key = new DESKeySpec(password.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        CryptString crypt = new CryptString(keyFactory.generateSecret(key));
        var mapper = new ObjectMapper();

        var str = mapper.writeValueAsString(TeamJoinDto.builder()
                .teamId(req.getTeamId())
                .userId(userId)
                .time(System.currentTimeMillis())
                .build());


        return crypt.encryptBase64(str);
    }
}
