
//--------------------------------Map script--------------------------------//
var categoriesColors = ['#ffffcc', '#ffeda0', '#fed976', '#feb24c', '#fd8d3c',
    '#fc4e2a', '#e31a1c', '#bd0026', '#800026'];
var selectPointALF = true;
var geoLayer = null;
var searchLayer = null;
var circle = null;
var clickedFeature = null;
var globalFeatureCollection;

// Extend the Default marker class
var RedIcon = L.Icon.Default.extend({
    options: {
        iconUrl: 'marker-icon-red.png'
    }
});

var redIcon = new RedIcon();
var marker = null;

var mbAttr = 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, '
        + '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, '
        + 'Imagery © <a href="http://mapbox.com">Mapbox</a>', mbUrl = 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';
var grayscale = L.tileLayer(mbUrl, {
    id: 'mapbox.light',
    attribution: mbAttr
}), streets = L.tileLayer(mbUrl, {
    id: 'mapbox.streets',
    attribution: mbAttr
});
var osm = L
        .tileLayer(
                'http://{s}.tile.openstreetmap.fr/osmfr/{z}/{x}/{y}.png',
                {
                    maxZoom: 20,
                    attribution: '&copy; Openstreetmap France | &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                });

var mymap = null;
var geoJson = null;
var geojsonMarkerOptions = {
    radius: 5,
    fillColor: "#ff7800",
    color: "#000",
    weight: 1,
    opacity: 1,
    fillOpacity: 0.8
};
var geojsonSelectedMarkerOptions = {
    radius: 5,
    fillColor: "#ff0000",
    color: "#000",
    weight: 1,
    opacity: 1,
    fillOpacity: 0.8
};
var geojsonMarker = {
    radius: 6,
    fillColor: "#00ff00",
    color: "#000",
    weight: 1,
    opacity: 1,
    fillOpacity: 0.8
};
// /---- new marker TODO
var features = [];

function onEachFeature(feature, layer) {
    layer
            .on(
                    'click',
                    function (e) {
                        var legendsize = $('.categorie').length;
                        var catIndex;
                        if (selectPointALF) {
                            document.getElementById("api").setAttribute(
                                    'value', feature.properties.phonem);
                            document.getElementById('lat').setAttribute(
                                    'value', feature.geometry.coordinates[1]);
                            document.getElementById('lon').setAttribute(
                                    'value', feature.geometry.coordinates[0]);
                            marker.setLatLng(feature.geometry.coordinates);
                            document.getElementById('info-lon').innerHTML = feature.geometry.coordinates[0];
                            document.getElementById('info-lat').innerHTML = feature.geometry.coordinates[1];
                            document.getElementById("info-api").innerHTML = feature.properties.phonem;
                            document.getElementById("info-name").innerHTML = feature.properties.name;
                            document.getElementById("info-id").innerHTML = feature.properties.id;
                            if (feature.properties.lemme)
                                document.getElementById("info-lemme").innerHTML = feature.properties.lemme;
                            else
                                document.getElementById("info-lemme").innerHTML = "";
                            if (feature.properties.dbp)
                                document.getElementById("info-link").innerHTML = "<a href=\""
                                        + feature.properties.dbp
                                        + "\" target='_blank'>"
                                        + feature.properties.dbp + "</a>";
                            else
                                document.getElementById("info-link").innerHTML = "";
                            if (clickedFeature != null) {
                                clickedFeature.setStyle(geojsonSelectedMarkerOptions);
                                catIndex = (clickedFeature.feature.properties.category < legendsize - 1) ? clickedFeature.feature.properties.category : legendsize - 1;
                                $("#dd" + catIndex).removeClass("selectedCategorie");
                            }
                            geojsonSelectedMarkerOptions.fillColor = e.target.options.fillColor;

                            catIndex = (e.target.feature.properties.category < legendsize - 1) ? e.target.feature.properties.category : legendsize - 1;
                            $("#dd" + catIndex).addClass("selectedCategorie");
                            e.target.setStyle(geojsonMarker);
                            clickedFeature = e.target;
                        }
                    });
}

// ////region drawing

/**
 * charge la géométrie d'un région et la dessine
 * @param {type} region l'identifiant (san sprefix de la region
 * @returns {undefined}
 */
function loadAndDrawRegion(region) {
    $.get('getgeom',
            "region=" + region,
            function (data) {
                var i;
                var wkt = new Wkt.Wkt();
                wkt.read(data.geom);
                obj = wkt.toObject(mymap.defaults);
                if (Wkt.isArray(obj)) { // Distinguish multigeometries (Arrays) from objects
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
            },
            'json');

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
    if ($("#ch-rayon").is(":checked") && $("#spatiale").is(":checked") && document.getElementById('point').checked) {
        circle = L.circle([$("#lat").val(), $("#lon").val()], {
            color: 'red',
            fillOpacity: 0.1,
            radius: $("#rayon").val()
        }).addTo(mymap);
    }

    if (geoLayer != null)
        mymap.removeLayer(geoLayer);
    document.getElementById('nbPoint').innerHTML = geojson.features.length;
    geoLayer = L
            .geoJSON(
                    geojson,
                    {
                        onEachFeature: onEachFeature,
                        pointToLayer: function (feature, latlng) {
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
    if ($("#ch-rayon").is(":checked") && $("#spatiale").is(":checked") && document.getElementById('point').checked) {
        circle = L.circle([$("#lat").val(), $("#lon").val()], {
            color: 'red',
            fillOpacity: 0.1,
            radius: $("#rayon").val()
        }).addTo(mymap);
    }

    if (searchLayer != null)
        mymap.removeLayer(searchLayer);

    searchLayer = L
            .geoJSON(
                    geojson,
                    {
                        onEachFeature: onEachFeature,
                        pointToLayer: function (feature, latlng) {
                            geojsonMarkerOptions.fillColor = "#00FFFF";
                            var marker = L.circleMarker(latlng,
                                    geojsonMarkerOptions);
                            return marker;
                        }
                    });
    searchLayer.addTo(mymap);
}

function initMap(lat, lon, featureCollection) {
    // var mymap = L.map('mapid').setView([${test.lat},${test.lon}], 6);
    // marker = L.marker([lat, lon], {icon: redIcon});
    marker = L.marker([lat, lon]);
    mymap = L.map('mapid', {
        center: [lat, lon],
        zoom: 4,
        layers: [grayscale]
    });
    geoJson = featureCollection;
    globalFeatureCollection = geoJson;
    var stateChangingButton = L.easyButton({
        states: [{
                stateName: 'selectPointALF', // name the state
                icon: 'fa-map-marker fa-2x', // and define its properties
                title: 'zoom to a forest', // like its title
                onClick: function (btn, map) { // and its callback
                    selectPointALF = false;
                    marker.addTo(mymap);
                    btn.state('selectPointOnMap'); // change state on click!
                }
            }, {
                stateName: 'selectPointOnMap',
                icon: 'fa-hand-pointer-o fa-2x',
                title: 'zoom to a school',
                onClick: function (btn, map) {
                    selectPointALF = true;
                    mymap.removeLayer(marker);
                    btn.state('selectPointALF');
                }
            }]
    });
    stateChangingButton.addTo(mymap);
    // ----end button

    createDots(geoJson);
    mymap.on('click', function (e) {
        if (!selectPointALF) {
            document.getElementById('lat').setAttribute('value', e.latlng.lat);
            document.getElementById('lon').setAttribute('value', e.latlng.lng);
            marker.setLatLng(e.latlng);
        }
    });
    var baseLayers = {
        "Grayscale": grayscale,
        "Streets": streets,
        "OSM": osm
    };
    L.control.layers(baseLayers).addTo(mymap);
    /*
     * L.tileLayer('http://{s}.tile.openstreetmap.fr/osmfr/{z}/{x}/{y}.png', {
     * maxZoom: 20, attribution: '&copy; Openstreetmap France | &copy; <a
     * href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
     * }).addTo(mymap);
     */
}

/**
 * Recréée les gestinnaire d'évenement pour les checkboxes de la légende.
 * @returns {undefined}
 */
function setCatChange() {
    var catNum = 0;
    for (catNum = 0; catNum <= 9; catNum++) {    // TODO faire en sorte que cela marche même si moins de 9 catégories
        $("#cat" + catNum).change(function () {
            createDots(globalFeatureCollection);
        });

    }
}

////-----------------------------------AJAX---------------------------------------//

function chercher() {

    var donnees = $("#formulaire").serialize(); // On créer une variable content le formulaire sérialisé
    clearRegion();
    if ($("#spatiale").prop('checked') && $('input[name=select]:checked', '#formulaire').val() === "region") {
        loadAndDrawRegion($("#SelectRegion").val());
    }
    $.get(
            'ajaxtest',
            donnees,
            function (data) {
                console.log("data");
                console.log(JSON.stringify(data));
                createSearchLayer(data);
            },
            'json'
            );

}



// Shorthand for $( document ).ready()
$(function () {
    setCatChange();

    $("#resetSearch").click(function (e) {
        e.preventDefault();
        $("#spatiale").prop('checked', false);
        $("#phoneme").prop('checked', false);
        $("#ch-rayon").prop('checked', false);
        //clickedFeature=null;
        if (searchLayer !== null) {
            mymap.removeLayer(searchLayer);
        }
        clearRegion();
        if (circle != null) {
            mymap.removeLayer(circle);
        }
        $("#chercher").prop('disabled', true);
    });

    $("#chercher").click(function () {
        chercher();
    });

    $("#catAll").change(function () {

        $(".categorie").prop("checked", $("#catAll").prop('checked'));
        createDots(globalFeatureCollection);
        if (searchLayer != null)
            searchLayer.bringToFront();
    });

    $(".categorie").change(function () {
        if ($(".categorie:checked").length == 9)
            $("#catAll").prop("checked", true);
        else
            $("#catAll").prop("checked", false);
        if (searchLayer != null)
            searchLayer.bringToFront();

    });

    $("#selectCarte").change(function () {
        if (searchLayer !== null) {
            mymap.removeLayer(searchLayer);
        }
        console.log("log1");
        // 1er appel ajax pour récupérer la légende
        $.get("refreshCategoriesLegend?carte=" + $("#selectCarte").val(), function (data, status) {
             // m.a.j. de la légende pour la nouvelle carte
            $("#legendList").html(data);
            setCatChange();
            // 2ème appel ajax pour obtenir les points d'enquête
            $.get(
                    'ajaxtest',
                    "carte=" + $("#selectCarte").val() + "&newCarte=true",
                    function (data) {
                        clearRegion();
                        createDots(data);
                        globalFeatureCollection = data;
                        if ($("#selectCarte").val() === "227") {
                            document.getElementById('link').innerHTML = '<a href="http://cartodialect.imag.fr/cartoDialect/seadragon.jsp?carte=CarteALF0227&width=6192&height=7308" target="_blank">ALF 227 Champignon</a>';
                        } else
                        {
                            document.getElementById('link').innerHTML = '<a href="http://cartodialect.imag.fr/cartoDialect/seadragon.jsp?carte=CarteALF1319&width=6192&height=7308" target="_blank">ALF 1319 Toupie</a>';
                        }
                        document.getElementById('info-lon').innerHTML = "";
                        document.getElementById('info-lat').innerHTML = "";
                        document.getElementById("info-api").innerHTML = "";
                        document.getElementById("info-name").innerHTML = "";
                        document.getElementById("info-id").innerHTML = "";
                        document.getElementById("info-lemme").innerHTML = "";
                        document.getElementById("info-link").innerHTML = "";
                    },
                    'json'
                    );
        });

    }
    );
    console.log("log2");
});
