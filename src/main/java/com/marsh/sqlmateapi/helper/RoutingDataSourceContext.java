package com.marsh.sqlmateapi.helper;

import org.springframework.stereotype.Component;

public class RoutingDataSourceContext  {

    public static final ThreadLocal<String> RouteKey = new ThreadLocal<>();
    /**
     * 获取主数据库的key
     * @return
     */
    public static String getMainKey() {
        return "master";
    }





    /**
     * 获取数据库key
     * @return
     */
    public static String getRouteKey() {
        return  RouteKey.get();
    }

    /**
     * 设置数据库的key
     * @param key
     */
    public static void setRouteKey(String key) {
        RouteKey.set(key);
    }

}
