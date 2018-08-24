<!DOCTYPE HTML>
<!--
	Industrious by TEMPLATED
	templated.co @templatedco
	Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
-->
<html>
	<%@include file="head.jsp"%>
	
	<body class="is-preload">

  <%@include file="nav.jsp"%>

<?php
include("functions.php");
$custId = $_REQUEST['customer'];
?>


		<!-- Banner -->
			<section id="banner2">
				<div class="inner">
					<h1><?php getCustomerName($custId); ?> Admin</h1>
					<p>Add Applications</div>
			</section>

		<!-- Highlights -->
			<section class="wrapper">
				<div class="inner">
					<div class="highlights">



					</div>

<?php


# Get the customer details


# Get all the apps for that client
$apps = file_get_contents("http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/$custId/applications/");
#print_r($apps);
print "<table width=50%><thead><tr><td>Applications</td><td>Delete App</td></tr></thead><tbody>";
foreach (json_decode($apps,true) as $app) {
print "<tr><td>" . $app['Name'] . "</td><td><img src=images/trash.png height=30px width=30px></td></tr>" ;
}
print "</tbody></table>";

if (isset($_REQUEST['appName'])) {

## Add app to customer
$appName = $_REQUEST['appName'];
$appDesc = $_REQUEST['appDesc'];
print '<div id="message" class="message" style="display:none;">' . $_REQUEST['appName'] . ' Added</div>';

$data = array("Name" => $appName, "Description" => $appDesc);
$data_string = json_encode($data);      

$url = "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/" . $custId . "/applications/";
$ch = curl_init($url);
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");                                                                     
curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);                                                                  
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);                                                                      
curl_setopt($ch, CURLOPT_HTTPHEADER, array(                                                                          
    'Content-Type: application/json',                                                                                
    'Content-Length: ' . strlen($data_string))                                                                       
);                                                                                                                   

#print "<br>$url<br>";
#var_dump($data_string);
                                                                                                                     
$result = curl_exec($ch); 

}


?>

<h4>Add New Application</h4>
<form id="myForm" action="#" method="post"> 
    Application Name <input type="text" name="appName" /> 
    Application Description <input type="text" name="appDesc" /> <br>
    <input type="submit" value="Add" /> 
</form>
<a href="admin.php"><button>Back To Admin</button></a>

				</div>
			</section>


		<!-- Scripts -->
			<script src="assets/js/jquery.min.js"></script>
			<script src="assets/js/browser.min.js"></script>
			<script src="assets/js/breakpoints.min.js"></script>
			<script src="assets/js/util.js"></script>
			<script src="assets/js/main.js"></script>
			<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
$(document).ready(function(){
	
	$('#message').fadeIn('slow', function(){
               $('#message').delay(5000).fadeOut(); 
            });
            
    $("button").click(function(){
        $("#aaa").toggle();
    });
});
</script>


	</body>
</html>
