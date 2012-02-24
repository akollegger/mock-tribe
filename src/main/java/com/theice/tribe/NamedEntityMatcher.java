package com.theice.tribe;

import com.theice.tribe.domain.NamedEntity;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Collection;

/**
 */
public class NamedEntityMatcher extends TypeSafeMatcher<NamedEntity> {
    private String name;

    public NamedEntityMatcher(String name) {
        this.name = name;
    }

    @Override
    public boolean matchesSafely(NamedEntity namedEntity) {
        return name.equals(namedEntity.getName());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is not named " + name);
    }

    @Factory
    public static <T> Matcher<NamedEntity> isNamed(String name) {
        return new NamedEntityMatcher(name);
    }

}
