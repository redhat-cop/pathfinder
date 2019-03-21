<!DOCTYPE HTML>
<!--
	Industrious by TEMPLATED
	templated.co @templatedco
	Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
-->
<html>
  <%@include file="head.jsp"%>
	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	
	<body class="is-preload">

  <%@include file="nav.jsp"%>

<script>
	function post(url, dataObject){
	  var xhr = new XMLHttpRequest();
	  xhr.open("POST", url, true);
	  if (dataObject != undefined){
	    xhr.send(JSON.stringify(dataObject));
	  }else{
	    xhr.send();
	  }
	  xhr.onloadend = function () {
	    loadOrRefresh();
	  };
	}
	
	function loadOrRefresh(){
		var xhr = new XMLHttpRequest();
	  xhr.open("GET", "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/", true);
	  xhr.send();
	  xhr.onloadend = function () {
			var json=JSON.parse(xhr.responseText);
			$('#test').empty();
			$('#test').append("<thead><tr><td>Customer Name</td><td>Customer Details</td><td>Edit</td><td>Delete</td></tr></thead>");
			for (var i=0;i<json.length;i++) {
				$("#test").append("<tr>")
					.append("<td>"+json[i]["CustomerName"]+"</td>")
					.append("<td>"+(json[i]["CustomerDescription"]==null)?"&nbsp;":json[i]["CustomerDescription"]+"</td>")
					.append("<td><a href='editCustomer.jsp?customer="+json[i]["CustomerId"]+"'><img src='images/edit.png'></a></td>")
					.append("<td><a href='deleteCustomer.jsp?custId="+json[i]["CustomerId"]+"'><img src='images/trash.png' width=32px height=32px></a></td>")
					.append("</tr>");
			}
		}
	}
	
	$(document).ready(function(){
		loadOrRefresh();
		
		$('#message').fadeIn('slow', function(){
	               $('#message').delay(5000).fadeOut(); 
	            });
	            
	    $("button").click(function(){
	        $("#aaa").toggle();
	    });
	});
	
	$( function() {
    $( "#dialog-confirm" ).dialog({
      resizable: false,
      height: "auto",
      width: 400,
      modal: true,
      buttons: {
        "Delete all items": function() {
          $( this ).dialog( "close" );
        },
        Cancel: function() {
          $( this ).dialog( "close" );
        }
      }
    });
  } );
</script>

		<!-- Highlights -->
			<section class="wrapper">
				<div class="inner">
					<div class="highlights">

					</div>
					<table id="test">
					</table>

<button data-toggle="modal" data-target="#exampleModal">Add New Customer (modal)</button>

</div>



				</div>
			</section>





	 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	 <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
   <link rel="stylesheet" href="assets/css/main.css" />
   
<script>
	function postFormData(formId){
	  var data = {};
	  var op="";
	  var form=document.getElementById(formId);
	  for (var i = 0, ii = form.length; i < ii; ++i) {
	    var input = form[i];
	    if (input.name) 
	      data[input.name]=input.value;
	  }
	  post("http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/", data);
	  reset();
	
	}
</script>


<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
  <div class="modal-dialog" role="document"> <!-- make wider by adding " modal-lg" to class -->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="exampleModalLabel">New Customer</h4>
      </div>
      <div class="modal-body">
        <form id="myform">
          <div id="form-id" class="form-group">
            <label for="name" class="control-label">Customer Name:</label>
            <input id="name" name="name" type="text" class="form-control"/>
          </div>
          <div class="form-group">
            <label for="description" class="control-label">Customer Description:</label>
            <input id="description" name="description" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="vertical" class="control-label">Customer Vertical:</label>
            <select name="vertical" id="vertical" class="form-control">
							<option value="Agriculture">Agriculture</option>
							<option value="Business Services">Business Services</option>
							<option value="Construction & Real Estate">Construction & Real Estate</option>
							<option value="Education">Education</option>
							<option value="Energy, Raw Materials & Utilities">Energy, Raw Materials & Utilities</option>
							<option value="Finance">Finance</option>
							<option value="Government">Government</option>
							<option value="Healthcare">Healthcare</option>
							<option value="IT">IT</option>
							<option value="Leisure & Hospitality">Leisure & Hospitality</option>
							<option value="Libraries">Libraries</option>
							<option value="Manufacturing">Manufacturing</option>
							<option value="Media & Internet">Media & Internet</option>
							<option value="Non-Profit & Professional Orgs.">Non-Profit & Professional Orgs.</option>
							<option value="Retail">Retail</option>
							<option value="Software">Software</option>
							<option value="Telecommunications">Telecommunications</option>
							<option value="Transportation">Transportation</option>
						</select>
          </div>
          <div class="form-group">
            <label for="assessor" class="control-label">Customer Assessor:</label>
            <input id="assessor" name="assessor" type="text" class="form-control">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button id="edit-ok" type="button" data-dismiss="modal" onclick="postFormData('myform'); return false;">Create</button>
      </div>
    </div>
  </div>
</div>



	</body>
</html>