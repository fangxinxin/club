<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<ul class="list list-unstyled mt-lg">
    <li class="mt-lg"><br/></li>
    <li class="mt-lg center">
        <h4 class="ml-lg">
            <c:if test="${gameId != 4443}">
                申请成功！请等待群主同意&nbsp;&nbsp;&nbsp;
            </c:if>
            <c:if test="${gameId == 4443}">
                成功加入俱乐部&nbsp;&nbsp;&nbsp;
            </c:if>
        </h4>
    </li>

    <li class="mt-lg"><br/></li>

    <li class="mt-lg center">
        <button class="btn btn-success btn-lg" onclick="location.href='${link}'">下载游戏</button>
    </li>

</ul>
<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp"%>