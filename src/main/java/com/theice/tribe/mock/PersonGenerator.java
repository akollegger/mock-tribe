package com.theice.tribe.mock;

import com.theice.tribe.domain.Person;
import com.theice.tribe.service.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: akollegger
 * Date: 2/22/12
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersonGenerator {

    @Autowired
    PersonRepository persons;

    public void generatePeople(int count) {
        for (int i=0; i<count; i++) {
//            persons.save(mockPerson());
        }
    }

}
