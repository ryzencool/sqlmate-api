package com.marsh.sqlmateapi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
public class SyncController {



    @PostMapping("/sync")
    public void sync() {

    }
}
