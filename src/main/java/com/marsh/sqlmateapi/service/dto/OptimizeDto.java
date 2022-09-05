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
public class OptimizeDto {

    private ExplainDto explain;

    private List<KVDto> checks;

    private List<IndexRecommendDto> indexRecommend;

    private Integer score;


}
