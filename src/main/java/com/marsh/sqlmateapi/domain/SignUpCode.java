package com.marsh.sqlmateapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpCode {

    private Integer phone;

    private String code;

    private LocalDateTime createTime;

    private Long validTime;
}
