package com.marsh.sqlmateapi;

import com.marsh.zutils.util.BeanUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@MapperScan("com.marsh.sqlmateapi.mapper")
@ComponentScan({"com.marsh.mpext", "com.marsh.zutils", "com.marsh.sqlmateapi"})
public class SqlmateApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqlmateApiApplication.class, args);
    }

}
