package org.ospic.platform.infrastructure.globalconfigurations.service;

import org.ospic.platform.infrastructure.globalconfigurations.data.Config;
import org.ospic.platform.infrastructure.globalconfigurations.domain.GlobalConfigurations;
import org.ospic.platform.infrastructure.globalconfigurations.exceptions.GlobalConfigurationNotFoundException;
import org.ospic.platform.infrastructure.globalconfigurations.repository.GlobalConfigurationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * This file was created by eli on 18/06/2021 for org.ospic.platform.infrastructure.globalconfigurations.service
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
public class GlobalConfigurationServiceImpl implements GlobalConfigurationService {
    private final GlobalConfigurationJpaRepository configurationJpaRepository;
    @Autowired
    public GlobalConfigurationServiceImpl(final GlobalConfigurationJpaRepository configurationJpaRepository){
        this.configurationJpaRepository = configurationJpaRepository;
    }

    @Override
    public GlobalConfigurations updateConfigurationStatus(Long configurationId, Boolean status) {
        return this.configurationJpaRepository.findById(configurationId).map(configuration ->{
            configuration.setActive(status);
            return this.configurationJpaRepository.save(configuration);
        }).orElseThrow(()-> new GlobalConfigurationNotFoundException());
    }

    @Override
    public GlobalConfigurations updateConfigurationValue(Config config) {
        return this.configurationJpaRepository.findById(config.getConfigurationId()).map(configuration ->{
            configuration.setValue(config.getConfigurationValue());
            return this.configurationJpaRepository.save(configuration);
        }).orElseThrow(()-> new GlobalConfigurationNotFoundException());
    }

    @Override
    public GlobalConfigurations getConfigurationById(Long configurationId) {
        return this.configurationJpaRepository.findById(configurationId).orElseThrow(()-> new GlobalConfigurationNotFoundException());
    }

    @Override
    public Collection<GlobalConfigurations> getConfigurations() {
        return this.configurationJpaRepository.findAll();
    }
}
