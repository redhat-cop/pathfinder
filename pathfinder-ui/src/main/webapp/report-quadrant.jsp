<!DOCTYPE HTML>
<html>
  
  <%@include file="head.jsp"%>
  
  <link href="assets/css/breadcrumbs.css" rel="stylesheet" />
	
  <!-- #### DATATABLES DEPENDENCIES ### -->
  <!-- Firefox doesnt support link imports yet
  <link rel="import" href="datatables-dependencies.jsp">
  -->
  <%@include file="datatables-dependencies.jsp"%>
	<script src="assets/js/datatables-plugins.js"></script>
	<script type="text/javascript" src="utils.jsp"></script>
	<!-- for pie/line/bubble graphing -->
	<!--
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
	<script src="assets/js/Chart-2.6.0.min.js"></script>
	-->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.bundle.min.js"></script>
	<script src="https://unpkg.com/lodash@4.17.10/lodash.min.js"></script>
  
	<body class="is-preload">
  	<%@include file="nav.jsp"%>
  	
		<%@include file="breadcrumbs.jsp"%>
		
		<div class="container-fluid">
		
	<!--
		<section id="banner2">
			<div class="inner">
				<h1>Report for <span id="customerName"></span></h1>
			</div>
		</section>
	-->
		
		<script>
		var customerId=Utils.getParameterByName("customerId");
		
		$(document).ready(function() {
			
			// ### Get Customer Details
			httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId, function(customer){
				// ### Populate the header with the Customer Name
				//document.getElementById("customerName").innerHTML=customer.CustomerName;
				if (undefined!=setBreadcrumbs) setBreadcrumbs("assessments", customer);
			});
			
		});
		</script>
						
		<section class="wrapper">
			<div class="inner">
				
				<!-- ### Page specific stuff here ### -->
				
				
				<h2>Current Landscape</h2>
				<div class="row">
					<div class="col-sm-3">
						<canvas id="gauge-1" style="width:200px;height:110px;"></canvas>
					</div>
					<div class="col-sm-3">
						<canvas id="gauge-2" style="width:200px;height:110px;"></canvas>
					</div>
					<div class="col-sm-3">
						<canvas id="gauge-3" style="width:200px;height:110px;"></canvas>
					</div>
				</div>
				<%@include file="report-summary.jsp"%>

				
				
				<br/><br/><br/>
				<h2>Adoption Candidate Distribution</h2>
				<div class="row">
					<div class="col-sm-4">
						<style>
						  /* override the datatables formatting to compress the screen real-estate used */
							input[type=search] {
								height: 23px;
								//width: 100px;
								padding: 0px;
								line-height: 1;
							}
							.dataTables_length label select{
								height: 23px;
								width:  50px;
								padding: 0px;
							}
							.dataTables_wrapper .dataTables_paginate .paginate_button.current, .dataTables_wrapper .dataTables_paginate .paginate_button.current:hover{
								height: 23px;
								padding: 0px !important;
							}
							table.dataTable thead th, table.dataTable thead td{
								padding: 0px 0px !important;
								font-size:10pt;
							}
							table.dataTable tbody tr td{
								font-size:10pt;
							}
						</style>
						<script>
							//$(document).ready(function() {
							function redrawApplications(applicationAssessmentSummary){
							    $('#appFilter').DataTable( {
							        "data": applicationAssessmentSummary,
							        "oLanguage": { 
							        	sSearch: "",             // remove the "Search" label text
							        	sLengthMenu: "_MENU_" }, // remove the "show X entries" text
							        "scrollCollapse": true,
							        "paging":         true,
							        "lengthMenu":     [[10, 25, 50, 100, 200, -1], [10, 25, 50, 100, 200, "All"]], // page entry options
							        "pageLength" :    10, // default page entries
							        "searching" :     true,
							        "bInfo" :         false, // removes "Showing N entries" in the table footer
							        //"order" :         [[1,"desc"],[2,"desc"],[0,"asc"]],
							        "order" :         [[4,"desc"],[6,"asc"]],
							        "columns": [
							            { "data": "Id" },
							            { "data": "Name" },
							            { "data": "BusinessPriority" },
							            { "data": "WorkPriority" },
							            { "data": "Confidence", width:"15%" },
							            { "data": "Decision" },
							            { "data": "WorkEffort" },
							        ]
							        ,"columnDefs": [
							            { "targets": 0, "orderable": false, "render": function (data,type,row){
							              return "<input onclick='onChange2(this);' "+(row['Decision']!=null?"checked":"")+" type='checkbox' value='"+row['Id']+"' style='margin-right: 0rem;'/>";
							            }},
							        ]
							    } );
							};
							
							function checkAppByDefaultIf(row){
								return row['Decision']!=null
											&& (row['Decision']=="REHOST" || row['Decision']=="REFACTOR" || row['Decision']=="REPLATFORM")
							}
							//);
						</script>
				  	<div id="wrapper">
					    <div>
					    </div>
					    <div id="tableDiv">
						    <table id="appFilter" class="display" cellspacing="0" width="100%">
					        <thead>
				            <tr>
			                <th align="left"></th>
			                <th align="left" title="Application Name">Application</th>
			                <th align="left" title="Business Criticality">Critical</th>
			                <th align="left" title="Work Priority">Priority</th>
			                <th align="left" title="Confidence">Confidence</th>
			                <th align="left" title="Recommended Decision or Action to take">Decision</th>
			                <th align="left" title="Estimated Effort">Effort</th>
				            </tr>
					        </thead>
						    </table>
						  </div>
				  	</div> <!-- dtable wrapper -->
				  	
						<script>
							var appFilter=[];
							function onChange2(t){
								t.checked?appFilter.push(t.value):appFilter.splice(appFilter.indexOf(t.value),1);
								redrawBubble(applicationAssessmentSummary);
								redrawAdoptionPlan(applicationAssessmentSummary);
							}
						</script>
				  	
					</div> <!-- /col-sm-? -->
					
					<div class="col-sm-8">
					<!--\
						x=business priority, y=# of dependencies, size=effort, color=Action (REHOST=red, )
						x=business criticality, y=work priority, size=effort, color=Action (REHOST=red, )
						x=confidence, y=business criticality, size=effort, color=Action (REHOST=red, )
					-->
					
						<!--
						bubble chart
						x=biz priority
						y=deps (inbound)
						color?=action
						size=effort
						//transparency=certainty
						-->
						
						<script>
						  var decisionColors=[];
						  // colors got from https://brand.redhat.com/elements/color/
						  decisionColors['REHOST']    ="#92d400"; //green
						  decisionColors['REPLATFORM']="#f0ab00"; //amber
						  decisionColors['REFACTOR']  ="#cc0000"; //red
						  decisionColors['REPURCHASE']="#3B0083"; //purple
						  decisionColors['RETAIN']    ="#A3DBE8"; //light blue
						  decisionColors['RETIRE']    ="#004153"; //dark blue
						  decisionColors['NULL']      ="#808080"; //grey
						  var sizing=[];
						  sizing['0']=10;
						  sizing['SMALL']=15;
						  sizing['MEDIUM']=27;
						  sizing['LARGE']=36;
						  sizing['XLarge']=45;
						  var randomNumbers=[];
						  
						  var bubbleChart;
						  
						  function getData(summary){
								var datasets = _.chain(summary)
								.filter(summaryItem => (appFilter.includes(summaryItem.Id)))
								.map(app => {
									return {
										label: app.Name,
										backgroundColor: app.Decision ? decisionColors[app.Decision] : decisionColors.NULL,
										data: [
											{
												x: app.Confidence || 0,
												y: app.BusinessPriority || 0,
												r: sizing[app.WorkEffort || 0]
											}
										]
									}
								 })
								 .value()
								return {datasets}
						  }
						  
						  function getDataOriginal(summary){
						    	
								var datasets=[];
								var i;
								for(i=0;i<summary.length;i++){
									var app=summary[i];
									
									if (!appFilter.includes(app['Id'])) continue;
									
									var dataset={};
									
									var name=app['Name'];
									var businessPriority=app['BusinessPriority'];
									var workPriority=app['WorkPriority'];
									var decision=app['Decision'];
									var workEffort=app['WorkEffort'];
									//var inboundDependencies=5; // TODO: not implemented in the back end yet
									var inboundDependencies=randomNumbers[i]
									var confidence=app['Confidence'];
									
									//TODO: this shouldnt be possible unless the assessment is incomplete
									if (businessPriority==null) businessPriority=0;
									if (workEffort==null) workEffort=0;
									if (confidence==null) confidence=0;
									
									// label
									dataset['label']=[name];
									
									//data points
									dataset['data']=[];
									dataset['data'].push({"x":confidence-50,"y":businessPriority-5,"r":sizing[workEffort]});
									
									// color
									if (decision!=null){
										if (greyscale){
											dataset['backgroundColor']=decisionColors.NULL;
										}else{
											dataset['backgroundColor']=decisionColors[decision];
										}
									}else{
										dataset['backgroundColor']=decisionColors.NULL;
									}
									
									datasets.push(dataset);
								}
								
								var result={datasets};
								//console.log(JSON.stringify(datasets));
								return result;
						  }
						  
						  function redrawBubble(applicationAssessmentSummary, initial){
						  	//console.log("redraw -> "+initial);
						  	
						  	var summary=JSON.parse(JSON.stringify(applicationAssessmentSummary));
								
								// build a map of AppId's to App
								var appIdToAppMap=[];
								var appNameToAppMap=[];
								for(i=0;i<summary.length;i++){
									appIdToAppMap[summary[i].Id]=summary[i];
									appNameToAppMap[summary[i].Name]=summary[i];
								}
								
								// reference the OutboundDeps App Id's into App Names and add them in a field called DependsOn for easy reference when building the charts
								for(i=0;i<summary.length;i++){
									if (null!=summary[i]['OutboundDeps']){
										summary[i]['DependsOn']=[];
										//var dependsOn=[];
										for(d=0;d<summary[i]['OutboundDeps'].length;d++)
										  summary[i]['DependsOn'].push(appIdToAppMap[summary[i]['OutboundDeps'][d]].Name);
									}
								}
								
								//console.log(JSON.stringify(summary));
								
								if (!initial){
									//bubbleChart.destroy();
								}else{
									$('#appFilter tbody tr td input[type=checkbox]:checked').each(function () {
										appFilter.push(this.value);
									});
								}
								
								if (bubbleChart!=null){
									bubbleChart.data=getDataOriginal(summary);
									bubbleChart.options.animation.duration=initial?1000:1;
									bubbleChart.update();
									return;
								}
								
								//console.log(JSON.stringify(getDataOriginal(summary)));
								
							  var ctx=document.getElementById("bubbleChart").getContext('2d');
								bubbleChart=new Chart(ctx,{
									"type":"bubble",
									"data": 
										getDataOriginal(summary)
									,
									options:{
									  legend: {
									  	display: false,
									  	position: "top"
									  },
										animation: {
											duration: 1000
										},
										tooltips:{
											enabled: false
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
													suggestedMin: -5,
													suggestedMax: 5,
													beginAtZero: true
												},
												scaleLabel:{
													display: true,
													labelString: "Business Criticality",
												}
											}],
											xAxes: [{
												display: true,
												ticks: {
													display: false,
													suggestedMin: -50,
													suggestedMax: 50,
													beginAtZero: true
												},
												scaleLabel:{
													display: true,
													labelString: "Confidence",
												}
											}],
										}
									}
								});
								//bubbleChart.generateLegend();
								
								Chart.pluginService.register({
								  beforeDraw: function(chart) {
								    var width = chart.chart.width,
								        height = chart.chart.height,
								        ctx = chart.chart.ctx,
								        type = chart.config.type;
								    
								    if (type == 'bubble'){
								      //ctx.restore();
									    ctx.clearRect(0, 0, chart.chart.width, chart.chart.height);
								      var fontSize = 1.1;
								      ctx.font = fontSize + "em sans-serif";
								      ctx.textBaseline = "middle"
								      ctx.fillStyle="#555";
											
											var topLeftText    ="Impactful but not advisable to move",  topLeftX=((width/4)*1)-(ctx.measureText(topLeftText).width/2), topLeftTextY=15;
											var topRightText   ="Impactful and migratable",              topRightX=((width/4)*3)-(ctx.measureText(topRightText).width/2), topRightTextY=15;
											var bottomLeftText ="Inadvisable",    bottomLeftX=((width/4)*1)-(ctx.measureText(bottomLeftText).width/2), bottomLeftTextY=chart.chartArea.bottom-15;
											var bottomRightText="Trivial but migratable",                bottomRightX=((width/4)*3)-(ctx.measureText(bottomRightText).width/2), bottomRightTextY=chart.chartArea.bottom-15;
											
											// quadrant text
									    ctx.fillText(topLeftText, topLeftX, topLeftTextY);
									    ctx.fillText(topRightText, topRightX, topRightTextY);
									    ctx.fillText(bottomLeftText, bottomLeftX, bottomLeftTextY);
									    ctx.fillText(bottomRightText, bottomRightX, bottomRightTextY);
									    
									    //var goldilocks=$('#goldilocks').val();
											var x=(width/2)+15, y=0, w=(width/2)-15, h=(height/2)-13;
									    
									    //if (goldilocks=="Solid"){
										  //  // quadrant color / fill top right
										  //  ctx.fillStyle = "rgba(46, 212, 0, 0.5)";
											//	ctx.globalAlpha = 0.4;
										  //  ctx.fillRect(x,y,w,h);
										  //  ctx.globalAlpha = 1.0;
									    //}else if (goldilocks=="Gradient"){
									    	// adjust the width of the green gradient area (higher = wider)
										    var adjustment=140;
										    x=x-adjustment;
										    w=w+adjustment;
										    
												var grd=ctx.createLinearGradient(x,0,width-150,0);
												var grdOpacity=0.12;
												grd.addColorStop(0,"rgba(255,255,255,0)");
												grd.addColorStop(1,"rgba(46, 212, 0, "+grdOpacity+")");
												
												ctx.fillStyle=grd;
												ctx.fillRect(x,y,w,(h*2)-5);
									    //}
									    
									    
								    	// draw the dependency line(s)
									    if (!dependencies){
									    	//ctx.fillStyle = "rgba(0, 0, 0, 0.8)";
									    	ctx.strokeStyle = 'rgba(0,0,0,0.2)';
										    ctx.lineWidth=5;
									    	
										    var bubbleMapModels=[];
										    for(i=0;i<chart.config.data.datasets.length;i++){
										    	var model_x=chart.getDatasetMeta(i).data[0]._model.x;
										    	var model_y=chart.getDatasetMeta(i).data[0]._model.y;
										    	var model_r=chart.getDatasetMeta(i).data[0]._model.radius;
										    	bubbleMapModels[chart.config.data.datasets[i].label[0]]={Name:chart.config.data.datasets[i].label[0],x:model_x,y:model_y,r:model_r};
										    	//console.log(chart.config.data.datasets[i].label[0] +" = "+JSON.stringify({x:model_x,y:model_y}));
										    }
										    
										    for(i=0;i<chart.config.data.datasets.length;i++){
										    	var bubble=chart.config.data.datasets[i];
										    	var r=bubble.data[0].r;
										    	var label=bubble.label[0];
										    	var x=bubbleMapModels[label].x;
										    	var y=bubbleMapModels[label].y;
										    	
										    	if (undefined==appNameToAppMap[label].DependsOn) continue;
										    	
										    	for (var d=0;d<appNameToAppMap[label].DependsOn.length;d++){
										    		var targetBubble=bubbleMapModels[appNameToAppMap[label].DependsOn[d]];
										    		
										    		if (targetBubble==null) continue; //dependency may not be displayed on the graph
										    		
										    		var radius=targetBubble.r;
										    		var angle  = Math.atan2(targetBubble.y - y, targetBubble.x - x) * 180 / 3.14;
										    		var height = targetBubble.y - y;
												    var width  = targetBubble.x - x;
												    var length = Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2));
												    var subtractWidth = radius * width / length;
												    var subtractHeight = radius * height / length;
										    		
										    		targetBubble.x-=subtractWidth;
										    		targetBubble.y-=subtractHeight;
										    		
										    		ctx.beginPath();
										    		//canvas_arrow(ctx,x,y,targetBubble.x, targetBubble.y);
										    		
										    		// draw arrow
												    var arrowSize=20;   // length of arrow head in pixels
												    var angle=Math.atan2(targetBubble.y-y,targetBubble.x-x);
												    ctx.moveTo(x, y);
												    ctx.lineTo(targetBubble.x, targetBubble.y);
												    ctx.lineTo(targetBubble.x-arrowSize*Math.cos(angle-Math.PI/6),targetBubble.y-arrowSize*Math.sin(angle-Math.PI/6));
												    ctx.moveTo(targetBubble.x, targetBubble.y);
												    ctx.lineTo(targetBubble.x-arrowSize*Math.cos(angle+Math.PI/6),targetBubble.y-arrowSize*Math.sin(angle+Math.PI/6));
												    
										    		ctx.stroke();
										    		console.log("BubbleGraph:: drawn dependency line from '"+label+"' to '"+targetBubble.Name+"' which is from '"+x+","+y+"' to '"+targetBubble.x+","+targetBubble.y+"'");
										    		
										    	}
										    	
										    }
										  }
									    
									    //console.log("chart="+JSON.stringify(chart.config));
									    
											ctx.save();
										}								    
									}
								});
								
						  }
						  
							var data;
							var applicationAssessmentSummary;
							var appIdToNameMap=[];
							httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId+"/applicationAssessmentSummary", function(summary){
								applicationAssessmentSummary=summary;
								for(i=0;i<summary.length;i++){
									appIdToNameMap[summary.Id]=summary.Name;
								}
								redrawApplications(applicationAssessmentSummary);
								redrawBubble(applicationAssessmentSummary, true);
								redrawAdoptionPlan(applicationAssessmentSummary, true);
							});
							
						</script>
						
						<style>
						#bubbleLegend{
					    justify-content:space-around;
					    list-style-type:none;
						}
						#bubbleLegend ul li{
