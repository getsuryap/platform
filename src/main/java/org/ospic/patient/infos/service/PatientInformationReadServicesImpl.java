package org.ospic.patient.infos.service;

import org.ospic.domain.PageableResponse;
import org.ospic.patient.contacts.repository.ContactsInformationRepository;
import org.ospic.patient.contacts.services.ContactsInformationService;
import org.ospic.fileuploads.service.FilesStorageService;
import org.ospic.patient.infos.data.PatientData;
import org.ospic.patient.infos.data.PatientTrendDatas;
import org.ospic.patient.infos.data.PatientTrendsDataRowMapper;
import org.ospic.patient.infos.data.StatisticsData;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.infos.repository.PatientRepository;
import org.ospic.security.authentication.users.payload.response.MessageResponse;
import org.ospic.organization.staffs.domains.Staff;
import org.ospic.organization.staffs.service.StaffsReadPrinciplesService;
import org.ospic.util.constants.DatabaseConstants;
import org.ospic.util.exceptions.ResourceNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    public PatientInformationReadServicesImpl(
            DataSource dataSource,
            StaffsReadPrinciplesService staffsReadPrinciplesService,
            FilesStorageService filesStorageService) {
        this.staffsReadPrinciplesService = staffsReadPrinciplesService;
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
        List<Patient> patients = session.createQuery("from m_patients WHERE staff_id IS NOT NULL").list();
        session.close();
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @Override
    public ResponseEntity<List<Patient>> retrieveAllUnAssignedPatients() {
        Session session = this.sessionFactory.openSession();
        List<Patient> patients = session.createQuery("from m_patients WHERE staff_id IS NULL").list();
        session.close();
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @Override
    public ResponseEntity retrievePatientCreationDataTemplate() {
        List<Staff> staffs = staffsReadPrinciplesService.retrieveAllStaffs();
        for (int i = 0; i < staffs.size(); i++) {
            //staffs.get(i).getPatients().clear();
        }
        return ResponseEntity.ok().body(PatientData.patientCreationTemplate(staffs));
    }

    @Override
    public ResponseEntity<List<PatientTrendDatas>> retrieveAllPatientTrendData() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT date(created_date) as date, count(*) as total,");
        sb.append("count(case when gender = 'male' then 1 else null end) as male, ");
        sb.append("count(case when gender = 'female' then 1 else null end) as female, ");
        sb.append("count(case when gender = 'unspecified' then 1 else null end) as other FROM m_patients group by date(created_date)");
        String queryString = sb.toString();
        Session session = this.sessionFactory.openSession();
        List<PatientTrendDatas> patientstrends = jdbcTemplate.query(queryString, new PatientTrendsDataRowMapper());
        session.close();

        return ResponseEntity.ok().body(patientstrends);
    }


    @Override
    public ResponseEntity<?> retrievePatientById(Long id) throws ResourceNotFoundException {
        if (patientRepository.existsById(id)) {
            Patient patient = patientRepository.findById(id).get();
            if (
                    patient.getServiceResources() != null) {
                //patient.getStaff().getPatients().clear();
               // patient.getStaff().setUser(null);
            }
            return ResponseEntity.ok().body(patient);
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new MessageResponse(String.format("Patient with given Id:  %s is not found", id)));
    }

    @Override
    public ResponseEntity<List<Patient>> retrievePatientAdmittedInThisBed(Long bedId) {
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
            /**List<Patient> patients = new ArrayList<>();
            patients = page.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("currentPage", page.getNumber());
            if (!page.isLast()) { response.put("next", page.getNumber() + 1); }
            if (!page.isFirst()){ response.put("previous", page.getNumber() -1); }
            response.put("totalItems", page.getTotalElements());
            response.put("totalPages", page.getTotalPages());
            response.put("data", patients);
            **/
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
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" COUNT(*) as total,  ");
        sb.append(" COUNT(IF(isAdmitted,1, NULL))'ipd', ");
        sb.append(" COUNT(IF(isAdmitted = 0,1, NULL))'opd', ");
        sb.append(" COUNT(IF(is_active,1,NULL))'assigned', ");
        sb.append(" COUNT(IF(is_active = 0,1,NULL)) AS unassigned, ");
        sb.append(" COUNT(IF(gender = 1 ,1, NULL))'male', ");
        sb.append(" COUNT(IF(gender = 2 ,1, NULL))'female', ");
        sb.append(" COUNT(IF(gender = 0 ,1, NULL))'unspecified' ");
        sb.append(" FROM m_patients; ");
        String queryString = sb.toString();
        Session session = this.sessionFactory.openSession();
        List<StatisticsData> statisticsData =  jdbcTemplate.query(queryString, new StatisticsData.StatisticsDataRowMapper());
        session.close();
        return ResponseEntity.ok().body(statisticsData);
    }
}
