package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.controller.request.SignInReq;
import com.marsh.sqlmateapi.controller.request.SignUpReq;
import com.marsh.sqlmateapi.controller.response.AuthResp;
import com.marsh.sqlmateapi.domain.SignUpCode;
import com.marsh.sqlmateapi.domain.UserInfo;
import com.marsh.sqlmateapi.exception.ErrorCode;
import com.marsh.sqlmateapi.mapper.SignUpCodeMapper;
import com.marsh.sqlmateapi.mapper.UserInfoMapper;
import com.marsh.sqlmateapi.utils.JwtHelper;
import com.marsh.zutils.exception.BaseBizException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Service
public class UserInfoService {

    private final UserInfoMapper userInfoMapper;

    private final SignUpCodeMapper signUpCodeMapper;

    public UserInfoService(UserInfoMapper userInfoMapper, SignUpCodeMapper signUpCodeMapper) {
        this.userInfoMapper = userInfoMapper;
        this.signUpCodeMapper = signUpCodeMapper;
    }

    public AuthResp signUp(SignUpReq req) {
        var codeObj = signUpCodeMapper.selectOne(new QueryWrapper<SignUpCode>().lambda().eq(SignUpCode::getPhone, req
                .getPhone()));
        if (codeObj == null) {
            throw new BaseBizException(ErrorCode.ERROR_CAPTCHA_CODE);
        }
        if (!Objects.equals(req.getCode(), codeObj.getCode())) {
            throw new BaseBizException(ErrorCode.ERROR_CAPTCHA_CODE);
        }

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
        var token = JwtHelper.encode("login token", 30, "sqlmate", Map.of("userId", String.valueOf(userInfo.getId())));
        return AuthResp.builder().token(token).build();
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
        String token;
        if (user.getExpiredTime().isBefore(LocalDateTime.now())) {
            // 过期了，重新生成一个
            token = JwtHelper.encode("login token", 30, "sqlmate", Map.of("userId", String.valueOf(user.getId())));
            user.setToken(token);
            user.setExpiredTime(LocalDateTime.now().plusDays(30));
            // 更新token
            userInfoMapper.updateById(user);
        } else {
            token = user.getToken();
        }
        return AuthResp.builder().token(token).build();
    }
}
