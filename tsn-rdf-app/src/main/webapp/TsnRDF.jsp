<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">

        <link rel="stylesheet" href="TsnRDF.css" />
        <link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css">

        <link rel="stylesheet" href="//unpkg.com/leaflet@1.0.3/dist/leaflet.css" />
        <script src="//unpkg.com/leaflet@1.0.3/dist/leaflet.js"></script>

        <script src="wicket.js"></script>
        <script src="wicket-leaflet.js"></script>

        <script src="bootstrap/js/jquery-3.2.1.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>

        <link rel="stylesheet" href="https://unpkg.com/leaflet-easybutton@2.0.0/src/easy-button.css">
        <script src="https://unpkg.com/leaflet-easybutton@2.0.0/src/easy-button.js"></script>

        <script src="./js/map.js" type="text/javascript"></script>
        <title>TSN-RDF 1.0.0</title>	
    </head>
    <body>
        <div class="container">
            <header class="page-header row">
                <div class="col-lg-12">
                    <div class="col-xs-3">
                        <h1>TSN-RDF</h1>
                        <small class="text-muted">Territorial Statistical Nomenclature RDF</small>
                    </div>
                    
                </div>
            </header>
            <form id="formulaire" >
                <div class="row" id ="map-div">
                    <div id="mapid" class="col-sm-8"></div>
                    <div class="col-sm-4"> 
                        <div class="row">
                            <div class="col-lg-12">
                                <dl class="dl-horizontal">
                                    <dt>Carte</dt>
                                    <dd>
                                        <select class="form-control" name="carte" id="selectCarte">
                                            <option value="1999">NUTS 1999</option>
                                            <option value="2003">NUTS 2003</option> 
                                            <option value="2006">NUTS 2006</option> 
                                            <option value="2010">NUTS 2010</option> 
                                            <input type="hidden" id="newCarte" name="newCarte" value="false" />
                                        </select>
                                    </dd>

                                </dl>
                                <h4>NUTS Territorial Unit Metadata</h4>
                                <dl class="dl-horizontal">
                                    <dt>Territorial Unit ID</dt>
                                    <dd id="info-id"></dd>
                                    <dt>Territorial Unit Name</dt>
                                    <dd id="info-name"></dd>
                                    <dt>Territorial Unit Level</dt>
                                    <dd id="info-level"></dd>
                                </dl>
                               
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-12">
                        <div class="col-sm-8"><!-- form initilale -->
                            <div class="form-group">
                                <legend>Search</legend>
                            </div>
                            <div class="panel-group">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h4 class="panel-title">
                                            <input type="checkbox" name="spatiale" id="spatiale" onclick="handleSearchType()"><a data-toggle="collapse" href="#collapse1" > Spatial</a>
                                        </h4>
                                    </div>
                                    <div id="collapse1" class="panel-collapse collapse">
                                        <div class="panel-body">
                                            <ul class="nav nav-tabs">
                                                <li class="active" ><a data-toggle="tab" href="#selectPoint" >Point</a></li>
                                                <li ><a data-toggle="tab" href="#selectRegion">Region</a></li>
                                            </ul>

                                            <div class="tab-content">						
                                                <div id="selectPoint" class="tab-pane fade in active">
                                                    <div class="row">
                                                        <div class="form-group">
                                                            <label for="lat" class="col-lg-2 control-label">Latitude</label>
                                                            <div class="col-lg-10">
                                                                <input type="text" id="lat" name="lat" value="${test.lat}"class="form-control" />
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="row">
                                                        <div class="form-group">
                                                            <label for="lon" class="col-lg-2 control-label">Longitude</label>
                                                            <div class="col-lg-10">
                                                                <input type="text" id="lon" name="lon" value="${test.lon}" class="form-control" />
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="row">
                                                        <div class="form-group">
                                                            <label for="rayon" class="col-lg-2 control-label">Rayon </label>
                                                            <div class="col-lg-10 ">
                                                                <div class="input-group">
                                                                    <span class="input-group-addon">
                                                                        <input  type="checkbox" id="ch-rayon" name="ch-rayon" onchange='handleRayon(this)'/>
                                                                    </span>
                                                                    <input type="text" id="rayon" name="rayon" value="${test.rayon}" class="form-control" disabled/>
                                                                    <span class="input-group-addon">m</span>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>


