package com.marsh.sqlmateapi.mapper.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableDetailResult {

    private Long columnId;

    private String columnName;

    private String columnType;

    private Integer tableId;

    private String tableName;
}
