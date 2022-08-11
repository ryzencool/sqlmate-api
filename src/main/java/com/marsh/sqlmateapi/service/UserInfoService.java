package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.SignInReq;
import com.marsh.sqlmateapi.controller.request.SignUpReq;
import com.marsh.sqlmateapi.controller.response.AuthResp;
import com.marsh.sqlmateapi.domain.UserInfo;
import com.marsh.sqlmateapi.exception.ErrorCode;
import com.marsh.sqlmateapi.mapper.SignUpCodeMapper;
import com.marsh.sqlmateapi.mapper.UserInfoMapper;
import com.marsh.sqlmateapi.mapper.param.UserInfoGetParam;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.exception.BaseBizException;
import com.marsh.zutils.helper.JwtHelper;
import com.marsh.zutils.util.DateUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserInfoService {

    private final UserInfoMapper userInfoMapper;

    private final JwtHelper jwtHelper;

    private final SignUpCodeMapper signUpCodeMapper;

    public UserInfoService(UserInfoMapper userInfoMapper, JwtHelper jwtHelper, SignUpCodeMapper signUpCodeMapper) {
        this.userInfoMapper = userInfoMapper;
        this.jwtHelper = jwtHelper;
        this.signUpCodeMapper = signUpCodeMapper;
    }

    public AuthResp signUp(SignUpReq req) {
//        var codeObj = signUpCodeMapper.selectOne(new QueryWrapper<SignUpCode>().lambda().eq(SignUpCode::getPhone, req
//                .getPhone()));
//        if (codeObj == null) {
//            throw new BaseBizException(ErrorCode.ERROR_CAPTCHA_CODE);
//        }
//        if (!Objects.equals(req.getCode(), codeObj.getCode())) {
//            throw new BaseBizException(ErrorCode.ERROR_CAPTCHA_CODE);
//        }

        var user = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getPhone, req.getPhone()));
        if (user != null) {
            throw new BaseBizException(ErrorCode.USER_IS_EXIST);
        }
        var encoder = new BCryptPasswordEncoder();
        var passwordEncoded = encoder.encode(req.getPassword());
        userInfoMapper.insert(UserInfo.builder()
                .phone(req.getPhone())
                .password(passwordEncoded)
                .email(req.getEmail())
                .build());
        var userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getPhone, req.getPhone()));
        if (userInfo == null) {
            throw new BaseBizException(ErrorCode.USER_NOT_EXIST);
        }
        var token = jwtHelper.encode("login token", 30, "sqlmate", Map.of("userId", userInfo.getId().toString()));
        userInfo.setToken(token);
        var expiredTime = LocalDateTime.now().plusDays(30);
        userInfo.setExpiredTime(expiredTime);
        userInfoMapper.updateById(userInfo);
        return AuthResp.builder().token(token).expiredTime(DateUtil.toMilli(expiredTime)).build();
    }

    public AuthResp signIn(SignInReq req) {
        var user = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getPhone, req.getPhone()));
        if (user == null) {
            throw new BaseBizException(ErrorCode.USER_NOT_EXIST);
        }
        var encoder = new BCryptPasswordEncoder();
        var isCorrect = encoder.matches(req.getPassword(), user.getPassword());
        if (!isCorrect) {
            throw new BaseBizException(ErrorCode.ERROR_PASSWORD);
        }
        String token = null;
        LocalDateTime expiredTime = null;
        if (user.getExpiredTime().isBefore(LocalDateTime.now())) {
            // 过期了，重新生成一个
            token = jwtHelper.encode("login token", 30, "sqlmate", Map.of("userId", String.valueOf(user.getId())));
            user.setToken(token);
            expiredTime = LocalDateTime.now().plusDays(30);
            user.setExpiredTime(expiredTime);
            // 更新token
            userInfoMapper.updateById(user);

        } else {
            token = user.getToken();
            expiredTime = user.getExpiredTime();
        }
        return AuthResp.builder().token(token).expiredTime(DateUtil.toMilli(expiredTime)).build();
    }

    public UserInfo userInfo(UserIdentity user) {
        return userInfoMapper.getUserInfo(UserInfoGetParam.builder().id(user.getUserId()).build());
    }
}
