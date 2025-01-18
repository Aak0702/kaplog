package com.example.kapDuty.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"com.example.*"})
public class SpringBootConfiguration implements WebMvcConfigurer {

    // Main database
    @Value("${spring.main.datasource.driver-class-name}")
    String driverClassName;

    @Value("${spring.main.datasource.url}")
    String mainUrl;

    @Value("${spring.main.datasource.username}")
    String mainUsername;

    @Value("${spring.main.datasource.password}")
    String mainPassword;

    @Bean(name = "mainDataSource")
    @Primary
    public DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(mainUrl);
        hikariConfig.setUsername(mainUsername);
        hikariConfig.setPassword(mainPassword);

        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("system-health-monitor-pool");

        hikariConfig.setConnectionTimeout(60000);
        hikariConfig.setMinimumIdle(5);

        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "sessionFactory")
    @Primary
    public SessionFactory getSessionFactory(@Qualifier("mainDataSource") DataSource dataSource) {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.scanPackages("com.kapturecrm.*", "com.kapture.*");
        return sessionBuilder.buildSessionFactory();
    }

    @Bean(name = "mainTransactionManager")
    public HibernateTransactionManager getTransactionManager(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Primary
    public DataSourceInitializer dataSourceInitializer(@Qualifier("mainDataSource") DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        return initializer;
    }
}