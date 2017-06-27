package org.at.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

public class TestBase {
	protected EntityManagerFactory emf;
	protected EntityManager em;

	public TestBase() {
		super();
	}

	@Before
	public void initEmfAndEm() {
		BasicConfigurator.configure();
		Logger.getLogger("org").setLevel(Level.ERROR);

		emf = Persistence.createEntityManagerFactory("examplePersistenceUnit");
		em = emf.createEntityManager();
	}

	@After
	public void cleanup() {
		em.close();
	}
}
