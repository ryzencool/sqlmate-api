package com.marsh.sqlmateapi.utils;


import com.marsh.sqlmateapi.service.dto.ExplainDto;
import com.marsh.sqlmateapi.service.dto.IndexRecommendDto;
import com.marsh.sqlmateapi.service.dto.KVDto;
import com.marsh.sqlmateapi.service.dto.OptimizeDto;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class SoarParser {

    public static OptimizeDto parse(String text) {
        String[] lines = text.split("\n## ");
        var optimizeDto = new OptimizeDto();
        var checkList = new ArrayList<KVDto>();
        var indexList = new ArrayList<IndexRecommendDto>();
        var explain = new ExplainDto();
        for (String line : lines) {
            if (line.contains("Query: ") && line.contains("分")) {
                var l = line.split("\n\n");
                var scoreStr = l[1].substring(9).replace("分", "").trim();
                var score = Integer.valueOf(scoreStr);
                optimizeDto.setScore(score);
            }
            // check
            if (set.stream().anyMatch(line::contains)) {
                var  l = line.split("\n\n");
                var item = l[0];
                var content = l[3].substring(16);
                var check = KVDto.builder().key(item).value(content).build();
                checkList.add(check);
            }

            // 索引
            if (line.contains("表添加索引")) {
                var l = line.split("\n\n");
                var table = (l[0].substring(l[0].indexOf("库的") +2, l[0].indexOf("表添加")));

                var field = (l[3].substring(16).replace("为列", "").replace("添加索引", "").trim());
                var sql = l[4].substring(12)
                        .replace("\\", "")
                        .replaceAll("`db_.+?`.", "");
                var rec = IndexRecommendDto.builder().alterIndex(sql).tableName(table).fields(new String[]{field}).build();
                indexList.add(rec);
            }



            if (line.contains("Explain信息")) {
                var l = line.split("### Explain信息解读\n\n");
                var table = l[0].split("\n\n")[1].split("\n");
                // expaintable
                var list = new ArrayList<KVDto>();
                var head = Arrays.stream(table[0].replace("|", ":").substring(1, table[0].length() - 1).split(":")).map(String::trim).collect(Collectors.toList());
                var body = Arrays.stream(table[2].replace("|", ":").substring(1, table[2].length() - 1).split(":")).map(it -> it.replace("*", "").replace("☠️", "").trim()).collect(Collectors.toList());
                // explain信息
                for (int i = 0; i < head.size(); i++) {
                    list.add(KVDto.builder()
                            .key(head.get(i))
                            .value(body.get(i))
                            .build());
                }
                explain.setExplainTable(list);



                var types = l[1].split("#### ");
                // 信息解读
                for (String type : types) {
                    if (type.contains("SelectType信息解读")) {
                        var t = type.split("\n\n");
                        var th = t[1].split(":");
                        var key = (th[0].replace("*", "").trim());
                        var value = (th[1].trim());
                        explain.setSelectType(KVDto.builder().key(key).value(value).build());
                    } else if (type.contains("Type信息解读") && !type.contains("SelectType信息解读")) {
                        var t = type.split("\n\n");
                        var th = (t[1].split(":"));
                        var key = (th[0].replace("*", "").replace("☠️", "").trim());
                        var value = (th[1].trim());
                        explain.setType(KVDto.builder()
                                .key(key)
                                .value(value)
                                .build());

                    } else if (type.contains("Extra信息解读")) {
                        var t = type.split("\n\n");
                        var th = (t[1].split(":"));
                        var key = (th[0].replace("*", "").replace("☠️", "").trim());
                        var value = (th[1].trim());
                        explain.setExtra(KVDto.builder().key(key).value(value).build());
                    }
                }
            }
        }
        optimizeDto.setExplain(explain);
        optimizeDto.setChecks(checkList);
        optimizeDto.setIndexRecommend(indexList);
        return optimizeDto;
    }


    private final static Set<String> set = Set.of("建议使用 AS 关键字显示声明一个别名",
            "不建议给列通配符'*'设置别名",
            "别名不要与表或列的名字相同",
            "修改表的默认字符集不会改表各个字段的字符集",
            "同一张表的多条 ALTER 请求建议合为一条",
            "删除列为高危操作，操作前请注意检查业务逻辑是否还有依赖",
            "删除主键和外键为高危操作，操作前请与 DBA 确认影响",
            "不建议使用前项通配符查找",
            "没有通配符的 LIKE 查询",
            "参数比较包含隐式转换，无法使用索引",
            "IN (NULL)/NOT IN (NULL) 永远非真",
            "IN 要慎用，元素过多会导致全表扫描",
            "应尽量避免在 WHERE 子句中对字段进行 NULL 值判断",
            "避免使用模式匹配",
            "OR 查询索引列时请尽量使用 IN 谓词",
            "引号中的字符串开头或结尾包含空格",
            "不要使用 hint，如：sql_no_cache, force index, ignore key, straight join等",
            "不要使用负向查询，如：NOT IN/NOT LIKE",
            "一次性 INSERT/REPLACE 的数据过多",
            "DDL 语句中使用了中文全角引号",
            "IN 条件中存在列名，可能导致数据匹配范围扩大",
            "最外层 SELECT 未指定 WHERE 条件",
            "不建议使用 ORDER BY RAND()",
            "不建议使用带 OFFSET 的LIMIT 查询",
            "不建议对常量进行 GROUP BY",
            "ORDER BY 常数列没有任何意义",
            "在不同的表中 GROUP BY 或 ORDER BY",
            "ORDER BY 语句对多个不同条件使用不同方向的排序无法使用索引",
            "请为 GROUP BY 显示添加 ORDER BY 条件",
            "ORDER BY 的条件为表达式",
            "GROUP BY 的条件为表达式",
            "建议为表添加注释",
            "将复杂的裹脚布式查询分解成几个简单的查询",
            "不建议使用 HAVING 子句",
            "删除全表时建议使用 TRUNCATE 替代 DELETE",
            "UPDATE 未指定 WHERE 条件",
            "不要 UPDATE 主键",
            "不建议使用 SELECT * 类型查询",
            "INSERT/REPLACE 未指定列名",
            "建议修改自增 ID 为无符号类型",
            "请为列添加默认值",
            "列未添加注释",
            "表中包含有太多的列",
            "表中包含有太多的 text/blob 列",
            "可使用 VARCHAR 代替 CHAR， VARBINARY 代替 BINARY",
            "建议使用精确的数据类型",
            "不建议使用 ENUM/BIT/SET 数据类型",
            "当需要唯一约束时才使用 NULL，仅当列不能有缺失值时才使用 NOT NULL",
            "TEXT、BLOB 和 JSON 类型的字段不建议设置为 NOT NULL",
            "TIMESTAMP 类型默认值检查异常",
            "为列指定了字符集",
            "TEXT、BLOB 和 JSON 类型的字段不可指定非 NULL 的默认值",
            "整型定义建议采用 INT(10) 或 BIGINT(20)",
            "VARCHAR 定义长度过长",
            "建表语句中使用了不推荐的字段类型",
            "不建议使用精度在秒级以下的时间数据类型",
            "消除不必要的 DISTINCT 条件",
            "COUNT(DISTINCT) 多列时结果可能和你预想的不同",
            "DISTINCT * 对有主键的表没有意义",
            "避免在 WHERE 条件中使用函数或其他运算符",
            "指定了 WHERE 条件或非 MyISAM 引擎时使用 COUNT(*) 操作性能不佳",
            "使用了合并为可空列的字符串连接",
            "不建议使用 SYSDATE() 函数",
            "不建议使用 COUNT(col) 或 COUNT(常量)",
            "使用 SUM(COL) 时需注意 NPE 问题",
            "不建议使用触发器",
            "不建议使用存储过程",
            "不建议使用自定义函数",
            "不建议对等值查询列使用 GROUP BY",
            "JOIN 语句混用逗号和 ANSI 模式",
            "同一张表被连接两次",
            "OUTER JOIN 失效",
            "不建议使用排它 JOIN",
            "减少 JOIN 的数量",
            "将嵌套查询重写为 JOIN 通常会导致更高效的执行和更有效的优化",
            "不建议使用联表删除或更新",
            "不要使用跨数据库的 JOIN 查询",
            "建议使用自增列作为主键，如使用联合自增主键时请将自增键作为第一列",
            "无主键或唯一键，无法在线变更表结构",
            "避免外键等递归关系",
            "提醒：请将索引属性顺序与查询对齐",
            "表建的索引过多",
            "主键中的列过多",
            "未指定主键或主键非 int 或 bigint",
            "ORDER BY 多个列但排序方向不同时可能无法使用索引",
            "添加唯一索引前请注意检查数据唯一性",
            "全文索引不是银弹",
            "SQL_CALC_FOUND_ROWS 效率低下",
            "不建议使用 MySQL 关键字做列名或表名",
            "不建议使用复数做列名或表名",
            "不建议使用使用多字节编码字符(中文)命名",
            "SQL 中包含 unicode 特殊字符",
            "INSERT INTO xx SELECT 加锁粒度较大请谨慎",
            "请慎用 INSERT ON DUPLICATE KEY UPDATE",
            "用字符类型存储IP地址",
            "日期/时间未使用引号括起",
            "一列中存储一系列相关数据的集合",
            "请使用分号或已设定的 DELIMITER 结尾",
            "非确定性的 GROUP BY",
            "未使用 ORDER BY 的 LIMIT 查询",
            "UPDATE/DELETE 操作使用了 LIMIT 条件",
            "UPDATE/DELETE 操作指定了 ORDER BY 条件",
            "UPDATE 语句可能存在逻辑错误，导致数据损坏",
            "永远不真的比较条件",
            "永远为真的比较条件",
            "不建议使用LOAD DATA/SELECT ... INTO OUTFILE",
            "不建议使用连续判断",
            "建表语句中定义为 ON UPDATE CURRENT_TIMESTAMP 的字段不建议包含业务逻辑",
            "更新请求操作的表包含 ON UPDATE CURRENT_TIMESTAMP 字段",
            "请谨慎使用TRUNCATE操作",
            "不使用明文存储密码",
            "使用DELETE/DROP/TRUNCATE等操作时注意备份",
            "发现常见 SQL 注入函数",
            "'!=' 运算符是非标准的",
            "库名或表名点后建议不要加空格",
            "索引起名不规范",
            "起名时请不要使用字母、数字和下划线之外的字符",
            "MySQL 对子查询的优化效果不佳",
            "如果您不在乎重复的话，建议使用 UNION ALL 替代 UNION",
            "考虑使用 EXISTS 而不是 DISTINCT 子查询",
            "执行计划中嵌套连接深度过深",
            "子查询不支持LIMIT",
            "不建议在子查询中使用函数",
            "外层带有 LIMIT 输出限制的 UNION 联合查询，其内层查询建议也添加 LIMIT 输出限制",
            "不建议使用分区表",
            "请为表选择合适的存储引擎",
            "以DUAL命名的表在数据库中有特殊含义",
            "表的初始AUTO_INCREMENT值不为0",
            "请使用推荐的字符集",
            "不建议使用视图",
            "不建议使用临时表",
            "请使用推荐的COLLATE");
}
