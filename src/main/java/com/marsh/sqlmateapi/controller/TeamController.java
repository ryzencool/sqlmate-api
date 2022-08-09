package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.TeamEditReq;
import com.marsh.sqlmateapi.controller.request.TeamJoinReq;
import com.marsh.sqlmateapi.controller.request.TeamQueryReq;
import com.marsh.sqlmateapi.controller.request.TeamUrlGenerateReq;
import com.marsh.sqlmateapi.domain.TeamInfo;
import com.marsh.sqlmateapi.domain.TeamUser;
import com.marsh.sqlmateapi.service.TeamService;
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
    public BaseResponse<List<TeamInfo>> listUserTeam(TeamQueryReq req) {
        return BaseResponse.success(teamService.listUserTeam(req));
    }

    @GetMapping("/member/list")
    public BaseResponse<List<TeamUser>> listMember(TeamQueryReq req) {
        return BaseResponse.success(teamService.listTeamMember(req));
    }

    @PostMapping("/add")
    public BaseResponse<Object> addTeam(@RequestBody TeamEditReq req) {
        teamService.addTeam(req);
        return BaseResponse.success();
    }

    @GetMapping("/join")
    public BaseResponse<Object> joinTeam(TeamJoinReq req) {
        teamService.joinTeam(req);
        return BaseResponse.success();
    }

    @GetMapping("/generateUrl")
    public void generateUrl(TeamUrlGenerateReq req) {
        teamService.generateTeamUrl(req);
        // redirect to
    }

}
