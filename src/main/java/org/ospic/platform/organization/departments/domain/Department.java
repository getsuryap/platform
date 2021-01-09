package org.ospic.platform.organization.departments.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.apache.http.client.utils.DateUtils;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.util.DateUtil;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This file was created by eli on 09/01/2021 for org.ospic.platform.organization.departments.domain
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
@Entity(name = DatabaseConstants.DEPARTMENT_TABLE)
@Table(name = DatabaseConstants.DEPARTMENT_TABLE,
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "name_org"),
        @UniqueConstraint(columnNames = {"extra_id"}, name = "extra_id_org")})
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@EqualsAndHashCode()
public class Department extends AbstractPersistableCustom implements Serializable {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private List<Department> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Department parent;


    @Column(length = 100, name = "name", unique = true)
    private String name;

    @Column(name = "hierarchy", nullable = true, length = 50)
    private String hierarchy;

    @Column(length = 300, name = "known_as", unique = true)
    private String knownAs;

    @Column(name = "opening_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date openingDate;


    @Column(name = "extra_id", length = 100)
    private String extraId;

    public static Department headDepartment(final Department parent, final String name, final LocalDate openingDate, final String knownAs, final String extraId){
        return new Department(parent, name, openingDate, knownAs, extraId);
    }

    private Department(final Department parent, final String name, final LocalDate openingDate, final String knownAs, final String extraId) {
        this.name = name;
        this.knownAs = knownAs;
        this.extraId = extraId;
        this.openingDate = new DateUtil().convertToDateViaSqlDate(openingDate);
        this.parent = parent;
        if (parent != null){
            this.parent.addChild(this);
        }

    }

    private void addChild(final Department department) {
        this.children.add(department);
    }
}
