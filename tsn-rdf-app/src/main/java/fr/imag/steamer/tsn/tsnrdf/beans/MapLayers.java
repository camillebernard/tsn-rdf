package fr.imag.steamer.tsn.tsnrdf.beans;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;


/**
 * The Class MapLayers.
 */
public class MapLayers {

	/** The Constant QUERY. Queries one TSN version. */
	private static final String QUERY = new StringBuilder("PREFIX tsn: <http://purl.org/net/tsn#>"
		+ " PREFIX nuts: <http://purl.org/steamer/nuts/>\"")
			.append("select ?TU where { ")
			.append("?TU a tsn:UnitVersion . ")
			.append("?TU tsn:isMemberOf ?level . ")
			.append("?level tsn:isDivisionOf ?tsn_version . ")
			.append("?tsn_version tsn:hasIdentifier \"%s\"^^xsd:string .} ")
			.toString();

	// ="PREFIX : <http://purl.org/fr/eclat/ontology#>\n"+"SELECT ?phonem (count(?phonem) as ?count)\n"+"WHERE { \n"+" ?pte a :SurveyPoint;\n"+" :isSurveyPointOf ?ei.\n"+" ?ei :hasResponse ?rep.\n"+"
	// ?rep :phoneticRepresentationAPI ?phonem.\n"+" ?ei :isAssociatedTo ?intitule. \n"+" ?intitule :hasMap <http://purl.org/fr/eclat/resource/carte_ALF_%s> . \n"+"} "+"group by ?phonem\n"+"order by
	// DESC(?count)";

	/**
	 * Query.
	 *
	 * @param idVersionTSN the id version TSN
	 * @return the map
	 */
	public static List<String> query(String idVersionTSN) {
		List<String> res = new ArrayList<String>();
		int rank = 0;
		String queryString = String.format(QUERY, idVersionTSN);
		System.out.println(queryString);
		HTTPRepository repository = new HTTPRepository("http://steamerlod.imag.fr/repositories/tsn");
		try (RepositoryConnection connection = repository.getConnection()) {
			TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			try ( // Evaluating a prepared query returns an iterator-like object
					// that can be traversed with the methods hasNext() and next()
					TupleQueryResult tupleQueryResult = tupleQuery.evaluate()) {
				while (tupleQueryResult.hasNext()) {
					// Each result is represented by a BindingSet, which corresponds to a result row
					BindingSet bindingSet = tupleQueryResult.next();
					// Each BindingSet contains one or more Bindings
					String territorialUnit = bindingSet.getValue("TU").stringValue();
					res.add(territorialUnit);
					rank++;
				}
			}
			return res;
		}

	}

}
