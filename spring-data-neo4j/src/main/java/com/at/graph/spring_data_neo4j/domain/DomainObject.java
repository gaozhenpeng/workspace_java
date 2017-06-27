package com.at.graph.spring_data_neo4j.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public abstract class DomainObject {
   @GraphId
   protected Long id;
}

