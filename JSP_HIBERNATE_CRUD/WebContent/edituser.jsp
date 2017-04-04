<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.simplecrud.handling.SimpleCrud"%>
<jsp:useBean id="u" class="com.simplecrud.handling.UserPOJO"></jsp:useBean>
<jsp:setProperty property="*" name="u" />

<%
	int i = SimpleCrud.update(u);
	response.sendRedirect("./jspTables/viewusers.jsp");
%>