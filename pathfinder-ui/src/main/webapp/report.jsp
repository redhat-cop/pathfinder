<!DOCTYPE HTML>
<html>

    <%@include file="head.jsp"%>

    <link href="assets/css/breadcrumbs.css" rel="stylesheet" />

    <%@include file="datatables-dependencies.jsp"%>
    <script src="assets/js/datatables-plugins.js"></script>
    <script type="text/javascript" src="utils.jsp"></script>

    <script
    src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.bundle.min.js"></script>
    <script src="https://unpkg.com/lodash@4.17.10/lodash.min.js"></script>

    <!-- plugin to add labels to bubble chart in chartjs -->
    <script src="assets/js/chartjs-plugin-datalabels-0.4.0.js"></script>

    <body class="is-preload">
        <%@include file="nav.jsp"%>

        <%@include file="breadcrumbs.jsp"%>

        <div class="container-fluid">


            <script>
                var customerId = Utils.getParameterByName("customerId");

                $(document).ready(function () {

                    // ### Get Customer Details
                    httpGetObject(Utils.SERVER + "/api/pathfinder/customers/" + customerId, function (customer) {
                        // ### Populate the header with the Customer Name
                        //document.getElementById("customerName").innerHTML=customer.CustomerName;
                        if (undefined != setBreadcrumbs)
                            setBreadcrumbs("assessments", customer);
                    });

                });
            </script>

            <section class="wrapper">
                <div class="inner">

                    <div id="title" alt="required for automated testing"
                         style="display: none;">REPORT</div>

                    <!-- ### Page specific stuff here ### -->


                    <h2>Current Landscape</h2>
                    <div class="row">
                        <div class="col-sm-3">
                            <canvas id="gauge-1" style="width: 200px; height: 110px;"></canvas>
                        </div>
                        <div class="col-sm-3">
                            <canvas id="gauge-2" style="width: 200px; height: 110px;"></canvas>
                        </div>
                        <div class="col-sm-3">
                            <canvas id="gauge-3" style="width: 200px; height: 110px;"></canvas>
                        </div>
                    </div>
                    <%@include file="report-summary.jsp"%>



                    <br /> <br /> <br />
                    <h2>Adoption Candidate Distribution</h2>
                    <div class="row">
                        <div class="col-sm-4">
                            <style>
                                /* override the datatables formatting to compress the screen real-estate used */
                                input[type=search] {
                                    height: 23px; 
                                    width: 100px;
                                    padding: 0px;
                                    line-height: 1;
                                }

                                .dataTables_length label select {
                                    height: 23px;
                                    width: 50px;
                                    padding: 0px;
                                }

                                .dataTables_wrapper .dataTables_paginate .paginate_button.current,
                                .dataTables_wrapper .dataTables_paginate .paginate_button.current:hover
                                {
                                    height: 23px;
                                    padding: 0px !important;
                                }

                                table.dataTable thead th, table.dataTable thead td {
                                    padding: 0px 0px !important;
                                    font-size: 10pt;
                                }

                                table.dataTable tbody tr td {
                                    font-size: 10pt;
                                }
                            </style>
                            <script>
                                var appFilter = [];

                                function redrawApplications(applicationAssessmentSummary) {
//                                    console.log("redrawApplications -> " + applicationAssessmentSummary);
                                    $('#appFilterTBL').DataTable({
                                        "data": applicationAssessmentSummary,
                                        "oLanguage": {
                                            sSearch: "Search:", // remove the "Search" label text
                                            sLengthMenu: "_MENU_"}, // remove the "show X entries" text
                                        "scrollCollapse": true,
                                        "paging": true,
                                        "lengthMenu": [[10, 25, 50, 100, 200, -1], [10, 25, 50, 100, 200, "All"]], // page entry options
                                        "pageLength": 10, // default page entries
                                        "searching": true,
                                        "bInfo": true, // removes "Showing N entries" in the table footer
                                        //"order" :         [[1,"desc"],[2,"desc"],[0,"asc"]],
                                        "order": [[4, "desc"], [6, "asc"]],
                                        "columns": [
                                            {"data": "Id"},
                                            {"data": "Name"},
                                            {"data": "BusinessPriority"},
                                            {"data": "WorkPriority"},
                                            {"data": "Confidence", width: "15%"},
                                            {"data": "Decision"},
                                            {"data": "WorkEffort"},
                                        ],
                                        "columnDefs": [
                                            {"targets": 0,
                                                "orderable": false,
                                                "render": function (data, type, row) {
                                                    return "<input onclick='onChange2(this);' " + (row['Decision'] != null ? "checked" : "") + " type='checkbox' value='" + row['Id'] + "' style='margin-right: 0rem;'/>";
                                                }},
                                        ]
                                    });
                                }
                                ;

                                function onChange2(t) {
//                                    console.log("onChange2 -> " + JSON.stringify(appFilter));
                                    t.checked ? appFilter.push(t.value) : appFilter.splice(appFilter.indexOf(t.value), 1);
                                    redrawBubble(applicationAssessmentSummary);
                                    redrawAdoptionPlan(applicationAssessmentSummary);
                                }

