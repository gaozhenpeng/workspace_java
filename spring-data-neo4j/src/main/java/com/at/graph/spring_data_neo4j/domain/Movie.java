package com.at.graph.spring_data_neo4j.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "Movie")
public class Movie extends DomainObject {

	@Property(name = "title")
	private String title;

	@Property(name = "released")
	private Integer released;
	
	@Property(name = "tagline")
	private String tagline;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getReleased() {
		return released;
	}

	public void setReleased(Integer released) {
		this.released = released;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	
	
	
}
