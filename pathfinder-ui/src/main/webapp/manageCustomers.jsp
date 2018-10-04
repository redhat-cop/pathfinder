<!DOCTYPE HTML>
<html>
  <%@include file="head.jsp"%>
  <!--
  <link rel="import" href="head.jsp">
  <link rel="import" href="nav.jsp">
  -->
	
  <link href="assets/css/breadcrumbs.css" rel="stylesheet" />
  
  <%@include file="datatables-dependencies.jsp"%>
	
	<body class="is-preload">
		<%@include file="nav.jsp"%>
		
		<form id="importForm" data-action="/api/pathfinder/customers/import/" method="POST" enctype="multipart/form-data">
			<input type="file" id="FileUpload1" style="display: none" />
		</form>
				
		<!-- #### DATATABLES ### -->
		<script>
			
			function onDatatableRefresh(json){
				console.log("onDatatableRefresh():: Called");
				buttonEnablement();
			}
			
			$(document).ready(function() {
				
					var dTable=$('#example').DataTable( {
							"ajax": {
									"url": Utils.SERVER+'/api/pathfinder/customers/',
									//"beforeSend": function(xhr){
									//		xhr.setRequestHeader(jwtToken);
									//},
									"data":{"_t":jwtToken},
									"dataSrc": "",
									"dataType": "json"
							},
							"fnInitComplete" : function(oSettings, json){ //unfortunately this method isnt called again on ajax refresh, so we have to push the update to another function that can ben called on both init and refresh events
								onDatatableRefresh(json);
							},
							"scrollCollapse": true,
							"paging":				 false,
							"lengthMenu": [[10, 25, 50, 100, 200, -1], [10, 25, 50, 100, 200, "All"]], // page entry options
							"pageLength" : 10, // default page entries
							"bInfo" : false, // removes "Showing N entries" in the table footer
							"columns": [
									{ "data": "CustomerId" },
									{ "data": "CustomerName" },
									{ "data": "CustomerDescription" },
									{ "data": "CustomerVertical" },
									{ "data": "CustomerId" },
									{ "data": "CustomerPercentageComplete" },
									{ "data": "CustomerId" },
									{ "data": "CustomerId" },
							],
							columnDefs: [
								{targets: 0, orderable: false, render: function (data,type,row){
										return "<input type='checkbox' name='id' value='"+row['CustomerId']+"'></input>";
								}},
								{targets: 1, orderable: true, render: function (data,type,row){
										var link="<a href='assessments-v2.jsp?customerId="+row["CustomerId"]+"'>"+row['CustomerName']+"</a>";
										return link+"&nbsp;<span class='editLink'>(<a href='#' onclick='loadEntity(\""+row["CustomerId"]+"\");' data-toggle='modal' data-target='#exampleModal'>edit</a>)</span>";
								}},
								{targets: 4, orderable: false, render: function (data,type,row){
										return "";
								}},
								{targets: 5, orderable: false, render: function (data,type,row){
									 var percentComplete=row['CustomerPercentageComplete'];
									 var link="<a href='assessments-v2.jsp?customerId="+row["CustomerId"]+"'>Assessments&nbsp;("+percentComplete+"%)</a>";
									 return "<div class='progress'><div class='progress-bar-success' role='progressbar' aria-valuenow='"+percentComplete+"' aria-valuemin='0' aria-valuemax='100' style='width:"+percentComplete+"%'>"+link+"</div></div>";
								}},
								{targets: 6, orderable: false, render: function (data,type,row){
										return "<a href='manageCustomerApplications.jsp?customerId="+row["CustomerId"]+"'>Applications ("+row['CustomerAppCount']+")</a>";
								}},
								{targets: 7, orderable: false, render: function (data,type,row){
										return "<a href='members.jsp?customerId="+row["CustomerId"]+"'>Members ("+row['CustomerMemberCount']+")</a>";
								}}
								 //,{ "targets": 6, "orderable": false, "render": function (data,type,row){
								 //	 return "<div class='btn btn-warning' title='Edit' onclick='load(\""+row["CustomerId"]+"\");' data-toggle='modal' data-target='#exampleModal'></div>";
								 // }}
								 //,{ "targets": 7, "orderable": false, "render": function (data,type,row){
								 //	 return "<div class='btn btn-danger' title='Delete' onclick='return deleteItem(\""+row["CustomerId"]+"\");'></div>";
								 // }}
							]
					} );
					
					// Search functionality (redirect search to button bar search box)
					$('#search').keyup(function (e) {
							$('#example').DataTable().search( this.value ).draw();
					});
					$('#example_filter').css({display:"none"});
			} );
			
			// ### enable/disable handlers for buttons on datatable buttonbar
			$(document).on('click', "input[type=checkbox]", function() {
				buttonEnablement();
			});
			buttonEnablement();
			function buttonEnablement(){
				$('button[name="btnRemoveCustomer"]').attr('disabled', $('#example input[name="id"]:checked').length<1);
				$('button[name="btnImport"]').attr('disabled', $('#example input[name="id"]:checked').length>0);
				$('button[name="btnExport"]').attr('disabled', $('#example input[name="id"]:checked').length<1);
			}
			// ### End: enable/disable handlers for buttons on buttonbar
			
			function getSelected(){
				var idsSelected=[];
				$('#example input[name="id"]').each(function() {
					if ($(this).is(":checked")) {
						idsSelected[idsSelected.length]=$(this).val();
					}
				});
				return idsSelected;
			}
			
			function btnImport_onclick(caller){
				caller.disabled=true;
				var fileupload = document.getElementById("FileUpload1");
				fileupload.onchange = function () {
						var fileName = fileupload.value.split('\\')[fileupload.value.split('\\').length - 1];
						//filePath.innerHTML = "<b>Selected File: </b>" + fileName;
						console.log("path = "+fileName);
						UploadFile(fileName, fileupload);
				};
				fileupload.click();
				//caller.disabled=false;
			}
			
			function UploadFile(fileName, file) {
				var xhr = new XMLHttpRequest();
				var typeExpected="image/jpeg";
				if (xhr.upload && file.type == "file"){//} && file.size <= $id("MAX_FILE_SIZE").value) {
					// create progress bar
//					var o = $("#progress")[0];
//					var progress = o.appendChild(document.createElement("p"));
//					progress.appendChild(document.createTextNode("upload " + file.name));
					
					// progress bar
					xhr.upload.addEventListener("progress", function(e) {
						var pc = parseInt(100 - (e.loaded / e.total * 100));
//						progress.style.backgroundPosition = pc + "% 0";
					}, false);
					
					// file received/failed
					xhr.onreadystatechange = function(e) {
						if (xhr.readyState == 4) {
//							progress.className = (xhr.status == 200 ? "success" : "failure");
						}
					};
					
					xhr.onloadend = function () {
						if (this.status == 200){
							$('#example').DataTable().ajax.reload(
								function(json){
									if (undefined!=onDatatableRefresh){
										onDatatableRefresh(json);
										$('#FileUpload1')[0].value=''; // do this so that next time you select a file (including the same one) then it attempts the upload again 
									}
								}
							);
						}
					}
					
					// start upload
					//var url=addAuthToken(Utils.SERVER+$("#FileUpload1").attr('action'));
					var url=addAuthToken(Utils.SERVER+$('#importForm').attr('data-action'));
					xhr.open("POST", url, true);
					xhr.setRequestHeader("X-FILENAME", fileName);
					xhr.send($('#FileUpload1')[0].files[0]);
					buttonEnablement();
					$('#example').DataTable().ajax.reload();
				}
			}
					
			
			function btnExport_onclick(caller){
				caller.disabled=true;
				
				// call to back-end
				var url=addAuthToken(Utils.SERVER+"/api/pathfinder/customers/export?ids="+getSelected());
				// call to UI
				//var url=addAuthToken("${pageContext.request.contextPath}/api/pathfinder/customers/export?ids="+getSelected());
				
				var a = document.createElement('A');
				a.href = url;
				a.download = url.substr(url.lastIndexOf('/') + 1);
				document.body.appendChild(a);
				a.click();
				document.body.removeChild(a);
				caller.disabled=false;
			}
			
			function btnRemoveCustomer_onclick(caller){
				if (!confirm("Are you sure? This will also remove any applications, assessments and/or associated reviews for the selected customers(s).")){
						return false;
				}else{
					caller.disabled=true;
					httpDelete(Utils.SERVER+"/api/pathfinder/customers/", getSelected());
				}
			}
		</script>

		<!--
		<script src="https://rawgit.com/henrya/js-jquery/master/BinaryTransport/jquery.binarytransport.js"></script>
		-->
  	<div id="wrapper" class="container-fluid">

			<div class="row title-row">
				<div class="col-xs-4">
					<h2 id="title">Customers</h2>
				</div>
			</div>
			<div class="section">
		    <div id="tableDiv">
					<div class="button-bar col-sm-9" style="float:right">
						<div class="col-xs-2 pull-right form-group">
							<input id="search" type="search" class="form-control" aria-controls="example" placeholder="Search"/>
						</div>
						<div class="col-xs-2 pull-right form-group">
							<button class="btn btn-secondary form-control" name="btnExport" type="button" onclick="return btnExport_onclick(this);">Export</button>
						</div>
						<div class="col-xs-2 pull-right form-group">
							<button class="btn btn-secondary form-control" name="btnImport" type="button" onclick="return btnImport_onclick(this);">Import</button>
						</div>
						<div class="col-xs-2 pull-right form-group">
							<button class="btn btn-danger form-control" name="btnRemoveCustomer" disabled onclick="btnRemoveCustomer_onclick(this);" type="button">Remove Customer</button>
						</div>
						<div class="col-xs-2 pull-right form-group">
							<button class="btn btn-primary form-control" name="btnAddCustomer" onclick="editFormReset();" type="button" data-toggle="modal" data-target="#exampleModal" data-whatever="@new">Add Customer</button>
						</div>
					</div>
			    <table id="example" class="display" cellspacing="0" width="100%">
						<thead>
							<tr>
									<th align="left"></th>
									<th align="left">Name</th>
									<th align="left">Description</th>
									<th align="left">Industry</th>
									<th align="left"></th>
									<th align="left"></th>
									<th align="left"></th>
									<th align="left"></th>
							</tr>
						</thead>
			    </table>
			  </div>
			</div>
  	</div>
    

		<%@include file="newCustomerForm.jsp"%>

	</body>
</html>