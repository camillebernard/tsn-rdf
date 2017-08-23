package fr.imag.steamer.tsn.tsnrdf.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.spatial4j.core.io.WktShapeParser;

/**
 * The Class MapController.
 */
public class MapController {

	/** The feature collection. */
	private JSONObject featureCollection = new JSONObject();

	/** The lat. */
	private String lat;

	/** The lon. */
	private String lon;

	/** The rayon. */
	private String rayon;

	/** The point. */
	private String point;

	/** The territorial unit. */
	private String territorialUnit;

	/** The by region. */
	private Boolean byTU, byRadius, byRegion;

	/** The tsn version. */
	private String tsnVersion;

	/** The region. */
	private String region;

	/** The polygon. */
	private JSONObject polygon = new JSONObject();

	/**
	 * Instantiates a new map controller.
	 *
	 * @param tsnversionID the tsn version ID
	 */
	public MapController(String tsnversionID) {
		featureCollection = new JSONObject();
		lon = new String("23.321737");
		lat = new String("42.678693");
		point = new String("POINT(23.321737 42.678693 )");
		tsnVersion = tsnversionID;
		byTU = true;
		byRadius = false;
		byRegion = false;
		request();

	}

	/**
	 * Instantiates a new map controller.
	 *
	 * @param latParam the lat param
	 * @param lonParam the lon param
	 * @param tsnversionID the tsnversion ID
	 */
	public MapController(String latParam, String lonParam, String tsnversionID) {
		featureCollection = new JSONObject();
		lon = new String(lonParam);
		lat = new String(latParam);
		point = new String("POINT(" + lonParam + " " + latParam + ")");
		tsnVersion = tsnversionID;
		byTU = true;
		byRadius = false;
		byRegion = false;
		request();

	}

	/**
	 * Instantiates a new map controller.
	 *
	 * @param latParam the lat param
	 * @param lonParam the lon param
	 * @param rayonParam the rayon param
	 * @param tsnversionID the tsnversion ID
	 */
	public MapController(String latParam, String lonParam, String rayonParam, String tsnversionID) {
		featureCollection = new JSONObject();
		lon = new String(lonParam);
		lat = new String(latParam);
		rayon = new String(rayonParam);
		point = new String("POINT(" + lonParam + " " + latParam + ")");
		tsnVersion = tsnversionID;
		byTU = true;
		byRadius = true;
		byRegion = false;
		request();

	}

	/**
	 * Instantiates a new map controller.
	 *
	 * @param latParam the lat param
	 * @param lonParam the lon param
	 * @param rayonParam the rayon param
	 * @param tu the tu
	 * @param tsnversionID the tsnversion ID
	 * @param radius the radius
	 */
	public MapController(String latParam, String lonParam, String rayonParam, String tu, String tsnversionID,
			boolean radius) {
		featureCollection = new JSONObject();
		lon = new String(lonParam);
		lat = new String(latParam);
		if (radius) {
			rayon = new String(rayonParam);
		}
		point = new String("POINT(" + lonParam + " " + latParam + ")");
		territorialUnit = new String(tu);
		tsnVersion = tsnversionID;
		byTU = false;
		byRadius = radius;
		byRegion = false;
		request();

	}

	/**
	 * Instantiates a new map controller.
	 *
	 * @param region the region
	 * @param tsnversionID the tsnversion ID
	 */
	public MapController(String region, String tsnversionID) {
		this.region = region;
		tsnVersion = tsnversionID;
		byRegion = true;
		byTU = true;
		request();
	}

	/**
	 * Instantiates a new map controller.
	 *
	 * @param region the region
	 * @param tsnversionID the tsnversion ID
	 * @param phonem the phonem
	 * @param api the api
	 */
	public MapController(String region, String tsnversionID, boolean phonem, String api) {
		this.region = region;
		tsnVersion = tsnversionID;
		territorialUnit = new String(api);
		byRegion = true;
		byTU = false;
		request();
	}

