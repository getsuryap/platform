package org.ospic.patient.service;

import org.ospic.contacts.domain.ContactsInformation;
import org.ospic.patient.domain.Patient;
import org.ospic.util.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
@Component
@Service
public interface PatientInformationServices {
    @Transactional
    public List<Patient> retrieveAllPatients();

    @Transactional
    public Patient createNewPatient(Patient patientInformation);

    @Transactional
    public List<Patient> createByPatientListIterate(List<Patient> patientInformationList);

    @Transactional
    public ResponseEntity retrievePatientById(Long id) throws ResourceNotFoundException;

    @Transactional
    public ResponseEntity deletePatientById(Long id);

    @Transactional
    public ResponseEntity updatePatient(Long id, Patient patient);

    @Transactional
    public ResponseEntity assignPatientToPhysician(Long patientId, Long physicianId) throws ResourceNotFoundException;

    @Transactional
    public ContactsInformation updatePatientContacts(Long patientId, ContactsInformation contactsInformationRequest);

    public ResponseEntity retrievePatientCreationDataTemplate();
}
