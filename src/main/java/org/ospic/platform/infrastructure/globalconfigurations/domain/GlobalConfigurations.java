/**
 * This file was created by eli on 18/06/2021 for org.ospic.platform.infrastructure.app.domain
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
package org.ospic.platform.infrastructure.globalconfigurations.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = DatabaseConstants.TABLE_GLOBAL_CONFIGURATION)
@Table(name = DatabaseConstants.TABLE_GLOBAL_CONFIGURATION)
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@EqualsAndHashCode(callSuper = false)
public class GlobalConfigurations extends AbstractPersistableCustom implements Serializable {

    @Column(length = 250, name = "name", unique = true)
    private String name;

    @Column(length = 250, name = "code", unique = true)
    private String code;

    @Column( name = "value")
    private Long value;

    @Column(length = 100, name = "active" )
    private Boolean active;
}
