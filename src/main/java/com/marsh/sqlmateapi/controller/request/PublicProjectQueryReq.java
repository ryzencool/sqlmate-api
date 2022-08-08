package com.marsh.sqlmateapi.controller.request;

import com.marsh.zutils.entity.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicProjectQueryReq extends BaseRequest {

    private String name;
}
