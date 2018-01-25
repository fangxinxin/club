<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<p style="margin-top:10px">获得的总返钻数量：${totalRebate}颗</p>
<table border="1" width="100%" style="line-height: 25px;margin-top:-20px" align="center" class="tb1">
    <tr style="background-color: #959595">
        <th style="text-align: center">购钻代理昵称</th>
        <th style="text-align: center">购钻代理ID</th>
        <th style="text-align: center">购买金额</th>
        <th style="text-align: center">提供返钻数量</th>
        <th style="text-align: center">时间</th>
    </tr>
    <tbody id="rebateDetail">
    <c:if test="${not empty DtRebateDetail}">
        <c:forEach items="${DtRebateDetail}" var="DRD">
            <tr>
                <td align="center">${DRD.getColumnValue("nickName")}</td>
                <td align="center">${DRD.getColumnValue("fromPromoterId")}</td>
                <td align="center">${DRD.getColumnValue("diamond")}</td>
                <td align="center">${DRD.getColumnValue("rebateDiamond")}</td>
                <td align="center">${DRD.getColumnValue("createTime")}</td>
            </tr>
        </c:forEach>
    </c:if>
    </tbody>
</table>