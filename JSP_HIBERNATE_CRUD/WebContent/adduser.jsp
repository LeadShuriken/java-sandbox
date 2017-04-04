<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.simplecrud.handling.SimpleCrud"%>
<jsp:useBean id="u" class="com.simplecrud.handling.UserPOJO"></jsp:useBean>
<jsp:setProperty property="*" name="u" />

<%
	int i = SimpleCrud.insert(u);
	if (i > 0) {
		response.sendRedirect("./jspMesseges/adduser-success.jsp");
	} else {
		response.sendRedirect("./jspMesseges/adduser-error.jsp");
	}
%>