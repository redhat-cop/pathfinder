    <!DOCTYPE HTML>
<!--
	Industrious by TEMPLATED
	templated.co @templatedco
	Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
-->
<html>
<?php
$custId = $_REQUEST['customer'];

?>
  <%@include file="head.jsp"%>

    <script src="assets/js/jquery.min.js"></script>
    <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.js"></script>

    <script type="text/javascript">


    function getRequestApp(url) {
        var xhttp = new XMLHttpRequest();
        console.log(url);
        let data = null;

        xhttp.onreadystatechange = function() {
            if (this.readyState === 4 && this.status === 200) {
            data = JSON.parse(this.responseText);
            console.log("data is 8 ");
            console.log(data);
            sortApplicationIds(data);
            }
        }
        xhttp.open("GET", url, true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send();
    }


    function sortApplicationIds(data){
        console.log("data length " + data.length);
        var appsAndId = [];
        // var xhr = [];
        for (let i = 0; i < data.length; i++){
            if (data[i]["Review"] !== null){
                appsAndId.push([data[i]["Name"], data[i]["Id"], data[i]["Review"]]);
                // console.log("apps and id: ", appsAndId);
            }
        }
        console.log("apps and id: ", appsAndId);
        getRequestReview(appsAndId);
    }


    function getRequestReview(appsAndId) {
        const xhttp = new XMLHttpRequest();
        let customerId = '<?php print $custId; ?>';
        let data = null;
        let applicationName = '';
        console.log(appsAndId);

        dataSet.pop()

        // xhttp.onreadystatechange = function() {
        //     if (this.readyState === 4 && this.status === 200) {
        //     let data = JSON.parse(this.responseText);
        //     console.log("return ", data);
        //     sortReviewData(data, applicationName);
        //     }
        // }
        let xhr = [];
        for (let i = 0; i < appsAndId.length; i++){
            let applicationName = appsAndId[i][0];
            let applicationId = appsAndId[i][1];
            let reviewId = appsAndId[i][2];
            let url = "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/" + String(customerId) + "/applications/" + applicationId + "/review/" + reviewId + "/";
            xhr[i] = new XMLHttpRequest();
            xhr[i].open("GET", url, true);
            xhr[i].setRequestHeader("Content-type", "application/json");
            xhr[i].onreadystatechange = function() {
                if (this.readyState === 4 && this.status === 200) {
                let data = JSON.parse(this.responseText);
                console.log("return ", data);
                sortReviewData(data, applicationName);
                }
            }
            xhr[i].send();
        }
        

    }

    function sortReviewData(data, applicationName) {
        // console.log("Data is here", data);
        // console.log("BP", data["BusinessPriority"]);
        // console.log("RD", data["ReviewDecision"]["rank"]);
        //var dataSet = [[]];
        dataSet.push([applicationName, data["ReviewDecision"]["rank"], data["WorkEffort"]["rank"], data["BusinessPriority"]]);
        console.log(dataSet);




        
        $(document).ready(function() {
        $('#reviewTable').DataTable( {
        data: dataSet,
        retrieve: true,
        paging: false,
        columns: [
            { title: "Application" },
            { title: "Decision" },
            { title: "Estimate" },
            { title: "Business Priority" },
        ]
    } );
} );

    }


    function getApplications() {
        let customerId = '<?php print $custId; ?>';
        let url = "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/" + String(customerId) + "/applications/";
        getRequestApp(url);

    }

    var dataSet = [
         [ "", "", "", ""],
     ];

        

    getApplications();

    // var applicationName = getAppInfo();
    // for (let i = 0; i < applicationName.length; i++){
    //     console.log(applicationName[i]);
    // }
    // console.log("AppNames 2 ",applicationName);
    // console.log("asdasd ", applicationName[0]);
    // console.log(typeof applicationName);



// $(document).ready(function() {
//     $('#reviewTable').DataTable( {
//         data: dataSet,
//         columns: [
//             { title: "Application" },
//             { title: "Decision" },
//             { title: "Estimate" },
//             { title: "Business Priority" },
//         ]
//     } );
// } );
</script>


		<script>
	  $( function() {
	    $( "#speed" ).selectmenu();

	    $( "#files" ).selectmenu();

	    $( "#number" )
	      .selectmenu()
	      .selectmenu( "menuWidget" )
	        .addClass( "overflow" );

	    $( "#salutation" ).selectmenu();
	  } );
	  </script>


      
	</head>

	<body class="is-preload">

		<!-- Header -->
			<header id="header">
				<a class="logo" href="index.jsp">Pathfinder</a>
				<nav>
					<a href="#menu">Menu</a>
				</nav>
			</header>



    <div class="table-responsive">  
    <section id="banner2">
        <div class="inner">
            <h1>Review Pane</h1>
            <p>Review and view the results of all assessments.</div>
    </section>


    <table id="reviewTable" class="display" width="100%"></table>
<!--  
        <table id="table_id" class="display">
            <thead>  
                <tr>  
                    <td><b>Application Name</b></td>  
                    <td><b>Decision</b></td>  
                    <td><b>Estimate</b></td>  
                    <td><b>Business Priority</b></td>  
                </tr>  
            </thead>  
            <tr>
                <td>My App 1</td>
                <td>Refactor</td>
                <td>Large</td>
                <td>6</td>
            </tr>
            <tr>
                <td>My App 2</td>
                <td>Retire</td>
                <td>Large</td>
                <td>3</td>
            </tr>
            <tr>
                <td>My App 3</td>
                <td>Rehost</td>
                <td>Small</td>
                <td>9</td>
            </tr>
        </table>
        <script>
      $(document).ready( function () {
      $('#table_id').DataTable();
      } );
      </script> -->

    </div>



		<!-- Scripts -->
			<script src="assets/js/browser.min.js"></script>
			<script src="assets/js/breakpoints.min.js"></script>
			<script src="assets/js/util.js"></script>
			<script src="assets/js/main.js"></script>
         <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
         


	</body>
</html>
