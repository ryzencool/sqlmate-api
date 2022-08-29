package com.marsh.sqlmateapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSqlEditReq {

    private Integer id;
    private Integer projectId;

    private String functionName;

    private Boolean isFavorite;

    private String name;

    private String sql;
}
