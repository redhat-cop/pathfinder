
function getEffortColor(effort) {
var nodeColor = "#FFFFFF";
switch (effort) {
case "SMALL":
		nodeColor = "#00FF00";
		break;
case "MEDIUM":
		nodeColor = "#ec7a08";
		break;
case "LARGE":
		nodeColor = "#FF0000";
		break;
case "XLARGE":
		nodeColor = "#FF0000";
		break;
}
return nodeColor;
}

function getNodeSize(BusinessPriority) {
var nodeSize = 1 + BusinessPriority;
return nodeSize;
}

function getRandomNodeSize(BusinessPriority) {
var nodeSize = 1 + Math.floor(Math.random() * 5) + 1  ;
return nodeSize;
}


var appUuids=[];
var nodes = new vis.DataSet([]);
var edges = new vis.DataSet([]);
   	httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId+"/applicationAssessmentSummary", function(applications){
var i;
//console.log(applications);
console.log("Number of Nodes: " + applications.length);
for(i=0;i<applications.length;i++){
 nodes.add({id:applications[i].Id, label: applications[i].Name, title:applications[i].Name, effort: applications[i].WorkEffort, color: getEffortColor(applications[i].WorkEffort) });
}   	

   	httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId+"/dependencyTree", function(deps){
function getRandomRgb() {
    var num = Math.round(0xffffff * Math.random());
    var r = num >> 16;
    var g = num >> 8 & 255;
    var b = num & 255;
    return 'rgb(' + r + ', ' + g + ', ' + b + ')';
}
var i;
for(i=0;i<deps['DepsList'].length;i++){
var nodeColor = getRandomRgb();
var fromId = deps['DepsList'][i]['from'];
var toId = deps['DepsList'][i]['to'];
if (appUuids.indexOf(fromId) == -1) {
appUuids.push(fromId);
}

if (appUuids.indexOf(toId) == -1) {
appUuids.push(toId);
}
edges.add({from: fromId, to:toId});
	}
edges.add(deps);	
console.log(nodes);				

   	});
});

//result.datasets[0]={id:[],label:[],color:[]};

//console.log(result);

//    function populateNodeArray() {
//    nodes = new vis.DataSet([
//        {id: 1, label: '6396dbdd-9c2a-4e90-ba24-b5c110c69602', title: 'Application description and other stuff for the Big App', color: '#FF0000', effort: "High", priority: "4"},
//        {id: 2, label: 'Oracle CRM', title: 'Web Application2 description and other stuff', color: "#7BE141", effort: "Low", priority: "5"},
//        {id: 3, label: 'Backend Database', title: 'Application3 description and other stuff', color: '#FF0000', effort: "High", priority: "9"},
//        {id: 4, label: 'Penny Pincher', title: 'Application4 description and other stuff', color: '#7BE141', effort: "Low", priority: "5"},
//        {id: 5, label: 'Customer Help CMS', title: 'Application5 description and other stuff', color: "#FCC200", effort: "Medium", priority: "2"},
//        {id: 6, label: 'Whinger', title: 'Application6 description and other stuff!', color: "#FCC200", effort: "Medium", priority: "4"},
//        {id: 7, label: 'Orphan Application', title: 'Orphan Application description and other stuff', color: '#7BE141', effort: "Low", priority: "1"},
//        {id: 8, label: 'Call Trace', title: 'Application8 description and other stuff', color: "#FCC200", effort: "Medium", priority: "3"},
//        {id: 9, label: 'Orange HRM', title: 'Application9 description and other stuff', color: '#7BE141', effort: "Low", priority: "6"},
//        {id: 10, label: 'Check-In API', title: 'Application10 description and other stuff', color: '#7BE141', effort: "Low", priority: "3"}
//    ]);
//    return nodes;
//    }

//    nodes = populateNodeArray();
    
// create an array with edges
//    var edges = new vis.DataSet([
//        {from: "6396dbdd-9c2a-4e90-ba24-b5c110c69602", to: 4, arrows: "from"},
//        {from: 2, to: 5,arrows: "from"},
//        {from: 3, to: 6, arrows: "from"},
//        {from: 4, to: 8, arrows: "from"},
//        {from: 5, to: 8, arrows: "to"},
//        {from: 8, to: 10, arrows: "to"},
//        {from: 9, to: 10},
//    ]);
    
    
    function getRemoveColouredNodes(val) {
	var count = 0;

	nodes.forEach(function(d) {
	if (d.color == val) {
    nodes.remove({id:d.id});
	}
	});
    }
    
    function removeRandomNode(nodeId) {
        nodes.remove({id:10});
        nodes.remove({id:9});
        nodes.remove({id:8});
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
//        document.getElementById('eventSpan').innerHTML = '<p>' + nodes.get(params)['label'] + '</p><p>Effort Estimate: ' + nodes.get(params)['effort'] + "</p><p>" + nodes.get(params)['title'] + "</p><p>Business Priority: " + nodes.get(params)['priority'] + "</p>";
//        document.getElementById('eventSpan').innerHTML = '<p>Effort Estimate: ' + nodes.get(params)['effort'] + "</p><p>" + nodes.get(params)['title'] + "</p><p>Business Priority: " + nodes.get(params)['priority'] + "</p>";
//        document.getElementById('eventSpan').innerHTML = "<p>" + nodes.get(params)['title'] + "</p><p>Business Priority: " + nodes.get(params)['priority'] + "</p>";
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
//        console.log('hoverNode Event:', params);
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
