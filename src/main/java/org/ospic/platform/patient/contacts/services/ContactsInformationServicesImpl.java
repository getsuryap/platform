package org.ospic.platform.patient.contacts.services;

import org.ospic.platform.patient.contacts.domain.ContactsInformation;
import org.ospic.platform.patient.contacts.exceptions.ContactNotFoundExceptions;
import org.ospic.platform.patient.contacts.repository.ContactsInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

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
@Repository
public class ContactsInformationServicesImpl implements ContactsInformationService {
    private static final Logger logger = LoggerFactory.getLogger(ContactsInformationServicesImpl.class);

    @Autowired
    ContactsInformationRepository contactsInformationRepository;

    @Override
    public ContactsInformation createNewContact(Long patientId, ContactsInformation contactsInformation) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateContactInformation(Long contactId, ContactsInformation payload) {
        return this.contactsInformationRepository.findById(contactId).map(contact->{
            payload.setId(contactId);
            ContactsInformation response = this.contactsInformationRepository.save(payload);
            return ResponseEntity.ok().body(response);
        }).orElseThrow(()->new ContactNotFoundExceptions(contactId));
    }

    @Override
    public List<ContactsInformation> retrieveAllContactsInformation() {
        return contactsInformationRepository.findAll();
    }

    @Override
    public List<ContactsInformation> createNewContactsByIteration(List<ContactsInformation> contactsInformationList) {
        return contactsInformationRepository.saveAll(contactsInformationList);
    }

    @Override
    public ResponseEntity retrievePatientContactByPatientId(Long patientId) {
        return ResponseEntity.status(HttpStatus.OK).body(contactsInformationRepository.findById(patientId));
    }
}
