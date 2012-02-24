package com.theice.tribe.util;

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

import com.theice.tribe.domain.City;
import com.theice.tribe.domain.Market;
import com.theice.tribe.domain.Organization;

@ContextConfiguration(locations = "classpath:META-INF/spring/testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class NamedIntersectionTest {

    @Autowired
    private Neo4jTemplate template;

    @Rollback(false)
    @BeforeTransaction
    public void clearDatabase()
    {
        Neo4jHelper.cleanDb(template);
    }


    @Test
    public void shouldCreateUniqueNamedSetsTopAtLevel()
    {
    	NamedIntersection intersection = NamedIntersection.named("markets");
    	Market marketA = new Market("A");
    	Market marketB = new Market("B");
    	Market marketC = new Market("C");
    	
    	intersection.intersect(marketA, marketB, marketC);
    	
//    	Collection<NameSet> justCityA = intersection.of(Market.class, marketA);
    	
    }
}
