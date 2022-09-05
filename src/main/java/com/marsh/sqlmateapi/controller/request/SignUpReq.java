package com.marsh.sqlmateapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpReq {

    private String username;

    private String phone;

    private String password;

    private String code;

    private String email;


}
