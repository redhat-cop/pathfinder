
  	<style>
.breadcrumb-buttons {
    list-style-type: none;
    margin: 0;
    padding: 0;
    overflow: hidden;
    background-color: #333;
    height: 45px;
}
.breadcrumb-buttons li {
    display:inline-block;
    height: 45px;
}
.breadcrumb-buttons li a {
    display: block;
    color: white;
    text-align: center;
    height: 45px;
    padding: 10px 20px;
    text-decoration: none;
    font-size: 13pt;
}
/*
*/
.breadcrumb-buttons li a:hover:not(.active) {
    background-color: #111;
}
/*
.breadcrumb-buttons .active {
    background-color: #4CAF50;
    background-color: #111;
    background-color: #80A0A9;
}
.stretch{
	width: 50%;
	text-align: center;
	color: white;
	font-size: 18pt;
}
#customer{
	position: relative;
	top:3px;
}
*/
.breadcrumb-customer{
	  background-color: #ccc !important;
    color: #444;
    font-size: 18pt;
    /*text-align: center;*/
    padding: 6px 16px;
    width: 300px;
    height: 52px;
    position: relative;
    left: 0px;
}
/*
*/
ol, ul {
    list-style: none;
}

/* add grey button separator to distinguish between them */
.breadcrumb-buttons li {
    border-right: 1px solid #bbb;
}
.breadcrumb-buttons li:last-child {
    border-right: none;
}
.disabledx a{
	background-color: #AAA !important;
	cursor: default;
}

  	</style>
  	
  	<script>
  		function setBreadcrumbs(active, customer){
		    
		    document.getElementById("customer").innerHTML=customer.CustomerName;
		    
		    //$('#breadcrumb-details>a').attr('href', '#');
		    $('#breadcrumb-applications>a').attr('href', 'manageCustomerApplications.jsp?customerId='+customer.CustomerId);
		    $('#breadcrumb-assessments>a').attr('href', 'assessments-v2.jsp?customerId='+customer.CustomerId);
		    $('#breadcrumb-members>a').attr('href', 'members.jsp?customerId='+customer.CustomerId);
		    
		    if (undefined!=$('#breadcrumb-'+active+'>a').get()){
		    	$('#breadcrumb-'+active+'>a').addClass("active");
		    	//$('#breadcrumb-'+active+'>a').attr("href","#"); // prevent navigation to current page
		    }
  		}
  		
  		// Duplicate of the above setBreadcrumbs method for pages where we get the customer details without a specific extra call. Trying to migrate to this method over setBreadcrumbs...
  		function initTabs(active, customerId, customerName){
		    document.getElementById("customer").innerHTML=customerName;
		    $('#breadcrumb-applications>a').attr('href', 'manageCustomerApplications.jsp?customerId='+customerId);
		    $('#breadcrumb-assessments>a').attr('href', 'assessments-v2.jsp?customerId='+customerId);
		    $('#breadcrumb-members>a').attr('href', 'members.jsp?customerId='+customerId);
		    if (undefined!=$('#breadcrumb-'+active+'>a').get()){
		    	$('#breadcrumb-'+active+'>a').addClass("active");
		    	//$('#breadcrumb-'+active+'>a').attr("href","#"); // prevent navigation to current page
		    }
  		}
  	</script>
  	
  	
  	<div>
			<ul class="breadcrumb-buttons">
				<!--
				<li id="customer" class="breadcrumb-customer">Loading...</li>
				<li id="breadcrumb-details"><a href="#">Details</a></li>
				-->
				<li id="breadcrumb-assessments"> <a href="#">Assessments</a></li>
				<li id="breadcrumb-applications"><a href="#">Applications</a></li>
				<li id="breadcrumb-members">     <a href="#">Members</a></li>
				<li id="breadcrumb-customer" class="stretch"><span id="customer">Loading...</span></li>
			</ul>
  	</div>
