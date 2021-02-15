package org.ospic.platform.inventory.pharmacy.categories.domains;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.ospic.platform.inventory.pharmacy.measurements.domain.MeasurementUnit;
import org.ospic.platform.inventory.pharmacy.medicine.domains.Medicine;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

/**
 * This file was created by eli on 12/11/2020 for org.ospic.platform.inventory.pharmacy.groups.domains
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
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity(name = DatabaseConstants.TABLE_MEDICINE_CATEGORY_)
@Table(name = DatabaseConstants.TABLE_MEDICINE_CATEGORY_,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name"}),
        })
@ApiModel(value = "Medicine", description = "Contain all medicine's available")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode
public class MedicineCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;


    @NotBlank
    @Column(name = "name", length = 200, nullable = false)
    private String name;


    @Column(name = "descriptions", length = 350)
    private String descriptions;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Set<Medicine> medicines = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private MeasurementUnit measurementUnit;

    public MedicineCategory instance(String name, String descriptions){
        return new MedicineCategory(name, descriptions);
    }

    private MedicineCategory(String name, String descriptions) {
        this.name = name;
        this.descriptions = descriptions;
    }
    
}