	/**
	 * Construct and Draw on map a JSON object, corresponding to the request parameter entered by the user from the TsnRDF.jsp GUI.
	 * 
	 * EXAMPLE of Build JSON : {"geometry":{"coordinates":[3.119,46.792],"type":"Point"},"type":"Feature","properties": {"phonem":"tøpi:","dbp":"http:\/\/fr.dbpedia.org\/resource\/Saint-Pierre-le-Moûtier","name":"Marcigny","id":"1","category":0}}
	 */
	public void request() {
		// HTTPRepository repository = new HTTPRepository("http://localhost:7200/repositories/change-nuts");
		HTTPRepository repository = new HTTPRepository("http://clash.imag.fr:7200/repositories/nuts_4326");
		RepositoryConnection connection = repository.getConnection();
		try {
			// Preparing a SELECT query for later evaluation
			String queryString = constructQuery();
			System.out.println(queryString);
			// init the geoJSON collection
			featureCollection.put("type", "FeatureCollection");
			JSONArray featureList = new JSONArray();
			TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
			TupleQueryResult tupleQueryResult = tupleQuery.evaluate();
			while (tupleQueryResult.hasNext()) {
				// Each result is represented by a BindingSet, which corresponds to a result row
				BindingSet bindingSet = tupleQueryResult.next();
				JSONObject feature = new JSONObject();
				JSONObject prop;
				prop = new JSONObject();

				feature.put("type", "Feature");
				// Each BindingSet contains one or more Bindings
				for (Binding binding : bindingSet) {
					// Each Binding contains the variable name and the value for this result row
					String name = binding.getName();
					Value value = binding.getValue();

					if (name.equals("geom")) {
						System.out.println("Find GEOM !");
						StringBuilder multipolygonJSON = new StringBuilder("[[");
						String inputGeom = value.stringValue();
						System.out.println("inputGeom " + inputGeom);
						Pattern pattern = Pattern.compile("\\(\\(\\((.*?)\\)\\)\\)");
						Matcher matcher = pattern.matcher(inputGeom);
						if (matcher.find()) {
							inputGeom = matcher.group(1);
						} else {
							System.out.println("Substring GEOM not found !!");
						}

						// get a table of each polygon composing the multipolygon
						System.out.println("input geom after matcher " + inputGeom);
						String[] polygons = inputGeom.split("[\\(]+|[\\)]+");
						// get list of point of one polygon
						for (int i = 0; i < polygons.length;) {
							String latlonglist = polygons[i];
							System.out.println("LatLongList one polygon " + latlonglist);
							String[] latlongpairs = latlonglist.split(", ");
							multipolygonJSON.append("[");
							/*
							 * for (String onelatlong :latlongpairs){ JSONArray latlongpaireJson = new JSONArray(); String[] parts = onelatlong.split(" "); System.out.println("lat "+parts[0].toString()); System.out.println("long "+parts[1].toString());
							 * latlongpaireJson.add(Double.parseDouble(parts[0].toString()));//lat latlongpaireJson.add(Double.parseDouble(parts[1].toString()));//long multipolygonJSON.append(latlongpaireJson); } multipolygonJSON.append("]");
							 */

							for (int j = 0; j < latlongpairs.length;) {
								String onelatlong = latlongpairs[j];
								JSONArray latlongpaireJson = new JSONArray();
								String[] parts = onelatlong.split(" ");
								System.out.println("lat " + parts[0].toString());
								System.out.println("long " + parts[1].toString());
								latlongpaireJson.add(Double.parseDouble(parts[0].toString()));// lat
								latlongpaireJson.add(Double.parseDouble(parts[1].toString()));// long
								multipolygonJSON.append(latlongpaireJson);
								if (++j == latlongpairs.length) {
									break;
								} else {
									multipolygonJSON.append(",");
								}
							}
							multipolygonJSON.append("]");
							if (++i == polygons.length) {
								break;
							} else {
								multipolygonJSON.append(",");
							}
						}
						multipolygonJSON.append("]]");

						// TEST []
						Pattern patternEmptyGeom = Pattern.compile("\\[\\]");
						Matcher matcherEmptyGeom = patternEmptyGeom.matcher(multipolygonJSON.toString());
						String multipolygonJSONToString = multipolygonJSON.toString();
						if (matcherEmptyGeom.find()) {
							System.out.println("Find empty geom");
							multipolygonJSONToString = multipolygonJSONToString.replace("[],", "");
						} else {
							System.out.println("No empty GEom !!");
						}

						JSONObject multipolygon = new JSONObject();
						multipolygon.put("type", "MultiPolygon");
						System.out.println("Final Geom :" + multipolygonJSONToString);
						multipolygonJSON = new StringBuilder(multipolygonJSONToString);
						multipolygon.put("coordinates", multipolygonJSON);
						feature.put("geometry", multipolygon);

					} else if (name.equals("name")) {
						prop.put("name", value.stringValue());
					} else if (name.equals("code")) {
						System.out.println("code --> " + value.stringValue());
						prop.put("code", value.stringValue());

					} else if (name.equals("level")) {
						prop.put("level", value.stringValue());

					} else if (name.equals("tsn_acronym")) {
						prop.put("version", value.stringValue());
					}
					/*
					 * JSONObject styleJson = new JSONObject(); styleJson.put("color", "black"); styleJson.put("opacity", 1); styleJson.put("fillColor", "white"); styleJson.put("fillOpacity", 1); prop.put("style",styleJson );
					 */
				}
				feature.put("properties", prop);
				featureList.add(feature);
			}
			featureCollection.put("features", featureList);
			if (!featureList.isEmpty()) {
				polygon = featureCollection;
				System.out.println("GeoJson: " + polygon.toJSONString());
			}
			// Bindings can also be accessed explicitly by variable name
			// Binding binding = bindingSet.getBinding("x");

			// Once we are done with a particular result we need to close it
			tupleQueryResult.close();

			// Doing more with the same connection object
			// ...
		} catch (RepositoryException | MalformedQueryException | QueryEvaluationException | NumberFormatException e) {
			e.printStackTrace();
			throw e;
		} finally {
			// It is best to close the connection in a finally block
			connection.close();
		}
	}

