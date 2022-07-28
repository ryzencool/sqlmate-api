package com.marsh.sqlmateapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColumnUpdateReq {

    private Integer id;

    private String name;

    private String type;

    private String note;

    private Integer tableId;
}
