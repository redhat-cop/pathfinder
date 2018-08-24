<!DOCTYPE HTML>
<!--
	Industrious by TEMPLATED
	templated.co @templatedco
	Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
-->
<html>
  <%@include file="head.jsp"%>

  <style>
    .bubbleChart {
      min-width: 400px;
      max-width: 500px;
      margin: 0 auto;
      float: left;
    }
  </style>

		<!-- Scripts -->
			<script src="assets/js/jquery.min.js"></script>
			<script src="assets/js/browser.min.js"></script>
			<script src="assets/js/breakpoints.min.js"></script>
			<script src="assets/js/util.js"></script>
			<script src="assets/js/main.js"></script>
			<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
			<script src="assets/js/jquery.tablesorter.js"></script>

<script type="text/javascript" >
$(document).ready(function() 
    { 
        $("#myTable").tablesorter(); 
    } 
); 
</script>

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

  
	<body class="is-preload">
	
<?php
## Check if it's a form return

if (isset($_REQUEST['reviewSubmitted'])) {
#phpinfo();
$now = date("Y-m-d");
$data = array( "AssessmentId" => $_REQUEST['assessment'], "customer" => $_REQUEST['customer'], "app" => $_REQUEST['app'], "ReviewDecision" => $_REQUEST['proposedAction'], "WorkEffort" => $_REQUEST['effortEstimate'], "ReviewNotes" => $_REQUEST['supportingNotes']) ;

$details = array( "assessment" => $_REQUEST['assessment'], "customer" => $_REQUEST['customer'], "app" => $_REQUEST['app']) ;
## get all the results for that assessment
$answersData = file_get_contents("http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/" . $details['customer'] . "/applications/" . $details['app'] . "/assessments/" . $details['assessment']);
$answers = json_decode($answersData, true);
$businessPriority = $answers['payload']['BUSPRIORITY'];


$bodyData = array(  "AssessmentId" => $_REQUEST['assessment'], "ReviewTimestamp" => "$now", "BusinessPriority" => "$businessPriority", "WorkPriority" => "3", "ReviewDecision" => array("rank" => $_REQUEST['proposedAction']), "WorkEffort" => array("rank" => $_REQUEST['effortEstimate']), "ReviewNotes" => $_REQUEST['supportingNotes']) ;
#$reviewType = array("BusinessPriority" => '1', )

$data_string = json_encode($bodyData);                                                                                   
$url = "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/" . $data['customer'] . "/applications/" . $data['app'] . "/review";
#print "URL : $url <br><br>";
#print_r($data_string);
#print "<br><br>";
$ch = curl_init($url);
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");                                                                     
curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);                                                                  
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);                                                                      
curl_setopt($ch, CURLOPT_HTTPHEADER, array(                                                                          
    'Content-Type: application/json',                                                                                
    'Content-Length: ' . strlen($data_string))                                                                       
);     
$result = curl_exec($ch);                                                                      
$info = curl_getinfo($ch);

#print_r($info);
#print_r($result);
}
?>	

  <%@include file="nav.jsp"%>



		<!-- Banner -->
			<section id="banner2">
				<div class="inner">
					<h1>Pathfinder </h1>
					<p>Assessment Review for <?php getCustomerName($_REQUEST['customer']); ?> </p></div>
			</section>

		<!-- Highlights -->
			<section class="wrapper">
				<div class="inner">
					<div class="highlights">

					</div>

<div id="leftReviewPane">


  <h3>Overview</h3>

<?php


# Get all the questions
$data = file_get_contents("http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/static/questions") ;
$questions = json_decode($data,true);

$details = array( "assessment" => $_REQUEST['assessment'], "customer" => $_REQUEST['customer'], "app" => $_REQUEST['app']) ;
## get all the results for that assessment
$answersData = file_get_contents("http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/" . $details['customer'] . "/applications/" . $details['app'] . "/assessments/" . $details['assessment']);
$answers = json_decode($answersData, true);
$businessPriority = $answers['payload']['BUSPRIORITY'];

$appRating = array("RED" => 0, "AMBER" => 0, "GREEN" => 0, "UNKNOWN" => 0);
$questionRating = array("RED" => "", "AMBER" => "", "GREEN" => "", "UNKNOWN" => "");
$allQuestions = array();
$allAnswers = array();
$allRatings = array();

foreach ($questions as $question) {
$shortAspect = $question['id'];
$aspect = $question['aspect'];
array_push($allQuestions, $aspect);
$minimum = $question['minimum'];
$qAnswer = $answers['payload'][$shortAspect];
array_push($allAnswers, $qAnswer);
#print "Question: $aspect   Answer: $qAnswer <br>";
$metadata = array($question['metaData'][$qAnswer]);
$rating = $question['metaData'][$qAnswer]['rank'];
array_push($allRatings, $rating);
$appRating[$rating]++;

}

#print_r($allQuestions);

