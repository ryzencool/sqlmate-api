package com.marsh.sqlmateapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColumnRelationShip {

    private List<FullTableRelDto> leftColumns;

    private List<FullTableRelDto> rightColumns;
}
