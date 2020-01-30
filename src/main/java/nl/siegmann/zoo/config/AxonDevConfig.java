package nl.siegmann.zoo.config;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.monitoring.NoOpMessageMonitor;
import org.axonframework.spring.config.AxonConfiguration;
import org.axonframework.spring.jdbc.SpringDataSourceConnectionProvider;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.axonframework.springboot.autoconfig.JpaEventStoreAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Non-kubernetes development config.
 */
@Configuration
@Profile("!kubernetes")
@Slf4j
public class AxonDevConfig {

	@Bean
	CommandBus commandBus(@Autowired TransactionManager axonTransactionManager) {
		return new SimpleCommandBus.Builder()
				.transactionManager(axonTransactionManager)
				.messageMonitor(NoOpMessageMonitor.INSTANCE)
				.build();
	}

	@Bean
	public EmbeddedEventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
		return EmbeddedEventStore.builder()
				.storageEngine(storageEngine)
				.messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore"))
				.build();
	}

	@Bean
	public EventStorageEngine eventStorageEngine(DataSource dataSource, EntityManagerProvider entityManagerProvider, TransactionManager transactionManager) throws SQLException {
		return JpaEventStorageEngine.builder().dataSource(dataSource).entityManagerProvider(entityManagerProvider).transactionManager(transactionManager).build();
	}

	@Bean
	TransactionManager axonTransactionManager(@Autowired PlatformTransactionManager platformTransactionManager,
	                                          @Autowired TransactionDefinition transactionDefinition) {
		return new SpringTransactionManager(platformTransactionManager, transactionDefinition);
	}
}
