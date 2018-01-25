<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	response.setHeader("cache-control", "max-age=5,public,must-revalidate"); //one day
	response.setDateHeader("expires", -1);
	String cdntime = String.valueOf(System.currentTimeMillis());
	request.setAttribute("cdntime",cdntime);
%>
<%--<!DOCTYPE html>--%>
<html>
<head>

	<title>俱乐部</title>

	<!-- Basic -->
	<meta charset="utf-8">
	<base href="<%=basePath%>">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<!-- Mobile Metas -->
	<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<meta name="x5-orientation" content="portrait">

	<!-- Favicon -->
	<link rel="shortcut icon" href="http://download.stevengame.com/webResource/common/img/favicon.ico" />

	<!-- Vendor CSS -->
	<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.css"/>

	<!-- Theme CSS -->
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/theme.css">
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/theme-elements.css">

	<!-- Skin CSS -->
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/skins/default.css">
	<!-- My Style -->
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/css/styles.0.01.css">

	<!-- Jquery -->
	<script src="http://download.stevengame.com/webResource/common/vendor/jquery/jquery.min.js"></script>

	<!-- Easy-Ui -->
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/jquery-easyui/themes/metro/easyui.css">
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/jquery-easyui/themes/mobile.css">
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/jquery-easyui/themes/icon.css">

	<style>
		.bd {border-width: 0 0 2px 0;}
		.rotating {
			transform:rotate(18deg);
			-ms-transform:rotate(18deg); /* Internet Explorer */
			-moz-transform:rotate(18deg); /* Firefox */
			-webkit-transform:rotate(18deg); /* Safari 和 Chrome */
			-o-transform:rotate(18deg); /* Opera */
		}
		.center-vertical {
			position:relative;
			top:50%;
			transform:translateY(-50%);
		}
		.easyui-dialog {
			display:none
		}
		.table td, .table th {
			text-align: center;
			font-size: 13px;
		}
	</style>

</head>
<body>