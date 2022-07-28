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
public class TableColumn {

    @TableId
    private Long id;

    private Integer tableId;

    private String name;

    private String type;

    @TableField(typeHandler = StringArrayTypeHandler.class)
    private String[] settings;

    private String note;

    private String comment;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer createId;

    private Integer updateId;

    private Boolean isDel;

}
