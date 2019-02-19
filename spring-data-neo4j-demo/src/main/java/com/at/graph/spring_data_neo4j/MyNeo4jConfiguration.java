package com.at.graph.spring_data_neo4j;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan({"com.at.graph.spring_data_neo4j"})
@EnableNeo4jRepositories(basePackages = "com.at.graph.spring_data_neo4j.repository")
@EnableTransactionManagement
public class MyNeo4jConfiguration extends Neo4jConfiguration {

	@Bean
	public org.neo4j.ogm.config.Configuration getConfiguration() {
		org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
		config
			.driverConfiguration()
			//.setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
			//.setURI("http://movies:movies@localhost:7474")
			// the bolt driver 2.1.x complains:
			//    NoSuchMethodError: org.neo4j.ogm.driver.Driver.newTransaction()
			.setDriverClassName("org.neo4j.ogm.drivers.bolt.driver.BoltDriver")
			.setURI("bolt://movies:movies@localhost:7687")
			.setEncryptionLevel("NONE")
			;
		return config;
	}

	@Bean
	public SessionFactory getSessionFactory() {
		// with domain entity base package(s)
		return new SessionFactory(getConfiguration(), "com.at.graph.spring_data_neo4j.domain");
	}

}
