package com.tackpad.configs;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Wojtek on 2016-11-20.
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String DB_DRIVER;

    @Value("${spring.datasource.password}")
    private String DB_PASSWORD;

    @Value("${spring.datasource.url}")
    private String DB_URL;

    @Value("${spring.datasource.username}")
    private String DB_USERNAME;

    @Value("${hibernate.dialect}")
    private String HIBERNATE_DIALECT;

    @Value("${hibernate.show_sql}")
    private String HIBERNATE_SHOW_SQL;

    @Value("${hibernate.connection.characterEncoding}")
    private String HIBERNATE_CONNECTION_CHARACTERENCODING;

    @Value("${hibernate.connection.charSet}")
    private String HIBERNATE_CONNECTION_CHARSET;

    @Value("${hibernate.hbm2ddl.import_files_sql_extractor}")
    private String HIBERNATE_HBM2DDL_IMPORT_FILES_SQL_EXTRACTOR;

    @Value("${hibernate.temp.use_jdbc_metadata_defaults}")
    private String HIBERNATE_TEMP_USE_JDBC_METADATA_DEFAULTS;

    @Value("${hibernate.hbm2ddl.auto}")
    private String HIBERNATE_HBM2DDL_AUTO;

    @Value("${entitymanager.packagesToScan}")
    private String ENTITYMANAGER_PACKAGES_TO_SCAN;

    @Primary
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", HIBERNATE_DIALECT);
        hibernateProperties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
        hibernateProperties.put("hibernate.hbm2ddl.auto", HIBERNATE_HBM2DDL_AUTO);
        hibernateProperties.put("hibernate.connection.characterEncoding", HIBERNATE_CONNECTION_CHARACTERENCODING);
        hibernateProperties.put("hibernate.connection.charSet", HIBERNATE_CONNECTION_CHARSET);
        hibernateProperties.put("hibernate.hbm2ddl.import_files_sql_extractor", HIBERNATE_HBM2DDL_IMPORT_FILES_SQL_EXTRACTOR);
        hibernateProperties.put("hibernate.temp.use_jdbc_metadata_defaults", HIBERNATE_TEMP_USE_JDBC_METADATA_DEFAULTS);
        sessionFactoryBean.setHibernateProperties(hibernateProperties);
        return sessionFactoryBean;
    }

}