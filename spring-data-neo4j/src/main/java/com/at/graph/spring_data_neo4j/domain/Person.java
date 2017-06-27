package com.at.graph.spring_data_neo4j.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label="Person")
public class Person extends DomainObject {
	@Property(name="name")
	private String name;
	@Property(name="born")
	private Integer born;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getBorn() {
		return born;
	}
	public void setBorn(Integer born) {
		this.born = born;
	}

}