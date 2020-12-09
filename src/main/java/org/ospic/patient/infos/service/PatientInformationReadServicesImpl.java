package org.ospic.patient.infos.service;

import org.ospic.patient.contacts.repository.ContactsInformationRepository;
import org.ospic.patient.contacts.services.ContactsInformationService;
import org.ospic.fileuploads.service.FilesStorageService;
import org.ospic.patient.infos.data.PatientData;
import org.ospic.patient.infos.data.PatientTrendDatas;
import org.ospic.patient.infos.data.PatientTrendsDataRowMapper;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.infos.repository.PatientInformationRepository;
import org.ospic.authentication.payload.response.MessageResponse;
import org.ospic.physicians.domains.Physician;
import org.ospic.physicians.service.PhysicianInformationService;
import org.ospic.util.constants.DatabaseConstants;
import org.ospic.util.exceptions.ResourceNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PatientInformationRepository patientInformationRepository;
    @Autowired
    ContactsInformationRepository contactsInformationRepository;
    @Autowired
    ContactsInformationService contactsInformationService;
    @Autowired
    SessionFactory sessionFactory;
    FilesStorageService filesStorageService;

    PhysicianInformationService physicianInformationService;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public PatientInformationReadServicesImpl(
            DataSource dataSource,
            PhysicianInformationService physicianInformationService,
            FilesStorageService filesStorageService) {
        this.physicianInformationService = physicianInformationService;
        this.filesStorageService = filesStorageService;

        jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public ResponseEntity<List<Patient>> retrieveAllPatients() {
        Session session = this.sessionFactory.openSession();
        List<Patient> patientList = session.createQuery(String.format("from %s", DatabaseConstants.PATIENT_INFO_TABLE)).list();
        session.close();
        return ResponseEntity.ok().body(patientList);
    }

    @Override
    public ResponseEntity<List<Patient>> retrieveAllAssignedPatients() {
        Session session = this.sessionFactory.openSession();
        List<Patient> patients = session.createQuery("from m_patients WHERE physician_id IS NOT NULL").list();
        session.close();
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @Override
    public ResponseEntity<List<Patient>> retrieveAllUnAssignedPatients() {
        Session session = this.sessionFactory.openSession();
        List<Patient> patients = session.createQuery("from m_patients WHERE physician_id IS NULL").list();
        session.close();
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @Override
    public ResponseEntity retrievePatientCreationDataTemplate() {
        List<Physician> physiciansOptions = physicianInformationService.retrieveAllPhysicians();
        for (int i = 0; i < physiciansOptions.size(); i++) {
            physiciansOptions.get(i).getPatients().clear();
        }
        return ResponseEntity.ok().body(PatientData.patientCreationTemplate(physiciansOptions));
    }

    @Override
    public ResponseEntity<List<PatientTrendDatas>> retrieveAllPatientTrendData() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT date(created_date) as date, count(*) as total,");
        sb.append("count(case when gender = '1' then 1 else null end) as male, ");
        sb.append("count(case when gender = '2' then 1 else null end) as female, ");
        sb.append("count(case when gender = '0' then 1 else null end) as other FROM m_patients group by date(created_date)");
        String queryString = sb.toString();
        Session session = this.sessionFactory.openSession();
        List<PatientTrendDatas> patientstrends = jdbcTemplate.query(queryString, new PatientTrendsDataRowMapper());
        session.close();

        return ResponseEntity.ok().body(patientstrends);
    }


    @Override
    public ResponseEntity retrievePatientById(Long id) throws ResourceNotFoundException {
        if (patientInformationRepository.existsById(id)) {
            Patient patient = patientInformationRepository.findById(id).get();
            if (patient.getPhysician() != null) {
                patient.getPhysician().getPatients().clear();
            }
            return ResponseEntity.ok().body(patient);
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new MessageResponse(String.format("Patient with given Id:  %s is not found", id)));
    }

    @Override
    public ResponseEntity<List<Patient>> retrievePatientAdmittedInThisBed(Long bedId) {
        String sb =
                " select p.id from  m_patients p where id =  " +
                " (select  pa.patients_id from " +
                " m_admissions a " +
                " inner join m_admissions_m_beds ba ON a.id = ba.admissions_id " +
                " inner join m_admissions_m_patients pa ON a.id = pa.admissions_id " +
                " where ba.beds_id = 1 AND a.is_active = true) ";
        Session session = this.sessionFactory.openSession();
        List<Patient> patient = session.createQuery(sb).list();
        session.close();
        return ResponseEntity.ok().body(patient);
    }

}
