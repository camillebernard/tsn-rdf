package fr.imag.steamer.tsn.tsnrdf.beans;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

/**
 *
 * @author Philippe GENOUD - Université Grenoble Alpes - Lab LIG-Steamer
 */
public class MapCategories {

    private static final String QUERY
            = "PREFIX : <http://purl.org/fr/eclat/ontology#>\n"
            + "SELECT ?phonem (count(?phonem) as ?count)\n"
            + "WHERE { \n"
            + "    ?pte a :SurveyPoint;\n"
            + "         :isSurveyPointOf ?ei.\n"
            + "     ?ei :hasResponse ?rep.\n"
            + "     ?rep :phoneticRepresentationAPI ?phonem.\n"
            + "     ?ei :isAssociatedTo ?intitule.  \n"
            + "     ?intitule :hasMap <http://purl.org/fr/eclat/resource/carte_ALF_%s> .  \n"
            + "} "
            + "group by ?phonem\n"
            + "order by DESC(?count)";

    /**
     * Crée une Map des catégories d'une carte.
     * @param idVersionTSN Acronym of the TSN to Query (e.g., version_2006 for the NUTS version 2006)
     * @return Map des catégories, la clé est le phonème qui identifie la catgorie
     *         la valeur est l'objet ResponseCategory qui correspond à la catégorie
     */
    public static Map<String,ResponseCategory> query(String idVersionTSN) {
        Map<String,ResponseCategory> res = new HashMap<>();
        int rank = 0;
        // Preparing a SELECT query for later evaluation
        String queryString = String.format(QUERY, idVersionTSN);
        System.out.println(queryString);
//        HTTPRepository repository = new HTTPRepository("http://localhost:7200/repositories/Eclat3");
//        HTTPRepository repository = new HTTPRepository("http://129.88.46.88:7200/repositories/Eclat3");
        HTTPRepository repository = new HTTPRepository("http://localhost:7200/repositories/change-nuts");
        try (RepositoryConnection connection = repository.getConnection()) {
            // try avec resources pour être sur de femer la connexion dans un bloc finally
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            try ( // Evaluating a prepared query returns an iterator-like object
            // that can be traversed with the methods hasNext() and next()
                    TupleQueryResult tupleQueryResult = tupleQuery.evaluate()) {
                while (tupleQueryResult.hasNext()) {
                    // Each result is represented by a BindingSet, which corresponds to a result row
                    BindingSet bindingSet = tupleQueryResult.next();
                    // Each BindingSet contains one or more Bindings
                    String phonem = bindingSet.getValue("phonem").stringValue();
                    int count = Integer.parseInt(bindingSet.getValue("count").stringValue());
                    System.out.printf("%d %s %d\n", rank, phonem, count);
                    res.put(phonem,new ResponseCategory(phonem, rank, count));
                    rank++;
                }
                // Bindings can also be accessed explicitly by variable name
                //Binding binding = bindingSet.getBinding("x");
                // Once we are done with a particular result we need to close it
            }
            // Bindings can also be accessed explicitly by variable name
            //Binding binding = bindingSet.getBinding("x");
            // Doing more with the same connection object
            // ...
            return res;
        }
        
    }

}
