package org.ospic.platform.patient.details.service;

import org.ospic.platform.infrastructure.app.exception.AbstractPlatformInactiveResourceException;
import org.ospic.platform.patient.contacts.domain.ContactsInformation;
import org.ospic.platform.patient.details.domain.Patient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

/**
 * This file was created by eli on 02/11/2020 for org.ospic.platform.patient.details.service
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
@Component
@Service
public interface PatientInformationWriteService {
    @Transactional
    public ResponseEntity<?> deletePatientById(Long id);

    @Transactional
    public ResponseEntity<?> updatePatient(Long id, Patient patient);

    @Transactional
    public ResponseEntity<?> assignPatientToPhysician(Long patientId, Long physicianId) throws AbstractPlatformInactiveResourceException.ResourceNotFoundException;

    @Transactional
    public ContactsInformation updatePatientContacts(Long patientId, ContactsInformation contactsInformationRequest);

    @Transactional
    public Patient createNewPatient(Patient patientInformation);

    @Transactional
    public List<Patient> createByPatientListIterate(List<Patient> patientInformationList);


    public ResponseEntity<?> uploadPatientImage(Long patientId,  MultipartFile file);

    public ResponseEntity<?> deletePatientImage(Long patientId ,String fileName);

}
