package com.marsh.sqlmateapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExplainDto {

    private List<KVDto> explainTable;

    private KVDto selectType;

    private KVDto type;

    private KVDto extra;
}
