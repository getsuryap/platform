package com.context.springsecurity.physicians.api;

import com.context.springsecurity.patient.domain.Patient;
import com.context.springsecurity.patient.service.PatientInformationServices;
import com.context.springsecurity.physicians.domains.Physician;
import com.context.springsecurity.physicians.service.PhysicianInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/api/physicians")
public class PhysicianApiResource {

    PhysicianInformationService physicianInformationService;

    @Autowired
    PhysicianApiResource(PhysicianInformationService physicianInformationService) {
        this.physicianInformationService = physicianInformationService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    List<Physician> all() {
        return physicianInformationService.retrieveAllPhysicians();
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity create(@Valid @RequestBody Physician physician) {
        return physicianInformationService.createNewPhysician(physician);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity getOne(@PathVariable Long id) {
        return physicianInformationService.getPhysicianById(id);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity update(@PathVariable Long id, @RequestBody Physician physician){
        return  physicianInformationService.updatePhysician(id,physician);
    }


}
