package com.theice.tribe;

import com.theice.tribe.domain.NamedEntity;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 */
public class NamedEntityIterableMatcher extends TypeSafeMatcher<Iterable<? extends NamedEntity>> {
    private String name;

    public NamedEntityIterableMatcher(String name) {
        this.name = name;
    }

    @Override
    public boolean matchesSafely(Iterable<? extends NamedEntity> namedEntities) {
        boolean foundMatchingName = false;
        for (NamedEntity entity : namedEntities) {
            if (name.equals(entity.getName())) {
                return true;
            }
        }
        return foundMatchingName;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("does not contain an entity named " + name);
    }

    @Factory
    public static <T> Matcher<Iterable<? extends NamedEntity>> containsName(String name) {
        return new NamedEntityIterableMatcher(name);
    }

}
