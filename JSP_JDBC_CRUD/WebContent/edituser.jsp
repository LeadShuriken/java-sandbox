<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="crudHandling.SimpleCrud"%>
<jsp:useBean id="u" class="crudHandling.UserBEAN"></jsp:useBean>
<jsp:setProperty property="*" name="u" />

<%
	int i = SimpleCrud.update(u);
	response.sendRedirect("./jspTables/viewusers.jsp");
%>