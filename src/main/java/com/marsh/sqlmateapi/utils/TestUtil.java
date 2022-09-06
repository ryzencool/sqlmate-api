package com.marsh.sqlmateapi.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marsh.sqlmateapi.service.dto.*;
import lombok.SneakyThrows;
import net.sf.jsqlparser.JSQLParserException;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TestUtil {



    @SneakyThrows
    public static void main(String[] args) throws JSQLParserException {


        String key = "7e6659eb-b45b-4e28-957b-e346164112b8";

        DES des = SecureUtil.des(key.getBytes(StandardCharsets.UTF_8));
        var str = JSONObject.toJSONString(TeamJoinDto.builder()
                .teamId(1)
                .userId(2)
                .time(System.currentTimeMillis())
                .build());
        var res = des.encrypt(str.getBytes(StandardCharsets.UTF_8));
        System.out.println(res);


    }
}
