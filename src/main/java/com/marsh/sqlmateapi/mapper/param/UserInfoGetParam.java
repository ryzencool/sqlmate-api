package com.marsh.sqlmateapi.mapper.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoGetParam {

    private Integer id;

    private String phone;

    private String email;
}