	/**
	 * Construct the Query to get the Feature to draw on map.
	 * 
	 * Example : PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX geosparql: <http://www.opengis.net/ont/geosparql#> PREFIX : <http://purl.org/fr/eclat/ontology#> PREFIX owl:
	 * <http://www.w3.org/2002/07/owl#> PREFIX dct: <http://purl.org/dc/terms/> PREFIX geof: <http://www.opengis.net/def/function/geosparql/> PREFIX uom: <http://www.opengis.net/def/uom/OGC/1.0/> PREFIX SKOS: <http://www.w3.org/2004/02/skos/core#> PREFIX
	 * rcs: <http://purl.org/fr/eclat/resource/> PREFIX foaf: <http://xmlns.com/foaf/0.1/> SELECT * WHERE { ?pte a :SurveyPoint; dct:identifier ?id; dct:title ?name; :isSurveyPointOf ?ei; geosparql:hasGeometry [ geosparql:asWKT ?sp2; ]. ?ei :hasResponse
	 * ?rep. ?rep :phoneticRepresentationAPI ?phonem. ?ei :isAssociatedTo ?intitule. ?intitule :hasMap <http://purl.org/fr/eclat/resource/carte_ALF_1319>. OPTIONAL{?pte foaf:based_near ?dbp.} OPTIONAL{?rep :hasLemme ?lemme.} }
	 * 
	 *
	 * @return the Query as a String
	 */
	private String constructQuery() {

		String QUERY = new StringBuilder("PREFIX tsn: <http://purl.org/net/tsn#> ")
				.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ")
				.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ")
				.append("PREFIX geosparql: <http://www.opengis.net/ont/geosparql#> ")
				.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ")
				.append("PREFIX dct: <http://purl.org/dc/terms/>	 ")
				.append("select * where { ")
				.append("?TU a tsn:UnitVersion ; ")
				.append("tsn:hasIdentifier ?code ; ")
				.append("tsn:hasName ?name ; ")

				.append("tsn:belongsToLevel ?level; ")
				.append("geosparql:hasGeometry [ geosparql:asWKT ?geom; ]. ")

				.append("?level tsn:hasIdentifier \"NUTS_version_1999_level_0\"^^xsd:string  ; ")
				.append("tsn:belongsToNomenclatureVersion ?tsn_version . ")

				.append("?tsn_version tsn:hasIdentifier \"")
				.append(tsnVersion)
				.append("\"^^xsd:string  ; ")
				.append("tsn:hasAcronym ?tsn_acronym .}")
				.toString();
		return QUERY;
	}

