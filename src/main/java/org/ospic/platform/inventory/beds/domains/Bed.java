package org.ospic.platform.inventory.beds.domains;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ospic.platform.inventory.admission.domains.Admission;
import org.ospic.platform.inventory.wards.domain.Ward;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * This file was created by eli on 06/11/2020 for org.ospic.platform.inventory.beds.domains
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
@Entity(name = DatabaseConstants.BEDS_TABLE)
@Table(name = DatabaseConstants.BEDS_TABLE)
@ApiModel(value = "Patient", description = "A Patient row containing specific patient information's")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Bed implements Serializable {
    private static final long serialVersionUID = -1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private @Setter(AccessLevel.PROTECTED) Long id;


    @Column(name = "identifier", unique = true, length = 20)
    private String identifier;



    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "admission_bed",
            joinColumns = @JoinColumn(name = "bed_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "admission_id", referencedColumnName = "id"))
    @JsonIgnore
    private Set<Admission> admissions = new HashSet<>();

    @Column(
            name = "is_occupied",
            nullable = false,
            columnDefinition = "boolean default false"
    )
    private Boolean isOccupied;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id",referencedColumnName = "id")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to ward")
    @JsonIgnore
    private Ward ward;

    public Bed(String identifier, Boolean isOccupied){
        this.isOccupied = isOccupied;
        this.identifier = identifier;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bed tag = (Bed) o;
        return Objects.equals(identifier, tag.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
