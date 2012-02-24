package com.theice.tribe.service;

import com.theice.tribe.domain.*;
import com.theice.tribe.service.Tribe.PersonBuilder;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class Tribe {

    @Autowired
    private CityRepository cities;

    @Autowired
    private MarketRepository markets;

    @Autowired
    private OrganizationRepository organizations;

    @Autowired
    private PersonRepository persons;

    @Autowired
    private SchoolRepository schools;

    @Autowired
    private Neo4jTemplate template;

    @Autowired
    private NamedReferenceService references;


    public PersonBuilder introduce(String personNamed) {
        return new PersonBuilder(personNamed);
    }

    public MarketBuilder market(String marketName) {
        return new MarketBuilder(marketName);
    }

    public CityBuilder city(String cityNamed) {
        return new CityBuilder(cityNamed);
    }

    public OrganizationBuilder organize(String organizationNamed) {
        return new OrganizationBuilder(organizationNamed);
    }

    public EndResult<Market> findMarkets() {
        return markets.findAll();
    }

    public long marketCount() {
        return markets.count();
    }

    public EndResult<City> findCities() {
        return cities.findAll();
    }

    public long cityCount() {
        return cities.count();
    }

    public EndResult<Organization> findOrganizations() {
        return organizations.findAll();
    }

    public long organizationCount() {
        return organizations.count();
    }

    public EndResult<Person> findPeople() {
        return persons.findAll();
    }

    public long populationCount() {
        return persons.count();
    }
    
    public Transaction beginTransaction() {
        return template.beginTx();
    }

    @Transactional
    public class PersonBuilder {
        private Person person = null;

        public PersonBuilder(String forPersonNamed) {
            person = persons.save(new Person(forPersonNamed));
        }

        public PersonBuilder employedAt(Organization employer) {
            EmployedWith r = person.relateTo(employer, EmployedWith.class, EmployedWith.RELATIONSHIP_TYPE);
            r.setCurrent(false);
            return this;
        }

        public PersonBuilder currentlyEmployedAt(Organization employer) {
            EmployedWith r = person.relateTo(employer, EmployedWith.class, EmployedWith.RELATIONSHIP_TYPE);
            r.setCurrent(true);
            return this;
        }

        public PersonBuilder currentlyEmployedAt(String employerName) {
            Organization employer = organizations.save(new Organization());
            employer.setName(employerName);
            return currentlyEmployedAt(employer);
        }

        public PersonBuilder tradesIn(Market market) {
            person.relateTo(market, TradeIn.class, TradeIn.RELATIONSHIP_TYPE);
            return this;
        }
        
        public PersonBuilder tradesIn(String marketNamed) {
            Market market = new MarketBuilder(marketNamed).register();
            return tradesIn(market);
        }

        public PersonBuilder wentTo(String schoolNamed) {
            School school = schools.save(new School());
            school.setName(schoolNamed);
            person.setSchool(school);
            return this;
        }

        public PersonBuilder livesIn(City city) {
            person.setCity(city);
            return this;
        }
        
        public PersonBuilder livesIn(String cityNamed) {
            City city = cities.save(new City(cityNamed));
            return livesIn(city);
        }

        public Person register() {
            return persons.save(person);
        }

		public PersonBuilder knownBy(String tribeId) {
			person.setTribeId(tribeId);
			return this;
		}
    }

    public class MarketBuilder {
        private Market market = null;

        public MarketBuilder(String forMarketNamed) {
            market = markets.save(new Market());
            market.setName(forMarketNamed);
            Node marketRootNode = template.getNode(references.getMarketReference().getId());
            template.createRelationshipBetween(template.getNode(market.getId()), marketRootNode, "IS_A", null);
        }

        public Market register() {
            return markets.save(market);
        }
    }

    public class CityBuilder {
        private City city = null;

        public CityBuilder(String forCityNamed) {
            city = cities.save(new City());
            city.setName(forCityNamed);
            Node referenceNode = template.getNode(references.getCityReference().getId());
            template.createRelationshipBetween(template.getNode(city.getId()), referenceNode, "IS_A", null);
        }

        public City register() {
            return cities.save(city);
        }
    }

    public class OrganizationBuilder {
        private Organization organization = null;

        public OrganizationBuilder(String forOrganizationNamed) {
            organization = organizations.save(new Organization());
            organization.setName(forOrganizationNamed);
            Node referenceNode = template.getNode(references.getOrganizationReference().getId());
            template.createRelationshipBetween(template.getNode(organization.getId()), referenceNode, "IS_A", null);
        }

        public Organization register() {
            return organizations.save(organization);
        }
    }

	public GraphDatabaseService getDatabase() {
		return template.getGraphDatabaseService();
	}

	public Person identify(Person someone, String withTribeId) {
		someone.setTribeId(withTribeId);
		return persons.save(someone);
	}

	public Person findPersonIdentifiedBy(String tribeId) {
		return persons.findByPropertyValue("tribeId", tribeId);
	}

}
