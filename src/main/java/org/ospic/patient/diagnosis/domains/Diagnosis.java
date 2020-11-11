package org.ospic.patient.diagnosis.domains;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.ospic.domain.Auditable;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.physicians.domains.Physician;
import org.ospic.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * This file was created by eli on 19/10/2020 for org.ospic.patient.diagnosis.domains
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

    @Column(name = "problem")
    private String problemIdentification;

    @Column(name = "diagnosis_report")
    private String diagnosisReport;

    @NotBlank
    @Column(name = "treatments")
    private String treatmentType;

    @NotBlank
    @Column(name = "medicines")
    private String medicineNames;

    @NonNull
    @NotBlank
    @Column(name = "lab_tests")
    private String laboratoryTests;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;

    public Diagnosis(String problemIdentification, String diagnosisReport, String treatmentType, String medicineNames,String laboratoryTests){
        this.diagnosisReport = diagnosisReport;
        this.laboratoryTests = laboratoryTests;
        this.treatmentType = treatmentType;
        this.medicineNames = medicineNames;
        this.problemIdentification = problemIdentification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient )) return false;
        return id != null && id.equals(((Diagnosis) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
