<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="crudHandling.SimpleCrud"%>
<jsp:useBean id="u" class="crudHandling.UserPOJO"></jsp:useBean>
<jsp:setProperty property="*" name="u" />

<%
	SimpleCrud.delete(u);
	response.sendRedirect("viewusers.jsp");
%>