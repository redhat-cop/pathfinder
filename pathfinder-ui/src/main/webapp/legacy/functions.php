<?php
function putMenu() {
print '		<!-- Nav -->
			<nav id="menu">
				<ul class="links">
					<li><a href="index.php">Home</a></liadmin>
					<li><a href="http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/" target=_blank>Assessment</a></li>
					<li><a href="results.php">Results</a></li>
					<li><a href="admin.php">Admin</a></li>
					<li><a href="workflow.php">Workflow</a></li>
					<li><a href="credits.php">Credits</a></li>
				</ul>
			</nav>';
}

function getCustomerName($custId) {
$customerDetails = file_get_contents("http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/$custId");
$nn = json_decode($customerDetails,true);
$name = $nn['CustomerName'];
return $name;
}

function deleteCustomer($custId) {
#$data = array("Name" => $appName, "Description" => $appDesc);
#$data_string = json_encode($data); 	
$url = "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/" . $custId;
$ch = curl_init($url);
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "DELETE");                                                                     
#curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);                                                                  
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);                                                                      
curl_setopt($ch, CURLOPT_HTTPHEADER, array(                                                                          
    'Content-Type: application/json') 
);                                                                                                                   

$result = curl_exec($ch); 
}
?>