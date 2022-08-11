package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.SignInReq;
import com.marsh.sqlmateapi.controller.request.SignUpReq;
import com.marsh.sqlmateapi.controller.response.AuthResp;
import com.marsh.sqlmateapi.domain.UserInfo;
import com.marsh.sqlmateapi.service.UserInfoService;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserInfoService userInfoService;

    public UserController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("/signUp")
    public BaseResponse<AuthResp> signUp(@RequestBody SignUpReq req) {
        var res = userInfoService.signUp(req);
        return BaseResponse.success(res);

    }

    @PostMapping("/signIn")
    public BaseResponse<AuthResp> signIn(@RequestBody SignInReq req) {
        var res = userInfoService.signIn(req);
        return BaseResponse.success(res);
    }

    @GetMapping("/userInfo")
    public BaseResponse<UserInfo> userInfo(UserIdentity user) {
        return BaseResponse.success(userInfoService.userInfo(user));
    }
}
