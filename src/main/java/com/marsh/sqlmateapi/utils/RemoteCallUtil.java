package com.marsh.sqlmateapi.utils;

import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.marsh.zutils.exception.BaseBizException;

import javax.xml.crypto.Data;
import java.util.Objects;

public class RemoteCallUtil {

    public static void handleErrorResponse(HttpResponse response) {
        var result = JSONObject.parseObject(response.body());
        var code = result.getString("code");
        if (!Objects.equals(code, "000000")) {
            var msg = result.getString("msg");
            throw new BaseBizException(msg);
        }
    }
}
