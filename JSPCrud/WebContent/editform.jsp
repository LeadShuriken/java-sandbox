<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Form</title>
</head>
<body>
	<%@page import="crudHandling.SimpleCrud,crudHandling.UserPOJO"%>

	<%
		String id = request.getParameter("id");
		UserPOJO u = SimpleCrud.selectUserById(Integer.parseInt(id));
	%>

	<h1>Edit Form</h1>
	<form action="edituser.jsp" method="post" class="form-group">
		<input type="hidden" name="id" value="<%=u.getId()%>" />
		<table>
			<tr>
				<div class="form-group">
					<td><label class="sr-only" for="name">Name:</label></td>
					<td><input class="form-control" type="text" name="name"
						value="<%=u.getName()%>" /></td>
				</div>
			</tr>
			<tr>
				<div class="form-group">
					<td><label class="sr-only" for="password">Password:</label></td> <td>
				<input
						class="form-control" type="text" name="password"
						value="<%=u.getPassword()%>" />
					</td>
					</div>
			</tr>
			<tr>
				<div class="form-group">
					<td><label class="sr-only" for="email">Email:</label></td>
					<td><input class="form-control" type="text" name="email"
						value="<%=u.getEmail()%>" /></td>
				</div>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" class="btn btn-info"
					value="Edit User" /></td>
			</tr>
		</table>
	</form>
</body>
</html>