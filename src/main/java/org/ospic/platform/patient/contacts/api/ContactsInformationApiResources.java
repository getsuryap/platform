package org.ospic.platform.patient.contacts.api;

import io.swagger.annotations.*;
import org.ospic.platform.patient.contacts.domain.ContactsInformation;
import org.ospic.platform.patient.contacts.services.ContactsInformationService;
import org.ospic.platform.patient.details.service.PatientInformationWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/contacts")
@Api(value = "/api/contacts", tags = "Contacts")
public class ContactsInformationApiResources {

    @Autowired
    ContactsInformationService contactsInformationService;
    @Autowired
    PatientInformationWriteService patientInformationWriteService;

    @Autowired
    public ContactsInformationApiResources(
            ContactsInformationService contactsInformationService,
            PatientInformationWriteService patientInformationWriteService) {
        this.patientInformationWriteService = patientInformationWriteService;
        this.contactsInformationService = contactsInformationService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    List<ContactsInformation> all() {
        return contactsInformationService.retrieveAllContactsInformation();
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ContactsInformation createNew(@RequestBody ContactsInformation contactsInformationRequest, @PathVariable Long id) {
        return patientInformationWriteService.updatePatientContacts(id, contactsInformationRequest);
    }

    @ApiOperation(
            value = "RETRIEVE patient contacts",
            notes = "RETRIEVE patient contacts"
    )
    @RequestMapping(
            value = "/{patientId}",
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    ResponseEntity retrievePatientContacts(@ApiParam(name = "patientId", required = true) @PathVariable Long patientId) {
        return contactsInformationService.retrievePatientContactByPatientId(patientId);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    List<ContactsInformation> createContacts(@Valid @RequestBody List<ContactsInformation> contactsInformationListRequest) {
        return contactsInformationService.createNewContactsByIteration(contactsInformationListRequest);
    }
}
