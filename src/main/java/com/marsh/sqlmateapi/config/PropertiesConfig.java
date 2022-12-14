package com.marsh.sqlmateapi.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(value = {ExeDBProperties.class, SqlExecutorProperties.class})
public class PropertiesConfig {
}
