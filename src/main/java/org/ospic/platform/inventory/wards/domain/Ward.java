package org.ospic.platform.inventory.wards.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.inventory.beds.domains.Bed;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
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
@Entity(name = DatabaseConstants.WARDS_TABLE)
@Table(name = DatabaseConstants.WARDS_TABLE, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}),
})
@ApiModel(value = "Wards", description = "A Wards ")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Ward extends AbstractPersistableCustom implements Serializable {

    @NotNull
    @Column(name = "name", unique = true, length = 15, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "ward_id")
    private List<Bed> beds = new ArrayList<>();

    public Ward(String name) {
        this.name = name;
    }

    public void addBed(Bed bed) {
        beds.add(bed);
        bed.setWard(this);
    }

    public void deletePatient(Bed bed) {
        beds.remove(bed);
        bed.setWard(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ward)) return false;
        return name != null && name.equals(((Ward) o).name);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