//                                function checkAppByDefaultIf(row) {
//                                    return row['Decision'] != null
//                                            && (row['Decision'] == "REHOST" || row['Decision'] == "REFACTOR" || row['Decision'] == "REPLATFORM")
//                                }


                            </script>
                            <div id="wrapper">
                                <div></div>
                                <div id="tableDiv">
                                    <table id="appFilterTBL" class="display" cellspacing="0"
                                           width="100%">
                                        <thead>
                                            <tr>
                                                <th align="left"></th>
                                                <th align="left" title="Application Name">Application</th>
                                                <th align="left" title="Business Criticality">Critical</th>
                                                <th align="left" title="Work Priority">Priority</th>
                                                <th align="left" title="Confidence">Confidence</th>
                                                <th align="left"
                                                    title="Recommended Decision or Action to take">Decision</th>
                                                <th align="left" title="Estimated Effort">Effort</th>
                                            </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                            <!-- dtable wrapper -->

                            <script>

                            </script>

                        </div>



                        <script>
                            var decisionColors = [];
                            // colors got from https://brand.redhat.com/elements/color/
                            decisionColors['REHOST'] = "#92d400"; //green
                            decisionColors['REPLATFORM'] = "#f0ab00"; //amber
                            decisionColors['REFACTOR'] = "#cc0000"; //red
                            decisionColors['REPURCHASE'] = "#3B0083"; //purple
                            decisionColors['RETAIN'] = "#A3DBE8"; //light blue
                            decisionColors['RETIRE'] = "#004153"; //dark blue
                            decisionColors['NULL'] = "#808080"; //grey
                            
                            var sizing = [];
                            sizing['0'] = 10;
                            sizing['SMALL'] = 15;
                            sizing['MEDIUM'] = 27;
                            sizing['LARGE'] = 36;
                            sizing['XLarge'] = 45;
                            var randomNumbers = [];

                            var bubbleChart;

