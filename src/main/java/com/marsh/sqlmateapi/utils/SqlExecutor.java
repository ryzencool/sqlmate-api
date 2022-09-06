package com.marsh.sqlmateapi.utils;


import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.marsh.sqlmateapi.config.SqlExecutorProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class SqlExecutor {

    private final SqlExecutorProperties sqlExecutorProperties;

    public SqlExecutor(SqlExecutorProperties sqlExecutorProperties) {
        this.sqlExecutorProperties = sqlExecutorProperties;
    }

    public  HttpResponse sendSql(String sql, String dbName, Integer dbType) {
        log.info("sql: {}, dbname: {}, dbType: {}", sql, dbName, dbType);
        var map = new HashMap<String, Object>();
        map.put("sql", sql);
        map.put("dbName", dbName);
        map.put("dbType", dbType);
        return HttpUtil.createPost(sqlExecutorProperties.getUrl() + "/executeSql")
                .body(JSONObject.toJSONString(map))
                .execute();

    }


    public HttpResponse sendPgMainSql(String sql) {
        return sendSql(sql, "pgMain", 2);
    }

    public HttpResponse sendMysqlMainSql(String sql) {
        return sendSql(sql, "mysqlMain", 1);
    }
    public  HttpResponse optimize(String sql, String dbName, Integer dbType,
                                        String username, String password,
                                        String host, Integer port) {
        var map = new HashMap<String, Object>();
        map.put("sql", sql);
        map.put("dbName", dbName);
        map.put("dbType", dbType);
        map.put("username", username);
        map.put("password", password);
        map.put("host", host);
        map.put("port", port);

        return HttpUtil.createPost(sqlExecutorProperties.getUrl() + "/optimize")
                .body(JSONObject.toJSONString(map))
                .execute();

    }




}
