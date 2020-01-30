package nl.siegmann.zoo.config;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.distributed.*;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.monitoring.NoOpMessageMonitor;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;


/**
 * Axon configuration to run the application in Kubernetes, using the Distributed Command Bus.
 */
@Configuration
@Profile("kubernetes")
@EnableDiscoveryClient
@Slf4j
public class AxonKubernetesConfig {

	@Bean
	CommandBus localSegment(@Autowired TransactionManager axonTransactionManager) {
		return new SimpleCommandBus.Builder()
				.transactionManager(axonTransactionManager)
				.messageMonitor(NoOpMessageMonitor.INSTANCE)
				.build();
	}

//	@Bean
//	public CommandRouter springCloudCommandRouter(@Autowired DiscoveryClient discoveryClient, @Autowired Registration registration) {
//		return new SpringCloudCommandRouter.Builder()
//				.discoveryClient(discoveryClient)
//				.localServiceInstance(registration)
//				.routingStrategy(new AnnotationRoutingStrategy())
//				.build();
//	}

//	@Bean
//	public CommandBusConnector springHttpCommandBusConnector(@Qualifier("localSegment") CommandBus localSegment,
//	                                                         RestOperations restOperations,
//	                                                         Serializer serializer) {
//		return new SpringHttpCommandBusConnector.Builder()
//					.localCommandBus(localSegment)
//					.restOperations(restOperations)
//					.serializer(serializer)
//					.build();
//	}

	@Bean
	public RestOperations restOperations() {
		return new RestTemplate();
	}

	@Primary // to make sure this CommandBus implementation is used for autowiring
	@Bean
	public DistributedCommandBus springCloudDistributedCommandBus(CommandRouter commandRouter,
	                                                              CommandBusConnector commandBusConnector) {
		return new DistributedCommandBus.Builder()
					.commandRouter(commandRouter)
					.connector(commandBusConnector)
					.build();
	}

	@Bean
	RoutingStrategy routingStrategy() {
		return new AnnotationRoutingStrategy();
	}

	@Bean
	TransactionManager axonTransactionManager(@Autowired PlatformTransactionManager platformTransactionManager,
	                                          @Autowired TransactionDefinition transactionDefinition) {
		return new SpringTransactionManager(platformTransactionManager, transactionDefinition);
	}
}
