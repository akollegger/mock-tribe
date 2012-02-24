package com.theice.tribe.domain;

import org.springframework.data.neo4j.annotation.*;

@NodeEntity
public class NamedReference implements NamedEntity {

    @SuppressWarnings("unused")
    @GraphId
    private Long id;

    @Indexed
    private String name;

    @RelatedTo(type = "REFERENCE")
    private NamedReference root;

    public NamedReference() {
    }

    public NamedReference(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public NamedReference getRoot() {
        return root;
    }

    public void setRoot(NamedReference root) {
        this.root = root;
    }
}
