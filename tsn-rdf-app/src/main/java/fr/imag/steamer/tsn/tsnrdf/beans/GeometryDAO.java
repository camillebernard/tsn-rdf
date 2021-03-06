package fr.imag.steamer.tsn.tsnrdf.beans;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

/**
 * Retourne la géométrie GEOSPARQL d'un entité géographique
 *
 * @author Philippe GENOUD - Université Grenoble Alpes - Lab LIG-Steamer
 */
public class GeometryDAO {

//	private static String QUERY = new StringBuilder("PREFIX ogc: <http://www.opengis.net/ont/geosparql#> ")
//			.append("PREFIX map: <http://purl.org/steamer/nuts#> ")
//			.append("SELECT ?geom ")
//			.append("WHERE {  map:version1999_level2_DEA3  ogc:hasGeometry [  ")
//			.append("ogc:asWKT ?geom;]. } ")
//			.toString();
	
	private static String QUERY =  "   PREFIX tsn: <http://purl.org/net/tsn#>   "  + 
		 "   PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>   "  + 
		 "   PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>   "  + 
		 "   PREFIX geosparql: <http://www.opengis.net/ont/geosparql#>   "  + 
		 "   PREFIX owl: <http://www.w3.org/2002/07/owl#>   "  + 
		 "   PREFIX dct: <http://purl.org/dc/terms/>	   "  + 
		 "   PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  "  + 
		 "   PREFIX nuts: <http://purl.org/steamer/nuts/>  "  + 
		 "   select * where {   "  + 
		 "       ?TU a tsn:UnitVersion ;   "  + 
		 "           tsn:hasIdentifier ?code ;   "  + 
		 "           tsn:hasName ?name ;   "  + 
		 "           tsn:isMemberOf ?level;   "  + 
		 "           geosparql:hasGeometry [ geosparql:asWKT ?geom; ].   "  + 
		 "       ?level tsn:hasIdentifier 'NUTS_V1999_L2'^^xsd:string  ;   "  + 
		 "                                                 tsn:hasName ?levelname ;   "  + 
		 "                                                 tsn:isDivisionOf ?tsn_version .   "  + 
		 "       ?tsn_version tsn:hasIdentifier 'NUTS1999'^^xsd:string  ;   "  + 
		 "                                                   tsn:hasAcronym ?tsn_acronym .}  "  ; 

	// "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
	// + "PREFIX geosparql: <http://www.opengis.net/ont/geosparql#>\n" + "PREFIX rcs: <http://purl.org/fr/eclat/resource/> \n" + "SELECT ?geom \n" + "WHERE { \n"
	// + " rcs:%s geosparql:hasGeometry [\n" + " geosparql:asWKT ?geom;\n" + " ].\n" + "} ";

	/**
	 * retourne la géométrie (WKT) associée à une URI
	 * 
	 * @param uri l'uri de la resource
	 * @return le WKT definissant la géométrie de la ressource
	 * 
	 * @throws IllegalArgumentException si la resource n'a pas de geométrie
	 */
	public static String getGeometry(String uri) {
		String queryString = String.format(QUERY, uri);
		HTTPRepository repository = new HTTPRepository("http://steamerlod.imag.fr/repositories/tsn");
		try (RepositoryConnection connection = repository.getConnection()) {
			// try avec resources pour être sur de femer la connexion dans un bloc finally
			TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			try ( // Evaluating a prepared query returns an iterator-like object
					// that can be traversed with the methods hasNext() and next()
					TupleQueryResult tupleQueryResult = tupleQuery.evaluate()) {
				if (tupleQueryResult.hasNext()) {
					BindingSet bindingSet = tupleQueryResult.next();
					return bindingSet.getValue("geom").stringValue();
				}
				throw new IllegalArgumentException("pas de géométrie pour uri " + uri);
			}
		}
	}
}
