<!DOCTYPE HTML>
<html>
  
  <%@include file="head.jsp"%>
	
  <link href="assets/css/breadcrumbs.css" rel="stylesheet" />
  
  <!-- #### DATATABLES DEPENDENCIES ### -->
  <!-- Firefox doesnt support link imports yet
  <link rel="import" href="datatables-dependencies.jsp">
  -->
  <%@include file="datatables-dependencies.jsp"%>

  <!-- #### DATATABLES ### -->
  
	<body class="is-preload">
  	<%@include file="nav.jsp"%>
		
		<%@include file="breadcrumbs.jsp"%>
  	
  	
  	<!-- #### DATATABLES ### -->
		<script>
			var customerId=Utils.getParameterByName("customerId");
			function deleteItem(custId, appId){
			  httpDelete(Utils.SERVER+"/api/pathfinder/customers/"+custId+"/applications/"+appId);
			}
			function onDatatableRefresh(json){
				buttonEnablement();
			}
			$(document).ready(function() {
					
					// ### Get Customer Details
					httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId, function(customer){
						if (undefined!=setBreadcrumbs) setBreadcrumbs("applications", customer);
					});
					
					// ### populate the customer applications in the datatable
			    $('#example').DataTable( {
			        "ajax": {
			            //"url": '${pageContext.request.contextPath}/api/pathfinder/customers/"+request.getParameter("customerId")+"/applications/',
			            "url": Utils.SERVER+'/api/pathfinder/customers/'+customerId+'/applications/',
			            "data":{"_t":jwtToken},
			            "dataSrc": "",
			            "dataType": "json"
			        },
			        "scrollCollapse": true,
			        "paging":         false,
			        "oLanguage": { 
			        	sSearch: "",             // remove the "Search" label text
			        	sLengthMenu: "_MENU_" }, // remove the "show X entries" text
			        "lengthMenu": [[10, 25, 50, 100, 200, -1], [10, 25, 50, 100, 200, "All"]], // page entry options
			        "pageLength" : 10, // default page entries
			        "bInfo" : false, // removes "Showing N entries" in the table footer
			        "order" : [[1,"asc"]],
			        "searching": true,
			        "columns": [
			            { "data": "Id" },
			            { "data": "Name" },
			            { "data": "Stereotype" },
			            { "data": "Owner" },
			            { "data": "Description" },
			        ]
			        ,"columnDefs": [
			        	{ "targets": 0, "orderable": false, "render": function (data,type,row){
									return "<input type='checkbox' name='id' value='"+row['Id']+"'></input>";
								}},
				      	{ "targets": 1, "orderable": true, "render": function (data,type,row){
							    return row['Name']+"&nbsp;<span class='editLink'>(<a href='#' onclick='loadEntity(\""+row["Id"]+"\");' data-toggle='modal' data-target='#exampleModal'>edit</a>)</span>";
								  //return "<a href='#' onclick='loadEntity(\""+row["Id"]+"\"); return false;' data-toggle='modal' data-target='#exampleModal'>"+row['Name']+"</a>";
								}},
			        	{ "targets": 2, "orderable": true, "render": function (data,type,row){
									return row['Stereotype']=="TARGETAPP"?"Assessable Application":"Dependency Only";
								}},
			        ],
			    } );
			    
			    // Search functionality (redirect search to button bar search box)
			    $('#search').keyup(function (e) {
					    $('#example').DataTable().search( this.value ).draw();
					});
					$('#example_filter').css({display:"none"});
					
					// alternative search /buttonbar impl where we move the button bar into the dtable itself, which would provide more consistency but is complex and hacky looking...
					//var buttons=$('.button-bar').html();
					//$('.button-bar').html("");
					//$('#example_wrapper div div[class=col-sm-6]:nth-child(2)').append(buttons);
					//$('#example_filter').addClass("pull-right form-group");
					//
					//$('#example_wrapper div:nth-child(1) div[class=col-sm-6]:nth-child(1)').removeClass("col-sm-6");
					//$('#example_wrapper div:nth-child(1) div[class=col-sm-6]:nth-child(2)').removeClass("col-sm-6").addClass("col-sm-9").css({float:"right"});// .addClass("col-sm-9");
					
					
			} );
			
			// ### enable/disable handlers for buttons on datatable buttonbar
			$(document).on('click', "input[type=checkbox]", function() {
				buttonEnablement();
			});
			buttonEnablement();
			function buttonEnablement(){
			  $('button[name="btnRemove"]').attr('disabled', $('#example input[name="id"]:checked').length<1);
			}
			// ### End: enable/disable handlers for buttons on buttonbar
			
			function btnDelete_onclick(caller){
				if (!confirm("Are you sure? This will also remove any associated assessments and/or reviews for the selected application(s).")){
						return false;
				}else{
				  var idsToDelete=[];
					$('#example input[name="id"]').each(function() {
						if ($(this).is(":checked")) {
						  idsToDelete[idsToDelete.length]=$(this).val();
						}
					});
					
					caller.disabled=true;
					httpDelete(Utils.SERVER+"/api/pathfinder/customers/"+Utils.getParameterByName("customerId")+"/applications/", idsToDelete);
				}
			}
		</script>
		<div id="wrapper" class="container-fluid">
			<div class="row title-row">
				<div class="col-xs-4">
					<h2>Applications</h2>
				</div>
			</div>
			<div class="section">
				<div id="tableDiv">
					<div class="button-bar col-sm-9" style="float:right">
						<div class="col-xs-2 pull-right form-group">
							<input id="search" type="search" class="form-control" aria-controls="example" placeholder="Search"/>
						</div>
						<div class="col-xs-2 pull-right form-group">
							<button class="btn btn-danger form-control" name="btnRemove" disabled onclick="btnDelete_onclick(this);" type="button">Remove Application</button>
						</div>
						<div class="col-xs-2 pull-right form-group">
							<button class="btn btn-primary form-control" name="New" onclick="editFormReset();" type="button" data-toggle="modal" data-target="#exampleModal" data-whatever="@new">Add Application</button>
						</div>
					</div>
					<table id="example" class="display" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th align="left"></th>
								<th align="left">Application Name</th>
								<th align="left">Type</th>
								<th align="left">Owner</th>
								<th align="left">Description</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
    </div>
    

		<%@include file="newApplicationForm.jsp"%>

	</body>
</html>