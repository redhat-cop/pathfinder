<!--
		    data: {
		        labels: ["Stream 1", "Stream 2", "Stream 3", "Stream 4"],
		        datasets: [
		        {
		        		label:"hidden",
		            data: [0, 100, 300, 100, 400],  // indent
		            backgroundColor: "rgba(0,0,0,0)",
		            hoverBackgroundColor: "rgba(0,0,0,0)"
		        },{
		            data: [100, 200, 100, 100], // actual bar
		            backgroundColor: ['rgb(146,212,0)', 'rgb(240,171,0)', 'rgb(204, 0, 0)', 'rgb(0, 65, 83)'],
		        }]
		    },
-->


<script>
function compareValues(key, order='asc') {
  return function(a, b) {
    if(!a.hasOwnProperty(key) || !b.hasOwnProperty(key)) {
      // property doesn't exist on either object
        return 0; 
    }

    const varA = (typeof a[key] === 'string') ? a[key].toUpperCase() : a[key];
    const varB = (typeof b[key] === 'string') ? b[key].toUpperCase() : b[key];

    let comparison = 0;
    if (varA > varB) {
      comparison = 1;
    } else if (varA < varB) {
      comparison = -1;
    }
    return (
      (order == 'desc') ? (comparison * -1) : comparison
    );
  };
}
</script>

<script>

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
		function getOrder(app, map){
			var order=app['Size']-(app['OutboundDeps']!=null?app['OutboundDeps'].length:0);
			for(x=0;x<app['OutboundDeps'].length;x++){
				var dependsOn=map[app['OutboundDeps'][x]];
				if (app['Id']==dependsOn['Id']) continue; //infinite loop protection
				if (recursion>100) break;
				recursion+=1;
				order+=getOrder(dependsOn, map);
			}
			return order;
		}
		
		var adoptionChart=null;
		function redrawAdoptionPlan(applicationAssessmentSummary, initial){
			lastColor=0;
			
			// deep copy
			var summary=JSON.parse(JSON.stringify(applicationAssessmentSummary));
			
			
			// build map of app Id->app
			var appIdToAppMap=[];
			for(i=0;i<summary.length;i++)
				appIdToAppMap[summary[i].Id]=summary[i];
			///
			
			// manipulate dependsOn into a list of app NAME's rather than ID's
			for(i=0;i<summary.length;i++){
				summary[i]['Padding']=0;
				if (null!=summary[i]['OutboundDeps']){
					summary[i]['DependsOn']=[];
					summary[i]["Size"]=adoptionSize[summary[i].WorkEffort];
					
					summary[i]["AdoptionOrder"]=summary[i]["Size"];
					var biggestDependencySize=0;
					
					var dependsOn=[];
					for(d=0;d<summary[i]['OutboundDeps'].length;d++)
						dependsOn.push(appIdToAppMap[summary[i]['OutboundDeps'][d]]);
					
					//summary[i]["AdoptionOrder"]=i*1000;
					//summary[i]["AdoptionOrder"]=summary[i]["AdoptionOrder"]*dependsOn.length;
					
					// sort the dependsOn by size order to be able to set the AdoptionOrder
					dependsOn.sort(function(a,b){
						return b['Size']-a['Size'];
					});
					for(d=0;d<dependsOn.length;d++){
						//if (!appFilter.includes(dependsOn[d]['Id']))
						//	continue;
						
						summary[i]['DependsOn'].push(dependsOn[d]['Name']);
						if (dependsOn[d]['Size']>biggestDependencySize)
						  biggestDependencySize=dependsOn[d]['Size']+dependsOn[d]['Padding'];
						console.log("AdoptionGraph::"+ summary[i]['Name']+" depends on "+dependsOn[d]['Name']);
						//dependsOn[d]['AdoptionOrder']=summary[i]["AdoptionOrder"]-1;
					}
					
					summary[i]["Padding"]=biggestDependencySize;
				}
			}
			
			
			// remove any apps that are not selected (because this screws up the sorting)
			for(i=0;i<summary.length;i++){
				if (!appFilter.includes(summary[i]['Id'])){
					summary.splice(i,1);
					continue;
				}
			}
			
			summary.sort(function(a,b){
				return a['Size']-b['Size'];
			});
			
			for(i=0;i<summary.length;i++){
				summary[i]["AdoptionOrder"]=i+getOrder(summary[i], appIdToAppMap);
			}
			
			// sort in size order (small to large)
			//summary.sort(compareValues("AdoptionOrder"));
			summary.sort(function(a,b){
				return a['AdoptionOrder']-b['AdoptionOrder'];
			});
			//summary.reverse();
			
			//function bubbleSort(a, key){
		  //  var swapped;
		  //  do {
	    //    swapped = false;
	    //    for (var i=0;i<a.length-1;i++) {
      //      if (a[i][key]>a[i+1][key]) {
      //      	console.log(a[i][key]+" is > than "+a[i+1][key] +" - so switching them "+a[i]['Name']+"<->"+a[i+1]['Name']);
      //        var temp = a[i];
      //        a[i] = a[i+1];
      //        a[i+1] = temp;
      //        swapped = true;
      //      }
	    //    }
		  //  } while (swapped);
			//}
			//bubbleSort(summary, "AdoptionOrder");
			
			
			for(i=0;i<summary.length;i++){
				if (!appFilter.includes(summary[i]['Id'])) continue;
				console.log("AdoptionGraph:: DisplayOrder->"+summary[i]['AdoptionOrder'] +" - "+summary[i]['Padding']+"/"+summary[i]['Size'] +"-"+ summary[i]['Name']);
			}
			
			//console.log(JSON.stringify(summary));
			
			
			//console.log("x="+JSON.stringify(getApp("Online Ticker",summary)));
			//console.log("l="+summary.length);
			//console.log("summary="+JSON.stringify(summary));
			
			
			var hiddenDS={
    		label:"hidden",
        data: [],  // indent
        backgroundColor: "rgba(0,0,0,0)",
        hoverBackgroundColor: "rgba(0,0,0,0)"
			};
			var realDS={
        data: [],  // actual bar
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
				realDS.backgroundColor[c]=getNextColor();
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
					    //    callbacks: {
					    //       label: function(tooltipItem,data) {
					    //       				var label = data.datasets[tooltipItem.datasetIndex].label || '';
					    //              return label;
					    //       }
					    //    }
					    //}
					}
			});
		};
			//window.myBar=chart;
			//// this part to make the tooltip only active on your real dataset
			//var originalGetElementAtEvent = chart.getElementAtEvent;
			//chart.getElementAtEvent = function (e) {
			//		console.log(e);
			//    return originalGetElementAtEvent.apply(this, arguments).filter(function (e) {
			//        return e._datasetIndex === 1;
			//    });
			//}
			
			
			
		//});
		

</script>