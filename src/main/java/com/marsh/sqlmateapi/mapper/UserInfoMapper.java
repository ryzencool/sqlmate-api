package com.marsh.sqlmateapi.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marsh.sqlmateapi.domain.UserInfo;
import com.marsh.sqlmateapi.mapper.param.UserInfoGetParam;

public interface UserInfoMapper extends BaseMapper<UserInfo> {
    UserInfo getUserInfo(UserInfoGetParam param);
}
