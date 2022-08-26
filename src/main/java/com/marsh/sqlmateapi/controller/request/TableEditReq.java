package com.marsh.sqlmateapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableEditReq {

    private Integer id;

    private String name;

    private String note;

    private Integer defaultColumnTemplateId;

    private String comment;

    private Integer projectId;
}
