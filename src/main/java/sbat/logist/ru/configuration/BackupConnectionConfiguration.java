package sbat.logist.ru.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Data
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "backupEntityManagerFactory",
        transactionManagerRef = "backupTransactionManager",
        basePackages = {"sbat.logist.ru.backup"})
@ConfigurationProperties(prefix = "spring.secondDatasource")
public class BackupConnectionConfiguration {
    private String username;
    private String url;
    private String password;
    private String driverClassName;

    @Bean(name = "backupDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean(name = "backupEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean barEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("backupDataSource") DataSource barDataSource) {
        return builder
                .dataSource(barDataSource)
                .packages("sbat.logist.ru.backup")
                .persistenceUnit("backup")
                .build();
    }

    @Bean(name = "backupTransactionManager")
    public PlatformTransactionManager barTransactionManager(
            @Qualifier("backupEntityManagerFactory") EntityManagerFactory barEntityManagerFactory
    ) {
        return new JpaTransactionManager(barEntityManagerFactory);
    }
}
