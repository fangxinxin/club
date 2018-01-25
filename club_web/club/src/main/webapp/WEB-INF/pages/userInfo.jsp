<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<style> li {  line-height: 20px;  } </style>

<c:set value="${promoter.gameCard}" var="gameCard"/>
<c:set value="${promoter.pLevel}"   var="pLevel"/>
<c:set value="${promoter.rebate}" var="rebate" />
<c:set value="${club.id}"           var="clubId"/>
<c:set value="${club.shareCard}"    var="shareCard"/>
<c:set value="${club.clubCard}"     var="clubCard"/>
<c:set value="${club.clubName}"     var="clubName"/>

<div class="col-xs-8">
    <ul class="list list-unstyled gray">
        <li class="mb-none mt-xs">俱乐部名:
            <c:if test="${fn:length(clubName) > 9}">${fn:substring(clubName, 0, 9)}...</c:if>
            <c:if test="${fn:length(clubName) <= 9}">${clubName}</c:if>
            <span>(${levelName})</span>
        </li>
        <li class="mb-none">俱乐部ID：${clubId}</li>
        <li class="mb-none">剩余钻石：${gameCard}</li>
        <li class="mb-none">
            开房钻石：${shareCard}&nbsp;
            <c:if test="${clubCard > 0}">
                <label style="color: gray">+ ${clubCard}</label>
            </c:if>
            <a href="javascript:;"
               class="btn btn-xs btn-primary" style="margin-top: -6px;"
               onclick="$('#change_diamond').dialog('open').dialog('center')">
                修改
            </a>
        </li>
        <li class="mb-none">俱乐部人数：${num}</li>
    </ul>
</div>
<div class="col-xs-4">
    <ul class="list list-unstyled gray" style="margin-left: -2px">
        <li class="mb-none mt-sm text-right" style="margin-top: 2px">
            <a href="javascript:;" class="btn btn-sm btn-default" onclick="$('#dlg1').dialog('open').dialog('center')"
               style="color: #383f48;">
                个人信息
            </a>
        </li>
        <c:if test="${pLevel!=2}">
            <li class="mt-lg mb-none text-right">
                <button class="btn btn-sm btn-default" onclick="$('#rebate').dialog('open').dialog('center')"
                        style="color: #383f48;line-height: 1.2">
                        <span>
                            <i class="fa fa-diamond" style="color: #0088cc"></i> ${rebate}&nbsp;
                            <br> 返钻数量
                        </span>
                </button>
            </li>
        </c:if>
    </ul>

</div>