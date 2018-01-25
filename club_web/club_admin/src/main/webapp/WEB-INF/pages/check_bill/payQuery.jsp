<!--充值查询-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>

<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">充值查询</h4>
                <div class="caption center-pill">
                </div>
            </div>
            <div class="portlet-body">
                <div class="tabbable tabbable-tabdrop">
                    <div class="form-group ">
                        <div class="col-md-5">
                            <ul class="nav nav-pills ">
                                <li class="active">
                                    <a id="t_promoterPay" href="#promoterPayTable" data-toggle="tab">代理商充值</a>
                                </li>
                                <li>
                                    <a id="t_gamePay" href="#gamePayTable" data-toggle="tab">游戏内购</a>
                                </li>
                                <li>
                                    <a id="t_hkmoviePay" href="#hkmoviePayTable" data-toggle="tab">公众号售钻</a>
                                </li>
                                <li>
                                    <div id="select" class="col-md-12 ">
                                        <select id="payType" class="form-control blue ml-xlg" style="height: 42px">
                                            <option style="height: 42px" value="0">全部渠道</option>
                                            <option style="height: 42px" value="1">提成金购买</option>
                                            <option style="height: 42px" value="2">微信支付</option>
                                            <option style="height: 42px" value="3">支付宝支付</option>
                                        </select>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <div class="col-md-2">
                            <div class="form-group" id="excelTool">
                                <ul class="nav nav-pills ">
                                    <li class="ml-xlg" id="promoterExcel">
                                        <a class="btn green btn-outline btn-xs"
                                           href="javascript:window.open('check_bill/promoterPayDownload?payType=' + $('#payType').val() + '&startDate=' + $('#startDate').val() + '&endDate=' + $('#endDate').val());">导出excel</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="col-md-5">
                            <div class="form-group">
                                <div class="col-md-5">
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

                                <div class="col-md-5">
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
                                <input id="chooseTab" type="text" value="1" hidden="hidden"/>
                            </div>

                        </div>
                    </div>
                </div>
                <div class="tab-content">
                    <!-- 每日简表 :: 查询结果 -->
                    <div class="tab-pane active" id="promoterPayTable"></div>
                    <div class="tab-pane active" id="gamePayTable"></div>
                    <div class="tab-pane active" id="hkmoviePayTable"></div>
                </div>

            </div>

        </div>
        <!-- END 页面内容-->

    </div>
</div>


<script>

    //初始加载 :: 代理商充值
    window.onload = loadPromoterPayTable();

    //代理商充值
    $("#t_promoterPay").click(function () {
        $("#chooseTab").val("1");
        $('#select').css('display', 'block');
        $('#excelTool').css('display', 'block');
        $("#promoterPayTable").load("check_bill/promoterPay?startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val());
    });

    //游戏内购
    $("#t_gamePay").click(function () {
        $('#select').css('display', 'none');
        $("#chooseTab").val("3");
        $('#excelTool').css('display', 'none');
        $("#gamePayTable").load("check_bill/gamePay");
    });

    //公众号售钻
    $("#t_hkmoviePay").click(function () {
        $('#select').css('display', 'none');
        $("#chooseTab").val("3");
        $('#excelTool').css('display', 'none');
        $("#hkmoviePayTable").load("check_bill/hkmoviePay");
    });

    function loadPromoterPayTable() {
        $("#chooseTab").val("1");
        $('#select').css('display', 'block');
        $('#excelTool').css('display', 'block');
        $("#promoterPayTable").load("check_bill/promoterPay");
    }

    function reload_content(url) {
        setTimeout(function () {
            $("#content").load(url, function (responseTxt, statusTxt, xhr) {
                if (statusTxt == "error") {
                    location.reload();
                }
            });
        }, 1000)
    }

    $('#start').click(function () {
        setEnd();
    });
    $('#end').click(function () {
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
        var chooseTab = $("#chooseTab").val();
        var start = $('#startDate').val();
        var end = $('#endDate').val();
        if (start > end) {
            layer.alert('开始日期不能大于结束日期，请重新选择！');
        } else {
            $("#promoterPayTable").load("check_bill/promoterPay?startDate=" + start + "&endDate=" + end + "&payType=" + $('#payType').val());
        }
    }

</script>
<!-- 时间插件 :: 初始化 -->
<script src="http://download.stevengame.com/webResource/common/js/plugins_init/components-datetimepicker-0.0.5.js"></script>
<!-- 时间插件 :: 选择中文语言 -->
<script charset="utf-8"
        src="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN-1.0.js"></script>
