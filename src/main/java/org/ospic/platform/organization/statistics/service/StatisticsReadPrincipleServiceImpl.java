package org.ospic.platform.organization.statistics.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.platform.organization.statistics.data.*;
import org.ospic.platform.patient.details.data.PatientTrendsDataRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

/**
 * This file was created by eli on 08/01/2021 for org.ospic.platform.organization.statistics.service
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
public class StatisticsReadPrincipleServiceImpl implements StatisticsReadPrincipleService {
    JdbcTemplate jdbcTemplate;
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    public StatisticsReadPrincipleServiceImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public ResponseEntity<?> retrieveStatistic() {
        StatisticResponseData rs = new StatisticResponseData();
        PatientStatistics patientStatistics = this.retrieveStatisticalData();
        Collection<PatientTrendStatistics> patientTrendStatistics = this.retrievePatientTrend();
        ServiceStatistics serviceStatistics = this.retrieveServiceStatistics();
        WardStatistics wardStatistics = this.retrieveWardStatistics();
        Collection<ServiceTrendStatistics> sts = this.retrieveServiceTrendStatistics();
        UserStatistics us = this.retrieveUserStatistics();
        rs.setPatientTrends(patientTrendStatistics);
        rs.setPatientStatistics(patientStatistics);
        rs.setServiceStatistics(serviceStatistics);
        rs.setWardStatistics(wardStatistics);
        rs.setServiceTrends(sts);
        rs.setUserStatistics(us);

        return ResponseEntity.ok().body(rs);
    }


    private Collection<PatientTrendStatistics> retrievePatientTrend() {
        PatientTrendsDataRowMapper rm = new PatientTrendsDataRowMapper();
        Session session = this.sessionFactory.openSession();
        List<PatientTrendStatistics> patientTrendStatistics = jdbcTemplate.query(rm.schema(),rm );
        session.close();
        return patientTrendStatistics;
    }


    private PatientStatistics retrieveStatisticalData() {
        PatientStatistics.StatisticsDataRowMapper rm = new PatientStatistics.StatisticsDataRowMapper();
        Session session = this.sessionFactory.openSession();
        List<PatientStatistics> patientStatisticsData = jdbcTemplate.query(rm.schema(), rm );
        session.close();
        return patientStatisticsData.get(0);
    }

    private ServiceStatistics retrieveServiceStatistics() {
        ServiceStatistics.ServiceStatisticsRowMapper rm = new ServiceStatistics.ServiceStatisticsRowMapper();
        Session session = this.sessionFactory.openSession();
        List<ServiceStatistics> serviceStatistics = jdbcTemplate.query(rm.schema(), rm);
        session.close();
        return serviceStatistics.get(0);
    }

    private WardStatistics retrieveWardStatistics(){
        WardStatistics.WardStatisticsRowMapper rm = new WardStatistics.WardStatisticsRowMapper();
        Session session = this.sessionFactory.openSession();
        List<WardStatistics> wardStatistics = jdbcTemplate.query(rm.schema(), rm);
        session.close();
        return wardStatistics.get(0);
    }

    private Collection<ServiceTrendStatistics> retrieveServiceTrendStatistics(){
        ServiceTrendStatistics.ServiceTrendStatisticsRowMapper rm = new  ServiceTrendStatistics.ServiceTrendStatisticsRowMapper();
        Session session = this.sessionFactory.openSession();
        List<ServiceTrendStatistics> serviceTrendStatistics = jdbcTemplate.query(rm.schema(), rm);
        session.close();

        return serviceTrendStatistics;
    }
    private UserStatistics retrieveUserStatistics(){
        UserStatistics.UserStatisticsRowMapper rm = new UserStatistics.UserStatisticsRowMapper();
        Session session = this.sessionFactory.openSession();
        List<UserStatistics> serviceTrendStatistics = jdbcTemplate.query(rm.schema(), rm);
        session.close();
        return serviceTrendStatistics.get(0);
    }
}
