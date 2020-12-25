package org.ospic.patient.infos.service;

import org.hibernate.SessionFactory;
import org.ospic.fileuploads.service.FilesStorageService;
import org.ospic.patient.contacts.domain.ContactsInformation;
import org.ospic.patient.contacts.repository.ContactsInformationRepository;
import org.ospic.patient.contacts.services.ContactsInformationService;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.infos.exceptions.PatientNotFoundException;
import org.ospic.patient.infos.repository.PatientRepository;
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
    private PatientRepository patientRepository;
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
    @Transactional
    public Patient createNewPatient(Patient patient) {
        patient.setIsActive(false);
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> createByPatientListIterate(List<Patient> patientInformationList) {
        return (List<Patient>) patientRepository.saveAll(patientInformationList);
    }

    @Override
    public ResponseEntity<?> deletePatientById(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Patient deleted Successfully"));

        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new MessageResponse("Patient with a given ID is either not available or has being deleted by someone else"));

    }

    @Override
    public ResponseEntity<?> updatePatient(Long id, Patient update) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setIsAdmitted(update.getIsAdmitted()==null ? patient.getIsAdmitted() : update.getIsAdmitted());
                    patient.setAddress(update.getAddress() == null ? patient.getAddress() :  update.getAddress());
                    patient.setName(update.getName() == null ? patient.getName() : update.getName());
                    patient.setGuardianName(update.getGuardianName()== null ? patient.getGuardianName() :  update.getGuardianName());
                    patient.setBloodGroup(update.getBloodGroup()== null ? patient.getBloodGroup(): update.getBloodGroup());
                    patient.setBloodGroup(update.getBloodGroup()== null ? patient.getBloodGroup() :  update.getBloodGroup());
                    patient.setWeight(update.getWeight()== null ? patient.getWeight() : update.getWeight());
                    patient.setHeight(update.getWeight()== null ? patient.getWeight():update.getWeight());
                    patient.setAge(update.getAge());
                    patient.setEmailAddress(update.getEmailAddress()== null ? patient.getEmailAddress() : update.getEmailAddress());
                    patient.setGender(update.getGender()== null ? patient.getGender(): update.getGender());
                    patient.setMarriageStatus(update.getMarriageStatus()== null ? patient.getMarriageStatus() : update.getMarriageStatus());
                    patient.setPhone(update.getPhone()== null ? patient.getPhone() : update.getPhone());
                    patient.setNote(update.getNote()== null ? patient.getNote(): update.getNote());
                    patient.setSymptoms(update.getSymptoms()== null ? patient.getSymptoms() :update.getSymptoms());
                    return ResponseEntity.ok(patientRepository.save(patient));

                }).orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Override
    public ContactsInformation updatePatientContacts(Long patientId, ContactsInformation contactsInformationRequest) {
        return patientRepository.findById(patientId).map(patientInformation -> {
            ContactsInformation contactsInformation = new ContactsInformation(
                    contactsInformationRequest.getIsReachable(), contactsInformationRequest.getEmail_address(),
                    contactsInformationRequest.getZipcode(), contactsInformationRequest.getCity(),
                    contactsInformationRequest.getState(), contactsInformationRequest.getPhysical_address(),
                    contactsInformationRequest.getWork_phone(), contactsInformationRequest.getWork_phone(), patientInformation
            );
            patientInformation.setContactsInformation(contactsInformation);
            contactsInformation.setPatient(patientInformation);

            patientRepository.save(patientInformation);
            return contactsInformation;
        }).orElseGet(() -> {
            return null;
        });
    }

    @Transactional
    @Override
    public ResponseEntity<?> assignPatientToPhysician(Long patientId, Long physicianId) throws ResourceNotFoundException {
        return patientRepository.findById(patientId).map(patient -> {
            staffsReadPrinciplesService.retrieveStaffById(physicianId).ifPresent(physician -> {

                // patient.setStaff(physician);
                patientRepository.save(patient);

            });

            return ResponseEntity.ok(staffsReadPrinciplesService.getStaffById(physicianId));
        }).orElseThrow(() -> new ResourceNotFoundException("Staff not set"));

    }

    @Transactional
    @Override
    public ResponseEntity<?> uploadPatientImage(Long patientId, MultipartFile file) {
        try {
            return patientRepository.findById(patientId).map(patient -> {
                String imagePath = filesStorageService.uploadPatientImage(patientId, "images", file);
                patient.setPatientPhoto(imagePath);
                return ResponseEntity.ok().body(patientRepository.save(patient));
            }).orElseThrow(() -> new ResourceNotFoundException("patient with id: " + patientId + "not found"));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    @Override
    public ResponseEntity<?> deletePatientImage(Long patientId, String fileName) {
        try {
            patientRepository.findById(patientId).map(patient -> {
                filesStorageService.deletePatientFileOrDocument("images", patientId, fileName);
                patient.setPatientPhoto(null);
                patientRepository.save(patient);
                return ResponseEntity.ok().body(patientRepository.findById(patientId));
            }).orElseThrow(() -> new ResourceNotFoundException("patient with id: " + patientId + "not found"));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new MessageResponse(String.format("Patient with given Id is not available "))
        );
    }
}
