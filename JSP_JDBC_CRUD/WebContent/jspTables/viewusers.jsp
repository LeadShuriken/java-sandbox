<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Users</title>
<style type="text/css">
	<%@include file="../styles/main.css" %>
    <%@include file="../bootstrap/css/bootstrap.min.css" %>
    <%@include file="../bootstrap/css/bootstrap-theme.min.css" %>
</style>
</head>
<body>

	<%@page
		import="crudHandling.SimpleCrud,
				crudHandling.UserBEAN,
				java.util.*"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<h1>Users List</h1>
	<hr>
	<%
		List<UserBEAN> list = SimpleCrud.getAllRecords();
		request.setAttribute("list", list);
	%>

	<table border="1" width="90%" class="table">
		<tr id="tableLable">
			<th scope="row">Id</th>
			<th scope="row">Name</th>
			<th scope="row">Password</th>
			<th scope="row">Email</th>
			<th scope="row">Edit</th>
			<th scope="row">Delete</th>
		</tr>
		<c:forEach items="${list}" var="u">
			<tr>
				<td>${u.getId()}</td>
				<td>${u.getName()}</td>
				<td>${u.getPassword()}</td>
				<td>${u.getEmail()}</td>
				<td><a href="../jspForms/editform.jsp?id=${u.getId()}">Edit</a></td>
				<td><a href="../deleteuser.jsp?id=${u.getId()}">Delete</a></td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<hr>
	<input type="button" class="btn btn-info" value="Add User" onclick="location.href = '../jspForms/adduserform.jsp';">

</body>
</html>