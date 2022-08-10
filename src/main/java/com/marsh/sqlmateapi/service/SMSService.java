package com.marsh.sqlmateapi.service;

import com.marsh.sqlmateapi.controller.request.SMSSendReq;
import com.marsh.zutils.entity.SendSMSParam;
import com.marsh.zutils.helper.SMSHelper;
import org.springframework.stereotype.Service;

@Service
public class SMSService {

    private final SMSHelper smsHelper;

    public SMSService(SMSHelper smsHelper) {
        this.smsHelper = smsHelper;
    }

    public void sendSms(SMSSendReq req) {
        smsHelper.sendSMS(SendSMSParam.builder()
                .phoneNumber(req.getPhone())
                .build());
    }
}
