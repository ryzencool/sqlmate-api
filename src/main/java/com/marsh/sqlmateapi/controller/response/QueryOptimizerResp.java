package com.marsh.sqlmateapi.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryOptimizerResp {

    private SoarResult soarResult;

    private SqlCheckResult[] scResult;
}
