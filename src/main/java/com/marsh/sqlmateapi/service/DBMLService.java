package com.marsh.sqlmateapi.service;

import com.marsh.sqlmateapi.controller.request.ColumnQueryReq;
import com.marsh.sqlmateapi.controller.request.TableQueryReq;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class DBMLService {

    private final TableService tableService;

    private final TableColumnService tableColumnService;

    public DBMLService(TableService tableService, TableColumnService tableColumnService) {
        this.tableService = tableService;
        this.tableColumnService = tableColumnService;
    }

    public String exportTableDBML(Integer tableId) {
        var table = tableService.getTable(tableId);
        var tableColumns = tableColumnService.listColumn(ColumnQueryReq.builder()
                .tableId(tableId).build());
        var dbml = new StringBuilder();
        var lineTable = String.format("Table %s {\n", table.getName());
        dbml.append(lineTable);
        for (var col : tableColumns) {
            var lineCol = String.format("\t %s %s \n", col.getName(), col.getType());
            dbml.append(lineCol);
        }
        dbml.append("}");
        return dbml.toString();
    }

    public String exportProjectDBML(Integer projectId) {
        var tables = tableService.listTable(TableQueryReq.builder().projectId(projectId).build());
        return tables.stream()
                .map(it -> exportTableDBML(it.getId()))
                .collect(Collectors.joining("\n\n"));
    }
}
