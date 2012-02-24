package com.theice.tribe.service;

import com.theice.tribe.domain.City;
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

import static junit.framework.Assert.assertEquals;

/**
 *
 */
@ContextConfiguration(locations = "classpath:META-INF/spring/testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CityRepositoryTest {

    @Autowired
    private CityRepository cities;

    @Autowired
    private Neo4jTemplate template;

    @Rollback(false)
    @BeforeTransaction
    public void clearDatabase()
    {
        Neo4jHelper.cleanDb(template);
    }

    @Test
    public void shouldAllowDirectCityCreation()
    {
        assertEquals(0, (long) template.count(City.class));
        City me = cities.save(new City("Atlanta"));
        assertEquals(1, (long) template.count(City.class));
        City foundCity = cities.findOne(me.getId());
        assertEquals(me.getName(), foundCity.getName());
    }


}
