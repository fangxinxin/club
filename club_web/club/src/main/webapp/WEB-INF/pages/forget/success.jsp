<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- R_head -->
<%@ include file="/common/include/R_head.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="http://code.jquery.com/jquery-1.8.0.min.js"></script>
<title>找回密码</title>

<!-- header -->
<header>
    <section class="page-header page-header-center page-header-no-title-border">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1> 修改密码成功 </h1>
                </div>
            </div>
        </div>
    </section>
</header>

<!-- main -->
<div role="main" class="main">

    <div class="col-md-12">

        <div class="form-group">
            <div class="col-md-10 text-center col-md-offset-1">
                <p style="color: red;font-size: 22px;">您的密码已发送到您的手机，请注意查收</p>
                <input type="submit" value="确&nbsp;&nbsp;定" class="btn btn-secondary btn-lg btn-block btn-3d"
                       onclick="window.location='login';">
            </div>
        </div>
    </div>

</div>

<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp" %>

<%--<script src="../../common/js/forgetPassword.js"></script>--%>
