package com.marsh.sqlmateapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marsh.sqlmateapi.domain.TeamUser;
import com.marsh.sqlmateapi.mapper.result.TeamUserResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeamUserMapper extends BaseMapper<TeamUser> {

     List<TeamUserResult> listUser(@Param("teamId") Integer teamId);
}
