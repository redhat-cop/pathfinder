<!DOCTYPE HTML>
<!--
	Industrious by TEMPLATED
	templated.co @templatedco
	Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
-->
<html>
	<%@include file="head.jsp"%>
	
	<!--
	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	-->
	<script>
		$(document).ready(function(){
			$("#loginForm").submit(function () {
				$("#submit").attr("disabled", true);
				//e.preventDefault();
				return true;
			});
		  $("#username").focus();
		});
	</script>
	
	
	  
	<body class="is-preload">
	
		<header id="header">
			<a class="logo" href="index.jsp">Pathfinder</a>
		</header>
		
		<%@include file="nav.jsp"%>

		<section id="banner">
			<div class="inner">
				<h1>Pathfinder</h1>
				<p>Pathfinder is an application assessment which can quickly assist a customer with creating a strategy for containerisation of their applications.</p>
			</div>
			<video autoplay loop muted playsinline src="images/path-photo-clip-blue.png"></video>
		</section>
		
		<style>
		.inner2 {
		  width: 32%;
		  margin: 0 auto;
		}
		.modal-body{
			margin: 0px 25px 0px 25px;
		}
		.wrapper{
			padding-bottom: 5em;
		}
		</style>
		
		<section class="wrapper">
			<div class="inner2">
					
					<%if (request.getSession().getAttribute("x-access-token")==null){%>
						
						<!-- display error message -->
						<%if (null!=request.getParameter("error")){%>
							
						<%}%>
							<script>
								//var error=Utils.getParameterByName("error");
								//if (null!=error){
								//	console.log("making error visible");
								//	$('#errorMessage1').css("visibility", "visible");
								//}
							</script>
							<div id="errorMessage1" class="row" style="visibility:<%=null!=request.getParameter("error")?"visible":"hidden"%>">
								<div class="w-100">
									<div class="alert alert-danger">
										<%=request.getParameter("error")%>
									</div>
								</div>
							</div>
						
						<div class="row">
							<div class="w-100">
								<div class="modal-content" style="margin: auto;">
									<div class="modal-body">
										<form id="loginForm" action="api/pathfinder/login" method="post">
											<div class="form-group">
												<label for="username" class="control-label">Username:</label>
												<input id="username" name="username" type="text" class="form-control">
											</div>
											<div class="form-group">
												<label for="password" class="control-label">Password:</label>
												<input id="password" name="password" type="password" class="form-control">
											</div>
											<input id="submit" class="btn btn-primary" type="submit" value="Submit">
										</form>
									</div>
								</div>
							</div>
						</div>
						
					<%}else{%>
						
						<div class="content" style="margin: auto;">
							<header  style="margin: auto;">
								<a href="manageCustomers.jsp" class="icon fa-line-chart"><span class="label">Icon</span>
								<h3>Admin & Assessments</h3></a>
							</header>
						</div>
								
					<%}%>
			</div>
		</section>


	</body>
</html>
