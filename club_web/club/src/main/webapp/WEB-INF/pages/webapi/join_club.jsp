<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<ul class="list list-unstyled mt-lg">
    <li class="">
        <h4 class="ml-md center">${mpName}</h4>
    </li>
    <li class="mt-lg"><br/></li>
    <li class="mt-lg">
        <h5 class="ml-md center">${mpName}${clubName}</h5>
    </li>
    <li class="mt-lg"><br/></li>
    <li class="mt-lg">
        <p class="mb-xs center" >
            ${clubName}邀请您的加入
        </p>
    </li>
    <li class="mt-lg"><br/></li>
    <li class="mt-lg">
        <%--<p class="mb-xs center" >(首次加入俱乐部赠送15钻石，后台买钻石请找群主。)</p>--%>
        <p class="mb-xs center" >(后台买钻石请找群主。)</p>
    </li>

    <li class="mt-lg"><br/></li>

    <li class="mt-lg center">
        <button class="btn btn-success btn-lg" onclick="agree()">确&nbsp;&nbsp;定</button>
    </li>

</ul>

<script>
    function agree(){
        location.href = "${pageContext.request.contextPath}/webapi/${className}/${clubId}/agree?gameId=${gameId}&unionid=${unionid}";
    }
</script>
<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp"%>