package com.marsh.sqlmateapi.controller.response;

import com.marsh.sqlmateapi.domain.ProjectInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectStatResp {

    private ProjectInfo projectInfo;

    private Long tableCount;

    private Long sqlCount;
}
