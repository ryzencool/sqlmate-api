package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.*;
import com.marsh.sqlmateapi.domain.TeamInfo;
import com.marsh.sqlmateapi.mapper.result.TeamUserResult;
import com.marsh.sqlmateapi.service.TeamService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/list")
    public BaseResponse<List<TeamInfo>> listUserTeam(TeamQueryReq req, UserIdentity identity) {
        return BaseResponse.success(teamService.listUserTeam(req , identity.getUserId()));
    }

    @GetMapping("/member/list")
    public BaseResponse<List<TeamUserResult>> listMember(TeamUserQueryReq req, UserIdentity identity) {
        return BaseResponse.success(teamService.listTeamMember(req, identity.getUserId()));
    }

    @PostMapping("/add")
    public BaseResponse<Object> addTeam(@RequestBody TeamEditReq req, UserIdentity identity) {
        teamService.addTeam(req , identity.getUserId());
        return BaseResponse.success();
    }

    @PostMapping("/join")
    public BaseResponse<Integer> joinTeam(@RequestBody TeamJoinReq req) {
        return BaseResponse.success(teamService.joinTeam(req));
    }

    @GetMapping("/generateUrl")
    public BaseResponse<String> generateUrl(TeamUrlGenerateReq req, UserIdentity identity) {
        var res = teamService.generateTeamUrl(req, identity.getUserId());
        return BaseResponse.success(res);

        // redirect to
    }

}
