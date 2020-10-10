package com.context.springsecurity.patient.contacts.api;

import com.context.springsecurity.patient.contacts.domain.ContactsInformation;
import com.context.springsecurity.patient.contacts.services.ContactsInformationService;
import com.context.springsecurity.patient.service.PatientInformationServices;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/contacts")
@Api(value = "/api/contacts", tags = "Contacts")
public class ContactsInformationApiResources {

    @Autowired
    ContactsInformationService contactsInformationService;
    @Autowired
    PatientInformationServices patientInformationServices;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    List<ContactsInformation> all() {
        return contactsInformationService.retrieveAllContactsInformation();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    ContactsInformation createNew(@Valid @RequestBody ContactsInformation contactsInformationRequest, @PathVariable Long id) {
        return patientInformationServices.updatePatientContacts(id,contactsInformationRequest);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    List<ContactsInformation> createContacts(@Valid @RequestBody List<ContactsInformation> contactsInformationListRequest) {
        return contactsInformationService.createNewContactsByIteration(contactsInformationListRequest);
    }
}
