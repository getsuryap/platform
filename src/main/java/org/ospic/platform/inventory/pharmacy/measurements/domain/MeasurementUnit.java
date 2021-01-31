package org.ospic.platform.inventory.pharmacy.measurements.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.ospic.platform.inventory.pharmacy.categories.domains.MedicineCategory;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * This file was created by eli on 26/01/2021 for org.ospic.platform.inventory.pharmacy.measurements.domain
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
@Entity(name = DatabaseConstants.UNITS_OF_MEASUREMENT_TABLE)
@Table(name = DatabaseConstants.UNITS_OF_MEASUREMENT_TABLE,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"unit", "symbol"}),
        })
@ApiModel(value = "Medicine measurement units", description = "Contain all medicine measurement unit's")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode
public class MeasurementUnit {
    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;


    @NotBlank
    @Column(name = "unit", length = 100, nullable = false)
    private String unit;

    @NotBlank
    @Column(name = "symbol", length = 20, nullable = false)
    private String symbol;

    @Column(name = "quantity", length = 80)
    private String quantity;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "unit_id")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "List of medicine categories under this measure")
    @JsonIgnore
    private List<MedicineCategory> medicineCategories = new ArrayList<>();


    public MeasurementUnit instance(final String unit, final String symbol, final String quantity) {
        return new MeasurementUnit(unit, symbol, quantity);
    }

    private MeasurementUnit(final String unit, final String symbol, final String quantity) {
        this.unit = unit;
        this.symbol = symbol;
        this.quantity = quantity;
    }
}