$chartData = json_encode($appRating);

$bubbleData = '';
foreach ($appRating as $key => $value) {
$bubbleData .= '{text: "' . $key . '", count: "' . $value . '"},'; 
}

?>  
  
<div class="bubbleChart"/></div>

<!-- <table>
<thead>
 <tr>
  <td>Question</td>
  <td>Assessment</td>
  <td>Rating</td>
 </tr> 
</thead> -->

<!-- </table> -->

    <h3>Details</h3>

    <div>
<table id="myTable" class="tablesorter"> 
<thead> 
<tr> 
    <th>Question</th> 
    <th>Response</th> 
    <th>Rating</th> 
</tr> 
</thead> 
<tbody>     
    
<?php 
for($i = 0, $l = count($allQuestions); $i < $l; ++$i) {

print "<tr><td>" . $allQuestions[$i] . "</td><td>" . $allAnswers[$i] . "</td><td>" . $allRatings[$i] . "</td></tr>"; 

}

  ?>
  </tbody>
  </table>
    </div>
</div>
</div>
<div id="rightReviewPane">

<h3>Architect Review</h3>
<p>Please use this section to provide your assessment of the possible migration/modernisation plan and an effort estimation.</p>

<form action="#" id="myForm" method="post">
<input type="hidden" name="reviewSubmitted" value="true" /> 
<h4>Proposed Action </h4>
<select name="proposedAction" id="proposedAction">
<option value="REHOST">Re-host</option>
<option value="REPLATFORM">Re-platform</option>
<option value="REFACTOR">Refactor</option>
<option value="REPURCHASE">Repurchase</option>
<option value="RETIRE">Retire</option>
<option value="RETAIN">Retain</option>
</select>

<h4>Effort Estimate</h4>
<select name="effortEstimate" id="effortEstimate">
<option value="SMALL">Small</option>
<option value="MEDIUM">Medium</option>
<option value="LARGE">Large</option>
<option value="XLarge">Extra Large</option>
</select> 
<h4>Supporting Notes</h4>
<input type="text" name="supportingNotes">
<br>
<input type="submit" value="Submit Review">
</form>
<a href="results.php?customer=<?php echo $_REQUEST['customer'] ?>"><button>Return to Results</button></a>
</div>

</section>


<script type="text/javascript" >


$(document).ready(function () {
  var bubbleChart = new d3.svg.BubbleChart({
    supportResponsive: true,
    //container: => use @default
    size: 600,
    //viewBoxSize: => use @default
    innerRadius: 600 / 3.5,
    //outerRadius: => use @default
    radiusMin: 50,
    //radiusMax: use @default
    //intersectDelta: use @default
    //intersectInc: use @default
    //circleColor: use @default

    data: {
      items: [
        <?php echo $bubbleData;  ?>
      ],
      eval: function (item) {return item.count;},
      classed: function (item) {return item.text.split(" ").join("");}
    },
    plugins: [
      {
        name: "lines",
        options: {
          format: [
            {// Line #0
              textField: "count",
              classed: {count: true},
              style: {
                "font-size": "28px",
                "font-family": "Source Sans Pro, sans-serif",
                "text-anchor": "middle",
                fill: "white"
              },
              attr: {
                dy: "0px",
                x: function (d) {return d.cx;},
                y: function (d) {return d.cy;}
              }
            },
            {// Line #1
              textField: "text",
              classed: {text: true},
              style: {
                "font-size": "14px",
                "font-family": "Source Sans Pro, sans-serif",
                "text-anchor": "middle",
                fill: "white"
              },
              attr: {
                dy: "20px",
                x: function (d) {return d.cx;},
                y: function (d) {return d.cy;}
              }
            }
          ],
          centralFormat: [
            {// Line #0
              style: {"font-size": "50px"},
              attr: {}
            },
            {// Line #1
              style: {"font-size": "30px"},
              attr: {dy: "40px"},
            }
          ]
        }
      }]
  });
});
</script>  

  <script src="http://phuonghuynh.github.io/js/bower_components/d3/d3.min.js"></script>
  <script src="http://phuonghuynh.github.io/js/bower_components/d3-transform/src/d3-transform.js"></script>
  <script src="http://phuonghuynh.github.io/js/bower_components/cafej/src/extarray.js"></script>
  <script src="http://phuonghuynh.github.io/js/bower_components/cafej/src/misc.js"></script>
  <script src="http://phuonghuynh.github.io/js/bower_components/cafej/src/micro-observer.js"></script>
  <script src="http://phuonghuynh.github.io/js/bower_components/microplugin/src/microplugin.js"></script>
  <script src="http://phuonghuynh.github.io/js/bower_components/bubble-chart/src/bubble-chart.js"></script>
  <script src="assets/js/central-click.js"></script>
  <script src="http://phuonghuynh.github.io/js/bower_components/bubble-chart/src/plugins/lines/lines.js"></script>

	</body>
</html>
