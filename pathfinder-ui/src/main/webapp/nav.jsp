<!-- Header -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<header id="header">
	<a id="customers" href="manageCustomers.jsp"> <img class="logo"
		id="logo" src="assets/images/RH_Pathfinder_Icon_Color-56.png" /><span
		class="logo-text vbold">Path</span><span class="logo-text normal">finder</span>
	</a>
	<%
		if (request.getSession().getAttribute("x-access-token") == null) {
	%>
	<%
		if (!"index.jsp".equals(this.getClass().getSimpleName().replaceAll("_", "."))) {
	%>
	<a id="logged-status" href="index.jsp">Login</a>
	<%
		}
	%>
	<%
		} else {
	%>
	<p id="logged-status">
		Logged in as
		<%=request.getSession().getAttribute("x-displayName")%>
		<a href="pathfinder/logout"> (Logout)</a>
	</p>
	<%
		}
	%>

	<c:if test="${not empty demoMode}">
		<div
			style="background-color: yellow; color: #a30000; border: 1px solid black; padding: 3px; text-align: center;">
			Demo Mode - Do not add sensitive data, database regularly purged</div>
	</c:if>
</header>

<!-- Nav -->
<nav id="menu">
	<ul class="links">
		<%
			if (request.getSession().getAttribute("x-access-token") == null) {
		%>
		<li><a href="index.jsp">Home</a></li>
		<%
			} else {
		%>
		<li><a href="manageCustomers.jsp">Home</a></li>

		<%
			}
		%>

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

		var $window = $(window), $banner = $('#banner'), $body = $('body');

		$window.on('load', function() {
			window.setTimeout(function() {
				$body.removeClass('is-preload');
			}, 100);
		});
	});
</script>
