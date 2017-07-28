package fr.imag.steamer.tsn.tsnrdf.beans;

import java.util.Map;
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

public class RDFTest {

    private JSONObject featureCollection = new JSONObject();

    private String lat;
    private String lon;
    private String rayon;
    private String point;
    private String phoneme;
    private Boolean byPoint, byRadius, byRegion;
    private String numCarte;
    private String region;
    private JSONObject polygon = new JSONObject();

    /**
     *
     * @param nc le numero de carte
     */
    public RDFTest(String nc, Map<String, ResponseCategory> categoriesMap) {
        featureCollection = new JSONObject();
        lon = new String("23.321737");
        lat = new String("42.678693");

        point = new String("POINT(23.321737 42.678693 )");
        numCarte = nc;
        byPoint = true;
        byRadius = false;
        byRegion = false;

        request(categoriesMap);
    }

    /**
     * @param latParam la latitude
     * @param lonParam la longitude
     * @param nc le numero de carte
     */
    public RDFTest(String latParam, String lonParam, String nc, Map<String, ResponseCategory> categoriesMap) {
        featureCollection = new JSONObject();
        lon = new String(lonParam);
        lat = new String(latParam);
        point = new String("POINT(" + lonParam + " " + latParam + ")");
        numCarte = nc;

        byPoint = true;
        byRadius = false;
        byRegion = false;

        request(categoriesMap);
    }

    /**
     *
     * @param latParam
     * @param lonParam
     * @param rayonParam
     * @param nc
     */
    public RDFTest(String latParam, String lonParam, String rayonParam, String nc, Map<String, ResponseCategory> categoriesMap) {
        featureCollection = new JSONObject();
        lon = new String(lonParam);
        lat = new String(latParam);
        rayon = new String(rayonParam);
        point = new String("POINT(" + lonParam + " " + latParam + ")");
        numCarte = nc;

        byPoint = true;
        byRadius = true;
        byRegion = false;

        request(categoriesMap);
    }

    /**
     *
     * @param latParam latitude
     * @param lonParam longitude
     * @param rayonParam rayon périmètre de recherhce
     * @param phonem le phoneme
     * @param nc le numéro de carte
     * @param radius indique si on utilise ou non le rayon
     */
    public RDFTest(String latParam, String lonParam, String rayonParam, String phonem, String nc, boolean radius, Map<String, ResponseCategory> categoriesMap) {
        featureCollection = new JSONObject();
        lon = new String(lonParam);
        lat = new String(latParam);
        if (radius) {
            rayon = new String(rayonParam);
        }

        point = new String("POINT(" + lonParam + " " + latParam + ")");
        phoneme = new String(phonem);
        numCarte = nc;

        byPoint = false;
        byRadius = radius;
        byRegion = false;

        request(categoriesMap);
    }

    /**
     *
     * @param region
     * @param nc
     */
    public RDFTest(String region, String nc, Map<String, ResponseCategory> categoriesMap) {
        this.region = region;
        numCarte = nc;

        byRegion = true;
        byPoint = true;
        request(categoriesMap);

    }

    /**
     *
     * @param region
     * @param nc
     * @param phonem
     * @param api
     */
    public RDFTest(String region, String nc, boolean phonem, String api, Map<String, ResponseCategory> categoriesMap) {
        this.region = region;
        numCarte = nc;
        phoneme = new String(api);

        byRegion = true;
        byPoint = false;
        request(categoriesMap);

    }

    /**
     *
     * @return
     */
    private String constructQuery() {
        if (!byPoint) {
            phoneme = phoneme.replace("?", "\\\\?");

        }

        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n";
        queryString += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n";
        queryString += "PREFIX geosparql: <http://www.opengis.net/ont/geosparql#>\n";
        queryString += "PREFIX : <http://purl.org/fr/eclat/ontology#>\n";
        queryString += "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n";
        queryString += "PREFIX dct: <http://purl.org/dc/terms/>	\n";
        queryString += "PREFIX geof: <http://www.opengis.net/def/function/geosparql/>\n";
        queryString += "PREFIX uom: <http://www.opengis.net/def/uom/OGC/1.0/>\n";
        queryString += "PREFIX SKOS: <http://www.w3.org/2004/02/skos/core#>\n";
        queryString += "PREFIX  rcs: <http://purl.org/fr/eclat/resource/>	\n";
        queryString += "PREFIX  foaf: <http://xmlns.com/foaf/0.1/>	\n";

        queryString += "SELECT * \n";
        queryString += "WHERE { \n";
        if (!byRegion) {
            if (!byPoint) { ///recherche par phoneme
                queryString += "  ?rep :phoneticRepresentationAPI ?phonem.\n";
                queryString += "  ?rep :isResponseOf ?ei.  \n";
                queryString += "  ?ei :hasSurveyPoint ?pte.  \n";
                queryString += "?pte dct:title ?name;\n";
                queryString += "dct:identifier ?id;\n";
                queryString += "geosparql:hasGeometry [\n";
                queryString += "geosparql:asWKT ?sp2; ].\n";
                queryString += "FILTER regex(?phonem, \"" + phoneme + "\")\n";
            } else {  //recherche par point
                queryString += "?pte a :SurveyPoint;\n";
                queryString += "dct:identifier ?id;\n";
                queryString += "dct:title ?name;\n";
                queryString += ":isSurveyPointOf ?ei;\n";
                queryString += "geosparql:hasGeometry [\n";
                queryString += "geosparql:asWKT ?sp2; ].\n";
                queryString += "?ei :hasResponse ?rep.\n";
                queryString += "?rep :phoneticRepresentationAPI ?phonem.\n";

            }
            if (byRadius) {
                queryString += "BIND ((geof:distance('" + point + "',?sp2, uom:metre)) as ?distance)\n";
                queryString += "filter(?distance<" + rayon + ")\n";
            }
            // queryString += "  ?intitule :hasTheme ?theme.  \n";
            //queryString += "  ?theme SKOS:prefLabel ?label.  \n";

        } else {
            queryString += "?pte a :SurveyPoint;\n";
            queryString += "dct:identifier ?id;\n";
            queryString += "dct:title ?name;\n";
            queryString += ":isSurveyPointOf ?ei;\n";
            queryString += "geosparql:hasGeometry [\n";
            queryString += "geosparql:asWKT ?sp2; ].\n";
            queryString += "?ei :hasResponse ?rep.\n";
            queryString += "?rep :phoneticRepresentationAPI ?phonem.\n";

            queryString += "rcs:" + region + " geosparql:hasGeometry [\n";
            queryString += "geosparql:asWKT ?poly; ].\n";
            queryString += "filter(geof:sfWithin(?sp2, ?poly))\n";
            if (!byPoint) {
                queryString += "FILTER regex(?phonem, \"" + phoneme + "\")\n";
            }

        }
        queryString += "  ?ei :isAssociatedTo ?intitule.  \n";
        queryString += "  ?intitule :hasMap <http://purl.org/fr/eclat/resource/carte_ALF_" + numCarte + ">.  \n";

        queryString += "OPTIONAL{?pte foaf:based_near ?dbp.}\n";
        queryString += "OPTIONAL{?rep :hasLemme ?lemme.}\n";
        queryString += "} \n";
        return queryString;
    }

