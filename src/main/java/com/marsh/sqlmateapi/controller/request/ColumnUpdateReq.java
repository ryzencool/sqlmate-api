package com.marsh.sqlmateapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColumnUpdateReq {

    private Long id;

    private String name;

    private String type;

    private String note;

    private Integer tableId;

    // 是否可空
    private Boolean isNull;

    // 是否唯一键
    private Boolean isUniqueKey;

    // 是否是主键
    private Boolean isPrimaryKey;

    // 是否自增
    private Boolean isAutoIncrement;

    //领域类型
    private String domainType;

    // 注释
    private String comment;
}
