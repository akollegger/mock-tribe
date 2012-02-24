package com.theice.tribe.service;

import com.theice.tribe.domain.Market;
import com.theice.tribe.domain.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends GraphRepository<Market> {

}
