package org.ospic.platform.configurations;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * This file was created by eli on 13/10/2020 for org.ospic.platform.configurations
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
@Configuration
@EnableTransactionManagement
public class HibernateConfigurations {
	
	@Value("${spring.datasource.driver}")
	private String springDataSourceDriverClassName;
	
	@Value("${spring.datasource.url}")
	private String springDataSourceUrl;

	@Value("${spring.datasource.username}")
	private String springDataSourceUsername;

	@Value("${spring.datasource.password}")
	private String springDataSourcePassword;
	
    @Bean(name = "entityManagerFactory")
    public LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan("org.ospic.platform");
        sessionFactoryBean.setHibernateProperties(hibernateProperties());

        return sessionFactoryBean;
    }
    

    @Bean public DataSource dataSource() {
      BasicDataSource basicDataSource = new BasicDataSource();
      basicDataSource.setDriverClassName(springDataSourceDriverClassName);
      basicDataSource.setUrl(springDataSourceUrl);
      basicDataSource.setUsername(springDataSourceUsername);
      basicDataSource.setPassword(springDataSourcePassword);
      return basicDataSource;
    }


    @Bean(name = "transactionManager")
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactoryBean().getObject());
        return hibernateTransactionManager;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        hibernateProperties.setProperty("show-sql", "true");
        return hibernateProperties;
    }
}
