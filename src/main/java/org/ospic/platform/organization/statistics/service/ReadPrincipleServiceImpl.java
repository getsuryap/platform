package org.ospic.platform.organization.statistics.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.platform.organization.statistics.data.*;
import org.ospic.platform.patient.infos.data.PatientTrendsDataRowMapper;
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
public class ReadPrincipleServiceImpl implements ReadPrincipleService {
    JdbcTemplate jdbcTemplate;
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    public ReadPrincipleServiceImpl(DataSource dataSource) {
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
        String queryString = "SELECT date(created_date) as date, count(*) as total," +
                "count(case when gender = 'male' then 1 else null end) as male, " +
                "count(case when gender = 'female' then 1 else null end) as female, " +
                "count(case when gender = 'unspecified' then 1 else null end) as other FROM m_patients group by date(created_date)";
        Session session = this.sessionFactory.openSession();
        List<PatientTrendStatistics> patientTrendStatisticst = jdbcTemplate.query(queryString, new PatientTrendsDataRowMapper());
        session.close();
        return patientTrendStatisticst;
    }


    private PatientStatistics retrieveStatisticalData() {
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
        List<PatientStatistics> patientStatisticsData = jdbcTemplate.query(queryString, new PatientStatistics.StatisticsDataRowMapper());
        session.close();

        return patientStatisticsData.get(0);
    }

    private ServiceStatistics retrieveServiceStatistics() {
        String queryString = "  SELECT COUNT(*) as total, " +
                "  COUNT(IF(is_active,1,NULL))'totalActive', " +
                "  COUNT(IF(is_active = 0,1,NULL))'totalInActive', " +
                "  COUNT(IF(staff_id,1,NULL))'totalAssigned', " +
                "  COUNT(IF(staff_id IS NULL,1,NULL))'totalUnAssigned', " +
                "  COUNT(IF(is_admitted,1,NULL))'totalIpd', " +
                "  COUNT(IF(is_admitted = 0,1,NULL))'totalOpd'  " +
                "  FROM m_service; ";
        Session session = this.sessionFactory.openSession();
        List<ServiceStatistics> serviceStatistics = jdbcTemplate.query(queryString, new ServiceStatistics.ServiceStatisticsRowMapper());
        session.close();
        return serviceStatistics.get(0);
    }

    private WardStatistics retrieveWardStatistics(){
        String queryString =
                        "  SELECT COUNT(*) AS total, " +
                        "  COUNT(IF(is_occupied, 1,NULL))'occupied', " +
                        "  COUNT(IF(is_occupied = 0, 1,NULL))'unoccupied' " +
                        "  FROM m_beds;";
        Session session = this.sessionFactory.openSession();
        List<WardStatistics> wardStatistics = jdbcTemplate.query(queryString, new WardStatistics.WardStatisticsRowMapper());
        session.close();
        return wardStatistics.get(0);
    }

    private Collection<ServiceTrendStatistics> retrieveServiceTrendStatistics(){
        String queryString = " "+
                "  SELECT date(fromdate) as date, count(*) as total," +
                "  COUNT(IF(is_active, 1, NULL))'active', " +
                "  COUNT(IF(is_active = 0, 1, NULL))'inactive'," +
                "  COUNT(IF(is_admitted, 1, NULL))'admitted'," +
                "  COUNT(IF(is_admitted = 0, 1, NULL))'unadmitted'" +
                "  FROM m_service group by date(fromdate)";
        Session session = this.sessionFactory.openSession();
        List<ServiceTrendStatistics> serviceTrendStatistics = jdbcTemplate.query(queryString, new ServiceTrendStatistics.ServiceTrendStatisticsRowMapper());
        session.close();

        return serviceTrendStatistics;
    }
    private UserStatistics retrieveUserStatistics(){
        String queryString = "SELECT COUNT(*) as users, COUNT(IF(isStaff,1,NULL))'staff' FROM users; ";
        Session session = this.sessionFactory.openSession();
        List<UserStatistics> serviceTrendStatistics = jdbcTemplate.query(queryString, new UserStatistics.UserStatisticsRowMapper());
        session.close();
        return serviceTrendStatistics.get(0);
    }
}
