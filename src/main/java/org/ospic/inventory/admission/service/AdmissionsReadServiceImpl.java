package org.ospic.inventory.admission.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.inventory.admission.data.AdmissionResponseData;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.inventory.admission.repository.AdmissionRepository;
import org.ospic.inventory.beds.repository.BedRepository;
import org.ospic.patient.infos.data.PatientAdmissionData;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.infos.repository.PatientInformationRepository;
import org.ospic.util.constants.DatabaseConstants;
import org.ospic.util.enums.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
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

    private final JdbcTemplate jdbcTemplate;
    private final AdmissionRepository admissionRepository;
    @Autowired
    BedRepository bedRepository;
    @Autowired
    PatientInformationRepository patientRepository;
    @Autowired
    SessionFactory sessionFactory;

    public AdmissionsReadServiceImpl(
            BedRepository bedRepository,
            PatientInformationRepository patientRepository, final DataSource dataSource,
            AdmissionRepository admissionRepository) {
        this.admissionRepository = admissionRepository;
        this.bedRepository = bedRepository;
        this.patientRepository = patientRepository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }





    @Override
    public ResponseEntity<List<Admission>> retrieveAllAdmissions() {
        Session session = this.sessionFactory.openSession();
        List<Admission> admissions = session.createQuery(String.format("from %s", DatabaseConstants.TABLE_ADMISSION_INFO)).list();
        session.close();

        return ResponseEntity.ok(admissions);
    }

    @Override
    public Collection<AdmissionResponseData> retrieveAdmissionById(Long admissionId) {
        final AdmissionResponseDataRowMapper rm = new AdmissionsReadServiceImpl.AdmissionResponseDataRowMapper();
        final String sql = "select " + rm.schema() + " where a.id = ?  order by a.id DESC ";
        return this.jdbcTemplate.query(sql, rm, new Object[]{admissionId});
    }

    @Override
    public ResponseEntity<List<AdmissionResponseData>> retrieveListOfAdmissionInBedId(Long bedId) {
        final String sql = "";

        return null;
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfAdmissionInWardId(Long wardId) {
        return null;
    }

    @Override
    public Collection<AdmissionResponseData> retrieveListOfPatientAdmission(Long patientId) {
        final AdmissionResponseDataRowMapper rm = new AdmissionsReadServiceImpl.AdmissionResponseDataRowMapper();
        final String sql = "select " + rm.schema() + " where a.id in (select pa.admissions_id from m_admissions_m_patients pa where pa.patients_id = ? ) order by a.id DESC ";
        return this.jdbcTemplate.query(sql, rm, new Object[]{patientId});
    }

    @Override
    public ResponseEntity<?> retrieveAdmissionInThisBed(Long bedId) {
        final PatientAdmissionRowMapper rm = new PatientAdmissionRowMapper();
        final String sql =  "select " +rm.schema() +  " where bd.id = ? order by adb.admissions_id desc limit 0, 1";
        return ResponseEntity.ok().body(this.jdbcTemplate.query(sql,rm,new Object[]{bedId}));
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfActiveAdmissions() {
        return null;
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfInactiveAdmissions() {
        return null;
    }

    private static final class AdmissionResponseDataRowMapper implements RowMapper<AdmissionResponseData> {

        public String schema() {
            return  " a.id as id, a.is_active as isActive, a.start_date as startDate, a.end_date as endDate, ab.beds_id as bedId, ap.patients_id as patientId, " +
                    " b.ward_id as wardId, b.identifier bedIdentifier, w.name as wardName from m_admissions a  " +
                    " inner join m_admissions_m_beds ab ON ab.admissions_id = a.id " +
                    " inner join m_admissions_m_patients ap ON ap.admissions_id = a.id "+
                    " inner join m_beds b on ab.beds_id = b.id " +
                    " inner join m_wards w on b.ward_id = w.id " +
                    " ";
        }

        @Override
        public AdmissionResponseData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            final Long id = rs.getLong("id");
            final Date startDate = rs.getDate("startDate");
            final Date endDate = rs.getDate("endDate");
            final boolean isActive = rs.getBoolean("isActive");
            final Long bedId = rs.getLong("bedId");
            final Long wardId = rs.getLong("wardId");
            final Long patientId = rs.getLong("patientId");
            final String bedIdentifier = rs.getString("bedIdentifier");
            final String wardName = rs.getString("wardName");
            return  AdmissionResponseData.responseTemplate(id,startDate,endDate,isActive, wardId,bedId, wardName, bedIdentifier,patientId);
        }
    }
    private static final class PatientAdmissionRowMapper implements RowMapper<PatientAdmissionData> {

        public String schema() {
            return  "  p.id as id,p.address as address, p.age as age, p.blood_group as bloodGroup, p.blood_pressure as bloodPressure, "+
                    "  p.email_address as emailAddress, p.gender as gender, p.guardian_name as guardianName, p.height as height, "+
                    "  p.marital_status as martialStatus, p.name as name, p.thumbnail as thumbNail, p.weight as weight, p.phone as phone "+
                    " from ospic_default.m_beds bd "+
                    " inner join m_admissions_m_beds adb on bd.id =  adb.beds_id "+
                    " inner join m_admissions_m_patients ap  on adb.admissions_id = ap.admissions_id "+
                    " inner join m_patients p ON ap.patients_id = p.id "+
                    " " ;
        }

        @Override
        public PatientAdmissionData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            Patient pt = new Patient();
            final Long id = rs.getLong("id");
            final String address = rs.getString("address");
            final Integer age = rs.getInt("age");
            final String bloodGroup = rs.getString("bloodGroup");
            final String bloodPressure = rs.getString("bloodPressure");
            final String emailAddress = rs.getString("emailAddress");
            final String gender = rs.getString("gender");
            final String guardianName = rs.getString("guardianName");
            final String height = rs.getString("height");
            final String martiaStatus = rs.getString("martialStatus");
            final String name = rs.getString("name");
            final String imageUrl = rs.getString("thumbNail");
            final String weight = rs.getString("weight");
            final String phone = rs.getString("phone");
            return new PatientAdmissionData(id,name,phone,gender, height, weight, guardianName, bloodPressure, age, emailAddress,martiaStatus, imageUrl,bloodGroup );
        }
    }


}
