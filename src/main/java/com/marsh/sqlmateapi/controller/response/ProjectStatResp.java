package com.marsh.sqlmateapi.controller.response;

import com.marsh.sqlmateapi.domain.ProjectInfo;
import com.marsh.sqlmateapi.domain.UserInfo;
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

    private UserInfo createUser;

    private UserInfo updateUser;

    private Long tableCount;

    private Long sqlCount;

    private Long indexCount;

    private Long columnCount;
}