    public void request(Map<String, ResponseCategory> categoriesMap) {
        //HTTPRepository repository = new HTTPRepository("http://localhost:7200/repositories/Eclat3");
//    	HTTPRepository repository = new HTTPRepository("http://129.88.46.88:7200/repositories/Eclat3");
    	HTTPRepository repository = new HTTPRepository("http://localhost:7200/repositories/change-nuts");
        RepositoryConnection connection = repository.getConnection();
        try {
            // Preparing a SELECT query for later evaluation
            String queryString = constructQuery();
            System.out.println(queryString);
            //init the geoJSON collection
            featureCollection.put("type", "featureCollection");
            JSONArray featureList = new JSONArray();

            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            // Evaluating a prepared query returns an iterator-like object
            // that can be traversed with the methods hasNext() and next()
            TupleQueryResult tupleQueryResult = tupleQuery.evaluate();
            while (tupleQueryResult.hasNext()) {
                // Each result is represented by a BindingSet, which corresponds to a result row
                BindingSet bindingSet = tupleQueryResult.next();

                JSONObject feature = new JSONObject();
                JSONObject prop;
                prop = new JSONObject();;
                feature.put("type", "Feature");
                // Each BindingSet contains one or more Bindings
                for (Binding binding : bindingSet) {
                    // Each Binding contains the variable name and the value for this result row
                    String name = binding.getName();
                    Value value = binding.getValue();

                    if (name.equals("sp2")) {
                        String[] temp = value.stringValue().split("\\(|\\)");
                        temp = temp[1].split(" ");
                        JSONArray coord = new JSONArray();
                        coord.add(Double.parseDouble(temp[0]));
                        coord.add(Double.parseDouble(temp[1]));
                        JSONObject point = new JSONObject();
                        point.put("type", "Point");
                        point.put("coordinates", coord);
                        feature.put("geometry", point);
                    } else if (name.equals("name")) {
                        prop.put("name", value.stringValue());
                    } else if (name.equals("phonem")) {
                        System.out.println("Phoneme --> " + value.stringValue() + "category  " + categoriesMap.get(value.stringValue()).getRank());
                        prop.put("phonem", value.stringValue());
                        prop.put("category", categoriesMap.get(value.stringValue()).getRank());
                    } else if (name.equals("dbp")) {
                        prop.put("dbp", value.stringValue());

                    } else if (name.equals("lemme")) {
                        prop.put("lemme", value.stringValue());

                    } else if (name.equals("id")) {
                        prop.put("id", value.stringValue());
                    } else if (name.equals("label")) {
                        //System.out.println(name +" = "+value);
                    } else if (name.equals("poly")) {
                        polygon.put("polygon", value.stringValue());
                    }
                }
                feature.put("properties", prop);
                featureList.add(feature);
            }
            featureCollection.put("features", featureList);
            if (!featureList.isEmpty()) {
                polygon.put("featureCollection", featureCollection);
            }
            // Bindings can also be accessed explicitly by variable name
            //Binding binding = bindingSet.getBinding("x");

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

    public JSONObject getFeatureCollection() {
        return featureCollection;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getRayon() {
        if (rayon == null) {
            return "0";
        }
        return rayon;
    }

    public void setRayon(String rayon) {
        this.rayon = rayon;
    }

    public String getPhoneme() {
        return phoneme;
    }

    public void setPhoneme(String phoneme) {
        this.phoneme = phoneme;
    }

    public Boolean getByPoint() {
        return byPoint;
    }

    public void setByPoint(Boolean byPoint) {
        this.byPoint = byPoint;
    }

    public Boolean getByRadius() {
        return byRadius;
    }

    public void setByRadius(Boolean byRadius) {
        this.byRadius = byRadius;
    }

    public String getNumCarte() {
        return numCarte;
    }

    public void setNumCarte(String numCarte) {
        this.numCarte = numCarte;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getByRegion() {
        return byRegion;
    }

    public void setByRegion(Boolean byRegion) {
        this.byRegion = byRegion;
    }

    public JSONObject getPolygon() {
        return polygon;
    }

    public void setPolygon(JSONObject polygon) {
        this.polygon = polygon;
    }

}
