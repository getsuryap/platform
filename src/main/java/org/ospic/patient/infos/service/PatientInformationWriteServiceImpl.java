package org.ospic.patient.infos.service;

import org.hibernate.SessionFactory;
import org.ospic.fileuploads.service.FilesStorageService;
import org.ospic.patient.contacts.domain.ContactsInformation;
import org.ospic.patient.contacts.repository.ContactsInformationRepository;
import org.ospic.patient.contacts.services.ContactsInformationService;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.infos.repository.PatientInformationRepository;
import org.ospic.security.authentication.users.payload.response.MessageResponse;
import org.ospic.organization.staffs.service.StaffsReadPrinciplesService;
import org.ospic.util.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * This file was created by eli on 02/11/2020 for org.ospic.patient.infos.service
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
    private PatientInformationRepository patientInformationRepository;
    @Autowired
    ContactsInformationRepository contactsInformationRepository;
    @Autowired
    ContactsInformationService contactsInformationService;
    @Autowired
    SessionFactory sessionFactory;
    FilesStorageService filesStorageService;

    StaffsReadPrinciplesService staffsReadPrinciplesService;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public PatientInformationWriteServiceImpl(
            DataSource dataSource,
            StaffsReadPrinciplesService staffsReadPrinciplesService,
            FilesStorageService filesStorageService) {
        this.staffsReadPrinciplesService = staffsReadPrinciplesService;
        this.filesStorageService = filesStorageService;

        jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public Patient createNewPatient(Patient patientInformation) {
        return patientInformationRepository.save(patientInformation);
    }

    @Override
    public List<Patient> createByPatientListIterate(List<Patient> patientInformationList) {
        return (List<Patient>) patientInformationRepository.saveAll(patientInformationList);
    }
    @Override
    public ResponseEntity deletePatientById(Long id) {
        if (patientInformationRepository.existsById(id)) {
            patientInformationRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Patient deleted Successfully"));

        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new MessageResponse("Patient with a given ID is either not available or has being deleted by someone else"));

    }

    @Override
    public ResponseEntity updatePatient(Long id, Patient update) {
        return patientInformationRepository.findById(id)
                .map(patient -> {
              patient.setContactsInformation(patient.getContactsInformation());
                    patient.setIsAdmitted(update.getIsAdmitted());
                    return ResponseEntity.ok(patientInformationRepository.save(patient));

                }).orElseThrow(() -> new EntityNotFoundException());
    }

    @Override
    public ContactsInformation updatePatientContacts(Long patientId, ContactsInformation contactsInformationRequest) {
        return patientInformationRepository.findById(patientId).map(patientInformation -> {
            ContactsInformation contactsInformation = new ContactsInformation(
                    contactsInformationRequest.getIsReachable(), contactsInformationRequest.getEmail_address(),
                    contactsInformationRequest.getZipcode(), contactsInformationRequest.getCity(),
                    contactsInformationRequest.getState(), contactsInformationRequest.getPhysical_address(),
                    contactsInformationRequest.getWork_phone(), contactsInformationRequest.getWork_phone(), patientInformation
            );
            patientInformation.setContactsInformation(contactsInformation);
            contactsInformation.setPatient(patientInformation);

            patientInformationRepository.save(patientInformation);
            return contactsInformation;
        }).orElseGet(() -> {
            return null;
        });
    }

    @Transactional
    @Override
    public ResponseEntity assignPatientToPhysician(Long patientId, Long physicianId) throws ResourceNotFoundException {
        return patientInformationRepository.findById(patientId).map(patient -> {
            staffsReadPrinciplesService.retrieveStaffById(physicianId).ifPresent(physician -> {

                patient.setStaff(physician);
                patientInformationRepository.save(patient);

            });

            return ResponseEntity.ok(staffsReadPrinciplesService.getStaffById(physicianId));
        }).orElseThrow(() -> new ResourceNotFoundException("Staff not set"));

    }

    @Transactional
    @Override
    public ResponseEntity uploadPatientImage(Long patientId, MultipartFile file) {
        try {
            return patientInformationRepository.findById(patientId).map(patient -> {
                String imagePath = filesStorageService.uploadPatientImage(patientId, "images", file);
                patient.setPatientPhoto(imagePath);
                return ResponseEntity.ok().body(patientInformationRepository.save(patient));
            }).orElseThrow(() -> new ResourceNotFoundException("patient with id: " + patientId + "not found"));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    @Override
    public ResponseEntity deletePatientImage(Long patientId, String fileName) {
        try {
            patientInformationRepository.findById(patientId).map(patient -> {
                filesStorageService.deletePatientFileOrDocument("images", patientId, fileName);
                patient.setPatientPhoto(null);
                patientInformationRepository.save(patient);
                return ResponseEntity.ok().body(patientInformationRepository.findById(patientId));
            }).orElseThrow(() -> new ResourceNotFoundException("patient with id: " + patientId + "not found"));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new MessageResponse(String.format("Patient with given Id is not available "))
        );
    }
}
