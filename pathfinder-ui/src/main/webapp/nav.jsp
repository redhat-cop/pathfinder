<!-- Header -->
<header id="header">

	<%if (request.getSession().getAttribute("x-access-token")==null){%>
		<a href="index.jsp">
			<img class="logo" src="assets/images/RH_Pathfinder_Icon_Color-56.png"/><span class="logo-text vbold">Path</span><span class="logo-text normal">finder</span>
		</a>
		<%if (!"index.jsp".equals(this.getClass().getSimpleName().replaceAll("_", "."))){%>
			<a id="logged-status" href="index.jsp">Login</a> 
		<%}%>
	<%}else{%>
		<a id="customers" href="manageCustomers.jsp">
			<img class="logo" id="logo" src="assets/images/RH_Pathfinder_Icon_Color-56.png"/><span class="logo-text vbold">Path</span><span class="logo-text normal">finder</span>
		</a>
		<p id="logged-status">Logged in as <%=request.getSession().getAttribute("x-displayName")%> <a href="pathfinder/logout"> (Logout)</a></p>
	<%}%>
</header>

<!-- Nav -->
<nav id="menu">
	<ul class="links">
	<%if (request.getSession().getAttribute("x-access-token")==null){%>
		<li><a href="index.jsp">Home</a></li>
	<%}else{%>
		<li><a href="manageCustomers.jsp">Home</a></li>

	<%}%>
		
		<li><a href="manageCustomers.jsp">Customers</a></li>
		<li><a href="workflow.jsp">Workflow</a></li>
		<li><a href="credits.jsp">Credits</a></li>
	</ul>
</nav>

<!--
<script src="assets/js/breakpoints.min.js"></script>
-->
<script src="assets/js/util.js"></script>

<script>
$(document).ready(function() {

	var	$window = $(window),
		$banner = $('#banner'),
		$body = $('body');

	$window.on('load', function() {
		window.setTimeout(function() {
			$body.removeClass('is-preload');
		}, 100);
	});
});
</script>