<!--                                                 <div id="selectRegion" class="tab-pane fade"> -->
<!--                                                     <div class="row"> -->
<!--                                                         <div class="form-group"> -->
<!--                                                             <label for="rayon" class="col-lg-2 control-label">Region </label> -->
<!--                                                             <div class="col-lg-10 "> -->
<!--                                                                 <select class="form-control" name="region" id="SelectRegion"> -->
<!--                                                                     <option value="region_ara">Auvergne-Rhône-Alpes</option> -->
<!--                                                                     <option value="region_idf">Ile-de-France</option> -->
<!--                                                                     <option value="region_occ">Occitanie</option> -->
<!--                                                                     <option value="region_pac">Provence-Alpes-Côte d'Azur</option>															 -->
<!--                                                                 </select> -->
<!--                                                             </div> -->
<!--                                                         </div> -->
<!--                                                     </div> -->
<!--                                                 </div> -->
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="panel-group">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h4 class="panel-title">
                                            <input type="checkbox" name="TU_code" id="TU_code" onclick="handleSearchType()"><a data-toggle="collapse" href="#collapse2" > Forme Phonétique (API)</a>
                                        </h4>
                                    </div>
                                    <div id="collapse2" class="panel-collapse collapse">
                                        <div class="panel-body">
                                            <div class="row">
                                                <div class="form-group">
                                                    <label for="api" class="col-lg-2 control-label">API </label>
                                                    <div class="col-lg-10 ">
                                                        <div class="input-group ">
                                                            <input type="text" id="api" name="api" value="${test.TU_code}" class="form-control" />
                                                            <span class="input-group-btn">
                                                                <button class="btn btn-default " id="btnClavier" type="button" data-toggle="popover" data-placement="top">Clavier</button>
                                                            </span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <input type="button" value="search" class="btn btn-primary" id="search" disabled="true"/> 
                            <input type="button" value="Effacer" id="resetSearch" class="btn btn-secondary" >
                            <br />
                            <div class="row">
                                <div class="form-group">
                                    <div class="col-lg-10 ">
                                        <input type="radio" name="select" class="select" value="point" id="point" checked="true" />
                                        <input type="radio" name="select" class="select" value="region" id="region" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

            <!-- <footer class="row">
                <div class="col-lg-12">
                    <div class="col-xs-offset-1 col-xs-1"><img alt="ANR" src="images/label-ANR.png" height=100></div>
                    <div class="col-xs-offset-3 col-xs-1"><img alt="ANR" src="images/LIG_coul.jpg" height=100></div>

                </div>
            </footer>-->

        </div>


    </body>
<!--     <script src="js/apiKeyboard.js" type="text/javascript"></script> -->
    <script type="text/javascript">

                    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                        var target = $(e.target).attr("href");
                        if ((target == '#selectPoint')) {
                            document.getElementById('region').checked = false;
                            document.getElementById('point').checked = true;
                        } else {
                            document.getElementById('point').checked = false;
                            document.getElementById('region').checked = true;
                        }
                    });
                    $('#ch-rayon').onchange = function () {
                        if (this.checked) {
                            document.getElementById('rayon').disabled = false;
                        } else {
                            document.getElementById('rayon').disabled = true;
                        }
                    }

                    function handleRayon(checkbox) {
                        if (checkbox.checked == true) {
                            document.getElementById('rayon').disabled = false;
                        } else {
                            document.getElementById('rayon').disabled = true;
                        }
                    }

                    function handleSearchType() {
                        if (document.getElementById('TU_code').checked == true || document.getElementById('spatial').checked == true) {
                            document.getElementById('search').disabled = false;
                        } else {
                            document.getElementById('search').disabled = true;
                        }
                    }


                    $(function () {
                        // Enables popover
                        $("[data-toggle=popover]").popover({
                            html: true,
                            container: 'body',
                            title: '<span class="text-info"><strong>Clavier API</strong></span>' + '<button type="button" id="close" class="close" onclick="$(&quot;#btnClavier&quot;).click();">&times;</button>',
                            content: function () {
                                return $('#clavier').html();
                            }
                        });
                    });

                    // initialisation de la carte avec les paramètres de la requête
                    initMap(${test.lat}, ${test.lon}, ${test.featureCollection});
    </script>
</html>
