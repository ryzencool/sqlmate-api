package com.marsh.sqlmateapi.utils;


import com.marsh.sqlmateapi.mapper.TableColumnMapper;
import com.marsh.sqlmateapi.mapper.param.TableDetailParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.List;

@Slf4j
@Component
public class JSQLTools {

    private final TableColumnMapper tableColumnMapper;

    public JSQLTools(TableColumnMapper tableColumnMapper) {
        this.tableColumnMapper = tableColumnMapper;
    }

    /**
     * SQL 解析
     *
     * @throws JSQLParserException
     */
    public void testSelectParser(Integer projectId) throws JSQLParserException {
        String SQL002 = "select u.name from user u left join (select * from order) o on u.id = o.id where u.routes > ? and id = ? and kula like ? and k = (select vwe from bb where bba = ?)";   // 多表SQL

        Select select = (Select) CCJSqlParserUtil.parse(SQL002);
        List<SelectColumn> scList = new ArrayList<SelectColumn>();
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

        System.out.println("*********");
        parseBody((PlainSelect) select.getSelectBody(), projectId);
        System.out.println("*********");

        System.out.println(select.getSelectBody());

//        // 4.解析Join
//        List<Join> joins = plainSelect.getJoins();
//        joins.forEach(e -> {
//            Expression onExpression = e.getOnExpression();
//            System.err.println(onExpression); // 获取ON 表达式 t1.user_id = t2.user_id
//        });

        // 5.解析IN
        String SQL_IN = "SELECT *  FROM tableName WHERE ID IN (8,9,10)";
        PlainSelect plainSelectIn = (PlainSelect) ((Select) CCJSqlParserUtil.parse(SQL_IN)).getSelectBody();
        InExpression inExpression = (InExpression) plainSelectIn.getWhere();
        ItemsList rightItemsList = inExpression.getRightItemsList();

        System.err.println(rightItemsList); // (8, 9, 10)

        // plainSelect.getDistinct();
        // plainSelect.getFetch();
        // plainSelect.getFirst();
        // plainSelect.getGroupBy();
        // .......
    }


    public void parseBody(PlainSelect select, Integer projectId) {
        var tableList = new ArrayList<ParserTable>();
        var outTable = (Table) (select.getFromItem());
        var outTableName = outTable.getName();
        var outTableAlias = outTable.getAlias();
        var parserTable = new ParserTable();

        parserTable.setTableName(outTableName);
        tableList.add(parserTable);
        // 查询表的字段

        if (outTableAlias != null) {
            parserTable.setAliasName(outTableAlias.getName());
            System.out.println(" aliasTable:" + outTableAlias.getName());
        }

        var equalsTo = (select.getWhere());
        parseWhere(equalsTo, projectId);


        List<SelectItem> selectItems = select.getSelectItems();
        for (SelectItem selectItem : selectItems) {
            var alias = ((SelectExpressionItem) selectItem).getAlias();
            var selectColumn = new SelectColumn();
            if (alias != null) {
                selectColumn.setAliasName(alias.getName());
                System.out.print("aliasColumn:" + alias.getName());
            }
            var node = ((SelectExpressionItem) selectItem).getExpression().getASTNode();
            if (Objects.equals(node.toString(), "SubSelect")) {
                // 继续探索
                selectColumn.setType("SubSelect");
                // 探究这个返回的是什么类型, 结果里面是否包含？ item是什么类型， 存在什么表
                var sb = (PlainSelect) (((SubSelect) (node.jjtGetValue())).getSelectBody());
                parseBody(sb, projectId);

            } else if (Objects.equals(node.toString(), "Column")) {
                var colName = (((Column) node.jjtGetValue()).getColumnName());

                selectColumn.setType("Column");
                System.out.println(" tableColumn:" + colName);
            } else if (Objects.equals(node.toString(), "Function")) {
                var colName = (((Function) node.jjtGetValue()).getName());
                System.out.println(" tableColumn:" + colName);
            }
            // 加入到参数里面
        }

    }

    public void parseWhere(Expression express, Integer projectId) {
        if (express == null) {
            return;
        }
        if (express instanceof AndExpression) {
            var expression = (AndExpression) express;
            var right = (expression.getRightExpression()).getASTNode();
            System.out.println("where:" + right.jjtGetValue());

            var rightValue = ((BinaryExpression) (right.jjtGetValue())).getRightExpression().getASTNode();
            if (rightValue != null) {
                if (Objects.equals(rightValue.toString(), "SubSelect")) {
                    var param = ((SubSelect) (rightValue.jjtGetValue())).getSelectBody();
                    parseBody((PlainSelect) param, projectId);
                }
            } else {
                if (Objects.equals(((BinaryExpression) (right.jjtGetValue())).getRightExpression().toString(), "?")) {
                    ((BinaryExpression) (right.jjtGetValue())).setRightExpression(new JdbcNamedParameter(((BinaryExpression) (right.jjtGetValue())).getLeftExpression().toString()));
                }
            }

            var left = expression.getLeftExpression();
            if (left.getASTNode() == null) {
                // 继续解析
                parseWhere(left, projectId);
            } else {
                //只有一个
                var leftNode = left.getASTNode();

                var exp = (BinaryExpression) leftNode.jjtGetValue();

                if (Objects.equals(exp.getRightExpression().toString(), "?")) {
                    var value = ((Column) exp.getLeftExpression()).getColumnName();
                    exp.setRightExpression(new JdbcNamedParameter(value));
                }


//                if (exp instanceof EqualsTo) {
//                    var exps = (EqualsTo) exp;
//
//                    if (Objects.equals(exps.getRightExpression().toString(), "?")) {
//                        var value = ((Column) exps.getLeftExpression()).getColumnName();
//                        exps.setRightExpression(new JdbcNamedParameter(value));
//                    }
//                }
            }
        } else {
            var expression = (BinaryExpression) express;
            if (Objects.equals(expression.getRightExpression().toString(), "?")) {
                expression.setRightExpression(new JdbcNamedParameter(expression.getLeftExpression().toString()));
            }
        }
    }

//    public static void main(String[] args) throws JSQLParserException {
//        var tool = new JSQLTools(tableColumnMapper);
////        tool.getTableNamesFromSql();
//        tool.testSelectParser();
//    }

}