package com.marsh.sqlmateapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullTableRelDto {

    private Integer id;

    private Integer leftTableId;

    private String leftTableName;

    private Integer rightTableId;

    private String rightTableName;

    private Long leftColumnId;

    private String leftColumnName;

    private Long rightColumnId;

    private String rightColumnName;
}
