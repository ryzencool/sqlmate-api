package com.marsh.sqlmateapi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.marsh.mpext.common.IntArrayTypeHandler;
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
public class ProjectInfo {

    @TableId
    private Integer id;

    private String name;

    private String note;

    private Integer dbType;

    private Integer ownerId;

    @TableField(typeHandler = IntArrayTypeHandler.class)
    private Integer[] teamIds;

    @TableField(typeHandler = StringArrayTypeHandler.class)
    private String[] tags;

    private Boolean isPublic;

    private Integer defaultColumnTemplateId;

    private Integer parentProjectId;

    private Integer openCount;

    private Integer cloneCount;

    private Integer collectCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer createId;

    private Integer updateId;

    private Boolean isDel;
}
