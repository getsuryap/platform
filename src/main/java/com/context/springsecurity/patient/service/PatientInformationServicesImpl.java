package com.context.springsecurity.patient.service;

import com.context.springsecurity.patient.contacts.domain.ContactsInformation;
import com.context.springsecurity.patient.contacts.repository.ContactsInformationRepository;
import com.context.springsecurity.patient.contacts.services.ContactsInformationService;
import com.context.springsecurity.patient.domain.Patient;
import com.context.springsecurity.patient.repository.PatientInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
public class PatientInformationServicesImpl implements PatientInformationServices {

    @Autowired
    private PatientInformationRepository patientInformationRepository;
    @Autowired
    ContactsInformationRepository contactsInformationRepository;

    @Autowired
    ContactsInformationService contactsInformationService;

    @Override
    public List<Patient> retrieveAllPatients() {
        return patientInformationRepository.findAll();
    }

    @Override
    public Patient createNewPatient(Patient patientInformation) {
        return patientInformationRepository.save(patientInformation);
    }

    @Override
    public List<Patient> createByPatientListIterate(List<Patient> patientInformationList) {
        return patientInformationRepository.saveAll(patientInformationList);
    }

    @Override
    public Optional<Patient> retrievePatientById(Long id) {
        return patientInformationRepository.findById(id);
    }

    @Override
    public ContactsInformation updatePatientContacts(Long patientId, ContactsInformation contactsInformationRequest) {
        return patientInformationRepository.findById(patientId).map(patientInformation -> {
            ContactsInformation contactsInformation = new ContactsInformation(
                    contactsInformationRequest.getIsReachable(), contactsInformationRequest.getEmail_address(),
                    contactsInformationRequest.getZipcode(), contactsInformationRequest.getCity(),
                    contactsInformationRequest.getState(), contactsInformationRequest.getPhysical_address(),
                    contactsInformationRequest.gethome_phone(), contactsInformationRequest.getwork_phone(),patientInformation
            );
            patientInformation.setContactsInformation(contactsInformation);
            contactsInformation.setPatient(patientInformation);

            patientInformationRepository.save(patientInformation);
            return contactsInformation;
        }).orElseGet(() -> {
            return null;
        });
    }
}
