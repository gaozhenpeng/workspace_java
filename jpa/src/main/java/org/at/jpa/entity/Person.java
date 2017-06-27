package org.at.jpa.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String firstName;
	private char middleInitial;
	private String lastName;
	@ManyToOne(optional = true)
	private Company job;

	@Embedded
	private Address address;

	public Person() {
	}

	public Person(final String fn, final char mi, final String ln, final Address address) {
		setFirstName(fn);
		setMiddleInitial(mi);
		setLastName(ln);
		setAddress(address);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public char getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(final char middleInitial) {
		this.middleInitial = middleInitial;
	}

	public final Address getAddress() {
		return address;
	}

	public final void setAddress(final Address address) {
		this.address = address;
	}

	public Company getJob() {
		return job;
	}

	public void setJob(Company job) {
		this.job = job;
	}

	public boolean equals(final Object rhs) {
		if (rhs instanceof Person) {
			final Person other = (Person) rhs;
			return other.getLastName().equals(getLastName()) && other.getFirstName().equals(getFirstName())
					&& other.getMiddleInitial() == getMiddleInitial();
		}

		return false;
	}

	public int hashCode() {
		return getLastName().hashCode() * getFirstName().hashCode() * getMiddleInitial();
	}
}
