package nl.siegmann.zoo.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"nl.siegmann.zoo.query.repository", "org.axonframework.modelling.saga.repository.jpa" })
@EntityScan(basePackages = {"nl.siegmann.zoo.query.entity", "org.axonframework.eventsourcing.eventstore.jpa", "org.axonframework.modelling.saga.repository.jpa", "org.axonframework.eventhandling.tokenstore.jpa"})
public class DatabaseConfig {

}
