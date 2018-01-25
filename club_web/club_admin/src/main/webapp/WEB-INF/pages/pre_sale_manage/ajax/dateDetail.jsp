<!--工单明细（工单查询的子功能）-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<style>
    .table td, .table th {
        text-align: center;
    }

    body {
        background-color: white;
    }
</style>

<!-- 工单明细 -->
<div class="row" style="height:100%;width:100%;background-color: white;padding:0px;margin:0px;">
    <c:if test="${not empty dt}">
        <div>
            <table width="100%">
                <tr>
                    <td style="text-align: left;">
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        <input type="button" class="btn green btn-outline" value="导出excel"
                               onclick="downLoad()">
                        <input type="hidden" id="date" value="${date}">
                    </td>
                    <td style="text-align: right;">${date}&nbsp;&nbsp;&nbsp;&nbsp;</td>
                </tr>
            </table>
            <br/>
        </div>
        <div class="table-responsive" style="height:100%;background-color: white;padding:0px;margin:0px;">
            <table class="table table-hover table-striped table-bordered">
                <thead>
                <tr>
                    <th>员工名称</th>
                    <th>玩家ID</th>
                    <th>玩家昵称</th>
                    <th>俱乐部ID</th>
                    <th>俱乐部人数</th>
                    <th>开通时间</th>
                    <th>转正时间</th>
                    <th>俱乐部状态</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${dt}" var="row">
                    <tr>
                        <td>${row.getColumnValue("editAdmin")}</td>
                        <td>${row.getColumnValue("gameUserId")}</td>
                        <td>${row.getColumnValue("gameNickName")}</td>
                        <td>${row.getColumnValue("clubId")}</td>
                        <td>${row.getColumnValue("peopleNum")}</td>
                        <td>${row.getColumnValue("createTime")}</td>
                        <td>${row.getColumnValue("changeTime")}</td>
                        <td>
                            <c:if test="${row.getColumnValue('promoterStatus')==0}">待转正</c:if>
                            <c:if test="${row.getColumnValue('promoterStatus')==1}">已转正</c:if>
                            <c:if test="${row.getColumnValue('promoterStatus')==2}">已取消</c:if>
                            <c:if test="${row.getColumnValue('promoterStatus')==3}">已取消</c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </div>
    </c:if>
    <c:if test="${not empty editAdminDt}">
        <div style="background-color: white;">
            <table width="100%">
                <tr>
                    <td style="text-align: left;">
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        <input type="button" class="btn green btn-outline" value="导出员工excel"
                               onclick="editAdminDetailDownload(this)">
                        <input type="hidden" value="${editAdmin}">
                    </td>
                </tr>
            </table>
            <br/>
        </div>
        <div class="table-responsive" style="height:100%;background-color: white;padding:0px;margin:0px;">
            <table class="table table-hover table-striped table-bordered tb1">
                <thead>
                <tr>
                    <th>员工名称</th>
                    <th>玩家ID</th>
                    <th>玩家昵称</th>
                    <th>俱乐部ID</th>
                    <th>俱乐部人数</th>
                    <th>开通时间</th>
                    <th>转正时间</th>
                    <th>俱乐部状态</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${editAdminDt}" var="row">
                    <tr>
                        <td>${row.getColumnValue("editAdmin")}</td>
                        <td>${row.getColumnValue("gameUserId")}</td>
                        <td>${row.getColumnValue("gameNickName")}</td>
                        <td>${row.getColumnValue("clubId")}</td>
                        <td>${row.getColumnValue("peopleNum")}</td>
                        <td>${row.getColumnValue("createTime")}</td>
                        <td>${row.getColumnValue("changeTime")}</td>
                        <td>
                            <c:if test="${row.getColumnValue('promoterStatus')==0}">待转正</c:if>
                            <c:if test="${row.getColumnValue('promoterStatus')==1}">已转正</c:if>
                            <c:if test="${row.getColumnValue('promoterStatus')==2}">已取消</c:if>
                            <c:if test="${row.getColumnValue('promoterStatus')==3}">已取消</c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </div>
    </c:if>
    <c:if test="${empty dt and empty editAdminDt}">
        <div>
            <h5 align="center">暂无数据</h5>
        </div>
    </c:if>
    <div style="width: 100%;background-color: white;text-align: center;line-height: 35px;">
        <br/>
        <input type="button" value="确&nbsp;&nbsp;定" name="create"
               style="width: 150px;"
               class="btn btn-lg btn-success" onclick="parent.layer.closeAll();">
    </div>
    <div style="width: 100%;text-align: center;line-height: 35px;"><br/></div>
</div>

<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp" %>

<script>

    function downLoad() {
        var date = $('#date').val();
        window.open('pre_sale_manage/detailDownload?date=' + date);
    }

    function editAdminDetailDownload(obj) {
        var editAdmin = $(obj).next().val();
        window.open('pre_sale_manage/editAdminDetailDownload?editAdmin=' + editAdmin);
    }

</script>
