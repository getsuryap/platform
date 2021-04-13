package org.ospic.platform.infrastructure.tenants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This file was created by eli on 11/04/2021 for org.ospic.platform.infrastructure.tenants
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
@Configuration
@ConfigurationProperties(prefix = "tenants")
public class DataSourceProperties {
    private Map<Object, Object> dataSources = new LinkedHashMap<>();
    public Map<Object, Object> getDataSources(){
        return dataSources;
    }

    public void setDataSources(Map<String, Map<String, String>> dataSources) {
        dataSources.forEach((key, value)-> this.dataSources.put(key, convert(value)));
    }

    public DataSource convert(Map<String, String> source){
        return DataSourceBuilder.create()
                .url(source.get("jdbcUrl"))
                .driverClassName(source.get("driverClassName"))
                .username(source.get("username"))
                .password(source.get("password"))
                .build();
    }
}
