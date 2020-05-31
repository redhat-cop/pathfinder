
<script>
    var logLevel = "INFO";

    var adoptionPlanColors = [];
    var lastColor = 0;
    adoptionPlanColors[0] = 'rgb(146,212,0)';
    adoptionPlanColors[1] = 'rgb(240,171,0)';
    adoptionPlanColors[2] = 'rgb(204, 0, 0)';
    adoptionPlanColors[3] = 'rgb(0, 65, 83)';
    adoptionPlanColors[4] = '#3B0083';
    adoptionPlanColors[5] = '#A3DBE8';
    adoptionPlanColors[6] = '#808080';

    adoptionPlanColors[7] = '#a30000';
    adoptionPlanColors[8] = '#004254';
    adoptionPlanColors[9] = '#0088ce';
    adoptionPlanColors[10] = '#409c36';
    adoptionPlanColors[11] = '#7aa4ae';
    adoptionPlanColors[12] = '#820000';
    adoptionPlanColors[13] = '#40717f';
    adoptionPlanColors[14] = '#40a6da';
    adoptionPlanColors[15] = '#70b568';
    adoptionPlanColors[16] = '#516d74';


    function getNextColor() {
        var c = lastColor++;
        if (lastColor >= adoptionPlanColors.length)
            lastColor = 0;
        return adoptionPlanColors[c];
    }

    var adoptionSize = [];
    adoptionSize['null'] = 0;
    adoptionSize['SMALL'] = 10;
    adoptionSize['MEDIUM'] = 20;
    adoptionSize['LARGE'] = 40;
    adoptionSize['XLarge'] = 80;


    // ugly fix - do something like embed the order so we dont need external variables
    var maxRecursion = 100;
    var recursion = 0;
    function getOrder(parent, app, map) {
        console.log("getOrder -->" + parent + " " + app + " " + map);
        var order = app['Size'] - (app['OutboundDeps'] != null ? app['OutboundDeps'].length : 0);
        if (logLevel == "DEBUG")
            console.log("DEBUG:: OrderCalc: " + (parent == null ? "" : parent['Name'] + ".") + app['Name'] + ": order = " + order);
        for (x = 0; x < app['OutboundDeps'].length; x++) {
            var dependsOn = map[app['OutboundDeps'][x]];
            if (undefined == dependsOn)
                continue;
            if (app['Id'] == dependsOn['Id'])
                continue; //infinite loop protection
            if (recursion > 100)
                break;
            recursion += 1;
            //console.log("OrderCalc: "+app['Name']+" -> "+dependsOn['Name']);
            order += getOrder(app, dependsOn, map);
        }
        return order;
    }


    function getPadding(dependsOn) {
        console.log("getPadding -->" + dependsOn);
        if (null != dependsOn && dependsOn.length != 0) {
            for (d = 0; d < dependsOn.length; d++) {
                if (dependsOn[d]['Padding'] == null) {
                    dependsOn[d]['Padding'] = getPadding(dependsOn[d]['DependsOn']);
                }
            }
            // all should have padding now
            var padding = 0;
            for (d = 0; d < dependsOn.length; d++) {
                if (appFilter.includes(dependsOn[d]['Id'])) {
                    padding = Math.max(padding, dependsOn[d]['Padding'] + dependsOn[d]['Size']);
                } else {
                    padding = Math.max(padding, dependsOn[d]['Padding']);
                }
            }
            return padding;
        } else {
            return 0;
        }
    }


    var adoptionChart = null;
    function redrawAdoptionPlan(applicationAssessmentSummary, initial) {
        console.log("redrawAdoptionPlan -->" + applicationAssessmentSummary + " " + initial);
        lastColor = 0;

        // deep copy
        var summary = JSON.parse(JSON.stringify(applicationAssessmentSummary));

        // init colors
        var colors = [];
        for (i = 0; i < summary.length; i++) {
            colors[summary[i]['Name']] = getNextColor();
        }

        // build map of app Id->app
        var appIdToAppMap = [];
        for (i = 0; i < summary.length; i++) {
            appIdToAppMap[summary[i].Id] = summary[i];
            summary[i]["Size"] = adoptionSize[summary[i].WorkEffort];
        }

        // re-order summary by # of dependencies (0 first) so that when we get to calculate padding the deps have their padding set first before any dependants
        for (i = 0; i < summary.length; i++) {
            summary[i]['DependsOn'] = [];
            if (undefined != summary[i]['OutboundDeps']) {
                for (d = 0; d < summary[i]['OutboundDeps'].length; d++) {

                    var dependent = appIdToAppMap[summary[i]['OutboundDeps'][d]];
                    if (dependent != undefined) { // this could happen if the dependency is on a non-assessable application
                        summary[i]['DependsOn'].push(dependent);
                        //console.log("INFO :: AdoptionGraph:: DependsOn:: "+ summary[i]['Name']+" depends on "+summary[i]['DependsOn'][d]['Name']);
                    }
                    //summary[i]['DependsOn'].push(appIdToAppMap[summary[i]['OutboundDeps'][d]]);

                }
            } else {
                summary[i]['OutboundDeps'] = [];
            }
        }

        // Padding
        for (i = 0; i < summary.length; i++) {
            summary[i]['Padding'] = getPadding(summary[i]['DependsOn']);
        }


        // TODO: This almost works, is has a defect on transitive dependencies, if A->B->C then if you untick B then C starts at 0 with A
        var summary2 = [];
        var added = [];
        var loop = 0, maxLoop = 100;
        while (summary.length > summary2.length) {
            loop += 1;
            if (loop > maxLoop)
                break;

            for (i = 0; i < summary.length; i++) {
                if (summary.length <= summary2.length)
                    break; //shortcut

                if (!added.includes(summary[i]['Name'])) {
                    summary2.push(summary[i]);
                    added.push(summary[i]['Name']);
                    // if all deps added, then go back through them to calculate the padding
                }
            }
        }
        summary = summary2;


        // manipulate dependsOn into a list of app NAME's rather than ID's
        for (i = 0; i < summary.length; i++) {
            if (null != summary[i]['OutboundDeps']) {

                summary[i]["AdoptionOrder"] = summary[i]["Size"];
            }
        }


        // remove any apps that are not selected (because this screws up the sorting)
        for (i = 0; i < summary.length; i++) {
            if (!appFilter.includes(summary[i]['Id'])) {
                summary.splice(i, 1);
                continue;
            }
        }

        for (i = 0; i < summary.length; i++) {
            summary[i]["AdoptionOrder"] = i + getOrder(null, summary[i], appIdToAppMap);
            //console.log("INFO :: AdoptionGraph:: AdoptionOrder:: "+summary[i]['Name']+" = "+summary[i]["AdoptionOrder"]);
        }


        // sort in size order (small to large)
        //summary.sort(compareValues("AdoptionOrder"));
        summary.sort(function (a, b) {
            return a['AdoptionOrder'] - b['AdoptionOrder'];
        });


        for (i = 0; i < summary.length; i++) {
            if (!appFilter.includes(summary[i]['Id']))
                continue;
            console.log("INFO :: AdoptionGraph:: DisplayOrder->" + summary[i]['AdoptionOrder'] + " - " + summary[i]['Padding'] + "/" + summary[i]['Size'] + "-" + summary[i]['Name']);
        }

        //console.log(JSON.stringify(summary));


        //console.log("x="+JSON.stringify(getApp("Online Ticker",summary)));
        //console.log("l="+summary.length);
        //console.log("summary="+JSON.stringify(summary));


        var hiddenDS = {
            label: "hidden",
            data: [], // indent
            backgroundColor: "rgba(0,0,0,0)",
            hoverBackgroundColor: "rgba(0,0,0,0)"
        };
        var realDS = {
            data: [], // actual bar
            backgroundColor: [],
        };
        var data = {labels: [], datasets: [hiddenDS, realDS]};

        var c = 0;
        for (i = 0; i < summary.length; i++) {
            var app = summary[i];

            if (!appFilter.includes(app['Id']))
                continue; // should never happen by this point

            data.labels[c] = app['Name'];
            hiddenDS.data[c] = app['Padding'];
            realDS.data[c] = app['Size'];
            realDS.backgroundColor[c] = colors[app['Name']];
            //realDS.backgroundColor[c]=getNextColor();
            c++;
        }

        //console.log(JSON.stringify(data));


        // if the chart has been drawn already, then just update the data
        if (adoptionChart != null) {
            adoptionChart.data = data;
            adoptionChart.options.animation.duration = initial ? 1000 : 1;
            adoptionChart.update();
            return;
        }

        var ctx = document.getElementById('adoption').getContext('2d');
        adoptionChart = new Chart(ctx, {
            type: 'horizontalBar',
            data: data,
            options: {
                plugins: {
                    datalabels: {
                        display: false
                    }
                },
                animation: {
                    duration: 1000
                },
                hover: {
                    animationDuration: 10
                },
                scales: {
                    xAxes: [{
                            label: "Duration",
                            ticks: {
                                stepSize: 10,
                                display: false,
                                beginAtZero: true,
                                fontFamily: "'Open Sans Bold', sans-serif",
                                fontSize: 11
                            },
                            scaleLabel: {
                                display: false
                            },
                            gridLines: {
                            },
                            stacked: true
                        }],
                    yAxes: [{
                            gridLines: {
                                display: false,
                                color: "#fff",
                                zeroLineColor: "#fff",
                                zeroLineWidth: 0
                            },
                            ticks: {
                                fontFamily: "'Open Sans Bold', sans-serif",
                                fontSize: 11
                            },
                            stacked: true
                        }]
                },
                legend: {
                    display: false
                },
                tooltips: {
                    enabled: false
                },

            }
        });
    }
    ;
</script>