package com.marsh.sqlmateapi.utils;

import cn.hutool.core.util.RuntimeUtil;

public class CommandLineUtil {

    public static String execute(String commandLine) {
        return RuntimeUtil.execForStr(commandLine);
    }
}
