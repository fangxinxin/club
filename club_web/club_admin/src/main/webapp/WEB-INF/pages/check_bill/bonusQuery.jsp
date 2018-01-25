<!--提成查询-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>

<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">提成查询</h4>
                <div class="caption center-pill">
                </div>
            </div>
            <div class="portlet-body">
                <div class="tabbable tabbable-tabdrop">
                    <ul class="nav nav-pills">
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
                        <c:if test="${not empty bonusDt}">
                            <li class="ml-xlg"><label class="bold" style="padding: 9px 10px;">筛选：</label></li>
                            <li class="ml-md">
                                <select id="bonusId" class="form-control blue" style="height: 42px">
                                    <c:forEach items="${bonusDt}" var="row">

                                        <option value="${row.getColumnValue("id")}">
                                                ${row.getColumnValue("startDate")} ~ ${row.getColumnValue("endDate")}
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                        </c:if>

                    </ul>
                    <div class="tab-content">
                        <!-- 每日简表 :: 查询结果 -->
                        <div class="tab-pane active" id="dailyTable"></div>
                        <div class="tab-pane" id="singleDetail"></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- END 页面内容-->

    </div>
</div>

<script>
    //选择时间范围
    $("#bonusId").change(function () {
        var tab = $("#chooseTab").val();
        if (tab == 1) {
            $("#dailyTable").load("check_bill/ajax/dailyTable_r", {bonusId: $("#bonusId option:selected").val()});
        }
        else if (tab == 2) {
            $("#singleDetail").load("check_bill/ajax/singleDetail_r", {bonusId: $("#bonusId option:selected").val()});
        }
    });

    //初始加载 :: 每日简表
    $("#dailyTable").load("check_bill/ajax/dailyTable_r", {bonusId: $("#bonusId option:selected").val()});

    //每日简表
    $("#t_dailyTable").click(function () {
        $("#chooseTab").val("1");
        $("#dailyTable").load("check_bill/ajax/dailyTable_r", {bonusId: $("#bonusId option:selected").val()});
    });

    //单笔明细
    $("#t_singleDetail").click(function () {
        $("#chooseTab").val("2");
        $("#singleDetail").load("check_bill/ajax/singleDetail_r", {bonusId: $("#bonusId option:selected").val()});
    });

    //导出
    $("#download").click(function () {
        var tab = $("#chooseTab").val();
        var bonusId = $("#bonusId option:selected").val();
        if (tab == 1) {
            window.location.href = "check_bill/dailyTableDownload?bonusId=" + bonusId + "&fileName=充值报表_每日简表";
        }
        else if (tab == 2) {
            window.location.href = "check_bill/singleDetailDownload?bonusId=" + bonusId + "&fileName=处置报表_单笔明细";
        }
    })

</script>
