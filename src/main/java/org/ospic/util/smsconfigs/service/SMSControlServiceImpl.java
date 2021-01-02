package org.ospic.util.smsconfigs.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.ospic.util.smsconfigs.SMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

/**
 * This file was created by eli on 02/01/2021 for org.ospic.util.smsconfigs.service
 * --
 * --
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
@Component
public class SMSControlServiceImpl implements SMSControlService {
    @Autowired
    public SMSControlServiceImpl(){}


    private final String ACCOUNT_SID = "AC451130a2493121dfe309ac8a8734d82f";

    private final String AUTH_TOKEN = "cc532013188c932f0052dcec28c8c6ab";

    private final String FROM_NUMBER = "+15005550006";

    @Override
    public void sendMessage(SMS sms) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber(sms.getTo()), new PhoneNumber(FROM_NUMBER), sms.getMessage())
                .create();
        System.out.println("here is my id:" + message.getSid());// Unique resource ID created to manage this transaction

    }
    @Override
    public void receive(MultiValueMap<String, String> callback) {
    }
}
