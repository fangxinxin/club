<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>
<%@include file="/common/include/datetimepicker.jsp" %>


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
                <h4 class="center bold">工单查询</h4>
                <div class="caption center-pill"></div>
            </div>

            <div class="portlet-body">


                <div class="tabbable tabbable-tabdrop">
                    <div class="form-group ">
                        <div class="col-md-5">
                            <ul class="nav nav-pills ">
                                <li class="active">
                                    <a id="t_dateQuery" href="#dateQuery"
                                       style="width: 150px;text-align: center;"
                                       data-toggle="tab">按日期</a>
                                </li>
                                <li>
                                    <a id="t_editNameQuery" href="#editAdminQuery"
                                       style="width: 150px;text-align: center;"
                                       data-toggle="tab">按员工</a>
                                </li>
                                <li class="ml-xlg" id="liDownDate">
                                    <a id="downloadDate" class="btn green btn-outline btn-xs"
                                       href="javascript:downLoad();">导出日期excel</a>
                                </li>
                                <li class="ml-xlg" id="liDown">
                                    <a id="download" class="btn green btn-outline btn-xs"
                                       href="javascript:byEditAdminDownload();">导出员工excel</a>
                                </li>
                            </ul>
                        </div>
                        <div class="col-md-7" id="timeTool">
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
                                <input id="chooseTab" type="text" value="1" hidden="hidden"/>
                            </div>
                        </div>
                        <div class="col-md-7" id="inputAdmin">
                            <div class="form-group">
                                <div class="col-md-4 col-md-offset-5">
                                    <input type="text" class="form-control" id="editAdmin"
                                           placeholder="请输入员工姓名" maxlength="5"/>
                                </div>
                                <input type="button" class="btn btn-primary" id="searchAdmin"
                                       onclick="queryEditAdmin();"
                                       value="查&nbsp;&nbsp;询">&nbsp;&nbsp;
                                <input type="button" class="btn btn-primary" value="查询全部"
                                       onclick="editAdminQuery();$('#editAdmin').val('')">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="tab-content">
                    <!-- 按员工查询 :: 查询结果 -->
                    <div class="tab-pane active" id="dateQuery"></div>
                    <div class="tab-pane active" id="editAdminQuery"></div>
                </div>
            </div>


        </div>
    </div>
    <!-- END 页面内容-->

</div>
</div>


<script>

    //初始加载 :: 按日期
    window.onload = loadDateQuery();

    //按员工
    $("#t_editNameQuery").click(function () {
        editAdminQuery();
    });

    function editAdminQuery() {
        $('#liDown').css('display', 'block');
        $('#inputAdmin').css('display', 'block');
        $('#liDownDate').css('display', 'none');
        $('#timeTool').css('display', 'none');
        $('#editAdmin').val('');
        $("#editAdminQuery").load('pre_sale_manage/editAdminQuery');
    }
    function queryEditAdmin() {
        var editAdmin = $('#editAdmin').val();
        $("#editAdminQuery").load('pre_sale_manage/editAdminQuery?editAdmin=' + editAdmin);
    }
    //按日期
    $("#t_dateQuery").click(function () {
        $('#startDate').val('${startDate}');
        $('#endDate').val('${endDate}');
        loadDateQuery();
    });

    function loadDateQuery() {
        $('#liDown').css('display', 'none');
        $('#liDownDate').css('display', 'block');
        $('#timeTool').css('display', 'block');
        $('#inputAdmin').css('display', 'none');
        $("#dateQuery").load('pre_sale_manage/dateQuery');
    }

    //查询单日工单明细
    function queryByDate(obj) {
        layer.open({
            type: 2,
            closeBtn: 1,
            anim: -1,
            title: ['工单明细', 'text-align:center;'],
            area: ['950px', '620px'],
            content: "pre_sale_manage/queryByDate?date=" + $(obj).parent().prev().prev().prev().text()
        })
    }

    //查询指定员工工单明细
    function queryOneDetail(obj) {
        layer.open({
            type: 2,
            closeBtn: 1,
            anim: -1,
            title: ['工单明细', 'text-align:center;'],
            area: ['950px', '620px'],
            content: "pre_sale_manage/queryOneDetail?editAdmin=" + $(obj).next().val()
        })
    }
    function downLoad() {
        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();

        window.open('pre_sale_manage/byDateDownload?startDate=' + startDate + "&endDate=" + endDate);
    }

    function byEditAdminDownload() {
        window.open('pre_sale_manage/byEditAdminDownload?editAdmin=' + encodeURIComponent($('#editAdmin').val()));
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
        var start = $('#startDate').val();
        var end = $('#endDate').val();
        if (start > end) {
            layer.alert('开始日期不能大于结束日期，请重新选择！');
        } else {
            $("#dateQuery").load('pre_sale_manage/dateQuery?startDate=' + start + "&endDate=" + end);
        }
    }
</script>





