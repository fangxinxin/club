<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<link rel="stylesheet"
      href="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
<style>
    .table > tbody > tr > td, .table > tbody > tr > th, .table > tfoot > tr > td, .table > tfoot > tr > th, .table > thead > tr > td, .table > thead > tr > th {
        padding: 3px;
        line-height: 1.42857143;
        vertical-align: middle;
        border-top: 1px solid #ddd;
    }

    .scroll {
        overflow: scroll;
        -webkit-overflow-scrolling: touch;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0
    }
</style>

<div class="easyui-navpanel">
    <c:if test="${isGameRecord}">
        <div class="center">
            <ul class="pagination pagination-sm col-xs-12 ml-sm" style="margin: 6px 0;">
                <li onclick="tab('win/gameRecord_winner');$('#type').val(1);">
                    <a class="col-xs-3" href="javascript:;">大赢家表</a></li>
                <li onclick="tab('win/gameRecord_host');$('#type').val(2);">
                    <a class="col-xs-3" href="javascript:;">房主报表</a>
                </li>
                <li onclick="tab('win/gameRecord_detail');$('#type').val(3);">
                    <a class="col-xs-3" href="javascript:;">战绩明细</a></li>
                <li onclick="tab('win/gameRecord_playDetail');$('#type').val(4);">
                    <a class="col-xs-3" href="javascript:;">参与局数</a>
                </li>
            </ul>
        </div>


        <div class="container">
            <div class="col-xs-9">
                <div class="col-xs-12" style="margin-bottom:2px;">
                    <div class="col-xs-12 input-group date form_advance_datetime" id="start">
                        <input class="col-xs-12" id="startDate" type="text" value="${startDate}" readonly
                               style="text-align:center;height: 28px;font-size: 12px;line-height: 1.5;border-radius: 3px;">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" id="start2">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                    </div>
                </div>

                <div class="col-xs-12" style="margin-top:2px;">
                    <div class="input-group date form_advance_datetime" id="end">
                        <input class="col-xs-12" id="endDate" type="text" value="${endDate}" readonly
                               style="text-align:center;height: 28px;font-size: 12px;line-height: 1.5;border-radius: 3px;">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" id="end2">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                    </div>
                </div>
                <input type="hidden" id="type">
            </div>

            <div class="col-xs-3 center">
                <input type="button" id="search" class="btn btn-primary" style="margin-top: 17px;"
                       value="查&nbsp;&nbsp;询" onclick="searchByDate();">
            </div>
        </div>


        <div id="load_content" class="center mt-xs"></div>

    </c:if>
</div>


<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp" %>
<script>//禁止微信浏览器下拉
var overscroll = function (el) {
    el.addEventListener('touchstart', function () {
        var top = el.scrollTop
                , totalScroll = el.scrollHeight
                , currentScroll = top + el.offsetHeight

        if (top === 0) {
            el.scrollTop = 1
        } else if (currentScroll === totalScroll) {
            el.scrollTop = top - 1
        }
    })
    el.addEventListener('touchmove', function (evt) {
        //if the content is actually scrollable, i.e. the content is long enough
        //that scrolling can occur
        if (el.offsetHeight < el.scrollHeight)
            evt._isScroller = true
    })
}
overscroll(document.querySelector('.scroll'));
document.body.addEventListener('touchmove', function (evt) {
    //In this case, the default behavior is scrolling the body, which
    //would result in an overflow.  Since we don't want that, we preventDefault.
    if (!evt._isScroller) {
        evt.preventDefault()
    }
})
</script>

<script charset="utf-8">

    $(function () {
        var now = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate(), new Date().getHours(), new Date().getMinutes());

        $('#type').val(1);

        //init
        $("#load_content").load("win/gameRecord_winner", {
            startDate: $('#startDate').val(),
            endDate: $('#endDate').val()
        });

        $(".form_advance_datetime").datetimepicker({
            language: "zh-CN",
            format: "yyyy-MM-dd hh:ii",
            autoclose: true,
            todayBtn: true,
            endDate: now,
            pickerPosition: "bottom-left"
        });
    });

    function tab(url) {
        $("#load_content").load(url, {startDate: $('#startDate').val(), endDate: $('#endDate').val()});
    }

    function searchByDate() {
        var type = $('#type').val();
        var sd = $('#startDate').val();
        var ed = $('#endDate').val();
        if (Date.parse(ed) < Date.parse(sd)) {
            parent.$.messager.alert("提示", "结束日期不能小于开始日期，请重新选择!");
            $('#startDate').val('${startDate}');
            $('#endDate').val('${endDate}');
            return;
        }
        if (type == 2) {
            $("#load_content").load("win/gameRecord_host", {
                startDate: $('#startDate').val(),
                endDate: $('#endDate').val()
            });
        } else if (type == 3) {
            $("#load_content").load("win/gameRecord_detail", {
                startDate: $('#startDate').val(),
                endDate: $('#endDate').val()
            });
        } else if (type == 1) {
            $("#load_content").load("win/gameRecord_winner", {
                startDate: $('#startDate').val(),
                endDate: $('#endDate').val()
            });
        } else {
            $("#load_content").load("win/gameRecord_playDetail", {
                startDate: $('#startDate').val(),
                endDate: $('#endDate').val()
            });
        }
    }

</script>

<!-- 时间插件 :: 初始化 -->
<script src="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
<!-- 时间插件 :: 选择中文语言 -->
<script charset="utf-8"
        src="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN-1.0.js"></script>


