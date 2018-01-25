<%@include file="/common/include/lib.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table border="1" width="100%" style="line-height: 25px;" align="center" class="tb1">
    <tr style="background-color: #959595">
        <th style="text-align: center">成员ID</th>
        <th style="text-align: center">成员昵称</th>
        <th style="text-align: center">参与对局</th>
        <th style="text-align: center">消耗钻石</th>
        <th style="text-align: center">被发钻石</th>
        <th style="text-align: center">剩余钻石</th>
        <th style="text-align: center">账号创建时间</th>
        <th style="text-align: center">加入俱乐部时间</th>
        <th style="text-align: center">钻石流水</th>
    </tr>

    <c:if test="${not empty memberDetailVOList}">
        <c:forEach items="${memberDetailVOList}" var="vo">
            <tr>
                <td align="center">${vo.userId}</td>
                <td align="center">${vo.userNickName}</td>
                <td align="center">${vo.partiGameNums}</td>
                <td align="center">${vo.consumeDiamNums}</td>
                <td align="center">${vo.giveDiamNums}</td>
                <td align="center">${vo.remainDiaNums}</td>
                <td align="center">
                    <fmt:formatDate value="${vo.createTime}"
                                    pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td align="center">
                    <fmt:formatDate value="${vo.joinClubTime}"
                                    pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td align="center"><input type="button" value="查询" class="btn btn-warning btn-xs"
                                          onclick="check_record(this,${vo.userId})"></td>
            </tr>
        </c:forEach>
    </c:if>

</table>

