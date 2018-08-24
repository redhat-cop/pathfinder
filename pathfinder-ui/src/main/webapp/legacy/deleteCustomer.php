<!DOCTYPE HTML>
<!--
	Industrious by TEMPLATED
	templated.co @templatedco
	Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
-->
<html>
	<head>
		<title>Pathfinder</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
		<meta name="description" content="" />
		<meta name="keywords" content="" />
		<link rel="stylesheet" href="assets/css/main.css" />
      <link rel="stylesheet" type="text/css" href="http://overpass-30e2.kxcdn.com/overpass.css"/>

	</head>
	<body class="is-preload">

  <%@include file="nav.jsp"%>


<?php
include("functions.php");
$custId = $_REQUEST['custId'];
$customerName = getCustomerName($custId);
?>


		<!-- Banner -->
			<section id="banner2">
				<div class="inner">
					<h1>Delete Company</h1>
			</section>

		<!-- Highlights -->
			<section class="wrapper">
				<div class="inner">
					<div class="highlights">



					</div>
<center>
<?php

if (isset($_REQUEST['deleteConfirmed'])) {
deleteCustomer($_REQUEST['custId']);
print "Deleted";
} else {
print '<h2>delete ' . $customerName . '? </h2><h3> This action can not be undone</h3>
<form action="">
<input type="submit" value="Yes" id="confirmed">
<input type="hidden" name="deleteConfirmed" value="Y">
<input type="hidden" name="idToDelete" value="' . $custId . 'AA">
</form>';
}

?>
<br>
<button type="button"><a href=admin.php>Back</a></button>
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
