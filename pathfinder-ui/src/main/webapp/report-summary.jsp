				<script>
					function buildGuage(count,total,completeColorRGB,incompleteColorRGB,title){
					  var percentage=(count/total)*100;
					  var remaining=100-percentage;
					  var tooltip=(count==1?count+" Application":count+" Applications");
						return {
							"type":"doughnut",
			        "data": {
                "labels": [tooltip,"",remaining],
		            "datasets": [
		                {
		                    "data": [
		                        percentage,
		                        1,
		                        remaining
		                    ],
		                    "backgroundColor": [
		                        completeColorRGB,
		                        "rgba(0, 0, 0, 0.6)",
		                        incompleteColorRGB,
		                    ],
		                    "borderWidth": 0,
		                    "hoverBorderWidth": 0
		                },
		                {
		                    "data": [
		                        percentage,
		                        1,
		                        remaining
		                    ],
		                    "backgroundColor": [
		                        "rgba(0, 0, 0, 0)",
		                        "rgba(0, 0, 0, 0.6)",
		                        "rgba(0, 0, 0, 0)",
		                    ],
		                    "borderWidth": 0,
		                    "hoverBackgroundColor": [
		                        "rgba(0, 0, 0, 0)",
		                        "rgba(0, 0, 0, 0.6)",
		                        "rgba(0, 0, 0, 0)"
		                    ],
		                    "hoverBorderWidth": 0
		                }
		            	]
		        	},
							"options": {
								plugins: {
	            					datalabels: {
	            						display: false
	            					}
	            	},
    // String - Template string for single tooltips
    tooltipTemplate: "<\%if (label){\%><\%=label \%>: <\%}\%><\%= value + ' \%' \%>",
    // String - Template string for multiple tooltips
    multiTooltipTemplate: "<\%= value + ' \%' \%>",
		            "cutoutPercentage": 35,
		            "rotation": -3.1415926535898,
		            "circumference": 3.1415926535898,
		            "legend": {
		                "display": false
		            },
		            "tooltips": {
		                "enabled": false,
		                callbacks: {
												label: function(tooltipItem, data) {
														if (tooltipItem.index==0){
					                      var value = data.datasets[0].data[tooltipItem.index];
					                      var label = data.labels[tooltipItem.index];
					                      var percentage = Math.round(count / total * 100);
					                      return label + ' ' + percentage + '%';
														}
                  			}
		                }
		            },
		            "title": {
		                "display": true,
		                "text": title,
		                "position": "bottom",
		                "fontSize": 22,
		                "fontFamily": "Overpass",
		                "fontStyle": "normal",
		                "padding": 0,
		            }
		        	},
		        	
		        	
						};
					}
					
Chart.pluginService.register({
  beforeDraw: function(chart) {
    var width = chart.chart.width,
        height = chart.chart.height,
        ctx = chart.chart.ctx,
        type = chart.config.type;

    if (type == 'doughnut'){
      var percent = Math.round((chart.config.data.datasets[0].data[0] * 100) /
                    (chart.config.data.datasets[0].data[0] +
                    chart.config.data.datasets[0].data[2]));
      var oldFill = ctx.fillStyle;
      //var fontSize = ((height - chart.chartArea.top) / 100).toFixed(2);
      var fontSize = 1.1;

      ctx.restore();
      ctx.font = fontSize + "em sans-serif";
      ctx.textBaseline = "middle"
			
			var apps=chart.config.data.labels[0];
			
      var text = apps,
          textX = Math.round((width - ctx.measureText(text).width) / 2),
          //textY = ((height + 100 + chart.chartArea.top) / 2);
          textY = ((height -50+ chart.chartArea.bottom) / 2);

      ctx.fillStyle = chart.config.data.datasets[0].backgroundColor[0];
      
      // label with "1 Apps"
      ctx.fillText(apps, textX, textY);
      
      // label with "60%"
      //ctx.fillText("("+percent+"%)", textX, textY);
      
      ctx.fillStyle = oldFill;
      ctx.save();
    }
  }
});
</script>