package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.config.SqlExecutorProperties;
import com.marsh.sqlmateapi.controller.request.SignInReq;
import com.marsh.sqlmateapi.controller.request.SignUpReq;
import com.marsh.sqlmateapi.controller.request.TeamEditReq;
import com.marsh.sqlmateapi.controller.request.TeamJoinReq;
import com.marsh.sqlmateapi.controller.response.AuthResp;
import com.marsh.sqlmateapi.domain.DatabaseUser;
import com.marsh.sqlmateapi.domain.UserInfo;
import com.marsh.sqlmateapi.exception.ErrorCode;
import com.marsh.sqlmateapi.mapper.DatabaseUserMapper;
import com.marsh.sqlmateapi.mapper.SignUpCodeMapper;
import com.marsh.sqlmateapi.mapper.UserInfoMapper;
import com.marsh.sqlmateapi.mapper.param.UserInfoGetParam;
import com.marsh.sqlmateapi.utils.RemoteCallUtil;
import com.marsh.sqlmateapi.utils.SqlExecutor;
import com.marsh.zutils.auth.UserIdentity;
import com.marsh.zutils.exception.BaseBizException;
import com.marsh.zutils.helper.JwtHelper;
import com.marsh.zutils.util.DateUtil;
import com.marsh.zutils.util.UUIDUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserInfoService {

    private final UserInfoMapper userInfoMapper;

    private final JwtHelper jwtHelper;

    private final SignUpCodeMapper signUpCodeMapper;

    private final JdbcTemplate jdbcTemplate;

    private final TeamService teamService;

    private final SqlExecutorProperties sqlExecutorProperties;

    private final SqlExecutor sqlExecutor;
    private final DatabaseUserMapper databaseUserMapper;

    public UserInfoService(UserInfoMapper userInfoMapper,
                           JwtHelper jwtHelper,
                           SignUpCodeMapper signUpCodeMapper,
                           JdbcTemplate jdbcTemplate,
                           TeamService teamService, SqlExecutorProperties sqlExecutorProperties, SqlExecutor sqlExecutor, DatabaseUserMapper databaseUserMapper) {
        this.userInfoMapper = userInfoMapper;
        this.jwtHelper = jwtHelper;
        this.signUpCodeMapper = signUpCodeMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.teamService = teamService;
        this.sqlExecutorProperties = sqlExecutorProperties;
        this.sqlExecutor = sqlExecutor;
        this.databaseUserMapper = databaseUserMapper;
    }

    @Transactional(rollbackFor = Exception.class)
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
                        .username(req.getUsername())
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
        // 默认团队
        var team = teamService.addTeam(TeamEditReq.builder()
                .masterId(userInfo.getId())
                .name("我的团队")
                .build(), userInfo.getId());
        teamService.insertTeamUser(team.getId(), userInfo.getId());

        // 创建schema

        createDBUser(userInfo);
        return AuthResp.builder().token(token).expiredTime(DateUtil.toMilli(expiredTime)).build();
    }


    private void createDBUser(UserInfo userInfo) {
        var dbUsername = "user_" + userInfo.getPhone();
        var password = UUIDUtil.cleanLowerUUID();
        var pgSql = String.format("CREATE USER %s WITH PASSWORD '%s'", dbUsername, password);
        try (var pgRes = sqlExecutor.sendPgMainSql(pgSql)) {
            RemoteCallUtil.handleResponse(pgRes);
        }
        databaseUserMapper.insert(DatabaseUser.builder()
                .userId(userInfo.getId())
                .dbType(2)
                .password(password)
                .username(dbUsername)
                .build());

        var myDbUsername = "user_" + userInfo.getPhone();
        var myPassword = UUIDUtil.cleanLowerUUID();
        var mySql = String.format("create user '%s'@'localhost' identified by '%s'", myDbUsername, myPassword);
        try (var myResult = sqlExecutor.sendMysqlMainSql(mySql)) {
            RemoteCallUtil.handleResponse(myResult);
        }
        databaseUserMapper.insert(DatabaseUser.builder()
                .userId(userInfo.getId())
                .dbType(1)
                .password(myPassword)
                .username(myDbUsername)
                .build());
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
