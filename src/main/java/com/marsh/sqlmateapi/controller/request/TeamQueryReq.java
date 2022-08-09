package com.marsh.sqlmateapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamQueryReq {

    private Integer masterId;

    private String name;

    private Integer id;
}
