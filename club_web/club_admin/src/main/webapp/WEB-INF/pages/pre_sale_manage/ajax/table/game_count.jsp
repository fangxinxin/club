<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table border="1" width="100%" style="line-height: 25px;" align="center" class="tb1">
    <tr style="background-color: #959595">
        <th style="text-align: center">成员累计对局数</th>
        <th style="text-align: center">成员新增对局数</th>
        <th style="text-align: center">时间</th>
    </tr>
    <tbody id="tb1">
    <c:if test="${not empty dtRoomStatis}">
        <c:forEach items="${dtRoomStatis}" var="room">
            <tr>
                <td align="center">${room.getColumnValue("totalNums")}</td>
                <td align="center">${room.getColumnValue("roomCreateNum")}</td>
                <td align="center">${room.getColumnValue("statDate")}</td>
            </tr>
        </c:forEach>
    </c:if>
    </tbody>
</table>