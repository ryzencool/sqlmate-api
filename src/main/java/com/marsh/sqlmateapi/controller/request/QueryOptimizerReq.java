package com.marsh.sqlmateapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryOptimizerReq {

    private Integer id;

    private Integer projectId;

    private String sql;
}
