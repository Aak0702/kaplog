package com.example.kapDuty.configuration;

import com.example.kapDuty.dto.ErrorLogDto;
import com.example.kapDuty.kafka.ErrorLogDeserializer;
import com.example.kapDuty.kafka.ErrorLogSerializer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@EnableKafka
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

    @Value("${spring.kafka.bootstrap-servers:}")
    String kafkaserverIp;

    @Value("${spring.kafka.sasl-mechanism:}")
    String saslMechanism;

    @Value("${spring.kafka.jaas-config:}")
    String jaasConfig;

    @Value("${spring.kafka.security.protocol:}")
    String securityProtocol;

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

    @Bean(name = "errorLogKafkaTemplate")
    public KafkaTemplate<String, ErrorLogDto> errorLogKafkaTemplate() {
        return new KafkaTemplate<>(errorLogProducerFactory());
    }

    //Producer Factory
    public ProducerFactory<String, ErrorLogDto> errorLogProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaserverIp);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ErrorLogSerializer.class);
        config.put("security.protocol", securityProtocol);
        config.put("sasl.mechanism", saslMechanism);
        config.put("sasl.jaas.config", jaasConfig);
        return new DefaultKafkaProducerFactory<>(config);
    }

    //Consumer Factory
    @Bean
    public ConsumerFactory<String, ErrorLogDto> errorLogConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaserverIp);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "error-log-group");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorLogDeserializer.class);
        config.put("security.protocol", securityProtocol);
        config.put("sasl.mechanism", saslMechanism);
        config.put("sasl.jaas.config", jaasConfig);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ErrorLogDto> errorLogKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ErrorLogDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(errorLogConsumerFactory());
        return factory;
    }
}