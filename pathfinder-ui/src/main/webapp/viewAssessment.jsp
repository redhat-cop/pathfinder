<!DOCTYPE HTML>
<html>
  
  <%@include file="head.jsp"%>
  
  <link href="assets/css/main.css" rel="stylesheet" />
  <link href="assets/css/breadcrumbs.css" rel="stylesheet" />
	
  <!-- #### DATATABLES DEPENDENCIES ### -->
  <!-- Firefox doesnt support link imports yet
  <link rel="import" href="datatables-dependencies.jsp">
  -->
  <%@include file="datatables-dependencies.jsp"%>
	
	<!-- #### CHARTS DEPENDENCIES ### -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
		
	<body class="is-preload">
  	<%@include file="nav.jsp"%>
  	
  	<%@include file="breadcrumbs.jsp"%>
		
		<section class="wrapper">
			<div class="inner">
				
				<!-- ### Page specific stuff here ### -->
				
				<script>
				var customerId=Utils.getParameterByName("customer");
				var appId=Utils.getParameterByName("app");
				var assessmentId=Utils.getParameterByName("assessment");
				//var beenReviewed=false;
				
				$(document).ready(function() {
					httpGetObject(Utils.SERVER+'/api/pathfinder/customers/'+customerId+"/applications/"+appId+"?custom=assessment.NOTES,customer.name", function(application){
						//document.getElementById("breadcrumb2").innerHTML=application.Name;
						
						document.getElementById("applicationName").innerHTML=application.Name;
						document.getElementById("applicationDescription").innerHTML=application.Description!=""?application.Description:"No description provided";
						document.getElementById("assessmentNotes").innerHTML=application.CustomFields['assessment.NOTES']!=undefined?application.CustomFields['assessment.NOTES']:"None";
						
						console.log("application.CustomFields = "+JSON.stringify(application.CustomFields));
						
						if (undefined!=initTabs){
				      initTabs("assessments", customerId, application.CustomFields['customer.name']);
						}
						
						if (null!=application['Review'] && Utils.getParameterByName("review")!=null){
							httpGetObject(Utils.SERVER+'/api/pathfinder/customers/'+customerId+"/applications/"+appId+"/review/"+application['Review'], function(review){
								
								$('#ReviewDecision').val(review['ReviewDecision']);
								$('#WorkEffort').val(review['WorkEffort']);
								$('#BusinessPriority').val(review['BusinessPriority']);
								$('#WorkPriority').val(review['WorkPriority']);
								$('#ReviewNotes').val(review['ReviewNotes']);
								
								//console.log(JSON.stringify("review2="+review));
							});						
						}
						
					});
					
					
					//httpGetObject(Utils.SERVER+'/api/pathfinder/customers/'+customerId+"/applications/"+appId+"/review", function(review){
					//	console.log(JSON.stringify("review1="+review));
					//});
					
					//// ### Get Customer Details
					//httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId, function(customer){
					//	// ### Populate the header with the Customer Name
					//	//document.getElementById("customerName").innerHTML=customer.CustomerName;
					//	//document.getElementById("breadcrumb1").innerHTML="<a href='assessments-v2.jsp?customerId="+customer.CustomerId+"'>"+customer.CustomerName+"</a>";
					//	
				  //  if (undefined!=setBreadcrumbs){
				  //    setBreadcrumbs("assessments", customer);
				  //  }
					//	
					//});
					
				});
				
				</script>

<script>
var defaultRadiusMyChart;
var addRadiusMargin = -10;
var currentSelectedPieceLabel = "";

function filterDatatable(myChart){
	// ### Filters the datatable based on the pie chart selection
  var activePoints=myChart.getElementsAtEvent(event);
  if (activePoints[0]) {
    var chartData=activePoints[0]['_chart'].config.data;
    var idx=activePoints[0]['_index'];
		
    var label=chartData.labels[idx];
    var value=chartData.datasets[0].data[idx];
		
    var table=$('#example').DataTable();
    table.columns(2).search(label).draw();
  }
}
function resetDatatable(){
	var table=$('#example').DataTable();
  table.columns(2).search("").draw();
}

function onClickHandlers(myChart) {
	var defaultRadiusMyChart = myChart.outerRadius;
	$('#pieChart').on('click', function (event) {
		
		// ### Explode segment					      
    var activePoints = myChart.getElementsAtEvent(event);
    
    if (activePoints.length > 0) {    	    
      //get the internal index of slice in pie chart
      var clickedElementindex = activePoints[0]["_index"];

      //get specific label by index
      var clickedLabel = myChart.data.labels[clickedElementindex];

      if (currentSelectedPieceLabel.toUpperCase() == "") {
        // no piece selected yet, save piece label
        currentSelectedPieceLabel = clickedLabel.toUpperCase();

				filterDatatable(myChart);

        // clear whole pie
        myChart.outerRadius = defaultRadiusMyChart;
        myChart.update();

        // update selected pie
        activePoints[0]["_model"].outerRadius = defaultRadiusMyChart + addRadiusMargin;
      }
      else {
        if (clickedLabel.toUpperCase() == currentSelectedPieceLabel.toUpperCase()) {
          // already selected piece clicked, clear the chart
          currentSelectedPieceLabel = "";

					resetDatatable();

          // clear whole pie
          myChart.outerRadius = defaultRadiusMyChart;
          myChart.update();

          // update selected pie
          activePoints[0]["_model"].outerRadius = defaultRadiusMyChart;
        }
        else {
          // other piece clicked
          currentSelectedPieceLabel = clickedLabel.toUpperCase();

					filterDatatable(myChart);

          // clear whole pie
          myChart.outerRadius = defaultRadiusMyChart;
          myChart.update();

          // update the newly selected piece
          activePoints[0]["_model"].outerRadius = defaultRadiusMyChart + addRadiusMargin;
        }
      }
      myChart.render(500, false);
    }
  })};
