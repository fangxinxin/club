<!-- 提成金购买查询-->

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
                <h4 class="center bold">提成金购买查询</h4>
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
                                </div>

                            </div>
                        </div>

                    </div>
                </div>

                <!-- 查询结果 -->
                <c:if test="${not empty dt}">
                    <div class="row">
                        <div id="withdrawCheck" class="col-md-12">
                            <table width="100%" border="1px" class="tb1" style="line-height: 30px;font-size: 16px;">
                                <tr style="background-color: #e6e6e6">
                                    <th>代理商人数</th>
                                    <th>提成金购买人数</th>
                                    <th>提成金购买次数</th>
                                    <th>提成金购买金额</th>
                                    <th>时间</th>
                                    <th>单笔明细</th>
                                </tr>
                                <c:forEach items="${dt}" var="d">
                                    <tr>
                                        <td>${d.getColumnValue("promoterNum")}</td>
                                        <td>${d.getColumnValue("payNum")}</td>
                                        <td>${d.getColumnValue("times")}</td>
                                        <td>${d.getColumnValue("prices")}</td>
                                        <td>${d.getColumnValue("createDate")}</td>

                                        <td><input type="button" value="查&nbsp;&nbsp;询" class="btn btn-success btn-sm"
                                                   style="width: 60px;" onclick="openA(this)"></td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </div>
                </c:if>
                <!-- 提示信息 -->
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


<script>

    function upLoad(obj) {
        var form = $(obj).parent();
        var file = $(obj).prev().children('span').children('input').eq(1);
        if ($.trim(file.val()) == '') {
            layer.alert("请选择文件!");
        } else {
            $.ajax({
                url: 'check_bill/upload',
                type: 'POST',
                cache: false,
                data: new FormData(form[0]),
                processData: false,
                contentType: false
            }).done(function (res) {
                layer.alert(res, function () {
                    layer.closeAll();
                    search();
                });
            }).fail(function (res) {
                alert(res);
            });
        }
    }

    function downLoad() {
        var startDate = $.trim($('#startDate').val());
        var endDate = $.trim($('#endDate').val());
        window.open('check_bill/bonusPay/download?startDate=' + startDate + '&endDte=' + endDate);
    }
    function openA(obj) {
        layer.open({
            type: 2,
            closeBtn: 1,
            anim: -1,
            title: ['提成金购买明细', 'text-align:center;'],
            area: ['950px', '620px'],
            content: "check_bill/bonusPay/queryByDate?date=" + $(obj).parent().prev().text()
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
            $("#content").load("check_bill/bonusPay/queryInfo?startDate=" + $("#startDate").val() + "&endDate=" + $("#endDate").val());
        }
    }

</script>
<!-- 时间插件 :: 初始化 -->
<script src="http://download.stevengame.com/webResource/common/js/plugins_init/components-datetimepicker-0.0.5.js"></script>
<!-- 时间插件 :: 选择中文语言 -->
<script charset="utf-8"
        src="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN-1.0.js"></script>
