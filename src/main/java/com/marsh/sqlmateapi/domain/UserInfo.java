package com.marsh.sqlmateapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {

    @TableId
    private Integer id;

    private String username;

    private String email;

    private String password;

    private String phone;

    /**
     * 等级 0 无会员
     * 等级 1 会员
     * 等级 2 会员
     * 等级 3 会员
     */
    private Integer level;

    private String token;


    private LocalDateTime expiredTime;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer createId;

    private Integer updateId;

    private Boolean isDel;
}
