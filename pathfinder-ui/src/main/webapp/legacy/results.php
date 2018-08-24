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

if (isset($_REQUEST['customer'])) {
$custId = $_REQUEST['customer'];
}
?>

		<!-- Banner -->
			<section id="banner2">
				<div class="inner">
					<h1><?php print getCustomerName($custId); ?> Results</h1>
					<p>View the results of an assessment and review output.</div>
			</section>

		<!-- Highlights -->
			<section class="wrapper">
				<div class="inner">
					<div class="highlights">
						<form action="">

  <fieldset>
    <label for="customer">Select a Customer</label>
    <select name="customer" id="customer">
			<?php
			## Get customer list as drop down
			$response = file_get_contents('http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/');
			foreach (json_decode($response,true) as $customer) {
			print "<option value=" . $customer['CustomerId'] . ">" . $customer['CustomerName'] . "</option>";
			}
			 ?>

    </select>
	</fieldset>
<br>
	<input type="submit" value="Get Results">


<?php
if (isset($_REQUEST['customer'])) {

print "<a href=reviewTableView.php?customer=" . $_REQUEST['customer'] . "><button type='button'>Get Pane View</button></a>";
}
?>
    <div id="piechartAss" style="width: 500px; height: 500px; float: left;"></div>
<!--      <div id="piechartReview" style="width: 300px; height: 300px;"></div>  -->

		</form>	
	
	<?php
if (isset($_REQUEST['customer'])) {
$totalAssessed = $totalUnassessed = $totalReviewed = $totalNotReviewed = 0;

print '<table id="myTable"  class="tablesorter"><thead><tr><th>Application</th><th>Assessed?</th><th>Review</th><th>Business Priority</th><th>Decision</th><th>Effort</th><th>Review Date</th><th>View Details</th></tr></thead><tbody>';
## Results go here
$cust = $_REQUEST['customer'];
$customerDetails = file_get_contents("http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/$cust");
#print_r($customerDetails);
$nn = json_decode($customerDetails,true);

## Get the apps and results.
$appsRaw = file_get_contents("http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/$cust/applications/");
$appsArr = json_decode($appsRaw,true);
foreach ($appsArr as $app) {
$appName = $app['Name'];
$appId = $app['Id'];
$reviewId = $app['Review'];
## Get number of Assessments
$assessments = file_get_contents("http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/$cust/applications/$appId/assessments/");
#print_r($assessments);
$ass = json_decode($assessments,true);

## Get the ranking and effort

$assResults = file_get_contents("http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/$cust/applications/$appId/");
$assResultsArray = json_decode($assResults,true);

print "<tr><td>" . $appName . "</td>";

## check if app has any assessments
if (sizeof($ass) > 0) {
$totalAssessed++;
## Get the business priority from assessment
$firstAssessment = $ass[0];
#print "Asses ID $firstAssessment";	

## Get the business priority
$uurl = "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/$cust/applications/$appId/assessments/$firstAssessment";
#print $uurl . "<br>";
$aData = file_get_contents($uurl);
$a = json_decode($aData, true);
$businessPriority = $a['payload']['BUSPRIORITY'];



print "<td class='messageGreen' id='messageGreen'>Yes</td>";
## check if a review has been done
if ($reviewId == null) {
$totalNotReviewed++;
print "<td><a href=reviewAssessment.php?app=" . $appId . "&assessment=" . $ass[0] . "&customer=" . $cust . ">" . "<img src=images/review.png height=24px width=24px></td>";
## fill out the blank columns
print "<td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td><td>&nbsp</td>";
} else {
$totalReviewed++;
## Get the details of the review
$data = file_get_contents("http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/$cust/applications/$appId/review/$reviewId");
$reviewDetails = json_decode($data,true);
#print_r($reviewDetails);
$decision = ucfirst(strtolower($reviewDetails['ReviewDecision']['rank']));
$effort = ucfirst(strtolower($reviewDetails['WorkEffort']['rank']));
$notes = "";
$notes = $reviewDetails['ReviewNotes'];
$reviewDate = $reviewDetails['ReviewTimestamp'];

#print "<td><a href=viewApplication.php?customerId=$cust&applicationId=$appId&reviewId=$reviewId>Reviewed</a><td>$decision</td><td>$effort</td><td>$notes</td><td>$reviewDate</td>";
print "<td>Complete<td>$businessPriority</td><td>$decision</td><td>$effort</td><td>$reviewDate</td><td><a href=viewAssessment.php?app=" . $appId . "&assessment=" . $ass[0] . "&customer=" . $cust . "><img src=images/details.png></a></td>";

}
} else {

print "<td class='messageRed' id='messageRed'><a href='http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/' target=_blank>No</td><td></td><td></td><td></td><td></td><td></td><td></td>";
$totalUnassessed++;
}
print "</tr>";
}
print "	 </table>";
}

	 ?>

	 </div>

	 </div>
					</div>
					
				</div>
				
			</section>



		<!-- Scripts -->
			<script src="assets/js/jquery.min.js"></script>
			<script src="assets/js/jquery.tablesorter.js"></script>
<!-- 			<script src="assets/js/jquery.tablesorter.pager.js"></script> -->
			<script src="assets/js/browser.min.js"></script>
			<script src="assets/js/breakpoints.min.js"></script>
			<script src="assets/js/util.js"></script>
			<script src="assets/js/main.js"></script>
         <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
         <script type="text/javascript" src="https://canvasjs.com/assets/script/jquery.canvasjs.min.js"></script> 
 
  <script type="text/javascript" >
$(document).ready(function() 
    { 
        $("#myTable").tablesorter(); 
    } 
); 


</script>
<script type="text/javascript"> 
    google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {

        var data = google.visualization.arrayToDataTable([
          ['Task', 'Hours per Day'],
          ['Assessed',     <?php echo $totalAssessed; ?>],
          ['Not Assessed',      <?php echo $totalUnassessed; ?>]
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
 
</script> 

<script type="text/javascript"> 
    google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {

        var data = google.visualization.arrayToDataTable([
          ['Task', 'Hours per Day'],
          ['Assessed',     <?php echo $totalReviewed; ?>],
          ['Not Assessed',      <?php echo $totalNotReviewed; ?>]
        ]);

        var options = {
			backgroundColor: 'transparent',
         title: 'Review Status',
          pieHole: 0.2,
          is3D: true,
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechartReview'));

        chart.draw(data, options);
      }
 
</script> 

	 

	</body>
</html>
