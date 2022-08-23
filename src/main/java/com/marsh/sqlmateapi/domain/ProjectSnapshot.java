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
public class ProjectSnapshot {

    @TableId
    private Integer id;

    private Integer projectId;

    private String name;

    private String note;

    private String content;

    private LocalDateTime createTime;

    private Integer createId;

    private Boolean isDel;
}
