package com.marsh.sqlmateapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marsh.sqlmateapi.controller.request.ColumnQueryReq;
import com.marsh.sqlmateapi.controller.request.DBMLProjectImportReq;
import com.marsh.sqlmateapi.controller.request.TableQueryReq;
import com.marsh.sqlmateapi.domain.TableColumn;
import com.marsh.sqlmateapi.domain.TableInfo;
import com.marsh.sqlmateapi.mapper.TableColumnMapper;
import com.marsh.sqlmateapi.mapper.TableInfoMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DBMLService {


    private final TableService tableService;

    private final TableColumnService tableColumnService;

    private final TableInfoMapper tableInfoMapper;

    private final TableColumnMapper tableColumnMapper;

    public DBMLService(TableService tableService,
                       TableColumnService tableColumnService,
                       TableInfoMapper tableInfoMapper,
                       TableColumnMapper tableColumnMapper) {
        this.tableService = tableService;
        this.tableColumnService = tableColumnService;
        this.tableInfoMapper = tableInfoMapper;
        this.tableColumnMapper = tableColumnMapper;
    }

    public String exportTableDBML(Integer tableId) {
        var table = tableService.getTable(tableId);
        var tableColumns = tableColumnService.listColumn(ColumnQueryReq.builder()
                .tableId(tableId).build());
        var dbml = new StringBuilder();
        var lineTable = String.format("Table %s {\n", table.getName());
        dbml.append(lineTable);


        for (var col : tableColumns) {
            var lineCol = String.format("\t %s %s ", col.getName(), col.getType());
            var extend = new ArrayList<String>();
            if (StringUtils.isNotEmpty(col.getComment())) {
                extend.add(String.format("note : %s", col.getComment()));
            }
            if (StringUtils.isNotEmpty(col.getDefaultValue())) {
                if (col.getType().contains("int")
                    || col.getType().startsWith("float")
                    || col.getType().startsWith("double")
                    || col.getType().startsWith("decimal")
                    || col.getType().startsWith("bool")
                    || Objects.equals(col.getDefaultValue(), "null")) {
                    extend.add(String.format("default: %s", col.getDefaultValue()));
                } else {
                    extend.add(String.format("default: '%s'", col.getDefaultValue()));
                }
            }
            if (col.getIsAutoIncrement()) {
                extend.add("increment");
            }
            if (StringUtils.isNotEmpty(col.getNote())) {
                extend.add(String.format("note: '%s'", col.getNote()));
            }
            if (col.getIsNull()) {
                extend.add("null");
            } else {
                extend.add("not null");
            }
            if (col.getIsUniqueKey()) {
                extend.add("unique");
            }
            if (col.getIsPrimaryKey()) {
                extend.add("pk");
            }
            var extendStr = String.join(",", extend);
            if (StringUtils.isNotEmpty(extendStr)) {
                lineCol = lineCol + " [" + extendStr + "]\n";
            } else {
                lineCol = lineCol + "\n";
            }

            dbml.append(lineCol);
        }
        if (StringUtils.isNotEmpty(table.getNote())) {
            dbml.append(String.format("\n\t Note: '%s'\n", table.getNote()));
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


    @SneakyThrows
    @Transactional
    public void importProject(DBMLProjectImportReq dbml) {
        var mapper = new ObjectMapper();
        var jsonNode = mapper.readTree(dbml.getDbmlJson());

        var fieldMap = new HashMap<String, JsonNode>();

        jsonNode.get("fields").fields().forEachRemaining(it -> {
            fieldMap.put(it.getKey(), it.getValue());
        });




        jsonNode.get("tables").fields().forEachRemaining(it -> {
            // 判断表存在与否
            var tableName = it.getValue().get("name").asText();
            var tableId = it.getValue().get("id").asText();
            var table = tableInfoMapper.selectOne(new QueryWrapper<TableInfo>()
                    .lambda()
                    .eq(TableInfo::getName, tableName)
                    .eq(TableInfo::getProjectId, dbml.getProjectId()));

            if (table == null) {
                // 插入这个表
                Integer id = tableInfoMapper.insertReturnId(TableInfo.builder()
                                .name(tableName)
                                .projectId(dbml.getProjectId())
                        .build());


                it.getValue().get("fieldIds").elements().forEachRemaining(itt -> {
                    var column = fieldMap.get(itt.asText());
                    var columnName= column.get("name").asText();
                    var columnType = column.get("type").get("type_name").asText();
                    tableColumnMapper.insert(TableColumn.builder()
                                    .tableId(id)
                                    .name(columnName)
                                    .type(columnType)
                            .build());
                });
            }
        });
    }
}
