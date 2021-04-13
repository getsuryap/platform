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

    private final ContactsInformationService contactsInformationService;
    private final PatientInformationWriteService patientInformationWriteService;

    @Autowired
    public ContactsInformationApiResources(
            ContactsInformationService contactsInformationService,
            PatientInformationWriteService patientInformationWriteService) {
        this.patientInformationWriteService = patientInformationWriteService;
        this.contactsInformationService = contactsInformationService;
    }

    @ApiOperation(value = "GET all contacts", notes = "GET all contacts", response = ContactsInformation.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    ResponseEntity<?> listAllContacts() {
        return ResponseEntity.ok().body(contactsInformationService.retrieveAllContactsInformation());
    }

    @ApiOperation(value = "UPDATE patient add contacts", notes = "UPDATE patient add contacts", response = ContactsInformation.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updatePatientAddContactsInformation(@RequestBody ContactsInformation contactsInformationRequest, @PathVariable Long id) {
        return ResponseEntity.ok().body(patientInformationWriteService.updatePatientContacts(id, contactsInformationRequest));
    }

    @ApiOperation(value = "UPDATE contacts information", notes = "UPDATE contacts information", response = ContactsInformation.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateContactsInformation(@RequestBody ContactsInformation payload, @PathVariable Long id) {
        return this.contactsInformationService.updateContactInformation(id, payload);
    }

    @ApiOperation(value = "RETRIEVE patient contacts", notes = "RETRIEVE patient contacts", response = ContactsInformation.class)
    @RequestMapping(value = "/{patientId}",method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrievePatientContacts(@ApiParam(name = "patientId", required = true) @PathVariable Long patientId) {
        return contactsInformationService.retrievePatientContactByPatientId(patientId);
    }

    @ApiOperation(value = "CREATE patient contact", notes = "CREATE patient contact", response = ContactsInformation.class, responseContainer = "List")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    ResponseEntity<?> createContacts(@Valid @RequestBody List<ContactsInformation> contactsInformationListRequest) {
        return ResponseEntity.ok().body(contactsInformationService.createNewContactsByIteration(contactsInformationListRequest));
    }
}
