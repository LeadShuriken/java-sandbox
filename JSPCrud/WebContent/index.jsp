<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JSPD_CRUD_SAMPLE</title>
<style type="text/css">
	<%@include file="styles/main.css" %>
    <%@include file="bootstrap/css/bootstrap.min.css" %>
    <%@include file="bootstrap/css/bootstrap-theme.min.css" %>
</style>
</head>
<body>
	<input type="button" class="btn btn-info" value="Add User" onclick="location.href = 'jspForms/adduserform.jsp';">
	<input type="button" class="btn btn-info" value="View Users" onclick="location.href = 'jspTables/viewusers.jsp';">
</body>
</html>