<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/lib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>

	<!-- Basic -->
	<meta charset="utf-8">
	<base href="<%=basePath%>">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<!-- Favicon -->
	<link rel="shortcut icon" href="http://download.stevengame.com/webResource/common/img/favicon.ico" />
	<!-- Mobile Metas -->
	<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />

	<!-- Vendor CSS -->
	<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.css">
	<link rel="stylesheet" href="//cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.css"/>
	<link rel="stylesheet" href="//cdn.bootcss.com/simple-line-icons/2.4.1/css/simple-line-icons.css">

	<!-- Theme CSS -->
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/theme.css">
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/theme-elements.css">

	<!-- Skin CSS -->
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/css/skins/skin-corporate-3.css">
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/metronic/layout.min.css">
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/metronic/themes/darkblue.min.css">
	<!-- Theme Custom CSS -->
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/css/custom.css">
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/skins/default.css">

	<!-- Theme Global CSS -->
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/components.min.css">
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/components.min.0.0.1.css">
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/plugins.min.css">

	<!-- 时间插件 -->
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
	<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-fileinput/bootstrap-fileinput.css"/>

	<!-- Jquery -->
	<script src="http://download.stevengame.com/webResource/common/vendor/jquery/jquery.min.js"></script>

	<style>
		/** 垂直居中 **/
		.center-vertical {
			position:relative;
			top:50%;
			transform:translateY(-50%);
		}
		h5 { text-transform: none; }
		.font-lg2 { font-size: 16px; }
	</style>

</head>
<body>