package com.theice.tribe.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity
public class EmployedWith {

    @GraphId
    Long id;

    @StartNode
    Person person;
    @EndNode
    Organization employer;
    
    Boolean current;

    public static final String RELATIONSHIP_TYPE = EmployedWith.class.getSimpleName();

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Organization getEmployer() {
        return employer;
    }

    public void setEmployer(Organization employer) {
        this.employer = employer;
    }

	public Boolean isCurrent() {
		return current;
	}

	public void setCurrent(Boolean current) {
		this.current = current;
	}
    
    
}
