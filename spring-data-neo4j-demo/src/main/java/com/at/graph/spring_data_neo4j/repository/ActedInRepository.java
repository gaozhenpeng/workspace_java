package com.at.graph.spring_data_neo4j.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.at.graph.spring_data_neo4j.domain.ActedIn;

@Repository
public interface ActedInRepository extends GraphRepository<ActedIn> {

}
