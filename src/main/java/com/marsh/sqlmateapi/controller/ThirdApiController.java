package com.marsh.sqlmateapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.marsh.sqlmateapi.service.ThirdApiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThirdApiController {

    private final ThirdApiService thirdApiService;

    public ThirdApiController(ThirdApiService thirdApiService) {
        this.thirdApiService = thirdApiService;
    }


    @PostMapping("/call")
    public void send(@RequestBody JsonNode params) {


    }


}
