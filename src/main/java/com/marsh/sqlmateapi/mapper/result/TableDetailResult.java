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

    private String defaultValue;

    private Boolean isAutoIncrement;

    private Boolean isPrimaryKey;

    private Boolean isUniqueKey;

    private Boolean isNotNull;

    private Integer tableId;

    private String tableName;
}
