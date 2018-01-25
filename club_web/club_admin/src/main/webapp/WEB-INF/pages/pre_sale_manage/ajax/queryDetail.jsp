<!--个人战绩明细（）-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- R_head -->

<style>
    .table td, .table th {
        text-align: center;
    }

</style>

<!-- 战绩明细 -->
<div class="row" style="background-color: white;">
    <c:if test="${not empty userDetail}">
        <div class="table-responsive" style="background-color: white;">
            <table class="table table-hover table-striped table-bordered">
                <thead>
                <tr>
                    <th>房间号</th>
                    <th>开局数</th>
                    <th>实际对局数</th>
                    <th>游戏开始时间</th>
                    <th>游戏结束时间</th>
                    <th>房主</th>
                    <th>大赢家</th>
                    <th>房费</th>
                    <th>玩家1</th>
                    <th>玩家2</th>
                    <th>玩家3</th>
                    <th>玩家4</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${userDetail}" var="row">
                    <tr>
                        <td>${row.getColumnValue("roomId")}</td>
                        <td>${row.getColumnValue("gameNum")}</td>
                        <td>${row.getColumnValue("roundCount")}</td>
                        <td>${row.getColumnValue("gameStartTime")}</td>
                        <td>${row.getColumnValue("gameStopTime")}</td>
                        <td>${row.getColumnValue("ownerName")}</td>
                        <td>${row.getColumnValue("winnerName")}</td>
                        <td>${row.getColumnValue("gameCard")}</td>
                        <td>
                                ${row.getColumnValue("userName1")}
                            <c:if test="${not empty row.getColumnValue('userName1')}">(${row.getColumnValue("userCashDiff1")})</c:if>
                        </td>
                        <td>
                                ${row.getColumnValue("userName2")}
                            <c:if test="${not empty row.getColumnValue('userName2')}">(${row.getColumnValue("userCashDiff2")})</c:if>
                        </td>
                        <td>
                                ${row.getColumnValue("userName3")}
                            <c:if test="${not empty row.getColumnValue('userName3')}">(${row.getColumnValue("userCashDiff3")})</c:if>
                        </td>
                        <td>
                                ${row.getColumnValue("userName4")}
                            <c:if test="${not empty row.getColumnValue('userName4')}">(${row.getColumnValue("userCashDiff4")})</c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </div>
    </c:if>
    <c:if test="${empty userDetail}">
        <div class="row">
            <div class="col-md-12" style="height: 300px">
                <h4 class="center center-vertical">暂无数据</h4>
            </div>
        </div>
    </c:if>
    <div style="width: 100%;text-align: center;line-height: 35px;"><br/></div>
</div>
