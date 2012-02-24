package com.theice.tribe.service;

import com.theice.tribe.domain.Market;
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
public class MarketRepositoryTest {

    @Autowired
    private MarketRepository markets;

    @Autowired
    private Neo4jTemplate template;

    @Rollback(false)
    @BeforeTransaction
    public void clearDatabase()
    {
        Neo4jHelper.cleanDb(template);
    }

    @Test
    public void shouldAllowDirectMarketCreation()
    {
        assertEquals(0, (long) template.count(Market.class));
        Market me = markets.save(new Market("natural gas"));
        assertEquals(1, (long) template.count(Market.class));
        Market foundMarket = markets.findOne(me.getId());
        assertEquals(me.getName(), foundMarket.getName());
    }


}
