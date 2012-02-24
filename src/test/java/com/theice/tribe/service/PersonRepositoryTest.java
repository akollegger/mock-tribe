package com.theice.tribe.service;

import com.theice.tribe.domain.City;
import com.theice.tribe.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Node;
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
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository persons;

    @Autowired
    private Neo4jTemplate template;

    @Rollback(false)
    @BeforeTransaction
    public void clearDatabase()
    {
        Neo4jHelper.cleanDb(template);
    }

    @Test
    public void shouldAllowDirectPersonCreation()
    {
        assertEquals(0, (long) template.count(Person.class));
        Person me = persons.save(new Person("Andreas"));
        assertEquals(1, (long) template.count(Person.class));
        Person foundPerson = persons.findOne(me.getId());
        assertEquals(me.getName(), foundPerson.getName());
        Node myNode = template.getNode(me.getId());
        
        template.projectTo(myNode, Person.class);
    }

    @Test
    public void shouldProjectBackIntoAPerson()
    {
        Person me = persons.save(new Person("Andreas"));
        Node myNode = template.getNode(me.getId());
        
        Person myPerson = template.projectTo(myNode, Person.class);
        assertEquals(me.getName(), myPerson.getName());
    }

}
