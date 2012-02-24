package com.theice.tribe.service;

import com.theice.tribe.domain.NamedReference;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;

@Service
public class NamedReferenceService {

    private static final String ROOT_REFERENCE = "root";
    private static final String MARKET_REFERENCE = "markets";
    private static final String CITY_REFERENCE = "cities";
    private static final String ORG_REFERENCE = "organizations";

    @Autowired
    NamedReferenceRepository namedReferenceRepository;
    
    @Autowired
    Neo4jTemplate template;

    public NamedReference getRoot()
    {
        Node referenceNode = template.getReferenceNode();
        if (!referenceNode.hasProperty("__type__"))
        {
            Transaction tx = template.getGraphDatabaseService().beginTx();
            try
            {
                referenceNode.setProperty("__type__", NamedReference.class.getName());
                referenceNode.setProperty("name", ROOT_REFERENCE);
                tx.success();
            } finally {
                tx.finish();
            }
        }
        return namedReferenceRepository.findOne(0l);
    }


    public NamedReference getMarketReference() {
        return getOrCreateReference(MARKET_REFERENCE);
    }

    public NamedReference getCityReference() {
        return getOrCreateReference(CITY_REFERENCE);
    }

    public NamedReference getOrganizationReference() {
        return getOrCreateReference(ORG_REFERENCE);
    }
    
    private NamedReference getOrCreateReference(String named)
    {
        NamedReference namedReference = namedReferenceRepository.findByPropertyValue("name", named);
        if (namedReference == null) {
            namedReference = new NamedReference(named);
            namedReference.setRoot(getRoot());
            namedReference = namedReferenceRepository.save(namedReference);
        }
        return namedReference;
    }
}
