package com.marsh.sqlmateapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableInfo {

    @TableId
    private Integer id;

    private Integer projectId;

    private Integer schemaId;

    private String name;

    private String note;

    private String comment;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer createId;

    private Integer updateId;

    private Boolean isDel;
}
