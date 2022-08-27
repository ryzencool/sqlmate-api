package com.marsh.sqlmateapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectEditReq {

    private Integer id;

    private String name;

    private String note;

    private Integer dbType;

    private Integer[] teamIds;

    private String[] tags;

    private Boolean isPublic;


    private Integer defaultColumnTemplateId;


}
