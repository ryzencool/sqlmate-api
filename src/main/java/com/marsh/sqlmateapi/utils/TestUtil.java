package com.marsh.sqlmateapi.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserDefaultVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserTreeConstants;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class TestUtil {

    public static void main(String[] args) throws JSQLParserException {
        String sql = "select count(*) as tx from user t1";

        Statement statement = CCJSqlParserUtil.parse(sql);
        Select selectStatement = (Select) statement;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder() {
            @Override
            public void visit(Column tableColumn) {
                System.out.println(tableColumn);
            }
        };
        tablesNamesFinder.getTableList(selectStatement);

        System.out.println("-------------------------------------------");
        System.out.println("using ast nodes to get column names");
        SimpleNode node = (SimpleNode) CCJSqlParserUtil.parseAST(sql);

        node.jjtAccept(new CCJSqlParserDefaultVisitor() {
            @Override
            public Object visit(SimpleNode node, Object data) {
                if (node.getId() == CCJSqlParserTreeConstants.JJTCOLUMN) {
                    System.out.println(node.jjtGetValue());
                    return super.visit(node, data);
                } else {
                    return super.visit(node, data);
                }
            }
        }, null);
    }
}
