package com.marsh.sqlmateapi.utils;


import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

public class SqlExecutor {

    public static HttpResponse sendSql(String sql, String dbName, Integer dbType) {
        var map = new HashMap<String, Object>();
        map.put("sql", sql);
        map.put("dbName", dbName);
        map.put("dbType", dbType);
        return HttpUtil.createPost("http://localhost:8081/executeSql")
                .body(JSONObject.toJSONString(map))
                .execute();

    }
    public static HttpResponse optimize(String sql, String dbName, Integer dbType,
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

        return HttpUtil.createPost("http://localhost:8081/optimize")
                .body(JSONObject.toJSONString(map))
                .execute();

    }




}
