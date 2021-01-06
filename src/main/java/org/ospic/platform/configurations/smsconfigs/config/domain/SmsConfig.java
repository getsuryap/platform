package org.ospic.platform.configurations.smsconfigs.config.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This file was created by eli on 02/01/2021 for org.ospic.platform.configurations.smsconfigs.config.domain
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
@Data
@NoArgsConstructor
@Entity(name = DatabaseConstants.SMS_CONFIGURATION_TABLE)
@Table(name = DatabaseConstants.SMS_CONFIGURATION_TABLE)
@ApiModel(value = "SMS configurations", description = "Message configurations")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SmsConfig  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private @Setter(AccessLevel.PROTECTED) Long id;

    @Column(name = "name")
    String name;

    @Column(name = "sid")
    String sid;

    @Column(name = "token")
    String token;

    @Column(name = "phonenumber")
    String phoneNumber;

    @Column(name = "is_active",nullable = false, columnDefinition = "boolean default false")
    Boolean isActive;

    public SmsConfig(String name, String sid, String token, String phoneNumber, Boolean isActive) {
        this.name = name;
        this.sid = sid;
        this.token = token;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
    }
}
