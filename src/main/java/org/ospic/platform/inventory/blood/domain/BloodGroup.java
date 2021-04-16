package org.ospic.platform.inventory.blood.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * This file was created by eli on 18/12/2020 for org.ospic.platform.inventory.blood.domain
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
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity(name = DatabaseConstants.TABLE_BLOOD_GROUP)
@Table(name = DatabaseConstants.TABLE_BLOOD_GROUP)
@ApiModel(value = "Blood group", description = "A blood group")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BloodGroup implements Serializable {
    private static final long serialVersionUID = -2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private @Setter(AccessLevel.PROTECTED) Long id;

    @NotBlank
    @Column(name = "blood_group",unique = true, length = 15, nullable = false)
    private  String group;


    @NotNull
    @Column(name = "bags_count",unique = false, length = 15, nullable = false)
    private Integer counts;

    public BloodGroup(String group, Integer counts) {
        this.group = group;
        this.counts = counts;
    }

    public BloodGroup instance(String group,int counts) {
       return new BloodGroup(group, counts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloodGroup bg = (BloodGroup) o;
        return Objects.equals(bg, bg.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group);
    }

}
