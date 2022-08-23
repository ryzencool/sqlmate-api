package com.marsh.sqlmateapi.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectCondition {

    private String tableName;

    private String columnName;

    private String operator;
}
