<!-- 提成审核-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>

<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">提成审核</h4>
                <div class="caption center-pill">
                </div>
            </div>
            <div class="portlet-body">
                <c:if test="${not empty bonus and !bonus.isPass}">
                    <div class="tabbable tabbable-tabdrop">
                        <ul class="nav nav-pills ">
                            <li class="active">
                                <a id="t_dailyTable" href="#dailyTable" data-toggle="tab">每日简表</a>
                            </li>
                            <li>
                                <a id="t_singleDetail" href="#singleDetail" data-toggle="tab">单笔明细</a>
                            </li>
                            <li class="ml-xlg">
                                <a id="download" class="btn green btn-outline btn-xs" href="javascript:;">导出excel</a>
                                <input id="chooseTab" type="text" value="1" hidden="hidden"/>
                            </li>
                            <li style="margin-top: 9px">
                                <div class="col-md-12 col-md-offset-1">
                                    <h4>
                                        当前审核日期：
                                        <fmt:formatDate value="${bonus.startDate}" type="date" pattern="yyyy-MM-dd"/> ~
                                        <fmt:formatDate value="${bonus.endDate}" type="date" pattern="yyyy-MM-dd"/>
                                        <input id="bonusId" type="text" value="${bonus.id}" hidden="hidden">
                                    </h4>
                                </div>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <!-- 每日简表 :: 查询结果 -->
                            <div class="tab-pane active" id="dailyTable"></div>
                            <div class="tab-pane" id="singleDetail"></div>
                        </div>
                    </div>

                    <!-- 审核确认 -->
                    <div class="row">
                        <div class="col-md-12 center mt-xlg">
                            <a href="javascript:;" class="btn btn-success" onclick="givingBonus('${bonus.id}')">审核通过</a>
                        </div>
                    </div>

                    <!-- Start 模态 -->
                    <!-- 审核通过 -->
                    <div id="checkBonus" class="modal fade" tabindex="-1" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal"
                                            aria-hidden="true"></button>
                                    <h4 id="modal_title" class="modal-title center">审核确认</h4>
                                </div>
                                <div class="modal-body">
                                    <div id="modal_content" class="scroller mt-lg" style="height:200px"
                                         data-always-visible="1" data-rail-visible1="1">
                                        <div id="modal_content_row" class="row">
                                            <div class="col-md-offset-1 col-md-10">
                                                <h4>
                                                    当前审核日期：
                                                    <fmt:formatDate value="${bonus.startDate}" type="date"
                                                                    pattern="yyyy-MM-dd"/> ~
                                                    <fmt:formatDate value="${bonus.endDate}" type="date"
                                                                    pattern="yyyy-MM-dd"/>
                                                </h4>
                                                <h4>充值总金额：<span id="payTotal"></span>元</h4>
                                                <h4>提成总金额：<span id="bonusTotal"></span>元</h4>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div id="modal_footer" class="modal-footer center">
                                    <button type="button" class="btn green" onclick="checkedBonus('${bonus.id}');">
                                        确认审核通过
                                    </button>
                                    <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- End 模态 -->
                </c:if>


                <c:if test="${empty bonus}">
                    <div class="row">
                        <div class="col-md-12" style="height: 300px">
                            <h4 class="center center-vertical">暂无数据</h4>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty bonus and bonus.isPass}">
                    <div class="row">
                        <div class="col-md-12" style="height: 300px">
                            <h4 class="center center-vertical">上周提成报表已审核通过</h4>
                        </div>
                    </div>
                </c:if>
            </div>

        </div>
        <!-- END 页面内容-->

    </div>
</div>


<script>

    //初始加载 :: 每日简表
    window.onload = loadDailyTable();

    //每日简表
    $("#t_dailyTable").click(function () {
        $("#chooseTab").val("1");
        loadDailyTable();
    });

    //单笔明细
    $("#t_singleDetail").click(function () {
        $("#chooseTab").val("2");
        $("#singleDetail").load("check_bill/ajax/singleDetail", {bonusId: $("#bonusId").val()});
    });

    function loadDailyTable() {
        $("#dailyTable").load("check_bill/ajax/dailyTable", {bonusId: $("#bonusId").val()});
    }

    //确认审核通过
    function givingBonus(bonusId) {
        $.post("check_bill/ajax/getTotal", {bonusId: bonusId}, function (data) {
            var json = JSON.parse(data);
            if (json != null) {
                for (var key in json) {
                    if (key == "payTotal") {
                        $("#payTotal").text(json[key]);
                    }
                    if (key == "bonusTotal") {
                        $("#bonusTotal").text(json[key]);
                    }
                }
            }

            $("#checkBonus").modal();
        })
    }

    //二次确认
    function checkedBonus(bonusId) {
        $("#checkBonus").modal("hide");

        $.post("check_bill/ajax/givingBonus", {bonusId: bonusId}, function (data) {
            if (data) {
                var msg = "<br/><br/><h4 class='mt-xlg center'>提成审核成功</h4>"
                layerMsg_callback("<span style='margin-left: 48px;font-size: 16px;'>审核成功</span>", msg);
                reload_content("check_bill/bonusCheck");
            } else {
                var msg = "<br/><br/><h4 class='mt-xlg center font-red'>请确认每条信息已审核</h4>"
                layerMsg("<span style='margin-left: 48px;'>提示信息</span>", msg);
            }
        });
    }

    //导出
    $("#download").click(function () {
        var tab = $("#chooseTab").val();
        var bonusId = $("#bonusId").val();
        if (tab == 1) {
            window.location.href = "check_bill/dailyTableDownload?bonusId=" + bonusId;
        }
        else if (tab == 2) {
            window.location.href = "check_bill/singleDetailDownload?bonusId=" + bonusId + "&fileName=提成审核_单笔明细";
        }
    });

    function reload_content(url) {
        setTimeout(function () {
            $("#content").load(url, function (responseTxt, statusTxt, xhr) {
                if (statusTxt == "error") {
                    location.reload();
                }
            });
        }, 1000)
    }

</script>
