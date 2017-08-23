//--------------------------------Map script--------------------------------//

var geoLayer = null;
var searchLayer = null;
var polygonLayer = null;
var globalFeatureCollection;

var marker = null;

var mbAttr = 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, '
		+ '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, '
		+ 'Imagery © <a href="http://mapbox.com">Mapbox</a>', mbUrl = 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';
var grayscale = L.tileLayer(mbUrl, {
	id : 'mapbox.light',
	attribution : mbAttr
}), streets = L.tileLayer(mbUrl, {
	id : 'mapbox.streets',
	attribution : mbAttr
});
var osm = L
		.tileLayer(
				'http://{s}.tile.openstreetmap.fr/osmfr/{z}/{x}/{y}.png',
				{
					maxZoom : 20,
					attribution : '&copy; Openstreetmap France | &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
				});

var mymap = null;
var geoJson = null;

var features = [];

function initMap(lat, lon, featureCollection) {
	marker = L.marker([ lat, lon ]);
	mymap = L.map('mapid', {
		center : [ lat, lon ],
		zoom : 4,
		layers : [ streets ]
	});
	geoJson = featureCollection;
	globalFeatureCollection = geoJson;

	var baseLayers = {
		"Streets" : streets,
		"Grayscale" : grayscale,
		"OSM" : osm
	};

	var myStyle = {
		"color" : "grey",
		"weight" : 3,
		"opacity" : 0.5
	};

	polygonLayer = L.geoJSON(geoJson, {
		onEachFeature : onEachFeature,
		style : myStyle
	}).addTo(mymap);

	polygonLayer.addData(geoJson);

	var baseLayers = {
		"OSM" : osm,
		"Grayscale" : grayscale,
		"Streets" : streets

	};

	L.control.layers(baseLayers).addTo(mymap);
}

function styleFeature(feature) {
	return {
		weight : 2.5,
		opacity : 1,
		color : getColour('blue'),
	};
}

function onEachFeature(feature, layer) {
	var popupContent = "<h1>Territorial Unit <b>" + feature.properties.code
			+ "</b></h1>" + "<p>Name: <b>" + feature.properties.name
			+ "</b></p>" + "<p>Level: <b>" + feature.properties.level
			+ "</b></p>" + "<p>Version: <b>" + feature.properties.version
			+ "</b></p>";

	if (feature.properties && feature.properties.popupContent) {
		popupContent += feature.properties.popupContent;
	}

	layer.bindPopup(popupContent);
	layer
			.on(
					'click',
					function(e) {
						document.getElementById('info-code').innerHTML = feature.properties.code;
						document.getElementById('info-name').innerHTML = feature.properties.name;
						document.getElementById("info-level").innerHTML = feature.properties.level;
						document.getElementById("info-version").innerHTML = feature.properties.version;
						clickedFeature = e.target;
					});
}

/**
 * charge la géométrie d'un région et la dessine
 * 
 * @param {type}
 *            region l'identifiant (san sprefix de la region
 * @returns {undefined}
 */
function loadAndDrawRegion(region) {
	$.get('getgeom', "region=" + region, function(data) {
		var i;
		var wkt = new Wkt.Wkt();
		wkt.read(data.geom);
		obj = wkt.toObject(mymap.defaults);
		if (Wkt.isArray(obj)) { // Distinguish multigeometries (Arrays) from
			// objects
			for (i in obj) {
				if (obj.hasOwnProperty(i) && !Wkt.isArray(obj[i])) {
					obj[i].addTo(mymap);
					features.push(obj[i]);
				}
			}
		} else {
			obj.addTo(mymap); // Add it to the map
			features.push(obj);
		}
	}, 'json');

}

function clearRegion() {
	for (i in this.features) {
		if (this.features.hasOwnProperty(i)) {
			mymap.removeLayer(this.features[i]);
		}
	}
	this.features.length = 0;
}

function createDots(geojson) {
	if (circle != null)
		mymap.removeLayer(circle);
	if ($("#ch-rayon").is(":checked") && $("#spatiale").is(":checked")
			&& document.getElementById('point').checked) {
		circle = L.circle([ $("#lat").val(), $("#lon").val() ], {
			color : 'red',
			fillOpacity : 0.1,
			radius : $("#rayon").val()
		}).addTo(mymap);
	}

	if (geoLayer != null)
		mymap.removeLayer(geoLayer);
	document.getElementById('nbPoint').innerHTML = geojson.features.length;
	geoLayer = L
			.geoJSON(
					geojson,
					{
						onEachFeature : onEachFeature,
						pointToLayer : function(feature, latlng) {
							var colindex = feature.properties.category;
							var category = (feature.properties.category >= categoriesColors.length - 1) ? categoriesColors.length - 1
									: feature.properties.category;
							if (colindex >= categoriesColors.length - 1) {
								colindex = 0;
							} else {
								colindex = categoriesColors.length - 1
										- colindex;
							}
							if (document.getElementById('cat' + category).checked) {
								// geojsonMarkerOptions.fillColor = '#' +
								// (Math.random() * 0xFFFFFF << 0).toString(16);
								// // pour une couleur au hazard
								geojsonMarkerOptions.fillColor = categoriesColors[colindex];
								var marker = L.circleMarker(latlng,
										geojsonMarkerOptions);
								return marker;
							}
						}
					});
	geoLayer.addTo(mymap);
}

