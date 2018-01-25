<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>
<style>
    .table td, .table th {
        text-align: center;
    }
</style>

<!-- 代理商充值查询 -->
<c:if test="${not empty promoterPay}">
    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered">
            <thead>
            <tr>
                <th>充值人数</th>
                <th>充值次数</th>
                <th>充值金额</th>
                <th>时间</th>
                <th>单笔明细</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${promoterPay}" var="row">
                <tr>
                    <td>${row.getColumnValue("nums")}</td>
                    <td>${row.getColumnValue("times")}</td>
                    <td>${row.getColumnValue("price")}</td>
                    <td>${row.getColumnValue("createDate")}</td>
                    <td><input type="button" id="digest" class="btn green btn-sm" value="查询"
                               onclick="promoterPayByDate(this);"></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

<!-- 游戏内购充值查询 -->
<c:if test="${not empty gamePay}">
    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered">
            <thead>
            <tr>
                <th>充值人数</th>
                <th>充值次数</th>
                <th>安卓内购</th>
                <th>安卓微信内购</th>
                <th>安卓支付宝内购</th>
                <th>苹果内购</th>
                <th>游戏内购总额</th>
                <th>时间</th>
                <th>单笔明细</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${gamePay}" var="row">
                <tr>
                    <td>${row.getColumnValue("fromPromoterId")}</td>
                    <td><input type="button" id="" class="btn green btn-sm" value="查询"
                               onclick="promoterPayByDate(this);"></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

<!-- 公众号充值查询 -->
<c:if test="${not empty hkmoviePay}">
    <div class="table-responsive">
        <div class="tabbable tabbable-tabdrop">
            <ul class="nav nav-pills ">
                <li class="ml-xlg">
                    <a id="download3" class="btn green btn-outline btn-xs"
                       href="javascript:hkmovieDownload();">导出excel</a>
                    <input id="choose3" type="text" value="${chooseTab}" hidden="hidden"/>
                    <input id="type3" type="text" value="${payType}" hidden="hidden"/>
                    <input id="startTime3" type="text" value="${startDate}" hidden="hidden"/>
                    <input id="endTime3" type="text" value="${endDate}" hidden="hidden"/>
                </li>
            </ul>
        </div>
        <table class="table table-hover table-striped table-bordered">
            <thead>
            <tr>
                <th>充值人数</th>
                <th>充值次数</th>
                <th>充值金额</th>
                <th>时间</th>
                <th>单笔明细</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${hkmoviePay}" var="row">
                <tr>
                    <td>${row.getColumnValue("nums")}</td>
                    <td>${row.getColumnValue("times")}</td>
                    <td>${row.getColumnValue("price")}</td>
                    <td>${row.getColumnValue("createDate")}</td>
                    <td>
                        <input type="button" class="btn green btn-sm" value="查询" onclick="hkmoviePayByDate(this);">
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>


<!-- 提示信息 -->
<c:if test="${empty promoterPay and empty hkmoviePay and empty gamePay}">
    <div class="row">
        <div class="form-group col-md-12 col-xs-12 mt-lg">
            <div class="heading heading-border heading-middle-border heading-middle-border-center">
                <h4>暂无数据</h4>
            </div>
        </div>
    </div>
</c:if>

<script>

    function promoterDownload() {
        var tab = $('#choose').val();
        var type = $('#payType').val();
        var startDate = $('#startTime').val();
        var endDate = $('#endTime').val();
        if (tab == 1) {
            window.location.href = "check_bill/promoterPayDownload?payType=" + type + "&startDate=" + startDate + "&endDate=" + endDate;
        } else {
            alert(tab);
        }
    }

    function hkmovieDownload() {
        var startDate = $('#startTime3').val();
        var endDate = $('#endTime3').val();
        window.location.href = "check_bill/hkmoviePayDownload?startDate=" + startDate + "&endDate=" + endDate;
    }


    var title = $("#modal_title");
    var content = $("#modal_check_tb_content");
    var footer = $("#modal_footer");

    function digest(bonusId, payCreateDate) {
        content.empty();
        footer.empty();

        title.text('提成明细');
        content.load("check_bill/ajax/digestTable", {bonusId: bonusId, payCreateDate: payCreateDate});
        footer.append("<button type='button' data-dismiss='modal' class='btn blue'> 确&nbsp;&nbsp;认 </button>");

        $("#modal_check").modal('show');
    }

    //查询单日代理商充值明细
    function promoterPayByDate(obj) {
        layer.open({
            type: 2,
            closeBtn: 1,
            anim: -1,
            title: ['充值明细', 'text-align:center;'],
            area: ['950px', '620px'],
            content: "check_bill/promoterPayByDate?date=" + $(obj).parent().prev().text() + "&payType=" + $('#payType').val()
        })
        ;
    }

    //查询单日公众号充值明细
    function hkmoviePayByDate(obj) {
        layer.open({
            type: 2,
            closeBtn: 1,
            anim: -1,
            title: ['充值明细', 'text-align:center;'],
            area: ['950px', '620px'],
            content: "check_bill/hkmoviePayByDate?date=" + $(obj).parent().prev().text()
        })
        ;
    }

</script>
