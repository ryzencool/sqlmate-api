package com.marsh.sqlmateapi.controller;

import com.marsh.sqlmateapi.controller.request.SMSSendReq;
import com.marsh.sqlmateapi.service.SMSService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SMSController {
    private final SMSService smsService;

    public SMSController(SMSService smsService) {
        this.smsService = smsService;
    }

    @GetMapping("/send")
    public BaseResponse<Object> sendSms(SMSSendReq req) {
        smsService.sendSms(req);
        return BaseResponse.success();
    }

}
