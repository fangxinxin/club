<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<title>登录</title>
<script src="http://apps.bdimg.com/libs/jquery/1.10.2/jquery.min.js"></script>

<!-- head -->
<header class="mt-xs" style="border-width: 0 0 2px 0;">
    <div class="m-toolbar">
        <div id="HeadTitle" class="m-title"><strong style="font-size: 22px;text-align: center;">请选择游戏</strong></div>
        <div class="m-left ml-xs">
            <a href="javascript:window.history.go(-1);" class="btn btn-default m-back" plain="true" outline="true">
                返回 </a>
        </div>
    </div>
</header>

<!-- main -->
<div role="main" class="main">
    <div class="form-group">
        <div class="row">
            <hr class="solid short hr-sm">
        </div>
    </div>

    <div class="col-md-12">
        <c:forEach var="p" items="${promoter}">
            <c:forEach items="${gameList}" var="g">
                <c:if test="${p.getColumnValue('gameId')==g.getColumnValue('gameId')}">
                    <form action="select" method="post">
                        <div class="form-group">
                            <div class="col-md-10 text-center col-md-offset-1">
                                <input type="submit" value="${g.getColumnValue("preName")}${g.getColumnValue("remark")}"
                                       class="btn btn-primary btn-lg btn-block btn-3d">
                                <input type="hidden" name="id" value="${p.getColumnValue('id')}">
                            </div>
                        </div>
                    </form>
                </c:if>
            </c:forEach>

        </c:forEach>
    </div>

</div>

<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp" %>

<%--<script src="../common/js/login.js"></script>--%>