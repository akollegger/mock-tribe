package com.theice.tribe.domain;

import org.neo4j.helpers.collection.IteratorUtil;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.neo4j.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@NodeEntity
public class Person implements NamedEntity {

    @SuppressWarnings("unused")
    @GraphId
    private Long id;

    @Indexed
    private String tribeId;
    
    @Indexed
    private String name;
    
    @RelatedToVia(elementClass = EmployedWith.class, type = "EmployedWith")
    private Iterable<EmployedWith> employments;

    @RelatedTo(type="AtCity")
    private City city;

    @RelatedTo(type="WentTo")
    private School school;

    @RelatedToVia(elementClass = TradeIn.class, type="TradeIn")
    private Iterable<TradeIn> tradesInMarkets;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Organization> getOrganizations() {
        List<Organization> orgs = new ArrayList<Organization>();
        for (EmployedWith employment : employments) {
            orgs.add(employment.getEmployer());
        }
        return orgs;
    }

    public Collection<Market> getMarkets() {
        List<Market> markets = new ArrayList<Market>();
        for (TradeIn tradesIn: tradesInMarkets) {
            markets.add(tradesIn.getMarket());
        }
        return markets;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

	public String getTribeId() {
		return tribeId;
	}

	public void setTribeId(String tribeId) {
		this.tribeId = tribeId;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", tribeId=" + tribeId
				+ "]";
	}
    
	
}
