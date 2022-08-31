package com.marsh.sqlmateapi.annotation;

import lombok.Getter;

public enum DataSourceTypeEnum {

    MASTER("master"),

    GENERAL("general");

@Getter
    private final String value;

    DataSourceTypeEnum(String value) {
        this.value = value;
    }
}
