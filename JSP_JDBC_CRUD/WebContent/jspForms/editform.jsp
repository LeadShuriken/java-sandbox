<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Form</title>
<style type="text/css">
	<%@include file="../styles/main.css" %>
	<%@include file="../bootstrap/css/bootstrap.min.css" %>
	<%@include file="../bootstrap/css/bootstrap-theme.min.css" %>
</style>
</head>
<body>
	<%@page import="crudHandling.SimpleCrud,crudHandling.UserBEAN"%>

	<%
		String id = request.getParameter("id");
		UserBEAN u = SimpleCrud.selectUserById(Integer.parseInt(id));
	%>

	<h1>Edit Form</h1>
	<hr>
	<form action="../edituser.jsp" method="post" class="form-horizontal">
		<fieldset>
			<legend>Legend</legend>
			<input type="hidden" name="id" value="<%=u.getId()%>" />
			<div class="form-group">
				<label for="name" class="col-lg-2 control-label">Name</label>
				<div class="col-lg-10">
					<input type="text" class="form-control" name="name" id="name"
						placeholder="Input the user name" value="<%=u.getName()%>">
				</div>
			</div>
			<div class="form-group">
				<label for="password" class="col-lg-2 control-label">Password</label>
				<div class="col-lg-10">
					<input type="password" class="form-control" name="password"
						id="password" placeholder="Input the user password"
						value="<%=u.getPassword()%>">
				</div>
			</div>
			<div class="form-group">
				<label for="inputEmail" class="col-lg-2 control-label">Email</label>
				<div class="col-lg-10">
					<input type="text" class="form-control" name="email" id="email"
						placeholder="Input the user e-mail" value="<%=u.getEmail()%>">
				</div>
			</div>
			<div class="form-group">
				<div class="col-lg-10 col-lg-offset-2">
					<button type="reset" class="btn btn-default"
						onclick="location.href = '../jspTables/viewusers.jsp';">Cancel</button>
					<button type="submit" class="btn btn-primary" value="Edit User">Submit</button>
				</div>
			</div>
		</fieldset>
	</form>

</body>
</html>