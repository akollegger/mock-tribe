package com.theice.tribe.service;

import com.theice.tribe.domain.Organization;
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
public class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizations;

    @Autowired
    private Neo4jTemplate template;

    @Rollback(false)
    @BeforeTransaction
    public void clearDatabase()
    {
        Neo4jHelper.cleanDb(template);
    }

    @Test
    public void shouldAllowDirectOrganizationCreation()
    {
        assertEquals(0, (long) template.count(Organization.class));
        Organization me = organizations.save(new Organization("Neo Technology"));
        assertEquals(1, (long) template.count(Organization.class));
        Organization foundOrganization = organizations.findOne(me.getId());
        assertEquals(me.getName(), foundOrganization.getName());
    }


}
