package com.context.springsecurity.patient.service;

import com.context.springsecurity.contacts.domain.ContactsInformation;
import com.context.springsecurity.contacts.repository.ContactsInformationRepository;
import com.context.springsecurity.contacts.services.ContactsInformationService;
import com.context.springsecurity.patient.data.PatientData;
import com.context.springsecurity.patient.domain.Patient;
import com.context.springsecurity.patient.repository.PatientInformationRepository;
import com.context.springsecurity.payload.response.MessageResponse;
import com.context.springsecurity.physicians.domains.Physician;
import com.context.springsecurity.physicians.service.PhysicianInformationService;
import com.context.springsecurity.util.exceptions.ResourceNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
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
@Repository
public class PatientInformationServicesImpl implements PatientInformationServices {

    @Autowired
    private PatientInformationRepository patientInformationRepository;
    @Autowired
    ContactsInformationRepository contactsInformationRepository;
    @Autowired
    ContactsInformationService contactsInformationService;
    @Autowired
    SessionFactory sessionFactory;

    PhysicianInformationService physicianInformationService;
    @Autowired
    public PatientInformationServicesImpl(
            PhysicianInformationService physicianInformationService){
        this.physicianInformationService = physicianInformationService;
    }

    @Override
    public List<Patient> retrieveAllPatients() {
        Session session = this.sessionFactory.openSession();
        List<Patient> patientList = session.createQuery("from m_patient").list();
        session.close();
        return patientList;
    }

    @Override
    public ResponseEntity retrievePatientCreationDataTemplate() {
        List<Physician> physiciansOptions = physicianInformationService.retrieveAllPhysicians();
        for (int i = 0; i<physiciansOptions.size();i++){
            physiciansOptions.get(i).getPatients().clear();
        }
        return ResponseEntity.ok().body(PatientData.patientCreationTemplate(physiciansOptions));
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
    public ResponseEntity retrievePatientById(Long id) throws ResourceNotFoundException {
        Patient patient = patientInformationRepository.findById(id).get();
        if (patient.getPhysician() != null){
            patient.getPhysician().getPatients().clear();
        }
      return ResponseEntity.ok().body(patient);
    }

    @Override
    public ResponseEntity deletePatientById(Long id) {
        if (patientInformationRepository.existsById(id)) {
            patientInformationRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Patient deleted Successfully"));
        }
        else {
            return ResponseEntity.ok(new MessageResponse("Patient with a given ID is either not available or has being deleted by someone else"));
        }
    }

    @Override
    public ResponseEntity updatePatient(Long id, Patient update) {
        return patientInformationRepository.findById(id)
                .map(patient -> {
                    patient.setCountry(update.getCountry() == null ? patient.getCountry() : update.getCountry());
                    patient.setDob(update.getDob() == null ? patient.getDob() : update.getDob());
                    patient.setEthnicity(update.getEthnicity() == null ? patient.getEthnicity() : update.getEthnicity());
                    patient.setFirst_name(update.getFirst_name() == null ? patient.getFirst_name() : update.getFirst_name());
                    patient.setMiddle_name(update.getMiddle_name() == null ? patient.getMiddle_name() : update.getMiddle_name());
                    patient.setLast_name(update.getLast_name() == null ? patient.getLast_name() : update.getLast_name());
                    patient.setMdn(update.getMdn() == null ? patient.getMdn() : update.getMdn());
                    patient.setGender(update.getGender() == null ? patient.getGender() : update.getGender());
                    patient.setSuffix(update.getSuffix() == null ? patient.getSuffix() : update.getSuffix());
                    patient.setPrincipal_tribe(update.getPrincipal_tribe() == null ? patient.getPrincipal_tribe() : update.getPrincipal_tribe());
                    patient.setContactsInformation(patient.getContactsInformation());
                    patient.setSsn(update.getSsn() == null ? patient.getSsn() : update.getSsn());
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
    public ResponseEntity assignPatientToPhysician(Long patientId,  Long physicianId) throws ResourceNotFoundException{
        return patientInformationRepository.findById(patientId).map(patient -> {
            physicianInformationService.retrievePhysicianById(physicianId).ifPresent(physician -> {

                 patient.setPhysician(physician);
                 patientInformationRepository.save(patient);

            });

         return ResponseEntity.ok(physicianInformationService.getPhysicianById(physicianId));
        }).orElseThrow(() -> new ResourceNotFoundException("Physician not set"));

    }
}
