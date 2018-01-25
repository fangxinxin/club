<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table border="1" width="100%" style="line-height: 25px;" align="center" class="tb1">
    <tr style="background-color: #959595">
        <th style="text-align: center">成员累计消耗钻石</th>
        <th style="text-align: center">成员新消耗钻石</th>
        <th style="text-align: center">时间</th>
    </tr>
    <tbody id="tb2">
    <c:if test="${not empty diamondStatis}">
        <c:forEach items="${diamondStatis}" var="diamond">
            <tr>
                <td align="center">${diamond.getColumnValue("totalNums")}</td>
                <td align="center">${diamond.getColumnValue("gameCardConsume")}</td>
                <td align="center">${diamond.getColumnValue("statDate")}</td>
            </tr>
        </c:forEach>
    </c:if>
    </tbody>
</table>