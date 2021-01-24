package org.ospic.platform.inventory.admission.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.platform.inventory.admission.data.AdmissionResponseData;
import org.ospic.platform.inventory.admission.domains.Admission;
import org.ospic.platform.inventory.admission.repository.AdmissionRepository;
import org.ospic.platform.inventory.beds.repository.BedRepository;
import org.ospic.platform.patient.infos.data.PatientAdmissionData;
import org.ospic.platform.patient.infos.domain.Patient;
import org.ospic.platform.patient.infos.repository.PatientRepository;
import org.ospic.platform.patient.resource.repository.ServiceResourceJpaRepository;
import org.ospic.platform.util.constants.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * This file was created by eli on 09/11/2020 for org.ospic.platform.inventory.admission.service
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
    ServiceResourceJpaRepository serviceRJRepositoryy;
    @Autowired
    SessionFactory sessionFactory;

    public AdmissionsReadServiceImpl(
            BedRepository bedRepository,
            ServiceResourceJpaRepository serviceRJRepositoryy, final DataSource dataSource,
            AdmissionRepository admissionRepository) {
        this.admissionRepository = admissionRepository;
        this.bedRepository = bedRepository;
        this.serviceRJRepositoryy = serviceRJRepositoryy;
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
        final String sql = "select distinct " + rm.schema() + " where a.id = ?  order by a.id DESC ";
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
    public Collection<?> retrieveListOfServiceAdmission(Long serviceId) {
        final AdmissionResponseDataRowMapper rm = new AdmissionsReadServiceImpl.AdmissionResponseDataRowMapper();
        final String sql = "select distinct " + rm.schema() + "  where a.sid = ? order by a.id DESC; ";
        return this.jdbcTemplate.query(sql, rm, new Object[]{serviceId});
    }

    @Override
    public ResponseEntity<?> retrieveAdmissionInThisBed(Long bedId) {
        final PatientAdmissionRowMapper rm = new PatientAdmissionRowMapper();
        final String sql = "select " + rm.schema() + " where bd.id = ? order by adb.admission_id desc limit 0, 1";
        return ResponseEntity.ok().body(this.jdbcTemplate.query(sql, rm, new Object[]{bedId}));
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
            return " a.id as id, a.is_active as isActive, a.start_date as startDate, " +
                    " a.end_date as endDate, ab. bed_id as bedId, sa.id as serviceId, " +
                    " b.ward_id as wardId, b.identifier bedIdentifier, w.name as wardName from m_admissions a " +
                    " inner join  admission_bed  ab ON ab.admission_id = a.id " +
                    " inner join m_service sa ON sa.id = a.sid " +
                    " inner join m_beds b on ab. bed_id = b.id " +
                    " inner join m_wards w on b.ward_id = w.id  " +
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
            final Long serviceId = rs.getLong("serviceId");
            final String bedIdentifier = rs.getString("bedIdentifier");
            final String wardName = rs.getString("wardName");
            final LocalDate fromDateLocal = LocalDate.parse(new SimpleDateFormat("yyy-MM-dd").format(startDate));
            final LocalDate toDateLocal = LocalDate.parse(new SimpleDateFormat("yyy-MM-dd").format(endDate));

            return AdmissionResponseData.responseTemplate(id, fromDateLocal, toDateLocal, isActive, wardId, bedId, wardName, bedIdentifier, serviceId);
        }
    }

    private static final class PatientAdmissionRowMapper implements RowMapper<PatientAdmissionData> {

        public String schema() {
            return "  p.id as id, p.address as address, p.age as age, p.blood_group as bloodGroup, p.blood_pressure as bloodPressure, " +
                    " p.email_address as emailAddress, p.gender as gender, p.guardian_name as guardianName, p.height as height, " +
                    " p.marital_status as martialStatus, p.name as name, p.thumbnail as thumbNail, p.weight as weight, p.phone as phone " +
                    " from m_beds bd " +
                    " inner join  admission_bed  adb on bd.id =  adb.bed_id " +
                    " inner join m_admissions a on adb.admission_id = a.id " +
                    " inner join m_service s on a.sid = s.id " +
                    " inner join m_patients p  on s.patient_id = p.id  ";
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
            return new PatientAdmissionData(id, name, phone, gender, height, weight, guardianName, bloodPressure, age, emailAddress, martiaStatus, imageUrl, bloodGroup, address);
        }
    }


}
