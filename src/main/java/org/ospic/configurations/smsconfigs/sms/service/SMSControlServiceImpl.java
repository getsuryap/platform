package org.ospic.configurations.smsconfigs.sms.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.ospic.configurations.smsconfigs.config.domain.SmsConfig;
import org.ospic.configurations.smsconfigs.config.repository.SmsConfigurationsJpaRepository;
import org.ospic.configurations.smsconfigs.sms.SMS;
import org.ospic.configurations.smsconfigs.sms.domain.SmsCampaign;
import org.ospic.configurations.smsconfigs.sms.repository.SmsCampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

/**
 * This file was created by eli on 02/01/2021 for org.ospic.configurations.smsconfigs.message.service
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
    SmsCampaignRepository smsRepository;
    @Autowired
    SmsConfigurationsJpaRepository configuration;

    @Autowired
    public SMSControlServiceImpl( SmsCampaignRepository smsRepository, SmsConfigurationsJpaRepository configuration){
        this.smsRepository = smsRepository;
        this.configuration = configuration;
    }
    private final String ACCOUNT_SID = "AC451130a2493121dfe309ac8a8734d82f";

    private final String AUTH_TOKEN = "cc532013188c932f0052dcec28c8c6ab";

    private final String FROM_NUMBER = "+15005550006";

    @Override
    public void sendMessage(SMS sms) {
        Optional<SmsConfig> configOptional = configuration.findByIsActiveTrue();
        if (configOptional.isPresent()) {
            SmsCampaign smsCampaign = new SmsCampaign();
            SmsConfig config = configOptional.get();
            Twilio.init(config.getSid(),config.getToken());
            Message message = Message.creator(new PhoneNumber(sms.getTo()), new PhoneNumber(config.getPhoneNumber()), sms.getMessage()).create();
            System.out.println(smsCampaign.instance(message).toString());// Unique resource ID created to manage this transaction
            smsRepository.save(smsCampaign.instance(message));
        }else {
            System.out.println("Null sms configurations or no active configuration");
        }
    }
    @Override
    public void receive(MultiValueMap<String, String> callback) {
    }
}
