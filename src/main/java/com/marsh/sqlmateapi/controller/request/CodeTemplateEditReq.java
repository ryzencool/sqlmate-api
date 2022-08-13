package com.marsh.sqlmateapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeTemplateEditReq {

    private Integer id;

    private String name;

    private String lang;

    private Integer projectId;

    private String transferFn;
}
