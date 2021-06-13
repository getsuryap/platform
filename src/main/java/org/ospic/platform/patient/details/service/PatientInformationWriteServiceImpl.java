package org.ospic.platform.patient.details.service;

import org.hibernate.SessionFactory;
import org.ospic.platform.fileuploads.data.EntityType;
import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.ospic.platform.infrastructure.app.exception.AbstractPlatformInactiveResourceException;
import org.ospic.platform.organization.authentication.users.payload.response.MessageResponse;
import org.ospic.platform.organization.staffs.exceptions.StaffNotFoundExceptionPlatform;
import org.ospic.platform.organization.staffs.repository.StaffsRepository;
import org.ospic.platform.patient.contacts.domain.ContactsInformation;
import org.ospic.platform.patient.contacts.repository.ContactsInformationRepository;
import org.ospic.platform.patient.contacts.services.ContactsInformationService;
import org.ospic.platform.patient.details.domain.Patient;
import org.ospic.platform.patient.details.exceptions.PatientNotFoundExceptionPlatform;
import org.ospic.platform.patient.details.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
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
@Repository
public class PatientInformationWriteServiceImpl implements PatientInformationWriteService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    ContactsInformationRepository contactsInformationRepository;
    @Autowired
    ContactsInformationService contactsInformationService;
    @Autowired
    SessionFactory sessionFactory;
    FilesStorageService filesStorageService;

    StaffsRepository staffsRepository;
    JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(PatientInformationWriteServiceImpl.class);

    @Autowired
    public PatientInformationWriteServiceImpl(
            DataSource dataSource,
            StaffsRepository staffsRepository,
            FilesStorageService filesStorageService) {
        this.staffsRepository = staffsRepository;
        this.filesStorageService = filesStorageService;

        jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    @Transactional
    public Patient createNewPatient(Patient patient) {
        patient.setIsActive(false);
        patient.setHasSelfServiceUserAccount(false);
        logger.info(patient.toString());
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> createByPatientListIterate(List<Patient> patientInformationList) {
        return (List<Patient>) patientRepository.saveAll(patientInformationList);
    }

    @Override
    public ResponseEntity<?> deletePatientById(Long id) {
        return patientRepository.findById(id).map(patient -> {
            patientRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Patient deleted Successfully"));
        }).orElseThrow(()->new PatientNotFoundExceptionPlatform(id));
    }

    @Override
    public ResponseEntity<?> updatePatient(Long id, Patient updates) {
        return patientRepository.findById(id)
                .map(patient -> {
                    updates.setId(patient.getId());
                    Patient p = patientRepository.save(updates);
                    return ResponseEntity.ok(p);
                }).orElseThrow(() -> new PatientNotFoundExceptionPlatform(id));
    }

    @Override
    public ContactsInformation updatePatientContacts(Long patientId, ContactsInformation contactsInformationRequest) {
        return patientRepository.findById(patientId).map(patientInformation -> {
            ContactsInformation contactsInformation = new ContactsInformation().fromRequest(contactsInformationRequest);
            patientInformation.setContactsInformation(contactsInformation);
            contactsInformation.setPatient(patientInformation);

            patientRepository.save(patientInformation);
            return contactsInformation;
        }).orElseThrow(() -> new PatientNotFoundExceptionPlatform(patientId));
    }

    @Transactional
    @Override
    public ResponseEntity<?> assignPatientToPhysician(Long patientId, Long physicianId) throws AbstractPlatformInactiveResourceException.ResourceNotFoundException {
        return patientRepository.findById(patientId).map(patient -> {
            return staffsRepository.findById(physicianId).map(physician -> {
                patientRepository.save(patient);
                return ResponseEntity.ok(physician);
            }).orElseThrow(() -> new StaffNotFoundExceptionPlatform(physicianId));
        }).orElseThrow(() -> new PatientNotFoundExceptionPlatform(patientId));

    }

    @Transactional
    @Override
    public ResponseEntity<?> uploadPatientImage(Long patientId, MultipartFile file) {
        return patientRepository.findById(patientId).map(patient -> {
            String imagePath = filesStorageService.uploadPatientImage(patientId, EntityType.ENTITY_PATIENTS,  file,"images");
            patient.setPatientPhoto(imagePath);
            return ResponseEntity.ok().body(patientRepository.save(patient));
        }).orElseThrow(() -> new PatientNotFoundExceptionPlatform(patientId));
    }

    @Transactional
    @Override
    public ResponseEntity<?> deletePatientImage(Long patientId, String fileName) {
        return patientRepository.findById(patientId).map(patient -> {
            filesStorageService.deletePatientFileOrDocument("images",EntityType.ENTITY_PATIENTS, patientId, fileName);
            patient.setPatientPhoto(null);
            patientRepository.save(patient);
            return ResponseEntity.ok().body(patientRepository.findById(patientId));
        }).orElseThrow(() -> new PatientNotFoundExceptionPlatform(patientId));

    }

}
