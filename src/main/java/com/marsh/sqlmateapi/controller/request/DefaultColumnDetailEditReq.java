package com.marsh.sqlmateapi.controller.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultColumnDetailEditReq {


    private Integer templateId;

    private String name;

    private String type;

    private String defaultValue;

    private Boolean isNotNull;

    private Boolean isUniqueKey;

    private Boolean isPrimaryKey;

    private Boolean isAutoIncrement;

    private String note;

    private String comment;

}
