package com.theice.tribe.service;

import com.theice.tribe.domain.City;
import com.theice.tribe.domain.Market;
import com.theice.tribe.domain.Organization;
import com.theice.tribe.domain.Person;
import com.theice.tribe.service.Tribe.PersonBuilder;

import org.neo4j.graphdb.Transaction;

import java.util.Date;
import java.util.Random;

/**
 */
public class TribeGenerator {

    Tribe tribe;
    Random rnd = new Random();
	private int marketCount = 0;
	private int cityCount = 0;
	private int organizationCount = 0;
	private int populationCount = 0;

    public TribeGenerator(Tribe forTribe) {
        this.tribe = forTribe;
    }

    public Tribe generatePopulation(int count) {
        for (int i = 0; i < count; i++) {
            tribe.introduce(randomPersonName())
                    .currentlyEmployedAt(pickRandomOrganization())
                    .livesIn(pickRandomCity())
                    .tradesIn(pickRandomMarket())
                    .register();
        }
        return tribe;
    }

    public Tribe generatePopulation(int count, int marketsPerTrader, int citiesPerTrader, int employersPerTrader) {
        for (int i = 0; i < count; i++) {
            PersonBuilder personBuilder = tribe.introduce(randomPersonName());
            
            if (marketsPerTrader > 0) {
            	int randomLimit = rnd.nextInt(marketsPerTrader);
            	if (randomLimit == 0) randomLimit = 1;
	            for (int m=0; m<randomLimit; m++) {
	            	personBuilder = personBuilder.tradesIn(pickRandomMarket());
	            }
            }
            
            if (citiesPerTrader > 0) {
            	int randomLimit = rnd.nextInt(citiesPerTrader);
            	if (randomLimit == 0) randomLimit = 1;
	            for (int c=0; c<randomLimit; c++) {
	            	personBuilder = personBuilder.livesIn(pickRandomCity());
	            }
            }

            if (employersPerTrader > 0) {
            	int randomLimit = rnd.nextInt(employersPerTrader);

            	personBuilder.currentlyEmployedAt(pickRandomOrganization());
	            for (int e=1; e<randomLimit; e++) {
	            	personBuilder = personBuilder.employedAt(pickRandomOrganization());
	            }
            }
            
            personBuilder.register();
            
        }
        return tribe;
    }
    
    private String randomPersonName() {
        return "person " + uniqueRandomString();
    }

    private String randomMarketName() {
        return "Market " + uniqueRandomString();
    }

    private String randomCityName() {
        return "City " + uniqueRandomString();
    }

    private String randomOrganizationName() {
        return "Org " + uniqueRandomString();
    }

    private String uniqueRandomString() {
        return Long.toHexString(rnd.nextLong()) + Long.toHexString(new Date().getTime());
    }

    public Tribe generateMarkets(int marketCount) {
        for (int i = 0; i < marketCount; i++) {
            tribe.market(randomMarketName())
                    .register();
        }
        return tribe;
    }

    
    public Tribe generateCities(int cityCount) {

    	Transaction tx = tribe.beginTransaction();
    	try 
    	{
        for (int i = 0; i < cityCount; i++) {
            tribe.city(randomCityName())
                    .register();
        }
        tx.success();
    	} finally {
    		tx.finish();
    	}
        return tribe;
    }

    public Tribe generateOrganizations(int organizationCount) {
    	Transaction tx = tribe.beginTransaction();
    	try 
    	{

        for (int i = 0; i < organizationCount; i++) {
            tribe.organize(randomOrganizationName())
                    .register();
        }
        tx.success();
    	} finally {
    		tx.finish();
    	}

        return tribe;
    }

    public Market pickRandomMarket() {
    	if (this.marketCount == 0) {
    		this.marketCount = (int) tribe.marketCount();
    	}
        return pickRandomEntity(tribe.findMarkets(), marketCount);
    }

    public City pickRandomCity() {
    	if (this.cityCount == 0) {
    		this.cityCount = (int) tribe.cityCount();
    	}
        return pickRandomEntity(tribe.findCities(), cityCount);
    }

    public Organization pickRandomOrganization() {
    	if (this.organizationCount == 0) {
    		this.organizationCount = (int) tribe.organizationCount();
    	}
        return pickRandomEntity(tribe.findOrganizations(), organizationCount);
    }

    public Person pickRandomPerson() {
    	if (this.populationCount == 0) {
    		this.populationCount = (int) tribe.populationCount();
    	}
        return pickRandomEntity(tribe.findPeople(), populationCount);
    }
    
    private <T> T pickRandomEntity(Iterable<T> entities, int maxCount) {
        T picked = null;
        int randomIndex = rnd.nextInt(maxCount);
        for (T candidate : entities) {
            if (randomIndex <= 0) {
                picked = candidate;
                break;
            }
            randomIndex--;
        }
        return picked;
    }
    

	public Person markWellKnownPerson(String tribeId) {
        Person wellknown = pickRandomPerson();
        tribe.identify(wellknown, tribeId);
        return wellknown;
	}

	public GeneratorBuilder populate(int populationCount) {
		return new GeneratorBuilder(populationCount);
	}
	
	public class GeneratorBuilder {

		private int populationCount = 0;
		private int marketsPerTrader = 0;
		private int citiesPerTrader = 0;
		private int employersPerTrader = 0;
		
		public GeneratorBuilder(int populationCount) {
			this.populationCount = populationCount;
		}

		public GeneratorBuilder tradingIn(int marketsPerTrader) {
			this.marketsPerTrader = marketsPerTrader;
			return this;
		}

		public GeneratorBuilder livingIn(int citiesPerTrader) {
			this.citiesPerTrader = citiesPerTrader;
			return this;
		}

		public GeneratorBuilder employedAt(int employersPerTrader) {
			this.employersPerTrader = employersPerTrader;
			return this;
		}

		public void generate() {
			generatePopulation(populationCount, marketsPerTrader, citiesPerTrader, employersPerTrader);
		}
	}

}
