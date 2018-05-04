<%@ page language="java" contentType="text/html;charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page language="java"
	import="pers.acp.communications.server.http.servlet.handle.HttpServletResponseAcp"%>
<%
	HttpServletResponseAcp resp = (HttpServletResponseAcp) response;
	resp.doReturnError("请求非法");
%>