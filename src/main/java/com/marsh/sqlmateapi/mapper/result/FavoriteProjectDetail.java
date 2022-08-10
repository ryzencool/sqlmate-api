package com.marsh.sqlmateapi.mapper.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteProjectDetail {

    private Integer userId;

    private Integer projectId;

    private String projectName;

    private String projectNote;
}
