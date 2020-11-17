package com.journi.challenge.spring;

import com.journi.challenge.repositories.ProductRepository;
import com.journi.challenge.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(basePackageClasses = ProductRepository.class)
@ComponentScan(basePackageClasses = ProductsService.class)
@EntityScan("com.journi.challenge")
@EnableTransactionManagement
public class ApplicationConfiguration {

    @Autowired
    private Environment env;


//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource());
//        em.setPackagesToScan(new String[]{Product.class.getPackage().getName()});
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//
//        //jpa properties to auto create db
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.ddl-auto",env.getProperty("spring.jpa.hibernate.ddl-auto"));
//        em.setJpaProperties(properties);
//        return em;
//    }

    @Bean
    JpaTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
//        dataSource.setUrl(env.getProperty("spring.datasource.url"));
//        dataSource.setUsername(env.getProperty("spring.datasource.username"));
//        dataSource.setPassword(env.getProperty("spring.datasource.password"));
//        return dataSource;
//    }
}
