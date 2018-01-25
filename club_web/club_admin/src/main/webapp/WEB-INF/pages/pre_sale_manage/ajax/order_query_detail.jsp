<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>
<style type="text/css">
    .tb1 tr {
        background: #e6e6e6;
    }

    .tb1 tr:nth-child(even) {
        background: white;
    }

    td, th {
        text-align: center;
        border: 0px;
    }

    .tb1 td, th {
        border-left: 1px solid black;
    }
</style>

<!-- 按日期 -->
<c:if test="${not empty dateDt}">
    <div class="row">
        <div id="withdrawCheck" class="col-md-12">
            <div class="table-responsive">
                <table class="table table-hover table-striped table-bordered tb1">
                    <thead>
                    <tr style="background-color: white;">
                        <th>累计处理工单数</th>
                        <th>当天新增工单</th>
                        <th>累计转正工单</th>
                        <th>当天新增转正</th>
                        <th>时间</th>
                        <th>当月未转正</th>
                        <th>累计未转正</th>
                        <th>工单明细</th>
                    </tr>
                    </thead>
                    <c:forEach items="${dateDt}" var="d">
                        <tr>
                            <td>${d.getColumnValue("totalNum")}</td>
                            <td>${d.getColumnValue("newNum")}</td>
                            <td>${d.getColumnValue("totalFormalNum")}</td>
                            <td>${d.getColumnValue("formalNum")}</td>
                            <td>${d.getColumnValue("changeDate")}</td>
                            <td>${d.getColumnValue("monthInitNum")}</td>
                            <td>${d.getColumnValue("totalInitNum")}</td>

                            <td><input type="button" value="查&nbsp;&nbsp;询"
                                       class="btn btn-success btn-sm"
                                       style="width: 60px;" onclick="queryByDate(this)"></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
</c:if>

<!-- 按员工 -->
<c:if test="${not empty editAdminDt}">
    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered tb1">
            <thead>
            <tr style="background-color: white;">
                <th>员工姓名</th>
                <th>累计处理工单数</th>
                <th>当月新增工单</th>
                <th>上月新增工单</th>
                <th>累计转正工单</th>
                <th>当月新增转正</th>
                <th>上月新增转正</th>
                <th>当月未转正</th>
                <th>累计未转正</th>
                <th>工单明细</th>
            </tr>
            </thead>
            <c:forEach items="${editAdminDt}" var="d">
                <tr>
                    <td>${d.getColumnValue("editAdmin")}</td>
                    <td>${d.getColumnValue("num")}</td>
                    <td>${d.getColumnValue("monthNew")}</td>
                    <td>${d.getColumnValue("lastMonthNew")}</td>
                    <td>${d.getColumnValue("allFormal")}</td>
                    <td>${d.getColumnValue("monthFormal")}</td>
                    <td>${d.getColumnValue("lastMonthFormal")}</td>
                    <td>${d.getColumnValue("monthInit")}</td>
                    <td>${d.getColumnValue("allInit")}</td>
                    <td>
                        <input type="button" value="查&nbsp;&nbsp;询" class="btn btn-success btn-sm" style="width: 60px;"
                               onclick="queryOneDetail(this)">
                        <input type="hidden" value="${d.getColumnValue('editAdmin')}">
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</c:if>

<!-- 提示信息 -->
<c:if test="${empty dateDt and empty editAdminDt }">
    <div class="row">
        <div class="form-group col-md-12 col-xs-12 mt-lg">
            <div class="heading heading-border heading-middle-border heading-middle-border-center">
                <h4>暂无数据</h4>
            </div>
        </div>
    </div>
</c:if>

