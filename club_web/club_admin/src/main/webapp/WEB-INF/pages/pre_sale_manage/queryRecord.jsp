<!--充值查询-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>

<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">战绩查询</h4>
                <div class="caption center-pill">
                </div>
            </div>
            <div class="portlet-body">
                <div class="tabbable tabbable-tabdrop">
                    <div class="form-group ">
                        <div class="col-md-2">
                            <div id="select">
                                <select id="type" class="form-control blue ml-xlg" style="height: 37px">
                                    <option style="height: 50px" value="0">所有战绩</option>
                                    <option style="height: 50px" value="1">大赢家战绩</option>
                                    <option style="height: 50px" value="2">房主战绩</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6" style="text-align: center;">
                            <div class="form-group">
                                <div class="col-md-6">
                                    <div class="input-group date form_advance_datetime" id="start">
                                        <input id="startDate" type="text" size="16" class="form-control"
                                               placeholder="开始时间" name="startDate" value="${startDate}">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" id="start2">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="input-group date form_advance_datetime" id="end">
                                        <input id="endDate" type="text" size="16" class="form-control"
                                               placeholder="结束时间" name="endDate" value="${endDate}">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" id="end2">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <div class="col-md-2">
                            <div class="col-md-8">
                                <input type="text" placeholder="请输入玩家ID" name="gameUserId" id="userId"
                                       style="width: 250px;line-height: 28px;"/>
                            </div>
                        </div>
                        <div class="col-md-2" style="text-align: center;">
                            <input id="search" type="button" value="查&nbsp;&nbsp;询" class="btn btn-primary"
                                   onclick="queryDetail()">
                        </div>
                    </div>
                </div>
                <div id="queryDetail"></div>

            </div>

        </div>
        <!-- END 页面内容-->

    </div>
</div>

<script>

    $(function () {
        var now = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate(), new Date().getHours(), new Date().getMinutes());

        $(".form_advance_datetime").datetimepicker({
            language: "zh-CN",
            format: "yyyy-MM-dd hh:ii",
            autoclose: true,
            todayBtn: true,
            endDate: now,
            pickerPosition: "bottom-left"
        });

    });

    function queryDetail() {

        var userId = $.trim($("#userId").val());
        if (userId == '') {
            layer.alert("请输入玩家ID！");
            return;
        }
        if (isNaN(userId)) {
            layer.alert("请输入纯数字ID！");
            return;
        }

        var sd = $('#startDate').val();
        var ed = $('#endDate').val();
        if (Date.parse(ed) < Date.parse(sd)) {
            layer.alert("结束日期不能小于开始日期，请重新选择!");
            $('#startDate').val('${startDate}');
            $('#endDate').val('${endDate}');
            return;
        }

        var type = $('#type').val();
        var url = 'pre_sale_manage/allDetail';
        if (type == '1') {
            url = 'pre_sale_manage/winnerDetail';
        }
        if (type == '2') {
            url = 'pre_sale_manage/ownerDetail';
        }
        $('#queryDetail').load(url, {startDate: sd, endDate: ed, userId: userId});
    }


</script>
