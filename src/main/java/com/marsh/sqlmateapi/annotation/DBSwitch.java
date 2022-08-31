package com.marsh.sqlmateapi.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DBSwitch {

    DataSourceTypeEnum value() default DataSourceTypeEnum.MASTER;

}