function createSearchLayer(geojson) {
	if (circle != null)
		mymap.removeLayer(circle);
	if ($("#ch-rayon").is(":checked") && $("#spatiale").is(":checked")
			&& document.getElementById('point').checked) {
		circle = L.circle([ $("#lat").val(), $("#lon").val() ], {
			color : 'red',
			fillOpacity : 0.1,
			radius : $("#rayon").val()
		}).addTo(mymap);
	}

	if (searchLayer != null)
		mymap.removeLayer(searchLayer);

	searchLayer = L.geoJSON(geojson, {
		onEachFeature : onEachFeature,
		pointToLayer : function(feature, latlng) {
			geojsonMarkerOptions.fillColor = "#00FFFF";
			var marker = L.circleMarker(latlng, geojsonMarkerOptions);
			return marker;
		}
	});
	searchLayer.addTo(mymap);
}

/**
 * Recréée les gestinnaire d'évenement pour les checkboxes de la légende.
 * 
 * @returns {undefined}
 */
function setCatChange() {
	var catNum = 0;
	for (catNum = 0; catNum <= 9; catNum++) { // TODO faire en sorte que cela
		// marche même si moins de 9
		// catégories
		$("#cat" + catNum).change(function() {
			createDots(globalFeatureCollection);
		});

	}
}

// //-----------------------------------AJAX---------------------------------------//

function chercher() {

	var donnees = $("#formulaire").serialize(); // On créer une variable content
	// le formulaire sérialisé
	clearRegion();
	if ($("#spatiale").prop('checked')
			&& $('input[name=select]:checked', '#formulaire').val() === "region") {
		loadAndDrawRegion($("#SelectRegion").val());
	}
	$.get('ajaxtest', donnees, function(data) {
		console.log("data");
		console.log(JSON.stringify(data));
		createSearchLayer(data);
	}, 'json');

}

// Shorthand for $( document ).ready()
$(function() {
	setCatChange();

	$("#resetSearch").click(function(e) {
		e.preventDefault();
		$("#spatiale").prop('checked', false);
		$("#phoneme").prop('checked', false);
		$("#ch-rayon").prop('checked', false);
		// clickedFeature=null;
		if (searchLayer !== null) {
			mymap.removeLayer(searchLayer);
		}
		clearRegion();
		if (circle != null) {
			mymap.removeLayer(circle);
		}
		$("#chercher").prop('disabled', true);
	});

	$("#chercher").click(function() {
		chercher();
	});

	$("#catAll").change(function() {

		$(".categorie").prop("checked", $("#catAll").prop('checked'));
		createDots(globalFeatureCollection);
		if (searchLayer != null)
			searchLayer.bringToFront();
	});

	$(".categorie").change(function() {
		if ($(".categorie:checked").length == 9)
			$("#catAll").prop("checked", true);
		else
			$("#catAll").prop("checked", false);
		if (searchLayer != null)
			searchLayer.bringToFront();

	});

	$("#selectCarte")
			.change(
					function() {
						if (searchLayer !== null) {
							mymap.removeLayer(searchLayer);
						}
						$
								.get(
										'ajaxtest',
										"carte=" + $("#selectCarte").val()
												+ "&newCarte=true",
										function(data) {
											clearRegion();
											createDots(data);
											globalFeatureCollection = data;
											if ($("#selectCarte").val() === "227") {
												document.getElementById('link').innerHTML = '<a href="http://cartodialect.imag.fr/cartoDialect/seadragon.jsp?carte=CarteALF0227&width=6192&height=7308" target="_blank">ALF 227 Champignon</a>';
											} else {
												document.getElementById('link').innerHTML = '<a href="http://cartodialect.imag.fr/cartoDialect/seadragon.jsp?carte=CarteALF1319&width=6192&height=7308" target="_blank">ALF 1319 Toupie</a>';
											}
											document.getElementById('info-lon').innerHTML = "";
											document.getElementById('info-lat').innerHTML = "";
											document.getElementById("info-api").innerHTML = "";
											document
													.getElementById("info-name").innerHTML = "";
											document
													.getElementById("info-code").innerHTML = "";
											document
													.getElementById("info-lemme").innerHTML = "";
											document
													.getElementById("info-link").innerHTML = "";
										}, 'json');

					});
	console.log("log2");
});
