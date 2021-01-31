package org.ospic.platform.patient.diagnosis.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * This file was created by eli on 19/10/2020 for org.ospic.platform.patient.diagnosis.domains
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
@Entity(name = DatabaseConstants.DIAGNOSES_TABLE)
@Table(name = DatabaseConstants.DIAGNOSES_TABLE)
@ApiModel(value = "Diagnosis", description = "A Diagnosis row containing specific patient information's")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Diagnosis implements Serializable {
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Setter(AccessLevel.PROTECTED)  Long id;


    @NotBlank
    @Column(name = "symptoms")
    private String symptoms;

    @Column(name = "date",  nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "cid")
    @JsonIgnore
    private ConsultationResource service;

    public Diagnosis( String symptoms, LocalDate date, ConsultationResource service) {
        this.symptoms = symptoms;
        this.date = date;
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Diagnosis )) return false;
        return id != null && id.equals(((Diagnosis) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
