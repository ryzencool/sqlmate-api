package com.marsh.sqlmateapi.mapper.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamUserResult {

    private String username;

    private Integer userId;

    private String teamId;

    private LocalDateTime joinTime;
}
