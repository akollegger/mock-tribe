package com.theice.tribe.service;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.theice.tribe.domain.City;
import com.theice.tribe.domain.Market;
import com.theice.tribe.domain.Organization;
import com.theice.tribe.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Traverser.Order;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.UniquenessFactory;

import org.neo4j.kernel.AbstractGraphDatabase;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import static com.theice.tribe.NamedEntityIterableMatcher.containsName;
import static com.theice.tribe.NamedEntityMatcher.isNamed;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 *
 */
@ContextConfiguration(locations = "classpath:META-INF/spring/testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MockTribeTest {

	@Autowired
	private Tribe tribe;

	@Autowired
	private Neo4jTemplate template;

	@Test
	public void shouldMockUpASmallTribe() {
		Neo4jHelper.cleanDb(template);

		final int POPULATION = 100;
		final int ORG_COUNT = 20;
		int CITY_COUNT = 10;
		int MARKET_COUNT = 20;

		TribeGenerator generator = new TribeGenerator(tribe);

		System.out.println("Generating " + ORG_COUNT + " employers");
		generator.generateOrganizations(ORG_COUNT);
		assertThat((int) tribe.organizationCount(), is(ORG_COUNT));
		Organization org = generator.pickRandomOrganization();
		assertNotNull(org);
		assertThat(tribe.findOrganizations(), containsName(org.getName()));

		System.out.println("Generating " + CITY_COUNT + " cities");
		generator.generateCities(CITY_COUNT);
		assertThat((int) tribe.cityCount(), is(CITY_COUNT));
		City city = generator.pickRandomCity();
		assertNotNull(city);
		assertThat(tribe.findCities(), containsName(city.getName()));

		System.out.println("Generating " + MARKET_COUNT + " markets");
		generator.generateMarkets(MARKET_COUNT);
		assertThat((int) tribe.marketCount(), is(MARKET_COUNT));
		Market market = generator.pickRandomMarket();
		assertNotNull(market);
		assertThat(tribe.findMarkets(), containsName(market.getName()));

		System.out.println("Generating " + POPULATION + " traders");
		Transaction tx = tribe.beginTransaction();
		try {
			generator.generatePopulation(POPULATION);
			tx.success();
		} finally {
			tx.finish();
		}
		Person akollegger = generator.markWellKnownPerson("akollegger");

		System.out.println("Created [ "
				+ ((AbstractGraphDatabase) template.getGraphDatabaseService())
						.getStoreDir() + " ]");

		System.out.println("Known trader: " + akollegger);
	}

	@Test
	public void shouldMockUpARealisticTribe() {
		Neo4jHelper.cleanDb(template);

		final int POPULATION = 12000;
		final int ORG_COUNT = 600;
		int CITY_COUNT = 50;
		int MARKET_COUNT = 12;

		int MARKETS_PER_TRADER = 5;
		int CITIES_PER_TRADER = 1;
		int EMPLOYERS_PER_TRADER = 1;

		TribeGenerator generator = new TribeGenerator(tribe);

		Date start = new Date();
		System.out.println("Start: " + start);

		System.out.println("Generating " + ORG_COUNT + " employers");
		generator.generateOrganizations(ORG_COUNT);

		System.out.println("Generating " + CITY_COUNT + " cities");
		generator.generateCities(CITY_COUNT);

		System.out.println("Generating " + MARKET_COUNT + " markets");
		generator.generateMarkets(MARKET_COUNT);

		System.out.println("Generating " + POPULATION + " traders");
		Transaction tx = tribe.beginTransaction();
		try {
			generator.populate(POPULATION).tradingIn(MARKETS_PER_TRADER)
					.livingIn(CITIES_PER_TRADER)
					.employedAt(EMPLOYERS_PER_TRADER).generate();
			tx.success();
		} finally {
			tx.finish();
		}

		Date end = new Date();

		System.out.println("Created [ "
				+ ((AbstractGraphDatabase) template.getGraphDatabaseService())
						.getStoreDir() + " ]");

		System.out.println("End: " + end);
		System.out.println("Duration (seconds): "
				+ ((end.getTime() - start.getTime()) / 1000));

		Person trader = generator.markWellKnownPerson("akollegger");
		System.out.println("Known trader: " + trader);
		trader = generator.markWellKnownPerson("mesrii");
		System.out.println("Known trader: " + trader);
		trader = generator.markWellKnownPerson("systay");
		System.out.println("Known trader: " + trader);
		trader = generator.markWellKnownPerson("pbar");
		System.out.println("Known trader: " + trader);
		trader = generator.markWellKnownPerson("dxia");
		System.out.println("Known trader: " + trader);
	}

	@Test
	public void shouldFindWellKnownTrader() {
		ExecutionEngine engine = new ExecutionEngine(tribe.getDatabase());

		String cql = "start akollegger=node:Person(tribeId='akollegger') return akollegger";

		ExecutionResult result = engine.execute(cql);
		Iterator<Node> traders = result.javaColumnAs("akollegger");
		assertEquals(traders.next().getProperty("tribeId"), "akollegger");

	}
	
	@Test
	public void shouldFindWellKnownTraderWithCypher() {
		ExecutionEngine engine = new ExecutionEngine(tribe.getDatabase());

		String cql = "start me=node:Person(tribeId='akollegger') " +
				"match me-[:TradeIn]->mkt<-[:TradeIn]-others, " +
				"me-[:AtCity]->place<-[:AtCity]-others return distinct others";


		Date start = new Date();
		ExecutionResult result = engine.execute(cql);
		Date end = new Date();
		
		System.out.println("AtCity+TradeIn Cypher traversal found "
				+ result.size() + " rows");
		System.out.println("From " + tribe.populationCount() + " traders");
		System.out.println("In " + ((end.getTime() - start.getTime())) + " milliseconds");

	}


	@Test
	public void shouldFindOtherTradersByCityAndMarket() {

		// match me-[:AtCity]->place<-[r2:AtCity]-others,
		// me-[:TradeIn]->mkt<-[r:TradeIn]-others
		TraversalDescription cityTraversal = Traversal.description()
				.relationships(DynamicRelationshipType.withName("AtCity"))
				.relationships(DynamicRelationshipType.withName("TradeIn"))
				.breadthFirst().uniqueness(Uniqueness.NODE_PATH)
				.evaluator(new Evaluator() {
					public Evaluation evaluate(Path path) {
						Node currentNode = path.endNode();
						if (path.length() == 2) {
							if (currentNode != path.startNode()) {
								return Evaluation.INCLUDE_AND_PRUNE;

							} else {
								return Evaluation.EXCLUDE_AND_PRUNE;
							}
						} else if (path.length() > 2) {
							return Evaluation.EXCLUDE_AND_PRUNE;
						}
						return Evaluation.EXCLUDE_AND_CONTINUE;
					}
				});
		
		Date start = new Date();

		// start me=node:Person(tribeId="akollegger")
		Person akollegger = tribe.findPersonIdentifiedBy("akollegger");
		Node me = template.getNode(akollegger.getId());
		
		Traverser cityResults = cityTraversal.traverse(me);

		Set<Node> citySet = new HashSet<Node>();
		for (Path p : cityResults) {
			record(p.endNode(), p.lastRelationship());
		}

		citySet = filter();
		Date end = new Date();

		System.out.println("AtCity+TradeIn traversal found "
				+ matchedNodeMap.size() + " paths");
		System.out.println("AtCity+TradeIn traversal filtered to "
				+ citySet.size() + " traders");
		System.out.println("From " + tribe.populationCount() + " traders");
		System.out.println("In " + ((end.getTime() - start.getTime())) + " milliseconds");
	}

	Map<Node, Set<String>> matchedNodeMap = null;

	private void record(Node n, Relationship r) {
		if (matchedNodeMap == null) {
			matchedNodeMap = new HashMap<Node, Set<String>>();
		}

		Set<String> relSet = matchedNodeMap.get(n);
		if (relSet == null) {
			relSet = new HashSet<String>();
			matchedNodeMap.put(n, relSet);
		}
		relSet.add(r.getType().name());
	}

	private Set<Node> filter() {
		Set<Node> filtered = new HashSet<Node>();

		for (Entry<Node, Set<String>> e : matchedNodeMap.entrySet()) {
			if (e.getValue().size() == 2) {
				filtered.add(e.getKey());
			}
		}
		return filtered;
	}
}
