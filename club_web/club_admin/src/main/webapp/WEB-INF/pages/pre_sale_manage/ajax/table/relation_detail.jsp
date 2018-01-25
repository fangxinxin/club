<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table border="1" width="100%" style="line-height: 25px;" align="center" class="tb1">
    <tr style="background-color: #959595">
        <th style="text-align: center">下级代理昵称</th>
        <th style="text-align: center">下级代理ID</th>
        <th style="text-align: center">下级代理俱乐部ID</th>
        <th style="text-align: center">时间</th>
    </tr>
    <tbody >
    <c:if test="${not empty relationDetail}">
        <c:forEach items="${relationDetail}" var="RD">
            <tr>
                <td align="center">${RD.getColumnValue("nickName")}</td>
                <td align="center">${RD.getColumnValue("promoterId")}</td>
                <td align="center">${RD.getColumnValue("cId")}</td>
                <td align="center">${RD.getColumnValue("createDate")}</td>
            </tr>
        </c:forEach>
    </c:if>
    </tbody>
</table>