//							float: right;
//							position: relative;
//							top: 30px;
//							left: -30px;
							display: inline-block;
							width: 120px;
						}
						</style>
						
						<div id="bubbleLegend">
							<ul>
								<li>
									<svg height="25" width="200">
									  <rect width="120" height="25" stroke="black" style="fill:#92d400;stroke-width:0;stroke:rgb(0,0,0)" />
									  <text x="7" y="17" font-family="Overpass" font-size="13" fill="#333">REHOST</text>
									</svg>
								</li>
								<li>
									<svg height="25" width="200">
									  <rect width="120" height="25" stroke="black" style="fill:#f0ab00;stroke-width:0;stroke:rgb(0,0,0)" />
									  <text x="7" y="17" font-family="Overpass" font-size="13" fill="#EEE">REPLATFORM</text>
									</svg>
								</li>
								<li>
									<svg height="25" width="200">
									  <rect width="120" height="25" stroke="black" style="fill:#cc0000;stroke-width:0;stroke:rgb(0,0,0)" />
									  <text x="7" y="17" font-family="Overpass" font-size="13" fill="#EEE">REFACTOR</text>
									</svg>
								</li>
								<li>
									<svg height="25" width="200">
									  <rect width="120" height="25" stroke="black" style="fill:#3B0083;stroke-width:0;stroke:rgb(0,0,0)" />
									  <text x="7" y="17" font-family="Overpass" font-size="13" fill="#EEE">REPURCHASE</text>
									</svg>
								</li>
								<li>
									<svg height="25" width="200">
									  <rect width="120" height="25" stroke="black" style="fill:#A3DBE8;stroke-width:0;stroke:rgb(0,0,0)" />
									  <text x="7" y="17" font-family="Overpass" font-size="13" fill="#333">RETAIN</text>
									</svg>
								</li>
								<li>
									<svg height="25" width="200">
									  <rect width="120" height="25" stroke="black" style="fill:#004153;stroke-width:0;stroke:rgb(0,0,0)" />
									  <text x="7" y="17" font-family="Overpass" font-size="13" fill="#EEE">RETIRE</text>
									</svg>
								</li>
								<li>
									<svg height="25" width="200">
									  <rect width="120" height="25" stroke="black" style="fill:#808080;stroke-width:0;stroke:rgb(0,0,0)" />
									  <text x="7" y="17" font-family="Overpass" font-size="13" fill="#EEE">NOT REVIEWED</text>
									</svg>
								</li>
							</ul>
						</div>
						
						
						<div class="chartjs-wrapper" style="width:850px;height:530px;">
							<canvas id="bubbleChart" class="chartjs" width="850" height="531"></canvas>
						</div>
   					
						<!--select id="goldilocks" onchange="redrawBubble(applicationAssessmentSummary, false);">
							<option>None</option>
							<option>Solid</option>
							<option>Gradient</option>
						</select-->
						<script>
							var greyscale=true;
							var dependencies=true;
							
							function greyscaleToggle(t){
								t.value=="Show Decisions"?t.value="Hide Decisions":t.value="Show Decisions";
								greyscale=(t.value=="Show Decisions");
								redrawBubble(applicationAssessmentSummary, false);
							}
							function dependenciesToggle(t){
								t.value=="Show Dependencies"?t.value="Hide Dependencies":t.value="Show Dependencies";
								dependencies=(t.value=="Show Dependencies");
								redrawBubble(applicationAssessmentSummary, false);
							}
							
						</script>
						<input class="btn btn-default form-control" style="height:28px;padding:0px;width:120px;font-size:10pt;line-height:1rem;" type="button" id="greyscale" value="Show Decisions" onclick="greyscaleToggle(this);"/>
						<input class="btn btn-default form-control" style="height:28px;padding:0px;width:150px;font-size:10pt;line-height:1rem;" type="button" id="dependencies" value="Show Dependencies" onclick="dependenciesToggle(this);"/>
						
					</div> <!-- col-sm-? -->
				</div> <!-- /row -->
				
				
				<br/><br/><br/>
				<h2>Suggested Adoption Plan</h2>
				<div class="row">
					<div class="col-sm-8">
						<canvas id="adoption" style="width: 500px; height: 100px;"></canvas>
						<%@include file="report-adoption.jsp"%>
					</div> <!-- col-sm-? -->
				</div> <!-- /row -->
				
				
				
				<br/><br/><br/>
				<h2>
				<a class="twisty" style="text-decoration:none" role="button" aria-expanded="false" aria-controls="collapser" data-toggle="collapse" href="#collapser" >
					<img src="assets/images/twisty-off.png" style="width:30px;"/>
					<img src="assets/images/twisty-on.png"  style="width:30px;display:none"/>
				</a>
				<!--
				<i class="glyphicon glyphicon-triangle-right"></i>
				-->
				Identified Risks</h2>
				<div class="row">
					<div class="col-sm-10">
						
						<script>
							$(".twisty").click(function(){
							  $('img',this).toggle();
							});
							
							function drawRisks(data){
							  var risks=[];
							  if (data.risks!=undefined) risks=data.risks;
							  
						    $('#risks').DataTable( {
						        "data": risks,
						        "oLanguage": { 
						        	sSearch: "",             // remove the "Search" label text
						        	sLengthMenu: "_MENU_"    // remove the "show X entries" text
						        },
						        "scrollCollapse": true,
						        "paging":         false,
						        //"lengthMenu": [[10, 25, 50, 100, 200, -1], [10, 25, 50, 100, 200, "All"]], // page entry options
						        "pageLength" : -1, // default page entries
						        "bInfo" : false, // removes "Showing N entries" in the table footer
						        "columns": [
						            { "data": "question" },
						            { "data": "answer" },
						            { "data": "offendingApps" },
							        ],
						    } );
						  }
						</script>
						A list of questions with answers that that could cause migratory risk to a container platform.
						<div class="collapse" id="collapser">
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
					
					
<!--
<div class="card">
    <div class="card-header" id="heading-example">
        <h5 class="mb-0">
            <a data-toggle="collapse" href="#collapse-example" aria-expanded="true" aria-controls="collapse-example">
                <i class="fa glyphicon glyphicon-triangle-right pull-right"></i>
                this is some text
            </a>
        </h5>
    </div>
    <div id="collapse-example" class="collapsed hidden" aria-labelledby="heading-example">
        <div class="card-block">
            and this is more text
            safsdfsdf
            gfdlkjldfg
        </div>
    </div>
</div>
						<style>
						.card-header .fa {
						  transition: .3s transform ease-in-out;
						}
						.card-header .collapsed .fa {
						  transform: rotate(90deg);
						}
						</style>
-->
					
					</div>
				</div>

				<div class="row">
				&nbsp;
				</div>

			</div>
		</section>

	</body>
	
	<br/><br/><br/><br/><br/><br/><br/><br/>
</html>




				<script>
					httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId+"/report", function(report){
						new Chart(document.getElementById("gauge-1"),buildGuage(report.assessmentSummary.Easy,report.assessmentSummary.Total, "rgb(146,212,0)","rgb(220, 220, 220)","Cloud-Native Ready"));
						new Chart(document.getElementById("gauge-2"),buildGuage(report.assessmentSummary.Medium,report.assessmentSummary.Total, "rgb(240,171,0)","rgb(220, 220, 220)","Modernizable"));
						new Chart(document.getElementById("gauge-3"),buildGuage(report.assessmentSummary.Hard,report.assessmentSummary.Total, "rgb(204, 0, 0)","rgb(220, 220, 220)","Unsuitable for Containers"));
						drawRisks(report);
					});
				</script>