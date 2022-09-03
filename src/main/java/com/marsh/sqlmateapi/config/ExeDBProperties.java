package com.marsh.sqlmateapi.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfigurationProperties(prefix = "executor")
public class ExeDBProperties {

    private ChildDB pg;

    private ChildDB mysql;

    private String address;
}
