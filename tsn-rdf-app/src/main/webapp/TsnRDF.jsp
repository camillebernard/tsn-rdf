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
                                    <dt>Territorial Mesh versions</dt>
                                    <dd>
                                        <select class="form-control" name="carte" id="selectCarte">
                                            <option value="NUTS1999">NUTS version 1999</option>
                                           	<option value="NUTS2003">NUTS version 2003</option> 
                                            <option value="NUTS2006">NUTS version 2006</option> 
                                            <option value="NUTS2010">NUTS version 2010</option> 
                                            <input type="hidden" id="newCarte" name="newCarte" value="false" />
                                        </select>
                                    </dd>

                                </dl>
                                <h4>NUTS Territorial Unit Metadata</h4>
                                <dl class="dl-horizontal">
                                    <dt>Territorial Unit CODE</dt>
                                    <dd id="info-code"></dd>
                                    <dt>Territorial Unit Name</dt>
                                    <dd id="info-name"></dd>
                                    <dt>Territorial Unit Level</dt>
                                    <dd id="info-level"></dd>
                                    <dt>TSN version</dt>
                                    <dd id="info-version"></dd>
                                </dl>
                               
                            </div>
                        </div>
                    </div>
                </div>
</form>


        </div>


    </body>
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

                    initMap(${test.lat}, ${test.lon}, ${test.featureCollection});
    </script>
</html>
