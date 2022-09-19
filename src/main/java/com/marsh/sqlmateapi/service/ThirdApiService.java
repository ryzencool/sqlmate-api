package com.marsh.sqlmateapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class ThirdApiService {

    public void send() {
        // 通过token解析到对应的项目生产数据库

        // 寻找对应的数据库

        // 发送指令

        // 获取结果
    }

    public static void parse(JsonNode node) {
        var joinList = new ArrayList<String>();
        var whereList = new ArrayList<String>();
        var table = node.get("table").asText();
        var selectNode = node.get("select");
        var childNodes = node.get("children");
        var siblingNodes = node.get("sibling");
        var conditions = node.get("conditions");

        var ssssk = new ArrayList<String>();
        for (var cwn: conditions) {
            var opt = cwn.get("operator");
            if (opt != null) {
                var optText = opt.asText();

                if (Objects.equals(optText, "and")) {
                    var conds = cwn.get("conditions");
                    var kaka = new ArrayList<String>();
                    for (var cond: conds) {
                        var is = cwn.get("is").asText();
                        var variable = cwn.get("var").asText();
                        var value = cwn.get("val").asText();
                        if (Objects.equals(is, "eq")) {
                            var s = variable + " = " + value;
                            kaka.add(s);
                        }
                    }
                    var ka = kaka.stream().collect(joining(" and "));
                    ssssk.add("( "+ ka +" )");


                } else if (Objects.equals(optText, "or")) {
                    var conds = cwn.get("conditions");
                    var kaka = new ArrayList<String>();
                    for (var cond: conds) {
                        var is = cond.get("is").asText();
                        var variable = cond.get("var").asText();
                        var value = cond.get("val").asText();
                        if (Objects.equals(is, "eq")) {
                            var s = variable + " = " + value;
                            kaka.add(s);
                        }
                    }
                    var ka = kaka.stream().collect(joining(" or "));
                    ssssk.add("( "+ ka +" )");
                } else {

                }
            } else {
                var is = cwn.get("is").asText();
                var variable = cwn.get("var").asText();
                var value = cwn.get("val").asText();
                if (Objects.equals(is, "eq")) {
                    var s = variable + " = " + value;
                    ssssk.add(s);
                }
            }

        }
        var kkkk = ssssk.stream().collect(joining(" and "));

        System.out.println(kkkk);




        if (childNodes != null) {
            for (var cn : childNodes) {
                if (cn != null) {
                    var childTable = cn.get("table");
                    var childSelectNodes = cn.get("select");
                    var xxxxx = new ArrayList<String>();
                    for (var sn: childSelectNodes) {
                        var str = String.format("'%s', %s", sn.asText(), sn.asText());
                        xxxxx.add(str);
                    }
                    System.out.println(String.join(",", xxxxx));


                    var childWhereNodes = cn.get("conditions");
                    var relateKeys = cn.get("relateKeys");
                    if (!childWhereNodes.isNull()) {
                        var sss = new ArrayList<String>();

                        for (var cwn: childWhereNodes) {
                            var opt = cwn.get("operator");
                            if (!opt.isNull()) {
                                var optText = opt.asText();

                                if (Objects.equals(optText, "and")) {
                                    var conds = cwn.get("conditions");
                                    var kaka = new ArrayList<String>();
                                    for (var cond: conds) {
                                        var is = cond.get("is").asText();
                                        var variable = cond.get("var").asText();
                                        var value = cond.get("val").asText();
                                        if (Objects.equals(is, "eq")) {
                                            var s = variable + " = " + value;
                                            kaka.add(s);
                                        }
                                    }
                                    var ka = kaka.stream().collect(joining(" and "));
                                    sss.add("( "+ ka +" )");


                                } else if (Objects.equals(optText, "or")) {
                                    var conds = cwn.get("conditions");
                                    var kaka = new ArrayList<String>();
                                    for (var cond: conds) {
                                        var is = cwn.get("is").asText();
                                        var variable = cwn.get("var").asText();
                                        var value = cwn.get("val").asText();
                                        if (Objects.equals(is, "eq")) {
                                            var s = variable + " = " + value;
                                            kaka.add(s);
                                        }
                                    }
                                    var ka = kaka.stream().collect(joining(" or "));
                                    sss.add("( "+ ka +" )");
                                } else {

                                }
                            } else {
                                var is = cwn.get("is").asText();
                                var variable = cwn.get("var").asText();
                                var value = cwn.get("val").asText();
                                if (Objects.equals(is, "eq")) {
                                    var s = variable + " = " + value;
                                }
                            }

                        }
                    }
                    var relateKeysNode = cn.get("relateKeys");
                    var str = "left join " + childTable.asText() + " on " + table + "." + relateKeysNode.get(0).asText() + "=" + childTable.asText() + "." + relateKeysNode.get(1).asText();
                    joinList.add(str);
                }
            }
        }

        var joinStr = joinList.stream().collect(joining(" "));
        System.out.println(joinStr);

        if (siblingNodes != null) {
            for (var sn : siblingNodes) {
                if (sn != null) {
                    var childTable = sn.get("table");
                    var childSelectNode = sn.get("select");
                    var childWhereNode = sn.get("where");
                }
            }
        }


        var whereNode = node.get("where");
        List<String> values = new ArrayList<>();
        for (JsonNode n : selectNode) {
            values.add(n.asText());
        }
        var cond = values.stream().collect(joining(","));

        if (whereNode == null) {
            var sql = String.format("select %s from %s", cond, table);
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var str = "{\n" +
                  "    \"table\": \"user\",\n" +
                  "    \"name\": \"user\",\n" +
                  "    \"select\": [\"id\", \"name\"],\n" +
                  "    \"conditions\": [\n" +
                  "        {\n" +
                  "            \"operator\": \"or\",\n" +
                  "            \"conditions\": [\n" +
                  "              \n" +
                  "                        {\n" +
                  "                            \"is\": \"eq\",\n" +
                  "                            \"var\": \"id\",\n" +
                  "                            \"val\": \"5\"\n" +
                  "                        }, \n" +
                  "                        {\n" +
                  "                            \"is\": \"eq\",\n" +
                  "                            \"var\": \"name\",\n" +
                  "                            \"val\": \"6\"\n" +
                  "                        }\n" +
                  "                 \n" +
                  "            ]\n" +
                  "        }, {\n" +
                  "            \"is\": \"eq\",\n" +
                  "            \"var\": \"coma\",\n" +
                  "            \"val\": 2\n" +
                  "        }\n" +
                  "    ],\n" +
                  "    \"children\": [\n" +
                  "        {\n" +
                  "            \"table\": \"coma\",\n" +
                  "            \"select\": [\"id\", \"name\"],\n" +
                  "            \"conditions\": [],\n" +
                  "            \"relateKeys\": [\"id\", \"cid\"]\n" +
                  "        }, {\n" +
                  "            \"table\": \"sima\",\n" +
                  "            \"select\": [\"feima\",\"bima\"],\n" +
                  "            \"conditions\": [],\n" +
                  "            \"relateKeys\": [\"id\", \"sid\"]\n" +
                  "        }\n" +
                  "    ]\n" +
                  "}\n" +
                  "\n";
        var node = mapper.readTree(str);
        parse(node);
    }

}