</script>
				
				<div class="row title-row">
					<div class="col-sm-4">
						<%if ("true".equalsIgnoreCase(request.getParameter("review"))){%>
							<h2>Architect Review</h2>
						<%}else{%>
							<h2>Assessment Summary</h2>
						<%}%>
					</div>
					<div class="col-sm-8">
						<h2><span id="applicationName">Loading...</span></h2>
					</div>
				</div>
				
				
				<div class="row">
					<div class="col-sm-4">
						
						<!-- ### CHART GOES HERE -->
						<script>
							$(document).ready(function() {
								
								httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId+"/applications/"+appId+"/assessments/"+assessmentId+"/viewAssessmentSummary", function(viewAssessmentSummary){
									var data=viewAssessmentSummary
									
									var i;
									var newdata={};
									for (i=0;i<data.length;i++) { 
										if (newdata[data[i]['rating']]==undefined) newdata[data[i]['rating']]=0;
									  newdata[data[i]['rating']]=newdata[data[i]['rating']]+1;
									}
									
									var result={};
									result.labels=[];
									result.datasets=[];
									result.datasets[0]={data:[],backgroundColor:[]}
									
									i=0
									for(var key in newdata){
									  result.labels[i]=key;
									  result.datasets[0].data[i]=newdata[key];
									  result.datasets[0].backgroundColor[i]=Utils.chartColors[key];
									  i=i+1;
									}
									
									// ### LOAD CHART DATA ###
									var ctx = document.getElementById("pieChart").getContext("2d");
									var myDoughnutChart = new Chart(ctx, {
										type: 'doughnut',
										data: result,
							    	options: {
								    	hover: {
									      onHover: function(e, el) {
									         $("#pieChart").css("cursor", e[0] ? "pointer" : "default");
									      }
									   	},
							        legend: {
						            display: false,
		            		}}
									});
									
    							onClickHandlers(myDoughnutChart);  
									
							    // ### LOAD DATATABLE DATA ###
									$('#example').DataTable( {
							        "data": data,
							        "scrollCollapse": true,
							        "paging":         false,
							        "lengthMenu": [[10, 25, 50, 100, 200, -1], [10, 25, 50, 100, 200, "All"]], // page entry options
							        "pageLength" : 10, // default page entries
							        "bInfo" : false, // removes "Showing N entries" in the table footer
							        "searching" : true,
							        //"order" : [[1,"desc"],[2,"desc"],[0,"asc"]],
							        "columns": [
							            { "data": "question" },
							            { "data": "answer" },
							            { "data": "rating" },
							        ]
							        ,"columnDefs": [
							        		{ "targets": 2, "orderable": true, "render": function (data,type,row){
							        		  return "<span style='color:"+Utils.chartColors[row["rating"]]+"'>"+row['rating']+"</span>";
													}}
							        ]
							    } );
							    $('#example_filter').css({display:"none"});
							    
								});
							});
						</script>
						<canvas id="pieChart"></canvas>
						
					</div>
					<div class="col-sm-8">

						<div class="row">
							<div class="col-sm-12">

								<div class="col-sm-3">
									<b>Application Description:</b>
								</div>
								<div class="col-sm-9">
									 <span id="applicationDescription">Loading...</span><br/>
								</div>
								
								<!-- 
								put this one in later when we have time: https://codepen.io/trevanhetzel/pen/rOVrGK
								Business Criticality: <span id="businessCriticality">Loading...</span>
								-->

								<div class="col-sm-3">
									<b>Assessment Notes:</b> 
								</div>
								<div class="col-sm-9">
									<span id="assessmentNotes">Loading...</span>
								</div>

