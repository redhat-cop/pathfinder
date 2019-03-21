<!DOCTYPE HTML>
<html>
  
  <%@include file="head.jsp"%>
  
  <link href="assets/css/breadcrumbs.css" rel="stylesheet" />
	
  <!-- #### DATATABLES DEPENDENCIES ### -->
  <!-- Firefox doesnt support link imports yet
  <link rel="import" href="datatables-dependencies.jsp">
  -->
  <%@include file="datatables-dependencies.jsp"%>
  
	
	<body class="is-preload">
  	<%@include file="nav.jsp"%>
  	

		<section id="banner2">
			<div class="inner">
				<h1>Assessment Summary</h1>
				<p>View the summary of assesssment results and review output.</p>
			</div>
		</section>
		
		<%@include file="breadcrumbs.jsp"%>
		<!--
  	<div id="breadcrumbs">
			<ul class="breadcrumb">
				<li><a href="manageCustomers.jsp">Customers</a></li>
				<li><span id="breadcrumb"></span></li>
			</ul>
		</div>
		-->
		
		<section class="wrapper">
			<div class="inner">
			
			
			
			
    <script type="text/javascript" src="assets/js/vis.js"></script>

    <style type="text/css">
        #mynetwork {
	   position:absolute;
	   top:-10px;
	   right:0;
      vertical-align: top;
      width: 100%;
      height: 600px;
		font-family: overpass;        
        }
        
        #eventSpan {
        float: right;
        margin-right: 20px;
		  font-family: overpass;        
        }
    </style>
</head>
	<body class="is-preload">
		<%@include file="nav.jsp"%>
		
		<div id="breadcrumbs">
			<ul class="breadcrumb">
				<li><a href="manageCustomers.jsp">Customers</a></li>
			</ul>
		</div>
		
		<section class="wrapper">
			<div class="inner">
<div id="toggleNodes">		
<input type="button"  onclick="getRemoveColouredNodes('#FF0000');" value="Remove Red"></input>
<input type="button"  onclick="getRemoveColouredNodes('#FCC200');" value="Remove Amber"></input>
<input type="button"  onclick="getRemoveColouredNodes('#7BE141');" value="Remove Green"></input>
<input type="reset"  onclick="populateNodeArray();window.location.reload() "></input>
</div>

<div id="eventSpan"></div>
<div id="mynetwork"></div>

