<!--
				data: {
						labels: ["Stream 1", "Stream 2", "Stream 3", "Stream 4"],
						datasets: [
						{
								label:"hidden",
								data: [0, 100, 300, 100, 400],	// indent
								backgroundColor: "rgba(0,0,0,0)",
								hoverBackgroundColor: "rgba(0,0,0,0)"
						},{
								data: [100, 200, 100, 100], // actual bar
								backgroundColor: ['rgb(146,212,0)', 'rgb(240,171,0)', 'rgb(204, 0, 0)', 'rgb(0, 65, 83)'],
						}]
				},
-->

<script>
//function compareValues(key, order='asc') {
//	return function(a, b) {
//		if(!a.hasOwnProperty(key) || !b.hasOwnProperty(key)) {
//			// property doesn't exist on either object
//				return 0; 
//		}
//
//		const varA = (typeof a[key] === 'string') ? a[key].toUpperCase() : a[key];
//		const varB = (typeof b[key] === 'string') ? b[key].toUpperCase() : b[key];
//
//		let comparison = 0;
//		if (varA > varB) {
//			comparison = 1;
//		} else if (varA < varB) {
//			comparison = -1;
//		}
//		return (
//			(order == 'desc') ? (comparison * -1) : comparison
//		);
//	};
//}
</script>

<script>
		var logLevel="INFO";
		
		//httpGetObject(Utils.SERVER+"/api/pathfinder/customers/"+customerId+"/report", function(report){
			//var summaryList=report.summaryList;
		var adoptionPlanColors=[];
		var lastColor=0;
		adoptionPlanColors[0]='rgb(146,212,0)';
		adoptionPlanColors[1]='rgb(240,171,0)';
		adoptionPlanColors[2]='rgb(204, 0, 0)';
		adoptionPlanColors[3]='rgb(0, 65, 83)';
		adoptionPlanColors[4]='#3B0083';
		adoptionPlanColors[5]='#A3DBE8';
		adoptionPlanColors[6]='#808080';
		
		adoptionPlanColors[7]='#a30000';
		adoptionPlanColors[8]='#004254';
		adoptionPlanColors[9]='#0088ce';
		adoptionPlanColors[10]='#409c36';
		adoptionPlanColors[11]='#7aa4ae';
		adoptionPlanColors[12]='#820000';
		adoptionPlanColors[13]='#40717f';
		adoptionPlanColors[14]='#40a6da';
		adoptionPlanColors[15]='#70b568';
		adoptionPlanColors[16]='#516d74';