	/**
	 * Gets the feature collection.
	 *
	 * @return the feature collection
	 */
	public JSONObject getFeatureCollection() {
		return featureCollection;
	}

	/**
	 * Gets the lat.
	 *
	 * @return the lat
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * Sets the lat.
	 *
	 * @param lat the new lat
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * Gets the lon.
	 *
	 * @return the lon
	 */
	public String getLon() {
		return lon;
	}

	/**
	 * Sets the lon.
	 *
	 * @param lon the new lon
	 */
	public void setLon(String lon) {
		this.lon = lon;
	}

	/**
	 * Gets the point.
	 *
	 * @return the point
	 */
	public String getPoint() {
		return point;
	}

	/**
	 * Sets the point.
	 *
	 * @param point the new point
	 */
	public void setPoint(String point) {
		this.point = point;
	}

	/**
	 * Gets the rayon.
	 *
	 * @return the rayon
	 */
	public String getRayon() {
		if (rayon == null) {
			return "0";
		}
		return rayon;
	}

	/**
	 * Sets the rayon.
	 *
	 * @param rayon the new rayon
	 */
	public void setRayon(String rayon) {
		this.rayon = rayon;
	}

	/**
	 * Gets the phoneme.
	 *
	 * @return the phoneme
	 */
	public String getPhoneme() {
		return territorialUnit;
	}

	/**
	 * Sets the phoneme.
	 *
	 * @param phoneme the new phoneme
	 */
	public void setPhoneme(String phoneme) {
		this.territorialUnit = phoneme;
	}

	/**
	 * Gets the by point.
	 *
	 * @return the by point
	 */
	public Boolean getByPoint() {
		return byTU;
	}

	/**
	 * Sets the by point.
	 *
	 * @param byPoint the new by point
	 */
	public void setByPoint(Boolean byPoint) {
		this.byTU = byPoint;
	}

	/**
	 * Gets the by radius.
	 *
	 * @return the by radius
	 */
	public Boolean getByRadius() {
		return byRadius;
	}

	/**
	 * Sets the by radius.
	 *
	 * @param byRadius the new by radius
	 */
	public void setByRadius(Boolean byRadius) {
		this.byRadius = byRadius;
	}

	/**
	 * Gets the num carte.
	 *
	 * @return the num carte
	 */
	public String getNumCarte() {
		return tsnVersion;
	}

	/**
	 * Sets the num carte.
	 *
	 * @param numCarte the new num carte
	 */
	public void setNumCarte(String numCarte) {
		this.tsnVersion = numCarte;
	}

	/**
	 * Gets the region.
	 *
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * Sets the region.
	 *
	 * @param region the new region
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * Gets the by region.
	 *
	 * @return the by region
	 */
	public Boolean getByRegion() {
		return byRegion;
	}

	/**
	 * Sets the by region.
	 *
	 * @param byRegion the new by region
	 */
	public void setByRegion(Boolean byRegion) {
		this.byRegion = byRegion;
	}

	/**
	 * Gets the polygon.
	 *
	 * @return the polygon
	 */
	public JSONObject getPolygon() {
		return polygon;
	}

	/**
	 * Sets the polygon.
	 *
	 * @param polygon the new polygon
	 */
	public void setPolygon(JSONObject polygon) {
		this.polygon = polygon;
	}

}
