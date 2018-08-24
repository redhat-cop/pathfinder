function setProgress(progressBar, percentage, text){
  console.log("updating progress bar '"+text+"' to "+percentage+"%");
  document.getElementById(progressBar).innerHTML="";
	var bar = new ProgressBar.Line(document.getElementById(progressBar), {
	  strokeWidth: 100,
	  easing: 'easeInOut',
	  duration: 1400,
	  color: '#FFEA82',
	  trailColor: '#ccc',
	  trailWidth: 300,
	  svgStyle: {width: '100%', height: '100%'},
	  text: {
	    style: {
	      // Text color.
	      // Default: same as stroke color (options.color)
	      color: '#333',
	      position: 'relative',
	      right: '0px',
	      top: '-28px',
	      padding: 0,
	      margin: 0,
	      transform: null
	    },
	    autoStyleContainer: false
	  },
	  from: {color: '#cc0000'},
	  to: {color: '#4CAF50'},
	  step: (state, bar) => {
	    bar.path.setAttribute('stroke', state.color);
	    bar.setText(Math.round(bar.value() * 100) + '% '+text);
	  }
	});
	bar.animate(percentage/100);  // Number from 0.0 to 1.0
}