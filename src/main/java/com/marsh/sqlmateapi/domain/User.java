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
public class User {

    @TableId
    private Integer id;

    private String username;

    private String email;

    private String password;

    private String phone;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer createId;

    private Integer updateId;

    private Boolean isDel;
}