<br/><br/><br/>
								
						<div class="row">
							<div class="col-sm-8">
								
								<!-- ### TITLE SECTION GOES HERE -->
								
							
							</div>
							
							
							<div class="col-sm-10">
								<!-- ### REVIEW (OPTIONAL) GOES HERE -->
								<%
								if ("true".equalsIgnoreCase(request.getParameter("review"))){
								%>
								
								<p>Please use this section to provide your assessment of the possible migration/modernisation plan and an effort estimation.</p>
								
								<form action="/api/pathfinder/customers/<%=request.getParameter("customer")%>/applications/<%=request.getParameter("app")%>/" id="form" method="post">
									<input type="hidden" id="AssessmentId" name="AssessmentId" value="<%=request.getParameter("assessment")%>"/>
									<!--input type="hidden" id="ReviewTimestamp" name="ReviewTimestamp" value="2018-03-14 03:23:29pm"/-->
									<div class="row">
										<div class="col-sm-2">
											<h4>Proposed Action</h4>
										</div>
										<div class="col-sm-2">
											<h4>Effort Estimate</h4>
										</div>
										<div class="col-sm-2">
											<h4>Business Criticality</h4>
											(1=low, 10=high)
										</div>
										<div class="col-sm-2">
											<h4>Work Priority</h4>
											(1=low, 10=high)
										</div>
										<div class="col-sm-4">
											<h4>Supporting Notes</h4>
										</div>
									</div>
									<div class="row">
										<div class="col-sm-2">
											<select name="ReviewDecision" id="ReviewDecision">
												<option value="REHOST">Re-host</option>
												<option value="REPLATFORM">Re-platform</option>
												<option value="REFACTOR">Refactor</option>
												<option value="REPURCHASE">Repurchase</option>
												<option value="RETIRE">Retire</option>
												<option value="RETAIN">Retain</option>
											</select>
										</div>
										<div class="col-sm-2">
											<select name="WorkEffort" id="WorkEffort">
												<option value="SMALL">Small</option>
												<option value="MEDIUM">Medium</option>
												<option value="LARGE">Large</option>
												<option value="XLarge">Extra Large</option>
											</select> 
										</div>
										<div class="col-sm-2">
											<select type="text" id="BusinessPriority" name="BusinessPriority">
												<option>1</option>
												<option>2</option>
												<option>3</option>
												<option>4</option>
												<option>5</option>
												<option>6</option>
												<option>7</option>
												<option>8</option>
												<option>9</option>
												<option>10</option>
											</select>
										</div>
										<div class="col-sm-2">
											<select type="text" id="WorkPriority" name="WorkPriority">
												<option>1</option>
												<option>2</option>
												<option>3</option>
												<option>4</option>
												<option>5</option>
												<option>6</option>
												<option>7</option>
												<option>8</option>
												<option>9</option>
												<option>10</option>
											</select>
										</div>
										<div class="col-sm-4">
											<textarea id="ReviewNotes" name="ReviewNotes" style="width:325px;height:100px;"></textarea>
										</div>
									</div>
									
									<div class="row" style="height:10px;">
										<!-- spacer between review options and submit button -->
									</div>
									
									<div class="row">
										<div class="col-sm-12" style="text-align:right;">
											<input type="button" onclick="postReview('form');" value="Submit Review">
											<!--
											<input type="submit" onclick="postReview('form');" value="Submit Review">
											-->
											
										</div>
								
									</div>
									<script>
										function postReview(formId){
									    var form=document.getElementById(formId);
										  
									    var data = {};
									    for (var i = 0, ii = form.length; i < ii; ++i) {
										    if (form[i].name) data[form[i].name]=form[i].value;
										  }
										  
										  
										  var callback=new function(response){
										    // wait for the post response before redirecting or else the post will be cancelled
										    console.log("Callback::after post: response= "+response);
										    // TODO: this would be much nicer if the server provided a 302 so we could use a submit rather than an artificial wait
										    //window.location.href = "assessments-v2.jsp?customerId="+customerId;
										  };
										  
										  console.log("POSTING: "+JSON.stringify(data));
									    //postWait(addAuthToken(Utils.SERVER+"/api/pathfinder/customers/"+customerId+"/applications/"+appId+"/review"), data, callback);
									    
											var xhr = new XMLHttpRequest();
											xhr.open("POST", addAuthToken(Utils.SERVER+"/api/pathfinder/customers/"+customerId+"/applications/"+appId+"/review"), true);
											xhr.setRequestHeader("Content-type", "application/json");
											xhr.send(JSON.stringify(data));
											xhr.onloadend = function () {
											  //console.log("PostWait::status = "+xhr.status);
											  //console.log("PostWait::status = "+xhr.responseText);
											  if (xhr.status==200){
											    console.log("PostWait::http 200 returned ok, redirecting...");
											  	window.location.href = "assessments-v2.jsp?customerId="+customerId;
											  }
											};
										}
									</script>
									
								</form>
								<!--
								<a href="results.php?customer=<?php echo $_REQUEST['customer'] ?>"><button>Return to Results</button></a>
								-->
															
								<%
								}
								%>
							</div>
							
							
							<div class="col-sm-10">
								<!-- ### DETAILS DATATABLE GO HERE -->
							    <div id="buttonbar">
							    </div>
							    <div id="tableDiv">
								    <table id="example" class="display" cellspacing="0" width="100%">
							        <thead>
						            <tr>
					                <th align="left">Question</th>
					                <th align="left">Answer</th>
					                <th align="left">Rating</th>
						            </tr>
							        </thead>
								    </table>
								  </div>
								
							</div>
						</div>
						
					</div>
				</div>
				
			</div>
		</section>
		
	</body>
</html>


