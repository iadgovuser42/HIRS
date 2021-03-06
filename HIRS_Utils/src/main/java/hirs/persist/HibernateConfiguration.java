package hirs.persist;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * A spring configuration that houses the properties associated with the hibernate connection
 * to the database. Beans for classes doing actual queries on DB tables should go in to a separate
 * configuration (which would likely import this configuration).
 */
@Configuration
@EnableTransactionManagement
@PropertySources(value = {
        @PropertySource(value = "file:/etc/hirs/persistence.properties", ignoreResourceNotFound =
                true),
        @PropertySource(value = "classpath:persistence.properties"),
        @PropertySource(value = "classpath:persistence-extended.properties",
                ignoreResourceNotFound = true)
})
public class HibernateConfiguration {

    @Value("${persistence.db.url}")
    private String url;

    @Value("${persistence.db.username}")
    private String username;

    @Value("${persistence.db.password}")
    private String password;

    @Value("${persistence.db.driverClass}")
    private String driverClass;

    @Value("${persistence.db.maximumPoolSize}")
    private String maximumPoolSize;

    @Value("${persistence.db.connectionTimeout}")
    private String connectionTimeout;

    @Value("${persistence.db.leakDetectionThreshold}")
    private String leakDetectionThreshold;

    @Value("${persistence.hibernate.dialect}")
    private String dialect;

    @Value("${persistence.hibernate.ddl}")
    private String ddl;

    @Value("${persistence.hibernate.contextClass}")
    private String contextClass;

    @Value("${persistence.hibernate.provider}")
    private String provider;

    @Value("${persistence.db.maxTransactionRetryAttempts}")
    private int maxTransactionRetryAttempts;

    @Value("${persistence.db.retryWaitTimeMilliseconds}")
    private long retryWaitTimeMilliseconds;

    /**
     * @return bean to resolve injected Value.
     * property expressions for beans.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Configures the data source to be used by the hibernate session factory.
     *
     * @return configured data source
     */
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClass);
        dataSource.setMaximumPoolSize(Integer.parseInt(maximumPoolSize));
        dataSource.setConnectionTimeout(Long.parseLong(connectionTimeout));
        dataSource.setLeakDetectionThreshold(Long.parseLong(leakDetectionThreshold));
        return dataSource;
    }

    /**
     * Generates properties using configuration file that will be used to configure the session
     * factory.
     *
     * @return properties for hibernate session factory
     */
    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", ddl);
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.current_session_context_class", "thread");
        return properties;
    }

    /**
     * Configures a session factory bean that in turn configures the hibernate session factory.
     * Enables auto scanning of annotations such that entities do not need to be registered in a
     * hibernate configuration file.
     *
     * @return session factory
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setPackagesToScan("hirs");
        return sessionFactory;
    }

    /**
     * Configure a transaction manager for the hibernate session factory.
     *
     * @return transaction manager
     */
    @Bean
    public HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory().getObject());
    }

    /**
     * Bean holding the maximum retry attempts for a DB transaction.
     * @return the maximum retry count
     */
    @Bean(name = "maxTransactionRetryAttempts")
    public int maxTransactionRetryAttempts() {
        return maxTransactionRetryAttempts;
    }

    /**
     * Bean holding the time to wait until retrying a failed transaction.
     * @return the wait time, in milliseconds
     */
    @Bean(name = "retryWaitTimeMilliseconds")
    public long retryWaitTimeMilliseconds() {
        return retryWaitTimeMilliseconds;
    }
}
