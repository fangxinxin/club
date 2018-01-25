<!--售钻查询-->
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

<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">售钻查询</h4>
                <div class="caption center-pill">
                </div>
            </div>
            <div class="portlet-body">

                <!-- 查询 -->
                <div class="row">
                    <div class="col-md-12">

                        <div class="form-group ">
                            <div class="col-md-3">
                                <input type="button" class="btn green btn-outline" value="导出excel" onclick="downLoad()">&nbsp;&nbsp;
                            </div>

                            <div class="col-md-9">

                                <div class="form-group">
                                    <div class="col-md-3 col-md-offset-4">
                                        <div class="input-group date form_date" id="start">
                                            <input id="startDate" type="text" size="16" class="form-control"
                                                   placeholder="开始时间" name="startDate" value="${startDate}">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" id="start2">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                                        </div>
                                    </div>

                                    <div class="col-md-3">
                                        <div class="input-group date form_date" id="end">
                                            <input id="endDate" type="text" size="16" class="form-control"
                                                   placeholder="结束时间" name="endDate" value="${endDate}">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" id="end2">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                                        </div>
                                    </div>

                                    <input id="search" type="button" value="查&nbsp;&nbsp;询" class="btn btn-primary"
                                           onclick="search()">
                                    <input type="hidden" name="gameName" value="${gameName}" id="gameName">
                                </div>

                            </div>
                        </div>

                    </div>
                </div>

                <!-- 查询结果 -->
                <c:if test="${not empty dt}">
                    <div class="row">
                        <div id="withdrawCheck" class="col-md-12">
                            <table class="table table-hover table-striped table-bordered">
                                <tr>
                                    <th>推广员人数</th>
                                    <th>充值金额</th>
                                    <th>提取返钻数量</th>
                                    <th>新增钻石总额</th>
                                    <th>购买钻石</th>
                                    <th>发放钻石</th>
                                    <th>代理出售钻石</th>
                                    <th>时间</th>
                                </tr>
                                <c:forEach items="${dt}" var="d">
                                    <tr>
                                        <td>${d.getColumnValue("promoterNum")}</td>
                                        <td>${d.getColumnValue("allPrices")}</td>
                                        <td>
                                            <c:if test="${not empty (d.getColumnValue('rebateGet'))}">${d.getColumnValue("rebateGet")}</c:if>
                                            <c:if test="${empty (d.getColumnValue('rebateGet'))}">0</c:if>
                                        </td>
                                        <td>${d.getColumnValue("goodGivingNums")+d.getColumnValue("goodNums")+d.getColumnValue("changeNum")}</td>
                                        <td>${d.getColumnValue("goodGivingNums")+d.getColumnValue("goodNums")}</td>
                                        <td>${d.getColumnValue("changeNum")==""?"0":d.getColumnValue("changeNum")}</td>
                                        <td>${d.getColumnValue("sellNum")==""?"0":d.getColumnValue("sellNum")}</td>
                                        <td>${d.getColumnValue("createDate")}</td>
                                    </tr>
                                </c:forEach>
                            </table>


                        </div>
                    </div>
                </c:if>
                <c:if test="${empty dt}">
                    <div class="row">
                        <div class="form-group col-md-12 col-xs-12 mt-lg">
                            <div class="heading heading-border heading-middle-border heading-middle-border-center">
                                <h4>暂无数据</h4>
                            </div>
                        </div>
                    </div>
                </c:if>

            </div>
        </div>
        <!-- END 页面内容-->

    </div>
</div>


<!--确认打款成功！-->
<div id="div2" style="display:none;font-size: 18px;">
    <table border="0" width="100%" style="line-height: 35px;font-size: 25px;" align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td align="center">确认打款成功</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td align="center"><input type="button" value="确定" name="create" style="width: 160px;"
                                      class="btn btn-success btn-lg"
                                      onclick="layer.closeAll();search();">
            </td>
        </tr>
    </table>
</div>


<script>

    function downLoad() {
        var startDate = $.trim($('#startDate').val());
        var endDate = $.trim($('#endDate').val());
        window.open('check_bill/downloadSellDiamond?startDate=' + startDate + '&endDte=' + endDate);
    }
    function openA(obj) {
        layer.open({
            type: 2,
            closeBtn: 1,
            anim: -1,
            title: ['提现明细', 'text-align:center;'],
            area: ['950px', '620px'],
            content: "check_bill/queryDayDetail?date=" + $(obj).parent().prev().text()
        });
    }

    function openB(obj) {
        var date = $(obj).prev().val();
        $.post('check_bill/confirmPay',
                {date: date},
                function (data) {
                    if (data == 'OK') {
                        layer.open({
                            type: 1,
                            closeBtn: 1,
                            anim: -1,
                            title: ['确认打款成功', 'text-align:center;font-size:20px;'],
                            area: ['800px', '500px'],
                            content: $('#div2')
                        });
                    } else {
                        layer.alert('确认失败，请重试！');
                    }
                }, "text");
    }

    $('#start').click(function () {
        setEnd();
    });
    $('#end').click(function () {
        setEnd();
    });
    $('#start2').click(function () {
        setEnd();
    });
    $('#end2').click(function () {
        setEnd();
    });
    function setEnd() {
        var now = new Date();
        var yyyy = now.getFullYear(), mm = (now.getMonth() + 1).toString(), dd = now
                .getDate().toString();
        if (mm.length == 1) {
            mm = '0' + mm;
        }
        if (dd.length == 1) {
            dd = '0' + dd;
        }
        var yesterday = yyyy + '-' + mm + '-' + dd;
        $('#start').datetimepicker('setEndDate', yesterday);
        $('#end').datetimepicker('setEndDate', yesterday);
    }
    function search() {
        var start = $('#startDate').val();
        var end = $('#endDate').val();
        if (start > end) {
            layer.alert('开始日期不能大于结束日期，请重新选择！');
        } else {
            $("#content").load("check_bill/sellDiamondQuery?startDate=" + $("#startDate").val() + "&endDate=" + $("#endDate").val());
        }
    }

</script>
<!-- 时间插件 :: 初始化 -->
<script src="http://download.stevengame.com/webResource/common/js/plugins_init/components-datetimepicker-0.0.5.js"></script>
<!-- 时间插件 :: 选择中文语言 -->
<script charset="utf-8"
        src="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN-1.0.js"></script>
