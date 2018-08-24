<!DOCTYPE HTML>
<html>
  
  <%@include file="head.jsp"%>
  
  <link href="assets/css/breadcrumbs.css" rel="stylesheet" />
	
  <!-- #### DATATABLES DEPENDENCIES ### -->
  <!-- Firefox doesnt support link imports yet
  <link rel="import" href="datatables-dependencies.jsp">
  -->
  <%@include file="datatables-dependencies.jsp"%>
  
	<body class="is-preload">
  	<%@include file="nav.jsp"%>
  	
		<%@include file="breadcrumbs.jsp"%>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-3">
					<div class="row title-row">
						<div class="col-xs-12">
							<h2>Progress</h2>
						</div>
					</div>
					<div class="section2">
						<div class="row progress-bar-margin-offset">
							<div class="col-xs-12 form-group">
								<div id="assessedProgress" class="progress-bar"></div>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-12 form-group">
								<div id="reviewedProgress" class="progress-bar"></div>
							</div>
						</div>
						<div class="row">
							<!--
							<div class="col-xs-4">
								<div class="form-group">
									<a class="btn btn-default form-control" href="report.jsp?customerId=<%=request.getParameter("customerId")%>">Report</a>
								</div>
							</div>
							-->
							<div class="col-xs-4">
								<div class="form-group">
									<a class="btn btn-default form-control" href="report-quadrant.jsp?customerId=<%=request.getParameter("customerId")%>">Report</a>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="col-xs-9">
					<div class="row title-row">
						<div class="col-xs-4">
							<h2>Assessments</h2>
						</div>
					</div>
					<div class="section">
						<div id="tableDiv">
							<div class="button-bar">
								<div class="col-xs-2 pull-right form-group">
									<button class="btn btn-danger form-control" name="btnRemoveApps" disabled onclick="btnDelete_onclick(this);" type="button">Remove Application(s)</button>
								</div>
								<!-- 
								<div class="col-xs-2 pull-right form-group">
									<button class="btn btn-warning form-control" name="removeAssessments" disabled onclick="" type="button">Clear Assessment(s)</button>
								</div>
								<div class="col-xs-2 pull-right form-group">
									<button class="btn btn-warning form-control" name="removeReviews" disabled onclick="" type="button">Clear Review(s)</button>
								</div>
								-->
								<div class="col-xs-2 pull-right form-group">
									<button class="btn btn-primary form-control" name="New" onclick="editFormReset();" type="button" data-toggle="modal" data-target="#exampleModal" data-whatever="@new">Add Application</button>
								</div>
								<div class="col-xs-2 pull-right form-group">
									<button class="btn btn-default form-control" name="btnCloneApps" disabled onclick="showCloneDialog(this);" type="button">Clone Application</button>
								</div>
							</div>
							<table id="example" class="display" cellspacing="0" width="100%">
								<thead>
									<tr>
										<th align="left"></th>
										<th align="left">Name</th>
										<th align="left">Assessed</th>
										<th align="left">Reviewed</th>
										<th align="left" title="Business Criticality">Criticality</th>
										<th align="left">Decision</th>
										<th align="left">Effort</th>
										<th align="left">Review Date</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>

			</div>
		</div>
		
		<%@include file="newApplicationForm.jsp"%>
		
		<!-- ################ CLONE APP FORM #################-->
		<div class="modal fade" id="clone_formDialog" tabindex="-1" role="dialog" aria-labelledby="modalLabel">
			<div class="modal-dialog" role="document"> <!-- make wider by adding " modal-lg" to class -->
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" id="modalLabel">Clone Application</h4>
					</div>
					<div class="modal-body">
						<form id="clone_form">
							<!-- ### Hidden ID field -->
							<input id="clone_appId" name="clone_appId" type="hidden">
							<div class="form-group">
								<label for="clone_appName" class="control-label">Application to Clone</label>
								<input id="clone_appName" name="clone_appName" type="text" class="form-control">
							</div>
							<div class="form-group">
								<label for="clone_newAppNames" class="control-label">New Application Names (new line separated)</label>
								<textarea id="clone_newAppNames" name="clone_newAppNames" class="form-control"></textarea>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<a type="button" data-dismiss="modal">Cancel</a>
						<button class="btn btn-primary" id="modal-ok" type="button" data-dismiss="modal" onclick="form_save('clone_form', Utils.SERVER+'/api/pathfinder/customers/'+customerId+'/applications/'+document.getElementById('clone_appId').value+'/copy'); return false;">Clone</button>
					</div>
				</div>
			</div>
		</div>

		<script>
			var customerId=Utils.getParameterByName("customerId");
			var appsCount=assessed=unassessed=notReviewed=reviewed=0;
			
			$(document).ready(function() {
				
				// ### Get Customer Details
				httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId, function(customer){
					if (undefined!=setBreadcrumbs) setBreadcrumbs("assessments", customer);
				});
				
			});
		</script>
		
		<script src="assets/js/progressbar-kimmobrunfeldt-1.0.0.js"></script>
		<script src="assets/js/progressbar-functions.js"></script>

		<script>
			function onDatatableRefresh(json){
				console.log("onDatatableRefresh: apps/assessments.length="+json.length);
				var assessed=reviewed=0;
				for(var i=0;i<json.length;i++){
					if (json[i].Assessed) assessed+=1;
					if (json[i].ReviewDate!=null) reviewed+=1;
				}
				setProgress("assessedProgress", (100/json.length)*assessed, "Assessed");
				setProgress("reviewedProgress", (100/json.length)*reviewed, "Reviewed");
				buttonEnablement();
			}
			$(document).ready(function() {
				// ### Datatable load (has to be done after customer load because some links require the customer GUID ###
				var dTable=$('#example').DataTable( {
					"ajax": {
							"url": Utils.SERVER+'/api/pathfinder/customers/'+customerId+"/applicationAssessmentSummary",
							"data":{"_t":jwtToken},
							"dataSrc": "",
							"dataType": "json"
					},
					"fnInitComplete" : function(oSettings, json){ //unfortunately this method isnt called again on ajax refresh, so we have to push the update to another function that can ben called on both init and refresh events
						onDatatableRefresh(json);
					},
					"scrollCollapse": true,
					"paging":         false,
					"lengthMenu": [[10, 25, 50, 100, 200, -1], [10, 25, 50, 100, 200, "All"]], // page entry options
					"pageLength" : 10, // default page entries
					"bInfo" : false, // removes "Showing N entries" in the table footer
					"searching" : false,
					"order" : [[3,"desc"],[2,"desc"],[1,"asc"]],  //reviewed, assessed then app name
					columns: [
							{ data: "Id", width: "3%"},
							{ data: "Name"},
							{ data: "Assessed", width: "12%" },
							{ data: "ReviewDate", width: "12%" },
							{ data: "BusinessPriority" },
							{ data: "Decision" },
							{ data: "WorkEffort" },
							{ data: "ReviewDate", width: "12%" }
					],
					"columnDefs": [
							{
								"targets": 0,
								"orderable": false,
								"render": function (data, type, row){
									return "<input type='checkbox' name='appId' value='" + row.Id + "'></input><input type='hidden' name='" + row.Id + "_name' value='" + row.Name + "'></input>"
								}
							},
							{
								"targets": 1,
								"orderable": true,
								"render": function (data,type,row){
									if (row.LatestAssessmentId) {
										return "<a href='viewAssessment.jsp?app=" + row.Id + "&assessment=" + row.LatestAssessmentId + "&customer=" + customerId + "'>" + row.Name + "</a>"
									} else {
										return row.Name
									}
								}
							},
							{
								"targets": 2,
								"orderable": true,
								"render": function (data,type,row){
									var title = row.IncompleteAnswersCount+" answer(s) out of "+(row.CompleteAnswersCount+row.IncompleteAnswersCount)+" were answered as UNKNOWN";
									var color=row.IncompleteAnswersCount > 0?"Amber":"Green";
									var editUrl="<a href='survey-v2.jsp?customerId="+ customerId +"&applicationId="+ row.Id +"&assessmentId="+ row.LatestAssessmentId +"'><span class='editLink'>(edit)</span></a>";
									var completeIncomplete = row.IncompleteAnswersCount > 0 ? "<span title='" + title + "'>(" + row.CompleteAnswersCount + "/<span class='messageAmber'>" + row.IncompleteAnswersCount + "</span>)</span>" : ""
									return "<span class='" + (row.Assessed ? "message"+color+"'>Yes " + completeIncomplete +editUrl : "messageRed'><a href='survey-v2.jsp?customerId=" + customerId + "&applicationId=" + row.Id + "' class='messageRed'>No</a>") + "</span>"
								}
							},
							{
								"targets": 3,
								"orderable": true,
								"render": function (data, type, row) {
									if (row.ReviewDate) {
									  var edit='<a href="viewAssessment.jsp?review=true&app=' + row.Id + '&assessment=' + row.LatestAssessmentId + '&customer=' + customerId + '"><span class="editLink">(edit)</span></a>';
										return '<span class="messageGreen">Yes</span>'+' '+edit;
									} else if (row.Assessed) {
										return '<a href="viewAssessment.jsp?review=true&app=' + row.Id + '&assessment=' + row.LatestAssessmentId + '&customer=' + customerId + '" class="messageRed">No</a>'
									} else {
										return ''
									}
								}
							},
							{
								"targets": 5,
								"orderable": true,
								"render": function (data,type,row){
									return row.Decision || ''
								}
							},
							{
								"targets": 6,
								"orderable": true,
								"render": function (data,type,row){
									return row.WorkEffort || ''
								}
							},
							{
								"targets": 7,
								"orderable": true,
								"render": function (data,type,row){
									// formats are here: https://github.com/phstc/jquery-dateFormat
									if (row.ReviewDate) {
										var parsedDate = new Date(parseInt(row.ReviewDate, 10))
										return DateFormat.format.date(parsedDate, 'dd/MMM/yy HH:mm')
									} else {
										return ''
									}
								}
							}
					]
				});
				// ### END Datatable load
			} );
			
			// ### Model form functions ###
			function form_save(formId, saveUrl){
				var data = {};
				var op="";
				var form=document.getElementById(formId);
				for (var i = 0, ii = form.length; i < ii; ++i) {
					if (form[i].name) data[form[i].name]=form[i].value;
				}
				post(saveUrl, $('#clone_newAppNames').val().split("\n"));
				form_reset();
			}
			
			function form_reset(){
				document.getElementById("modal-ok").innerHTML="Create";
				document.getElementById("modalLabel").innerHTML=document.getElementById("modalLabel").innerHTML.replace("Update", "New");
				var form=$('#form').get();
				for (var i = 0, ii = form.length; i < ii; ++i)
					form[i].value="";
			}
			
			function showCloneDialog(caller){
				$('#example input[name="appId"]').each(function() {
					if ($(this).is(":checked")) {
						
						// set properties
						$('#clone_appId').val($(this).val());
						$('#clone_appName').val($("input[name='"+$(this).val()+"_name']").val());
						
						caller.disabled=true;
						
						// show form
						$('#clone_formDialog').modal();
					}
				});
			}
			
			// ### Datatable button functions ###
			
			function btnDelete_onclick(caller){
				if (!confirm("Are you sure? This will remove all associated assessments and reviews.")){
						return false;
				}else{
					var appIdsToDelete=[];
					$('#example input[name="appId"]').each(function() {
						if ($(this).is(":checked")) {
							appIdsToDelete[appIdsToDelete.length]=$(this).val();
						}
					});
					
					caller.disabled=true;
					httpDelete(Utils.SERVER+"/api/pathfinder/customers/"+Utils.getParameterByName("customerId")+"/applications/", appIdsToDelete);
				}
			}
			
			// ### enable/disable handlers for buttons on datatable buttonbar
			$(document).on('click', "input[type=checkbox]", function() {
				buttonEnablement();
			});
			buttonEnablement();
			function buttonEnablement(){
				$('button[name="btnCloneApps"]').attr('disabled', $('#example input[name="appId"]:checked').length!=1);
				$('button[name="btnRemoveApps"]').attr('disabled', $('#example input[name="appId"]:checked').length<1);
			}
			// ### End: enable/disable handlers for buttons on buttonbar
		</script>

<!--
<%@include file="corporate-color-swatches.jsp"%>
-->

	</body>
</html>
