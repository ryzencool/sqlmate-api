package com.marsh.sqlmateapi.mapper.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableDetailParam {

    private Integer projectId;

    private String tableName;

    private Integer tableId;
}
