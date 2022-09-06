package com.marsh.sqlmateapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColumnSimpleDto {

    private Long id;

    private String name;

    private String type;

    private String defaultValue;

    private Boolean isNotNull;

    private Boolean isAutoIncrement;

    private Boolean isUniqueKey;

    private Boolean isPrimaryKey;
}
