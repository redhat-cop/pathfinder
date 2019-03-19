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
  
	<body class="is-preload">
  	<%@include file="nav.jsp"%>
  	
  	<div id="breadcrumbs">
			<ul class="breadcrumb">
				<li><a href="manageCustomers.jsp">Customers</a></li>
				<li><span id="breadcrumb"></span> Assessments</li>
			</ul>
		</div>
		
		<section class="wrapper">
			<div class="inner">
				
				<!-- ### Page specific stuff here ### -->
				
				<script>
					var customerId=Utils.getParameterByName("customerId");
					var appsCount=assessed=unassessed=notReviewed=reviewed=0;
					
					$(document).ready(function() {
						var done=false;
						
						// ### Get Customer Details
						httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId, function(customer){
							// ### Populate the header with the Customer Name
							document.getElementById("customerName").innerHTML=customer.CustomerName;
							document.getElementById("breadcrumb").innerHTML=customer.CustomerName;
						});
						
						// ### Populate the progress bar
						httpGetObject(Utils.SERVER+'/api/pathfinder/customers/'+customerId+"/applicationAssessmentProgress", function(progress){
						  if (progress.Appcount.toString() == progress.Assessed.toString() && progress.Appcount.toString() == progress.Reviewed.toString()) {
								document.getElementById("allDone").innerHTML="<img src=images/ok-48.png>";
						  }
						  //$('#jqmeter-assessed').jQMeter({goal:progress.Appcount,raised:progress.Assessed,width:'290px',height:'40px',bgColor:'#dadada',barColor:'#9b9793',animationSpeed:100,displayTotal:true});
						  //$('#jqmeter-reviewed').jQMeter({goal:progress.Appcount,raised:progress.Reviewed,width:'290px',height:'40px',bgColor:'#dadada',barColor:'#9b9793',animationSpeed:100,displayTotal:true});
							
							setProgress("assessedProgress", (100/progress.Appcount.toString())*progress.Assessed.toString(), "Assessed");
							setProgress("reviewedProgress", (100/progress.Appcount.toString())*progress.Reviewed.toString(), "Reviewed");
						});
						
					});
				
				</script>
				
				<div class="row">
					<div class="col-sm-4">
						
						<!-- ### Progress -->
						
						<!--
						<script src="assets/js/jqmeter.min.js"></script>
						Assessed
						<div id="jqmeter-assessed"></div>
						Reviewed
						<div id="jqmeter-reviewed"></div>
						<style>
							.therm{height:30px;border-radius:5px;}
							.outer-therm{margin:20px 0;}
							.inner-therm span {color: #fff;display: inline-block;float: right;font-family: Overpass;font-size: 14px;font-weight: bold;}
							.vertical.inner-therm span{width:100%;text-align:center;}
							.vertical.outer-therm{position:relative;}
							.vertical.inner-therm{position:absolute;bottom:0;}
						</style>
						-->
						<div id="allDone"></div>
						
						
						
						<script src="https://rawgit.com/kimmobrunfeldt/progressbar.js/1.0.0/dist/progressbar.js"></script>
						
						<h2>Progress</h2>
						<div id="assessedProgress" class="progress-bar"></div>
						<div id="reviewedProgress" class="progress-bar"></div>
						
						<script>
							function setProgress(progressBar, percentage, text){
								var bar = new ProgressBar.Line(document.getElementById(progressBar), {
								  strokeWidth: 100,
								  easing: 'easeInOut',
								  duration: 1400,
								  color: '#FFEA82',
								  trailColor: '#ccc',
								  trailWidth: 300,
								  svgStyle: {width: '100%', height: '100%'},
								  text: {
								    style: {
								      // Text color.
								      // Default: same as stroke color (options.color)
								      color: '#333',
								      position: 'relative',
								      right: '0px',
								      top: '-28px',
								      padding: 0,
								      margin: 0,
								      transform: null
								    },
								    autoStyleContainer: false
								  },
								  from: {color: '#cc0000'},
								  to: {color: '#32CD32'},
								  step: (state, bar) => {
								    bar.path.setAttribute('stroke', state.color);
								    bar.setText(Math.round(bar.value() * 100) + '% '+text);
								  }
								});
								bar.animate(percentage/100);  // Number from 0.0 to 1.0
							}
						</script>
						
						<a href="report.jsp?customerId=<%=request.getParameter("customerId")%>"><button>View Report</button></a>
						
						<!-- ### Pie Chart Canvas -->
						<div id="piechartAss" style="width: 500px; height: 500px; float: left;"></div>
					</div>
					<div class="col-sm-8">
						<h2>Assessments</h2>
						<!-- #### DATATABLE ### -->
						<script>
							$(document).ready(function() {
								// ### Datatable load (has to be done after customer load because some links require the customer GUID ###
								$('#example').DataTable( {
					        "ajax": {
					            //"url": 'http://localhost:8083/pathfinder-ui/api/pathfinder/customers/'+customerId+"/assessmentSummary",
					            "url": Utils.SERVER+'/api/pathfinder/customers/'+customerId+"/applicationAssessmentSummary",
					            "dataSrc": "",
					            "dataType": "json"
					        },
					        "scrollCollapse": true,
					        "paging":         false,
					        "lengthMenu": [[10, 25, 50, 100, 200, -1], [10, 25, 50, 100, 200, "All"]], // page entry options
					        "pageLength" : 10, // default page entries
					        "searching" : false,
					        "order" : [[1,"desc"],[2,"desc"],[0,"asc"]],  //reviewed, assessed then app name
					        "columns": [
					            { "data": "Name" },
					            { "data": "Assessed" },
					            { "data": "ReviewDate" },
					            { "data": "BusinessPriority" },
					            { "data": "Decision" },
					            { "data": "WorkEffort" },
					            { "data": "ReviewDate" },
					            { "data": "LatestAssessmentId" }
					        ]
					        ,"columnDefs": [
					        		{ "targets": 0, "orderable": true, "render": function (data,type,row){
					              return "<a href='viewAssessment.jsp?app="+row['Id']+"&assessment="+row['LatestAssessmentId']+"&customer="+customerId+"'>"+row["Name"]+"</a>";
											}},
					        		{ "targets": 1, "orderable": true, "render": function (data,type,row){
					              return "<span class='"+(row["Assessed"]==true?"messageGreen'>Yes":"messageRed'><a href='survey.jsp?customerId="+customerId+"&applicationId="+row['Id']+"'>No</a>")+"</span>";
											}},
											{ "targets": 2, "orderable": true, "render": function (data,type,row){
												if (row["ReviewDate"]==null && row["Assessed"]==true){
												  return "<a href='viewAssessment.jsp?review=true&app="+row['Id']+"&assessment="+row['LatestAssessmentId']+"&customer="+customerId+"'><img height='24px' src='images/review.png'></a>";
												}else if (row["ReviewDate"]==null){
													return "No";
												}else{
													return "Yes";
												}
											}},
											{ "targets": 4, "orderable": true, "render": function (data,type,row){
					              return row['Decision']==null?"":row['Decision']; 
											}},
											{ "targets": 5, "orderable": true, "render": function (data,type,row){
					              return row['WorkEffort']==null?"":row['WorkEffort'];
											}},
						            { "targets": 7, "orderable": false, "render": function (data,type,row){
					            	return row["Assessed"]!=true?"":"<a href='viewAssessment.jsp?app="+row['Id']+"&assessment="+row['LatestAssessmentId']+"&customer="+customerId+"'><img src='images/details.png'/></a>";
											}}
					        ]
						    });
						    // ### END Datatable load

							} );
						</script>
				  	<div id="wrapper">
					    <div id="buttonbar">
					    </div>
					    <div id="tableDiv">
						    <table id="example" class="display" cellspacing="0" width="100%">
					        <thead>
				            <tr>
			                <th align="left">Application</th>
			                <th align="left">Assessed</th>
			                <th align="left">Review</th>
			                <th align="left">Business Priority</th>
			                <th align="left">Decision</th>
			                <th align="left">Effort</th>
			                <th align="left">Review Date</th>
			                <th align="left"></th>
				            </tr>
					        </thead>
						    </table>
						  </div>
				  	</div>
				  	
					</div>
				</div>
				
				
				
				<div class="highlights">
				</div>
			</div>
		</section>
		
	</body>
</html>



