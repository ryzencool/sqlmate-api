package com.marsh.sqlmateapi.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SqlCheckResult {

    private String title;

    private String content;

    private String advice;
}
