package com.marsh.sqlmateapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableIndexEditReq {

    private Integer id;

    private Integer tableId;

    private String columns;

    private String name;

    private String type;
}
