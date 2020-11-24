package org.ospic.inventory.admission.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.inventory.admission.data.AdmissionResponseData;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.inventory.admission.repository.AdmissionRepository;
import org.ospic.inventory.beds.repository.BedRepository;
import org.ospic.patient.infos.repository.PatientInformationRepository;
import org.ospic.util.constants.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * This file was created by eli on 09/11/2020 for org.ospic.inventory.admission.service
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
public class AdmissionsReadServiceImpl implements AdmissionsReadService {

    @Autowired
    AdmissionRepository admissionRepository;
    @Autowired
    BedRepository bedRepository;
    @Autowired
    PatientInformationRepository patientRepository;
    @Autowired
    SessionFactory sessionFactory;

    public AdmissionsReadServiceImpl(
            BedRepository bedRepository,
            PatientInformationRepository patientRepository,
            AdmissionRepository admissionRepository) {
        this.admissionRepository = admissionRepository;
        this.bedRepository = bedRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveAllAdmissions() {
        Session session = this.sessionFactory.openSession();
        List<Admission> admissions = session.createQuery(String.format("from %s", DatabaseConstants.TABLE_ADMISSION_INFO)).list();
        session.close();

        return ResponseEntity.ok(admissions);
    }

    @Override
    public ResponseEntity<List<AdmissionResponseData>> retrieveListOfAdmissionInBedId(Long bedId) {
        Session session = this.sessionFactory.openSession();
        String sb = "from m_admissions a where id IN (select pa.admissions_id from m_admissions_m_patients pa where pa.patients_id =1) order by a.id DESC";
        List<AdmissionResponseData> admissions = session.createQuery(sb).list();
        session.close();
        return ResponseEntity.ok().body(admissions);
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfAdmissionInWardId(Long wardId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfPatientAdmission(Long patientId) {
        Session session = this.sessionFactory.openSession();
        EntityManager entityManager = sessionFactory.createEntityManager();
        String sb = "select  a.start_date as fromDateTime, a.is_active as isActive, a.end_date as toDateTime from m_admissions a inner join m_admissions_m_patients pa on pa.admissions_id = a.id where pa.patients_id = 1";
        String sba = "select a from m_admissions a join fetch m_admissions_m_patients pa on pa.admissions_id = a.id where pa.patients_id = 1";
        List<Admission> admissions = entityManager.createQuery(sba, Admission.class).getResultList();
        entityManager.close();
        session.close();
        return ResponseEntity.ok().body(admissions);
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfActiveAdmissions() {
        return null;
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfInactiveAdmissions() {
        return null;
    }
}
