package com.context.springsecurity.patient.api;

import com.context.springsecurity.patient.domain.Patient;
import com.context.springsecurity.patient.service.PatientInformationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/patients")
public class PatientApiResources {

    PatientInformationServices patientInformationServices;

    @Autowired
    PatientApiResources(PatientInformationServices patientInformationServices){
        this.patientInformationServices = patientInformationServices;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    List<Patient> all() {
        return patientInformationServices.retrieveAllPatients();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
   ResponseEntity findById(@Valid @PathVariable Long id) {
        return patientInformationServices.retrievePatientById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return patientInformationServices.updatePatient(id,patient);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    Patient createNewPatient(@Valid @RequestBody Patient patientInformationRequest) {
        return patientInformationServices.createNewPatient(patientInformationRequest);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    List<Patient> createNewPatients(@Valid @RequestBody List<Patient> patientInformationListRequest){
        return patientInformationServices.createByPatientListIterate(patientInformationListRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    ResponseEntity deletePatient(@PathVariable Long id) {
        return patientInformationServices.deletePatientById(id);
    }

}
