package com.marsh.sqlmateapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamJoinDto {

    private Integer teamId;

    private Integer userId;

    private Long time;
}
