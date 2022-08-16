package com.marsh.sqlmateapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullTableColumnDto {

    private Long id;

    // 表编号
    private Integer tableId;

    // 名称
    private String name;

    // 类型
    private String type;

    // 默认值
    private String defaultValue;

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

    // 备注
    private String note;

    // 注释
    private String comment;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;

    private Integer createId;

    private Integer updateId;

    private Boolean isDel;

    private ColumnRelationShip columnRelationShip;



}
