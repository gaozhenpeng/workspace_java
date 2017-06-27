package org.at.jpa.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.at.test.TestBase;
import org.junit.Test;

public class PersonTest extends TestBase {
	private final Address a1 = new Address("A Rd.", "", "Dallas", "TX", "75001");
	private final Person p1 = new Person("Brett", 'L', "Schuchert", a1);

	private final Address a2 = new Address("B Rd.", "S2", "OkC", "OK", "73116");
	private final Person p2 = new Person("FirstName", 'K', "LastName", a2);

	// private final Person p1 = new Person("Brett", 'L', "Schuchert",
	// "Street1",
	// "Street2", "City", "State", "Zip");
	// private final Person p2 = new Person("FirstName", 'K', "LastName",
	// "Street1", "Street2", "City", "State", "Zip");
	public static List<Person> generatePersonObjects() {
		final List<Person> people = new ArrayList<Person>();
		final Address a1 = new Address("A Rd.", "", "Dallas", "TX", "75001");
		final Person p1 = new Person("Brett", 'L', "Schuchert", a1);

		final Address a2 = new Address("B Rd.", "S2", "OkC", "OK", "73116");
		final Person p2 = new Person("FirstName", 'K', "LastName", a2);

		people.add(p1);
		people.add(p2);

		return people;
	}

	@SuppressWarnings("unchecked")
	@Test
	public void insertAndRetrieve() {
		final List<Person> people = generatePersonObjects();

		em.getTransaction().begin();
		for (Person p : people) {
			em.persist(p);
		}
		em.getTransaction().commit();
		// em.getTransaction().begin();
		// em.persist(p1);
		// em.persist(p2);
		// em.getTransaction().commit();

		final List<Person> list = em.createQuery("select p from Person p").getResultList();

		assertEquals(2, list.size());
		for (Person current : list) {
			final String firstName = current.getFirstName();
			final String streetAddress1 = current.getAddress().getStreetAddress1();

			assertTrue(firstName.equals("Brett") || firstName.equals("FirstName"));
			assertTrue(streetAddress1.equals("A Rd.") || streetAddress1.equals("B Rd."));
		}
	}
}
