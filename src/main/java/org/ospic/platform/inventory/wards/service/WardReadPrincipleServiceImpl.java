package org.ospic.platform.inventory.wards.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.inventory.beds.domains.Bed;
import org.ospic.platform.inventory.beds.repository.BedRepository;
import org.ospic.platform.inventory.wards.data.WardResponseData;
import org.ospic.platform.inventory.wards.data.WardResponseDataRowMapper;
import org.ospic.platform.inventory.wards.domain.Ward;
import org.ospic.platform.inventory.wards.repository.WardRepository;
import org.ospic.platform.util.constants.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

/**
 * This file was created by eli on 07/11/2020 for org.ospic.platform.inventory.wards.service
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
public class WardReadPrincipleServiceImpl implements WardReadPrincipleService {
    @Autowired
    WardRepository wardRepository;
    @Autowired
    BedRepository bedRepository;
    @Autowired
    SessionFactory sessionFactory;
    JdbcTemplate jdbcTemplate;

    public WardReadPrincipleServiceImpl(DataSource dataSource, WardRepository wardRepository, BedRepository bedRepository) {
        this.wardRepository = wardRepository;
        this.bedRepository = bedRepository;

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Ward> retrieveListOfWards() {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Ward> wards = entityManager.createQuery("from "+ DatabaseConstants.WARDS_TABLE, Ward.class).getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return wards;
    }

    @Override
    public List<WardResponseData> retrieveAllWardsWithBedsCounts() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT w.*, (SELECT COUNT(*) FROM m_beds b WHERE b.ward_id = w.id) as counts FROM m_wards w");
        String queryString = sb.toString();
        Session session = this.sessionFactory.openSession();
        List<WardResponseData> wardResponseData = jdbcTemplate.query(queryString, new WardResponseDataRowMapper());
        session.close();
        return wardResponseData;
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        CustomReponseMessage cm = new CustomReponseMessage();
        HttpHeaders httpHeaders = new HttpHeaders();
        Optional<Ward> wardOptional =  wardRepository.findById(id);
        List<Bed> beds = bedRepository.findByWardId(id);
        if (!wardOptional.isPresent()){
            cm.setHttpStatus(HttpStatus.NOT_FOUND.value());
            cm.setMessage("Ward with ID "+ id + " is not found!");
            return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.CONFLICT);
        }
        Ward ward = wardOptional.get();
        ward.setBeds(null);
        ward.setBeds(beds);

        return ResponseEntity.ok(ward);
    }
}
