package com.theice.tribe.service;

import com.theice.tribe.domain.*;
import com.theice.tribe.service.Tribe;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import static com.theice.tribe.NamedEntityIterableMatcher.containsName;
import static com.theice.tribe.NamedEntityMatcher.isNamed;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 *
 */
@ContextConfiguration(locations = "classpath:META-INF/spring/testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TribeTest {

    @Autowired
    private Tribe tribe;

    @Autowired
    private Neo4jTemplate template;

    @Rollback(false)
    @BeforeTransaction
    public void clearDatabase()
    {
        Neo4jHelper.cleanDb(template);
    }

    @Test
    public void shouldBuildTheDomain()
    {
        Person builtPerson = tribe
                .introduce("Andreas")
        		.knownBy("akollegger")
                .currentlyEmployedAt("Neo Technology")
                .tradesIn("graphs")
                .wentTo("Goucher")
                .livesIn("Baltimore")
                .register();
        
        assertThat(builtPerson.getName(), is("Andreas"));
        assertThat(builtPerson.getTribeId(), is("akollegger"));
        assertThat(builtPerson.getOrganizations(), containsName("Neo Technology"));
        assertThat(builtPerson.getMarkets(), containsName("graphs"));
        assertThat(builtPerson.getSchool(), isNamed("Goucher"));
        assertThat(builtPerson.getCity(), isNamed("Baltimore"));

    }

    @Test
    public void shouldGenerateRandomMarkets()
    {
        final int EXPECTED_MARKET_COUNT = 100;
        TribeGenerator generator = new TribeGenerator(tribe);
        generator.generateMarkets(EXPECTED_MARKET_COUNT);
        assertEquals(EXPECTED_MARKET_COUNT, template.count(Market.class));
    }

    @Test
    public void shouldGenerateRandomCities()
    {
        final int EXPECTED_CITY_COUNT = 100;
        TribeGenerator generator = new TribeGenerator(tribe);
        generator.generateCities(EXPECTED_CITY_COUNT);
        assertEquals(EXPECTED_CITY_COUNT, template.count(City.class));
    }

    @Test
    public void shouldGenerateRandomOrganizations()
    {
        final int EXPECTED_ORG_COUNT = 100;
        TribeGenerator generator = new TribeGenerator(tribe);
        generator.generateOrganizations(EXPECTED_ORG_COUNT);
        assertEquals(EXPECTED_ORG_COUNT, template.count(Organization.class));
    }
    
    @Test
    public void shouldPickRandomMarket()
    {
        final int COUNT = 10;
        TribeGenerator generator = new TribeGenerator(tribe);
        generator.generateMarkets(COUNT);
        Market randomized = generator.pickRandomMarket();
        assertNotNull(randomized);
        assertThat(tribe.findMarkets(), containsName(randomized.getName()));
    }

    @Test
    public void shouldPickRandomCity()
    {
        final int COUNT = 10;
        TribeGenerator generator = new TribeGenerator(tribe);
        generator.generateCities(COUNT);
        City randomized = generator.pickRandomCity();
        assertNotNull(randomized);
        assertThat(tribe.findCities(), containsName(randomized.getName()));
    }

    @Test
    public void shouldPickRandomOrganization()
    {
        final int COUNT = 10;
        TribeGenerator generator = new TribeGenerator(tribe);
        generator.generateOrganizations(COUNT);
        Organization randomized = generator.pickRandomOrganization();
        assertNotNull(randomized);
        assertThat(tribe.findOrganizations(), containsName(randomized.getName()));
    }
}