<script type="text/javascript">

    var nodes = "";    
    // create an array with nodes
    function populateNodeArray() {
    nodes = new vis.DataSet([
        {id: 1, label: 'LandWork ERP System', title: 'Application description and other stuff for the Big App', color: '#FF0000', effort: "High", priority: "4"},
        {id: 2, label: 'Oracle CRM', title: 'Web Application2 description and other stuff', color: "#7BE141", effort: "Low", priority: "5"},
        {id: 3, label: 'Backend Database', title: 'Application3 description and other stuff', color: '#FF0000', effort: "High", priority: "9"},
        {id: 4, label: 'Penny Pincher', title: 'Application4 description and other stuff', color: '#7BE141', effort: "Low", priority: "5"},
        {id: 5, label: 'Customer Help CMS', title: 'Application5 description and other stuff', color: "#FCC200", effort: "Medium", priority: "2"},
        {id: 6, label: 'Whinger', title: 'Application6 description and other stuff!', color: "#FCC200", effort: "Medium", priority: "4"},
        {id: 7, label: 'Orphan Application', title: 'Orphan Application description and other stuff', color: '#7BE141', effort: "Low", priority: "1"},
        {id: 8, label: 'Call Trace', title: 'Application8 description and other stuff', color: "#FCC200", effort: "Medium", priority: "3"},
        {id: 9, label: 'Orange HRM', title: 'Application9 description and other stuff', color: '#7BE141', effort: "Low", priority: "6"},
        {id: 10, label: 'Check-In API', title: 'Application10 description and other stuff', color: '#7BE141', effort: "Low", priority: "3"}
    ]);
    return nodes;
    }

    nodes = populateNodeArray();
    
    // create an array with edges
    var edges = new vis.DataSet([
        {from: 1, to: 3},
        {from: 1, to: 2},
        {from: 2, to: 4, arrows: "from"},
        {from: 2, to: 5,arrows: "from", dashes:true},
        {from: 1, to: 5, arrows: "from"},
        {from: 1, to: 6, arrows: "to"},
        {from: 3, to: 6, arrows: "from"},
        {from: 3, to: 9, arrows: "to"},
        {from: 4, to: 8, arrows: "from"},
        {from: 5, to: 8, arrows: "to", dashes:true},
        {from: 8, to: 10, arrows: "to"},
        {from: 9, to: 10},
        {from: 10, to: 3}


    ]);
    
    
    function getRemoveColouredNodes(val) {
	var count = 0;
		console.log(val);
	nodes.forEach(function(d) {
//	if (d.color == '#7BE141') {
//var color = d.color;
	if (d.color == val) {
//    console.log(d.id);	
    nodes.remove({id:d.id});
	}
	});
    }
    
    function removeRandomNode(nodeId) {
        nodes.remove({id:10});
        nodes.remove({id:9});
        nodes.remove({id:8});
//        nodes.remove({color:'#7BE141'});
//console.log("Removing node " + nodeId);
console.log(nodes);
//        var index = nodeIds.indexOf(randomNodeId);
//        nodeIds.splice(index,1);
    }
 

    // create a network
    var container = document.getElementById('mynetwork');
    var data = {
        nodes: nodes,
        edges: edges
    };

    var options = {
			interaction:{hover:true},
			manipulation: {
				enabled: true
			}
		};

    var network = new vis.Network(container, data, options);

    network.on("click", function (params) {
        params.event = "[original event]";
        //console.log(params);
//        document.getElementById('eventSpan').innerHTML = '<h3>More info</h3><p>' + nodes.get(params)['title'] + '</p>';
//        document.getElementById('eventSpan').innerHTML = '<h2>Click event:</h2>' + JSON.stringify(params, null, 4);
//        console.log('click event, getNodeAt returns: ' + this.getNodeAt(params.pointer.DOM));
//console.log(nodes.get(params)['label']);
    });

//    network.on("oncontext", function (params) {
//        params.event = "[original event]";
//        document.getElementById('eventSpan').innerHTML = '<h2>oncontext (right click) event:</h2>' + JSON.stringify(params, null, 4);
//    });


    network.on("showPopup", function (params) {
        document.getElementById('eventSpan').innerHTML = '<h4>' + nodes.get(params)['label'] + '</h4><p>Effort Estimate: ' + nodes.get(params)['effort'] + "</p><p>" + nodes.get(params)['title'] + "</p><p>Business Priority: " + nodes.get(params)['priority'] + "</p>";
//        document.getElementById('eventSpan').innerHTML = '<h3>Effort Estimate: ' + nodes.get(params)['effort'] + "</h3><h4>" + nodes.get(params)['title'] + "</h4>" ;
        console.log(nodes.get(params)['effort']);
//        document.getElementById('eventSpan').innerHTML = '<h2>Effort Estimate ' + this.getNodeAt(params.pointer.DOM);
    });
    network.on("hidePopup", function () {
//        console.log('hidePopup Event');
    });
    network.on("select", function (params) {
//        console.log('select Event:', params);
    });
    network.on("selectNode", function (params) {
//        console.log('selectNode Event:', params);
    });
    network.on("selectEdge", function (params) {
//        console.log('selectEdge Event:', params);
    });
    network.on("deselectNode", function (params) {
//        console.log('deselectNode Event:', params);
    });
    network.on("deselectEdge", function (params) {
//        console.log('deselectEdge Event:', params);
    });
    network.on("hoverNode", function (params) {
        console.log('hoverNode Event:', params);
    });
    network.on("hoverEdge", function (params) {
//        console.log('hoverEdge Event:', params);
    });
    network.on("blurNode", function (params) {
//        console.log('blurNode Event:', params);
    });
    network.on("blurEdge", function (params) {
//        console.log('blurEdge Event:', params);
    });


</script>
			</div>
		</section>

</body>
</html>
