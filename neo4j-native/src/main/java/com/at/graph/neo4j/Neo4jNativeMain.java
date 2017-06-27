package com.at.graph.neo4j;

import static org.neo4j.driver.v1.Values.parameters;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Neo4jNativeMain {
	private static final Logger logger = LoggerFactory.getLogger(Neo4jNativeMain.class);
	
	
	public static void main(String[] args) {

		Driver driver = null;
		// bolt, the neo4j team only supports bolt protocol so far
		driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "admin"));
//		// http 
//		driver = GraphDatabase.driver("http://localhost:7687", AuthTokens.basic("neo4j", "admin"));
		
		Session session = driver.session();

		session.run("CREATE (a:Person {name: {name}, title: {title}})", parameters("name", "Arthur", "title", "King"));

		StatementResult result = session.run(
				"MATCH (a:Person) WHERE a.name = {name} " + "RETURN a.name AS name, a.title AS title",
				parameters("name", "Arthur"));
		while (result.hasNext()) {
			Record record = result.next();
			logger.info(record.get("title").asString() + " " + record.get("name").asString());
		}

		session.close();
		driver.close();
	}
}
