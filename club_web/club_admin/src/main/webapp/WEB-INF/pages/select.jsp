<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<title>史蒂夫游戏管理后台</title>

<!-- Header -->
<header id="header" class="header-narrow" data-plugin-options='{"stickyEnabled": true, "stickyEnableOnBoxed": true, "stickyEnableOnMobile": true, "stickyStartAt": 1, "stickySetTop": "1"}'>
    <div class="header-body" style="background-color: #383f48">

        <div class="header-top header-top-style-2">
            <div class="row">
                <div class="col-md-12">
                    <p class="pull-left hidden-xs ml-md">
                        <b class="font-lg"><i class="fa fa-home"></i> &nbsp; 史蒂夫游戏管理后台</b>
                    </p>
                    <p class="pull-right mr-md">
                        <span>账户登录：${remark}【${realName}】</span> |
                        <a class="btn blue btn-xs" href="javascript:;" onclick="getContent('user/password')">修改密码</a> |
                        <a class="btn blue btn-xs" href="logout">注销</a>
                    </p>
                </div>
            </div>
        </div>

    </div>
</header>

<!-- Main -->
<div class="page-container">

    <!-- Content -->
    <div class="page-content" style="min-height:760px">

        <div class="row">
            <div class="col-md-12">

                <!-- BEGIN 页面内容-->
                <div class="portlet light bordered">
                    <div class="portlet-title">
                        <h4 class="center">请选择游戏</h4>
                    </div>
                    <div class="portlet-body">

                        <!-- 选择游戏 -->
                        <div class="featured-boxes">
                            <div class="row">
                                <div class="col-md-6 col-md-offset-3">

                                    <div class="featured-box featured-box-primary align-left mt-xlg">
                                        <div class="box-content">
                                            <div class="row">
                                                <div class="col-md-12">

                                                    <c:if test="${not empty gameList}">
                                                        <c:forEach items="${gameList}" var="row">
                                                            <c:if test="${row.getColumnValue('parentId') != 0}">
                                                                <div class="col-md-3">
                                                                    <button type="button" class="btn btn-primary btn-block" onclick="changeGame('${row.getColumnValue('gameId')}');">
                                                                            ${row.getColumnValue('preName')}${row.getColumnValue('name')}
                                                                    </button>
                                                                    <div class="col-md-12 mt-lg"></div>
                                                                </div>
                                                            </c:if>
                                                        </c:forEach>
                                                    </c:if>

                                                </div>
                                            </div>

                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <!-- END 页面内容-->

            </div>
        </div>

    </div>

</div>


<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp"%>

<script>

    function changeGame(gameId) {
        $.post("changeGame", { gameId: gameId }, function(data, status) {
            if(status == "success") {
                location.reload();
            }
        });
    }

</script>