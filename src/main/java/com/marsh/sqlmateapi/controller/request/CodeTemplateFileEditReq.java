package com.marsh.sqlmateapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeTemplateFileEditReq {

    private Integer id;

    private Integer templateId;

    private String fileName;

    private String fileType;

    private String transferFn;

    private String content;
}
