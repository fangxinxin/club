<!--充值明细（充值查询的子功能）-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<style>
    .table td, .table th {
        text-align: center;
    }
    body{
        background-color: white;
    }
</style>

<!-- 充值明细 -->
<div class="row" style="background-color: white;">
    <c:if test="${not empty promoterDetail}">
        <div>
            <table width="100%">
                <tr>
                    <td style="text-align: left;">
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        <input type="button" class="btn green btn-outline" value="导出excel"
                               onclick="downLoad()">
                        <input type="hidden" id="date" value="${date}">
                        <input type="hidden" id="payType" value="${payType}">
                    </td>
                    <td style="text-align: right;">
                            ${date}<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    </td>
                </tr>
            </table>
            <br/>
        </div>
        <div class="table-responsive" style="background-color: white;">
            <table class="table table-hover table-striped table-bordered">
                <thead>
                <tr>
                    <th>代理商ID</th>
                    <th>代理商昵称</th>
                    <th>充值金额</th>
                    <th>充值渠道</th>
                    <th>充值订单</th>
                    <th>时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${promoterDetail}" var="row">
                    <tr>
                        <td>${row.getColumnValue("promoterId")}</td>
                        <td>${row.getColumnValue("nickName")}</td>
                        <td>${row.getColumnValue("price")}</td>
                        <td>
                            <c:if test="${row.getColumnValue('payType')==1}">提成金购买</c:if>
                            <c:if test="${row.getColumnValue('payType')==2}">微信支付</c:if>
                            <c:if test="${row.getColumnValue('payType')==3}">支付宝支付</c:if>
                        </td>
                        <td>${row.getColumnValue("orderId")}</td>
                        <td>${row.getColumnValue("createTime")}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </div>
    </c:if>
    <c:if test="${not empty hkmovieDetail}">
        <div>
            <table width="100%">
                <tr>
                    <td style="text-align: left;">
                        <input type="button" class="btn green btn-outline" value="导出excel"
                               onclick="downLoad3()">
                        <input type="hidden" id="date3" value="${date}">
                    </td>
                    <td style="text-align: right;">${date}</td>
                </tr>
            </table>
            <br/>
        </div>
        <div class="table-responsive">
            <table class="table table-hover table-striped table-bordered tb1">
                <thead>
                <tr>
                    <th>微信ID</th>
                    <th>用户昵称</th>
                    <th>充值金额</th>
                    <th>充值订单</th>
                    <th>时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${hkmovieDetail}" var="row">
                    <tr>
                        <td>${row.getColumnValue("userId")}</td>
                        <td></td>
                        <td>${row.getColumnValue("price")}</td>
                        <td>${row.getColumnValue("payOrderId")}</td>
                        <td>${row.getColumnValue("createTime")}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </div>
    </c:if>
    <c:if test="${empty promoterDetail and empty hkmovieDetail}">
        <div>
            <h5 align="center">暂无数据</h5>
        </div>
    </c:if>
    <div style="width: 100%;text-align: center;line-height: 35px;">
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
        var payType = $('#payType').val();
        window.open('check_bill/promoterByDateDownload?date=' + date + "&payType=" + payType);
    }

</script>