/*		
		red bright darker
		cc0000
		a30000
		820000
		
		teals
		004254
		40717f
		00313f
		
		blues
		0088ce
		bfe1f3
		40a6da
		00669a
		004467
		
		greens
		409c36
		a0ce9b
		70b568
		307528
		204e1b
		
		blue/greys
		7aa4ae
		516d74
		*/
		
		function getNextColor(){
			var c=lastColor++;
			if (lastColor>=adoptionPlanColors.length) lastColor=0;
			return adoptionPlanColors[c];
		}
		
		var adoptionSize=[];
		adoptionSize['null']=0;
		adoptionSize['SMALL']=10;
		adoptionSize['MEDIUM']=20;
		adoptionSize['LARGE']=40;
		adoptionSize['XLarge']=80;
		
		
		// ugly fix - do something like embed the order so we dont need external variables
		var maxRecursion=100;
		var recursion=0;
		function getOrder(parent, app, map){
			var order=app['Size']-(app['OutboundDeps']!=null?app['OutboundDeps'].length:0);
			if (logLevel=="DEBUG") console.log("DEBUG:: OrderCalc: "+(parent==null?"":parent['Name']+".")+app['Name'] +": order = "+order);
			for(x=0;x<app['OutboundDeps'].length;x++){
				var dependsOn=map[app['OutboundDeps'][x]];
				if (undefined==dependsOn) continue;
				if (app['Id']==dependsOn['Id']) continue; //infinite loop protection
				if (recursion>100) break;
				recursion+=1;
				//console.log("OrderCalc: "+app['Name']+" -> "+dependsOn['Name']);
				order+=getOrder(app, dependsOn, map);
			}
			return order;
		}
		
		
		function getPadding(dependsOn){
			if (null!=dependsOn && dependsOn.length!=0){
				for(d=0;d<dependsOn.length;d++){
					if (dependsOn[d]['Padding']==null){
						dependsOn[d]['Padding']=getPadding(dependsOn[d]['DependsOn']);
					}
				}
				// all should have padding now
				var padding=0;
				for(d=0;d<dependsOn.length;d++){
					if (appFilter.includes(dependsOn[d]['Id'])){
						padding=Math.max(padding, dependsOn[d]['Padding']+dependsOn[d]['Size']);
					}else{
						padding=Math.max(padding, dependsOn[d]['Padding']);
					}
				}
				return padding;
			}else{
				return 0;
			}
		}
		
		
		var adoptionChart=null;
		function redrawAdoptionPlan(applicationAssessmentSummary, initial){
			lastColor=0;
			
			// deep copy
			var summary=JSON.parse(JSON.stringify(applicationAssessmentSummary));
			
			// init colors
			var colors=[];
			for(i=0;i<summary.length;i++){
				colors[summary[i]['Name']]=getNextColor();
			}
			
			// build map of app Id->app
			var appIdToAppMap=[];
			for(i=0;i<summary.length;i++){
				appIdToAppMap[summary[i].Id]=summary[i];
				summary[i]["Size"]=adoptionSize[summary[i].WorkEffort];
			}
			
			// re-order summary by # of dependencies (0 first) so that when we get to calculate padding the deps have their padding set first before any dependants
			for(i=0;i<summary.length;i++){
				summary[i]['DependsOn']=[];
				if (undefined!=summary[i]['OutboundDeps']){
					for(d=0;d<summary[i]['OutboundDeps'].length;d++){
						
						var dependent=appIdToAppMap[summary[i]['OutboundDeps'][d]];
						if (dependent!=undefined){ // this could happen if the dependency is on a non-assessable application
							summary[i]['DependsOn'].push(dependent);
							console.log("INFO :: AdoptionGraph:: DependsOn:: "+ summary[i]['Name']+" depends on "+summary[i]['DependsOn'][d]['Name']);
						}
						//summary[i]['DependsOn'].push(appIdToAppMap[summary[i]['OutboundDeps'][d]]);
						
					}
				}else{
					summary[i]['OutboundDeps']=[];
				}
			}
			
			// Padding
			for(i=0;i<summary.length;i++){
				summary[i]['Padding']=getPadding(summary[i]['DependsOn']);
			}
			
			
			// TODO: This almost works, is has a defect on transitive dependencies, if A->B->C then if you untick B then C starts at 0 with A
			var summary2=[];
			var added=[];
			var loop=0,maxLoop=100;
			while(summary.length>summary2.length){
				loop+=1;
				if (loop>maxLoop) break;
				
				for(i=0;i<summary.length;i++){
					if (summary.length<=summary2.length) break; //shortcut
					
					if (!added.includes(summary[i]['Name'])){
//						if (summary[i]['DependsOn'].length==0){
//							summary2.push(summary[i]);
//							added.push(summary[i]['Name']);
//							summary[i]['Padding']=0;
//						}else{
//							var allDepsAdded=true;
//							for(d=0;d<summary[i]['DependsOn'].length;d++){
//								allDepsAdded=allDepsAdded && (added.includes(summary[i]['DependsOn'][d]['Name']) || !appFilter.includes(summary[i]['DependsOn'][d]['Id']));
//							}
//							if (allDepsAdded){
								summary2.push(summary[i]);
								added.push(summary[i]['Name']);
								// if all deps added, then go back through them to calculate the padding
//								var biggestDependencySize=0;
//								for(d=0;d<summary[i]['DependsOn'].length;d++){
//									var size=!appFilter.includes(summary[i]['DependsOn'][d]['Id'])?0:summary[i]['DependsOn'][d]['Size'];
//									if (size>biggestDependencySize)
//										biggestDependencySize=summary[i]['DependsOn'][d]['Size']+summary[i]['DependsOn'][d]['Padding'];
//								}
//								summary[i]['Padding']=biggestDependencySize;
//							}
//						}
					}
				}
			}
			summary=summary2;
			
			
			// manipulate dependsOn into a list of app NAME's rather than ID's
			for(i=0;i<summary.length;i++){
				
				if (null!=summary[i]['OutboundDeps']){
					//summary[i]['DependsOn']=[];
					//summary[i]["Size"]=adoptionSize[summary[i].WorkEffort];
					
					summary[i]["AdoptionOrder"]=summary[i]["Size"];
					
					//var dependsOn=[];
					//for(d=0;d<summary[i]['OutboundDeps'].length;d++)
					//	dependsOn.push(appIdToAppMap[summary[i]['OutboundDeps'][d]]);
					//
					//// sort the dependsOn by size order to be able to set the AdoptionOrder
					//dependsOn.sort(function(a,b){
					//	return b['Size']-a['Size'];
					//});
					//
					//summary[i]['DependsOn']=dependsOn;
					
					//for(d=0;d<dependsOn.length;d++){
					//	//dependsOn.push(summary[i]['DependsOn'][d]['Name']);
					//	console.log("INFO :: AdoptionGraph:: DependsOn:: "+ summary[i]['Name']+" depends on "+summary[i]['DependsOn'][d]['Name']);
					//	//summary[i]['DependsOn'][d]['AdoptionOrder']=summary[i]["AdoptionOrder"]-1;
					//}
					
					
				}
			}
			

			// remove any apps that are not selected (because this screws up the sorting)
			for(i=0;i<summary.length;i++){
				if (!appFilter.includes(summary[i]['Id'])){
					summary.splice(i,1);
					continue;
				}
			}
			
			
			// this sets the horiontal bar order that the apps are displayed (top to bottom), in an effort to try to group dependent apps
			//summary.sort(function(a,b){
			//	return a['Size']-b['Size'];
			//});
			for(i=0;i<summary.length;i++){
				summary[i]["AdoptionOrder"]=i+getOrder(null, summary[i], appIdToAppMap);
				//console.log("INFO :: AdoptionGraph:: AdoptionOrder:: "+summary[i]['Name']+" = "+summary[i]["AdoptionOrder"]);
			}
			
			
			// sort in size order (small to large)
			//summary.sort(compareValues("AdoptionOrder"));
			summary.sort(function(a,b){
				return a['AdoptionOrder']-b['AdoptionOrder'];
			});
			//summary.reverse();
			
			//function bubbleSort(a, key){
			//	var swapped;
			//	do {
			//		swapped = false;
			//		for (var i=0;i<a.length-1;i++) {
			//			if (a[i][key]>a[i+1][key]) {
			//				console.log(a[i][key]+" is > than "+a[i+1][key] +" - so switching them "+a[i]['Name']+"<->"+a[i+1]['Name']);
			//				var temp = a[i];
			//				a[i] = a[i+1];
			//				a[i+1] = temp;
			//				swapped = true;
			//			}
			//		}
			//	} while (swapped);
			//}
			//bubbleSort(summary, "AdoptionOrder");
			
			
			for(i=0;i<summary.length;i++){
				if (!appFilter.includes(summary[i]['Id'])) continue;
				console.log("INFO :: AdoptionGraph:: DisplayOrder->"+summary[i]['AdoptionOrder'] +" - "+summary[i]['Padding']+"/"+summary[i]['Size'] +"-"+ summary[i]['Name']);
			}
			
			//console.log(JSON.stringify(summary));
			
			
			//console.log("x="+JSON.stringify(getApp("Online Ticker",summary)));
			//console.log("l="+summary.length);
			//console.log("summary="+JSON.stringify(summary));
			
			
			var hiddenDS={
				label:"hidden",
				data: [],	// indent
				backgroundColor: "rgba(0,0,0,0)",
				hoverBackgroundColor: "rgba(0,0,0,0)"
			};
			var realDS={
				data: [],	// actual bar
				backgroundColor: [],
			};
			var data={labels:[],datasets:[hiddenDS,realDS]};
			
			var c=0;
			for(i=0;i<summary.length;i++){
				var app=summary[i];
				
				if (!appFilter.includes(app['Id'])) continue; // should never happen by this point
				
				data.labels[c]=app['Name'];
				hiddenDS.data[c]=app['Padding'];
				realDS.data[c]=app['Size'];
				realDS.backgroundColor[c]=colors[app['Name']];
				//realDS.backgroundColor[c]=getNextColor();
				c++;
			}
			
			//console.log(JSON.stringify(data));
			
			
			// if the chart has been drawn already, then just update the data
			if (adoptionChart!=null){
				adoptionChart.data=data;
				adoptionChart.options.animation.duration=initial?1000:1;
				adoptionChart.update();
				return;
			}
			
			var ctx = document.getElementById('adoption').getContext('2d');
			adoptionChart = new Chart(ctx, {
				type: 'horizontalBar',
				data: data,
				options:{
							plugins: {
								datalabels: {
									display: false
								}
							},
							animation: {
								duration: 1000
							},
							hover :{
								animationDuration:10
							},
							scales: {
									xAxes: [{
											label:"Duration",
											ticks: {
													stepSize: 10,
													display: false,
													beginAtZero:true,
													fontFamily: "'Open Sans Bold', sans-serif",
													fontSize:11
											},
											scaleLabel:{
													display:false
											},
											gridLines: {
											}, 
											stacked: true
									}],
									yAxes: [{
											gridLines: {
													display:false,
													color: "#fff",
													zeroLineColor: "#fff",
													zeroLineWidth: 0
											},
											ticks: {
													fontFamily: "'Open Sans Bold', sans-serif",
													fontSize:11
											},
											stacked: true
									}]
							},
							legend:{
									display:false
							},
							tooltips:{
								enabled: false
							},
							//tooltips: {
							//		callbacks: {
							//			 label: function(tooltipItem,data) {
							//			 				var label = data.datasets[tooltipItem.datasetIndex].label || '';
							//							return label;
							//			 }
							//		}
							//}
					}
			});
		};
			//window.myBar=chart;
			//// this part to make the tooltip only active on your real dataset
			//var originalGetElementAtEvent = chart.getElementAtEvent;
			//chart.getElementAtEvent = function (e) {
			//		console.log(e);
			//		return originalGetElementAtEvent.apply(this, arguments).filter(function (e) {
			//				return e._datasetIndex === 1;
			//		});
			//}
			
			
			
		//});
		

</script>