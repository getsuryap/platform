package org.ospic.platform.patient.details.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.platform.domain.PageableResponse;
import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.ospic.platform.infrastructure.app.exception.AbstractPlatformInactiveResourceException;
import org.ospic.platform.organization.staffs.domains.Staff;
import org.ospic.platform.organization.staffs.service.StaffsReadPrinciplesService;
import org.ospic.platform.organization.statistics.data.PatientStatistics;
import org.ospic.platform.patient.contacts.repository.ContactsInformationRepository;
import org.ospic.platform.patient.contacts.services.ContactsInformationService;
import org.ospic.platform.patient.details.data.PatientData;
import org.ospic.platform.patient.details.domain.Patient;
import org.ospic.platform.patient.details.exceptions.PatientNotFoundExceptionPlatform;
import org.ospic.platform.patient.details.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
public class PatientInformationReadServicesImpl implements PatientInformationReadServices {
     PatientRepository patientRepository;
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
    public PatientInformationReadServicesImpl(
            DataSource dataSource,PatientRepository patientRepository,
            StaffsReadPrinciplesService staffsReadPrinciplesService,
            FilesStorageService filesStorageService) {
        this.staffsReadPrinciplesService = staffsReadPrinciplesService;
        this.filesStorageService = filesStorageService;
        this.patientRepository = patientRepository;
        jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public List<Patient> retrieveAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return  patients;
    }

    @Override
    public ResponseEntity<?> retrieveAllAssignedPatients() {
        Session session = this.sessionFactory.openSession();
        List<Patient> patients = session.createQuery("from m_patients WHERE staff_id IS NOT NULL").list();
        session.close();
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @Override
    public ResponseEntity<?> retrieveAllUnAssignedPatients() {
        Session session = this.sessionFactory.openSession();
        List<Patient> patients = session.createQuery("from m_patients WHERE staff_id IS NULL").list();
        session.close();
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @Override
    public ResponseEntity<?> retrievePatientCreationDataTemplate() {
        List<Staff> staffs = staffsReadPrinciplesService.retrieveAllStaffs();
        for (int i = 0; i < staffs.size(); i++) {
            //staffs.get(i).getPatients().clear();
        }
        return ResponseEntity.ok().body(PatientData.patientCreationTemplate(staffs));
    }



    @Override
    public ResponseEntity<?> retrievePatientById(Long id) throws AbstractPlatformInactiveResourceException.ResourceNotFoundException {
        return patientRepository.findById(id).map(patient -> {
            return ResponseEntity.ok().body(patient);
        }).orElseThrow(()->new PatientNotFoundExceptionPlatform(id));
    }

    @Override
    public ResponseEntity<?> retrievePatientAdmittedInThisBed(Long bedId) {
        String sb =
                " select p.id from  m_patients p where id =  " +
                        " (select  pa.patient_id from " +
                        " m_admissions a " +
                        " inner join  admission_bed  ba ON a.id = ba.admission_id " +
                        " inner join m_admissions_m_patients pa ON a.id = pa.admission_id " +
                        " where ba. bed_id = 1 AND a.is_active = true) ";
        Session session = this.sessionFactory.openSession();
        List<Patient> patient = session.createQuery(sb).list();
        session.close();
        return ResponseEntity.ok().body(patient);
    }

    @Override
    public ResponseEntity<?> retrievePageablePatients(String group, Pageable pageable) {
        try {
            Page<Patient> page = patientRepository.findAll(pageable);
            return new ResponseEntity<>(new PageableResponse().instance(page, Patient.class), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> retrievePatientAssignedToThisStaff(Long staffId) {
        return ResponseEntity.ok().body(patientRepository.findById(staffId));
    }

    @Override
    public ResponseEntity<?> retrieveStatisticalData() {
        String queryString = " SELECT " +
                " COUNT(*) as total,  " +
                " COUNT(IF(isAdmitted,1, NULL))'ipd', " +
                " COUNT(IF(isAdmitted = 0,1, NULL))'opd', " +
                " COUNT(IF(is_active,1,NULL))'assigned', " +
                " COUNT(IF(is_active = 0,1,NULL)) AS unassigned, " +
                " COUNT(IF(gender = 'male' ,1, NULL))'male', " +
                " COUNT(IF(gender = 'female' ,1, NULL))'female', " +
                " SUM(case when gender like 'male' then 1 else 0 end) 'males', " +
                " COUNT(IF(gender = 'unspecified' ,1, NULL))'unspecified' " +
                " FROM m_patients; ";
        Session session = this.sessionFactory.openSession();
        List<PatientStatistics> patientStatisticsData =  jdbcTemplate.query(queryString, new PatientStatistics.StatisticsDataRowMapper());
        session.close();
        return ResponseEntity.ok().body(patientStatisticsData);
    }
}
