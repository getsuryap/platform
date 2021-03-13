package org.ospic.platform.configurations.smsconfigs.config.service;

import org.ospic.platform.configurations.smsconfigs.config.domain.SmsConfig;
import org.ospic.platform.configurations.smsconfigs.config.exceptions.SmsConfigurationNotFoundExceptions;
import org.ospic.platform.configurations.smsconfigs.config.repository.SmsConfigurationsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

/**
 * This file was created by eli on 02/01/2021 for org.ospic.platform.configurations.smsconfigs.config.service
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
@Repository
public class SmsConfigurationWriteServiceImpl implements SmsConfigurationWriteService {
    @Autowired
    SmsConfigurationsJpaRepository configurationsJpaRepository;

    public SmsConfigurationWriteServiceImpl(SmsConfigurationsJpaRepository configurationsJpaRepository) {
        this.configurationsJpaRepository = configurationsJpaRepository;
    }

    @Override
    public ResponseEntity<?> createSmsConfiguration(SmsConfig config) {
        config.setIsActive(false);
        SmsConfig configuration = configurationsJpaRepository.save(config);
        return ResponseEntity.ok().body(configuration);
    }

    @Override
    public ResponseEntity<?> activateSmsConfiguration(Long configId) {
        configurationsJpaRepository.findById(configId).map(config -> {
            configurationsJpaRepository.findByIsActiveTrue().map(configuration ->{
                configuration.setIsActive(false);
                configurationsJpaRepository.save(configuration);
                return null;
            });
            config.setIsActive(true);
            return ResponseEntity.ok().body(configurationsJpaRepository.save(config));
        }).orElseThrow(SmsConfigurationNotFoundExceptions::new);
        return null;
    }

    @Override
    public ResponseEntity<?> updateSmsConfiguration(Long id, SmsConfig cf) {
       return configurationsJpaRepository.findById(id).map(config -> {
            config.setName(cf.getName().isEmpty() ? config.getName() : cf.getName());
            config.setPhoneNumber(cf.getPhoneNumber().isEmpty()  ? config.getPhoneNumber() : cf.getPhoneNumber());
            config.setSid(cf.getSid().isEmpty() ? config.getSid() : cf.getSid());
            config.setToken(cf.getToken().isEmpty() ?  config.getToken() :  cf.getToken());
            return ResponseEntity.ok().body(configurationsJpaRepository.save(config));
        }).orElseThrow(SmsConfigurationNotFoundExceptions::new);
    }
}
