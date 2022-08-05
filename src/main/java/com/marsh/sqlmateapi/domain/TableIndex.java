package com.marsh.sqlmateapi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.marsh.mpext.common.StringArrayTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableIndex {

    @TableId
    private Integer id;

    private String name;

    private Integer tableId;

    private String type;

    @TableField(typeHandler = StringArrayTypeHandler.class)
    private String[] columns;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer createId;

    private Integer updateId;

    private Boolean isDel;
}
