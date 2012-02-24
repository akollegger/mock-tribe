package com.theice.tribe.service;

import com.theice.tribe.domain.NamedReference;
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
import static junit.framework.Assert.assertNotNull;

/**
 *
 */
@ContextConfiguration(locations = "classpath:META-INF/spring/testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class NamedReferenceServiceTest {

    @Autowired
    private NamedReferenceService references;

    @Autowired
    private Neo4jTemplate template;

    @Rollback(false)
    @BeforeTransaction
    public void clearDatabase()
    {
        Neo4jHelper.cleanDb(template);
    }

    @Test
    public void shouldGetReferenceNode()
    {
        NamedReference root = references.getRoot();
        assertNotNull(root);
        assertEquals(0, (long)root.getId());
    }

    @Test
    public void shouldGetMarketReferenceNode()
    {
        NamedReference reference = references.getMarketReference();
        assertNotNull(reference);
        assertEquals(0, (long)reference.getRoot().getId());
    }


    @Test
    public void shouldGetCityReferenceNode()
    {
        NamedReference reference = references.getCityReference();
        assertNotNull(reference);
        assertEquals(0, (long)reference.getRoot().getId());
    }

    @Test
    public void shouldGetOrganizationReferenceNode()
    {
        NamedReference reference = references.getOrganizationReference();
        assertNotNull(reference);
        assertEquals(0, (long)reference.getRoot().getId());
    }


}
