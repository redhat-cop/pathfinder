<!DOCTYPE HTML>
<html>
  
  <%@include file="head.jsp"%>
  
  <link href="assets/css/main.css" rel="stylesheet" />
  <link href="assets/css/breadcrumbs.css" rel="stylesheet" />
	
	<script src="datatables-functions.js"></script>
	
	<body class="is-preload">
  	<%@include file="nav.jsp"%>
		
  	<div id="breadcrumbs">
			<ul class="breadcrumb">
				<li><a href="manageCustomers.jsp">Admin</a></li>
			</ul>
		</div>
		
		<section class="wrapper">
			<div class="inner">
				
				<!-- ### Page specific stuff here ### -->
				
				<!-- ### Pie Chart Dependencies-->
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript" src="https://canvasjs.com/assets/script/jquery.canvasjs.min.js"></script>
        
				<script>
				var customerId=Utils.getParameterByName("customerId");
				
				$(document).ready(function() {
					var processedApps=0;
					var done=false;
					var totalAssessed=0;
					var totalUnassessed=0;
					
					// ### Get Customer Details
					httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId, function(customer){
						// ### Populate the header with the Customer Name
						document.getElementById("customerName").innerHTML=customer.CustomerName;
					});
					
					// ### Get Application & Results
					httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId+"/applications/", function(applications){
						
						for (i=0;i<applications.length;i++){
							// ### Get Application Assessments
							httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId+"/applications/"+applications[i].Id+"/assessments/", function(assessments){
								totalAssessed+=assessments.length>0;
								totalUnassessed+=assessments.length==0;
								processedApps+=1;
								done=applications.length==processedApps;
							});
						};
						
						setTimeout(loadAssessmentChart, 100);
						
					});					
					
					
					function loadAssessmentChart(){
					  console.log("Apps Processed="+processedApps +" (Done? = "+done+")");
					  if (done>0){
							// ### Pie Chart
							google.charts.load('current', {'packages':['corechart']});
							google.charts.setOnLoadCallback(drawChart);
							function drawChart() {
							  var data = google.visualization.arrayToDataTable([
							    ['Task', 'Hours per Day'],
							    ['Assessed',     totalAssessed],
							    ['Not Assessed', totalUnassessed]
							  ]);
							  var options = {
							    backgroundColor: 'transparent',
							    title: 'Assessment Status',
							    pieHole: 0.2,
							    is3D: true,
							  };
							  var chart = new google.visualization.PieChart(document.getElementById('piechartAss'));
							  chart.draw(data, options);
							}
						}else{
							setTimeout(loadChart, 100);
						}
					}
					
					});
				
				</script>
				
				<!-- ### Pie Chart Canvas -->
				<div id="piechartAss" style="width: 500px; height: 500px; float: left;"></div>
					
				<div class="highlights">
				</div>
			</div>
		</section>
		
	</body>
</html>



