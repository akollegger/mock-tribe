package com.theice.tribe.service;

import com.theice.tribe.domain.City;
import com.theice.tribe.domain.NamedReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface NamedReferenceRepository extends GraphRepository<NamedReference> {

}