//                                function getData(summary) {
//                                    console.log("getData -> " + summary);
//                                    var datasets = _.chain(summary)
//                                            .filter(summaryItem => (appFilter.includes(summaryItem.Id)))
//                                            .map(app => {
//                                                return {
//                                                    label: app.Name,
//                                                    backgroundColor: app.Decision ? decisionColors[app.Decision] : decisionColors.NULL,
//                                                    data: [
//                                                        {
//                                                            x: app.Confidence || 0,
//                                                            y: app.BusinessPriority || 0,
//                                                            r: sizing[app.WorkEffort || 0]
//                                                        }
//                                                    ]
//                                                }
//                                            })
//                                            .value()
//                                    return {datasets}
//                                }

                            function getDataOriginal(summary) {
//                                    console.log("getDataOriginal -> " + JSON.stringify(summary));
                                var datasets = [];
                                var i;
                                for (i = 0; i < summary.length; i++) {
                                    var app = summary[i];

                                    if (!appFilter.includes(app['Id']))
                                        continue;

                                    var dataset = {};

                                    var name = app['Name'];
                                    var businessPriority = app['BusinessPriority'];
                                    var workPriority = app['WorkPriority'];
                                    var decision = app['Decision'];
                                    var workEffort = app['WorkEffort'];
                                    //var inboundDependencies=5; // TODO: not implemented in the back end yet
                                    var inboundDependencies = randomNumbers[i]
                                    var confidence = app['Confidence'];

                                    //TODO: this shouldnt be possible unless the assessment is incomplete
                                    if (businessPriority == null)
                                        businessPriority = 0;
                                    if (workEffort == null)
                                        workEffort = 0;
                                    if (confidence == null)
                                        confidence = 0;

                                    // label
                                    dataset['label'] = [name];

                                    //data points
                                    dataset['data'] = [];
//                                    dataset['data'].push({"x": confidence - 50, "y": businessPriority - 5, "r": sizing[workEffort]});
                                    dataset['data'].push({
                                        "x": confidence - (50-(Math.random() * 5)), 
                                        "y": businessPriority - (5+(Math.random() * 1)), 
                                        "r": (sizing[workEffort])});

                                    // color
                                    if (decision != null) {
                                        if (greyscale) {
                                            dataset['backgroundColor'] = decisionColors.NULL;
                                        } else {
                                            dataset['backgroundColor'] = decisionColors[decision];
                                        }
                                    } else {
                                        dataset['backgroundColor'] = decisionColors.NULL;
                                    }
                                    dataset['borderColor'] = "#ffffff";
                                    dataset['borderWidth'] = 2;

                                    datasets.push(dataset);
                                }

                                function compare(a, b) {
                                    const A = a.data[0].r;
                                    const B = b.data[0].r;

                                    let comparison = 0;
                                    if (A > B) {
                                        comparison = 1;
                                    } else if (A < B) {
                                        comparison = -1;
                                    }
                                    return comparison;
                                }

                                datasets.sort(compare);

                                var result = {datasets};
                                return result;
                            }

                            function redrawBubble(applicationAssessmentSummary, initial) {
                                var appJSONString = JSON.stringify(applicationAssessmentSummary);

                                var summary = JSON.parse(appJSONString);

                                // build a map of AppId's to App
                                var appIdToAppMap = [];
                                var appNameToAppMap = [];
                                for (i = 0; i < summary.length; i++) {
                                    appIdToAppMap[summary[i].Id] = summary[i];
                                    appNameToAppMap[summary[i].Name] = summary[i];
                                }

                                // dereference the OutboundDeps App Id's into App Names and add them in a field called DependsOn for easy reference when building the charts
                                for (i = 0; i < summary.length; i++) {
                                    if (null != summary[i]['OutboundDeps']) {
                                        summary[i]['DependsOn'] = [];
                                        //var dependsOn=[];
                                        for (d = 0; d < summary[i]['OutboundDeps'].length; d++) {
                                            var dependent = appIdToAppMap[summary[i]['OutboundDeps'][d]];
                                            if (undefined != dependent) {
                                                summary[i]['DependsOn'].push(dependent.Name);
                                            }
                                        }
                                    }
                                }


                                if (initial) {
                                    for (i = 0; i < summary.length; i++) {
                                        var app = summary[i];
                                        appFilter.push(app['Id']);
                                    }
                                }


                                if (bubbleChart != null) {
                                    bubbleChart.data = getDataOriginal(summary);
                                    bubbleChart.options.animation.duration = initial ? 1000 : 1;
                                    bubbleChart.options.plugins.datalabels.display = labels;
                                    bubbleChart.update();
                                    return;
                                }

                                var ctx = document.getElementById("bubbleChart").getContext('2d');
                                bubbleChart = new Chart(ctx, {
                                    "type": "bubble",
                                    "data": getDataOriginal(summary),
                                    options: {
                                        legend: {
                                            display: false,
                                            position: "top"
                                        },
                                        animation: {
                                            duration: 1000
                                        },
                                        tooltips: {
                                            enabled: true,
                                            callbacks: {
                                                label: function (tooltipItem, data) {
                                                    return data.datasets[tooltipItem.datasetIndex].label || 'Unknown';
                                                }
                                            }
                                        },
                                        aspectRatio: 1,
                                        scales: {
                                            yAxes: [{
                                                    gridLines: {
                                                        lineWidth: 1
                                                    },
                                                    display: true,
                                                    ticks: {
                                                        display: false,
                                                        suggestedMin: -6,
                                                        suggestedMax: 6,
                                                        stepSize: 1,
                                                        beginAtZero: true
                                                    },
                                                    scaleLabel: {
                                                        display: true,
                                                        labelString: "Business Criticality",
                                                    }
                                                }],
                                            xAxes: [{
                                                    display: true,
                                                    ticks: {
                                                        display: false,
                                                        suggestedMin: -60,
                                                        suggestedMax: 60,
                                                        stepSize: 10,
                                                        beginAtZero: true
                                                    },
                                                    scaleLabel: {
                                                        display: true,
                                                        labelString: "Confidence",
                                                    }
                                                }],
                                        },
                                        plugins: {
                                            datalabels: {
                                                display: labels,
                                                anchor: function (context) {
                                                    var value = context.dataset.data[context.dataIndex];
                                                    return value.r <= 1500 ? 'end' : 'center';
                                                },
                                                align: function (context) {
                                                    var value = context.dataset.data[context.dataIndex];
                                                    return value.r <= 1500 ? 'end' : 'center';
                                                },
                                                color: function (context) {
                                                    var value = context.dataset.data[context.dataIndex];
                                                    return value.r <= 1500 ? context.dataset.backgroundColor : 'white';
                                                },
                                                font: {
                                                    weight: 'bold'
                                                },
                                                formatter: function (value, context) {
                                                    var valuex = context.dataset.label[0];
                                                    return valuex;
                                                },
                                                offset: 2,
                                                padding: 0
                                            }
                                        }
                                    }
                                });

                                Chart.pluginService.register({
                                    beforeDraw: function (chart) {
                                        var width = chart.chart.width,
                                                height = chart.chart.height,
                                                ctx = chart.chart.ctx,
                                                type = chart.config.type;

                                        if (type == 'bubble') {
                                            //ctx.restore();
                                            ctx.clearRect(0, 0, chart.chart.width, chart.chart.height);
                                            var fontSize = 1.1;
                                            ctx.font = fontSize + "em sans-serif";
                                            ctx.textBaseline = "middle"
                                            ctx.fillStyle = "#555";

                                            var topLeftText = "Impactful but not advisable to move";
                                            var topLeftX = ((width / 4) * 1) - (ctx.measureText(topLeftText).width / 2);
                                            var topLeftTextY = 15;

                                            var topRightText = "Impactful and migratable"
                                            var topRightX = ((width / 4) * 3) - (ctx.measureText(topRightText).width / 2)
                                            var topRightTextY = 15;

                                            var bottomLeftText = "Inadvisable";
                                            var bottomLeftX = ((width / 4) * 1) - (ctx.measureText(bottomLeftText).width / 2);
                                            var bottomLeftTextY = chart.chartArea.bottom - 15;

                                            var bottomRightText = "Trivial but migratable";
                                            var bottomRightX = ((width / 4) * 3) - (ctx.measureText(bottomRightText).width / 2);
                                            var bottomRightTextY = chart.chartArea.bottom - 15;

                                            // quadrant text
                                            ctx.fillText(topLeftText, topLeftX, topLeftTextY);
                                            ctx.fillText(topRightText, topRightX, topRightTextY);
                                            ctx.fillText(bottomLeftText, bottomLeftX, bottomLeftTextY);
                                            ctx.fillText(bottomRightText, bottomRightX, bottomRightTextY);

                                            var x = (width / 2) + 15, y = 0, w = (width / 2) - 15, h = (height / 2) - 13;

                                            var adjustment = 140;
                                            x = x - adjustment;
                                            w = w + adjustment;

                                            var grd = ctx.createLinearGradient(x, 0, width - 150, 0);
                                            var grdOpacity = 0.12;
                                            grd.addColorStop(0, "rgba(255,255,255,0)");
                                            grd.addColorStop(1, "rgba(46, 212, 0, " + grdOpacity + ")");

                                            ctx.fillStyle = grd;
                                            ctx.fillRect(x, y, w, (h * 2) - 5);


                                            // draw the dependency line(s)
                                            if (!dependencies) {
                                                //ctx.fillStyle = "rgba(0, 0, 0, 0.8)";
                                                ctx.strokeStyle = 'rgba(0,0,0,0.2)';
                                                ctx.lineWidth = 5;

                                                var bubbleMapModels = [];
                                                for (i = 0; i < chart.config.data.datasets.length; i++) {
                                                    var model_x = chart.getDatasetMeta(i).data[0]._model.x;
                                                    var model_y = chart.getDatasetMeta(i).data[0]._model.y;
                                                    var model_r = chart.getDatasetMeta(i).data[0]._model.radius;
                                                    bubbleMapModels[chart.config.data.datasets[i].label[0]] = {Name: chart.config.data.datasets[i].label[0], x: model_x, y: model_y, r: model_r};
                                                    //console.log(chart.config.data.datasets[i].label[0] +" = "+JSON.stringify({x:model_x,y:model_y}));
                                                }

                                                for (i = 0; i < chart.config.data.datasets.length; i++) {
                                                    var bubble = chart.config.data.datasets[i];
                                                    var r = bubble.data[0].r;
                                                    var label = bubble.label[0];
                                                    var x = bubbleMapModels[label].x;
                                                    var y = bubbleMapModels[label].y;

                                                    if (undefined == appNameToAppMap[label].DependsOn)
                                                        continue;

                                                    for (var d = 0; d < appNameToAppMap[label].DependsOn.length; d++) {
                                                        var targetBubble = bubbleMapModels[appNameToAppMap[label].DependsOn[d]];

                                                        if (targetBubble == null)
                                                            continue; //dependency may not be displayed on the graph

                                                        var radius = targetBubble.r;
                                                        var angle = Math.atan2(targetBubble.y - y, targetBubble.x - x) * 180 / 3.14;
                                                        var height = targetBubble.y - y;
                                                        var width = targetBubble.x - x;
                                                        var length = Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2));
                                                        var subtractWidth = radius * width / length;
                                                        var subtractHeight = radius * height / length;

                                                        ctx.beginPath();
                                                        drawArrow(ctx, x, y, targetBubble.x - subtractWidth, targetBubble.y - subtractHeight, 20);

                                                        ctx.stroke();
//                                                            console.log("BubbleGraph:: drawn dependency line from '" + label + "' to '" + targetBubble.Name + "' which is from '" + x + "," + y + "' to '" + targetBubble.x + "," + targetBubble.y + "'");

                                                    }

                                                }
                                            }
                                            ctx.save();
                                        }
                                    }
                                });
                            }

                            function drawArrow(ctx, x, y, targetX, targetY, arrowSize) {
                                var angle = Math.atan2(targetY - y, targetX - x);
                                ctx.moveTo(x, y);
                                ctx.lineTo(targetX, targetY);
                                ctx.lineTo(targetX - arrowSize * Math.cos(angle - Math.PI / 6), targetY - arrowSize * Math.sin(angle - Math.PI / 6));
                                ctx.moveTo(targetX, targetY);
                                ctx.lineTo(targetX - arrowSize * Math.cos(angle + Math.PI / 6), targetY - arrowSize * Math.sin(angle + Math.PI / 6));
                            }

                            var data;
                            var applicationAssessmentSummary;
                            var appIdToNameMap = [];
                            httpGetObject(Utils.SERVER + "/api/pathfinder/customers/" + customerId + "/applicationAssessmentSummary", function (summary) {
                                applicationAssessmentSummary = summary;
                                for (i = 0; i < summary.length; i++) {
                                    appIdToNameMap[summary.Id] = summary.Name;
                                }
                                redrawApplications(applicationAssessmentSummary);
                                redrawBubble(applicationAssessmentSummary, true);
                                redrawAdoptionPlan(applicationAssessmentSummary, true);
                            });

                        </script>

                        <div class="col-sm-8" style="position: relative;">

                            <style>
                                #bubbleLegend {
                                    justify-content: space-between;
                                    list-style-type: none;
                                }

                                #bubbleLegend ul li { 
                                    float: left; 
                                    top: 30px; 
                                    display: inline-block;
                                    width: 120px;
                                }
                            </style>

                            <div id="bubbleLegend">
                                <ul>
                                    <li><svg height="25" width="200">
                                        <rect width="120" height="25" stroke="black"
                                              style="fill:#92d400;stroke-width:0;stroke:rgb(0,0,0)" />
                                        <text x="7" y="17" font-family="Overpass" font-size="13"
                                              fill="#333">REHOST</text>
                                        </svg></li>
                                    <li><svg height="25" width="200">
                                        <rect width="120" height="25" stroke="black"
                                              style="fill:#f0ab00;stroke-width:0;stroke:rgb(0,0,0)" />
                                        <text x="7" y="17" font-family="Overpass" font-size="13"
                                              fill="#EEE">REPLATFORM</text>
                                        </svg></li>
                                    <li><svg height="25" width="200">
                                        <rect width="120" height="25" stroke="black"
                                              style="fill:#cc0000;stroke-width:0;stroke:rgb(0,0,0)" />
                                        <text x="7" y="17" font-family="Overpass" font-size="13"
                                              fill="#EEE">REFACTOR</text>
                                        </svg></li>
                                    <li><svg height="25" width="200">
                                        <rect width="120" height="25" stroke="black"
                                              style="fill:#3B0083;stroke-width:0;stroke:rgb(0,0,0)" />
                                        <text x="7" y="17" font-family="Overpass" font-size="13"
                                              fill="#EEE">REPURCHASE</text>
                                        </svg></li>
                                    <li><svg height="25" width="200">
                                        <rect width="120" height="25" stroke="black"
                                              style="fill:#A3DBE8;stroke-width:0;stroke:rgb(0,0,0)" />
                                        <text x="7" y="17" font-family="Overpass" font-size="13"
                                              fill="#333">RETAIN</text>
                                        </svg></li>
                                    <li><svg height="25" width="200">
                                        <rect width="120" height="25" stroke="black"
                                              style="fill:#004153;stroke-width:0;stroke:rgb(0,0,0)" />
                                        <text x="7" y="17" font-family="Overpass" font-size="13"
                                              fill="#EEE">RETIRE</text>
                                        </svg></li>
                                    <li><svg height="25" width="200">
                                        <rect width="120" height="25" stroke="black"
                                              style="fill:#808080;stroke-width:0;stroke:rgb(0,0,0)" />
                                        <text x="7" y="17" font-family="Overpass" font-size="13"
                                              fill="#EEE">NOT REVIEWED</text>
                                        </svg></li>
                                </ul>
                            </div>


                            <div class="chartjs-wrapper" style="width: 850px; height: 530px;">
                                <canvas id="bubbleChart" class="chartjs" width="850" height="531"></canvas>
                            </div>

                            <script>
                                var greyscale = false;
                                var dependencies = false;
                                var labels = false;

                                function greyscaleToggle(t) {
                                    t.value == "Show Decisions" ? t.value = "Hide Decisions" : t.value = "Show Decisions";
                                    greyscale = (t.value == "Show Decisions");
                                    redrawBubble(applicationAssessmentSummary, false);
                                }

                                function dependenciesToggle(t) {
                                    t.value == "Show Dependencies" ? t.value = "Hide Dependencies" : t.value = "Show Dependencies";
                                    dependencies = (t.value == "Show Dependencies");
                                    redrawBubble(applicationAssessmentSummary, false);
                                }
                                function labelsToggle(t) {
                                    t.value == "Show Labels" ? t.value = "Hide Labels" : t.value = "Show Labels";
                                    labels = (t.value == "Hide Labels");
                                    redrawBubble(applicationAssessmentSummary, false);
                                }

                            </script>

                            <!--                            <input
                                                            class="btn btn-default form-control"
                                                            style="height: 28px; padding: 0px; width: 150px; font-size: 10pt; line-height: 1rem;"
                                                            type="button" id="dependencies" value="Show Dependencies"
                                                            onclick="dependenciesToggle(this);" />
                                                        <input
                                                            class="btn btn-default form-control"
                                                            style="height: 28px; padding: 0px; width: 150px; font-size: 10pt; line-height: 1rem;"
                                                            type="button" id="labels" value="Show Labels"
                                                            onclick="labelsToggle(this);" />-->

                        </div>
                        <!-- col-sm-? -->
                    </div>
                    <!-- /row -->


                    <br /> <br /> <br />
                    <h2>Suggested Adoption Plan</h2>
                    <div class="row">
                        <div class="col-sm-10">
                            <canvas id="adoption" style="width: 500px; height: 100px;"></canvas>
                                <%@include file="report-adoption.jsp"%>
                        </div>
                        <!-- col-sm-? -->
                    </div>
                    <!-- /row -->

                    <br /> <br /> <br />
                    <h2>Identified Risks</h2>
                    <div class="row">
                        <div class="col-sm-10">
                            <script>

                                function drawRisks(data) {
                                    var risks = [];
                                    if (data.risks != undefined)
                                        risks = data.risks;

                                    $('#risks').DataTable({
                                        "data": risks,
                                        "oLanguage": {
                                            sSearch: "", // remove the "Search" label text
                                            sLengthMenu: "_MENU_"    // remove the "show X entries" text
                                        },
                                        "scrollCollapse": true,
                                        "paging": false,
                                        //"lengthMenu": [[10, 25, 50, 100, 200, -1], [10, 25, 50, 100, 200, "All"]], // page entry options
                                        "pageLength": -1, // default page entries
                                        "bInfo": false, // removes "Showing N entries" in the table footer
                                        "columns": [
                                            {"data": "q"},
                                            {"data": "a"},
                                            {"data": "apps"},
                                        ],
                                    });
                                }
                            </script>
                            A list of questions with answers that that could cause migratory
                            risk to a container platform.
                            <!--						
                            <div class="collapse" id="collapser">
                            -->
                            <div>
                                <div id="wrapper">
                                    <div id="tableDiv">
                                        <table id="risks" class="display" cellspacing="0" width="100%">
                                            <thead>
                                                <tr>
                                                    <th align="left">Question</th>
                                                    <th align="left">Answer</th>
                                                    <th align="left">Application(s)</th>
                                                </tr>
                                            </thead>
                                        </table>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>

                    <div class="row">&nbsp;</div>

                </div>
            </section>
    </body>

    <br />
    <br />
    <br />
    <br />
    <br />
    <br />
    <br />
    <br />
</html>




<script>
    httpGetObject(Utils.SERVER + "/api/pathfinder/customers/" + customerId + "/report", function (report) {
        new Chart(document.getElementById("gauge-1"), buildGuage(report.assessmentSummary.Easy, report.assessmentSummary.Total, "rgb(146,212,0)", "rgb(220, 220, 220)", "Low risk - Cloud-Native Ready"));
        new Chart(document.getElementById("gauge-2"), buildGuage(report.assessmentSummary.Medium, report.assessmentSummary.Total, "rgb(240,171,0)", "rgb(220, 220, 220)", "Medium Risk - Modernizable"));
        new Chart(document.getElementById("gauge-3"), buildGuage(report.assessmentSummary.Hard, report.assessmentSummary.Total, "rgb(204, 0, 0)", "rgb(220, 220, 220)", "High Risk - Requires substantial work"));
        drawRisks(report);
    });
</script>