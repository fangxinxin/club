<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>
<%@include file="/common/include/datetimepicker.jsp"%>
<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">代理操作记录</h4>
                <hr/>
                <div class="caption center-pill">
                </div>
            </div>
            <div class="portlet-body">

                <!-- 查询 -->
                <div class="row">
                    <div class="col-md-12">

                        <div class="form-group ">
                            <div class="col-md-2" >
                                <select class="form-control blue" style="height: 32px" id="checkType">
                                    <option value="preSaleManage">全部操作</option>
                                    <option value="11">开通代理</option>
                                    <option value="17">延长转正</option>
                                    <option value="19">取消转正</option>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <input  type="button" class="btn green btn-outline" value="导出excel" onclick="operation_download()">
                            </div>
                            <div class="col-md-8">

                                <div class="form-group">
                                    <div class="col-md-3 col-md-offset-4">
                                        <div class="input-group date form_date">
                                            <input id="startDate" type="text" size="16" class="form-control"
                                                   placeholder="开始时间" name="startDate" value="${startDate}">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button">
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
                                            <button class="btn default date-set" type="button">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                                        </div>

                                    </div>

                                    <input id="search" type="button" value="查&nbsp;&nbsp;询" class="btn btn-primary">
                                </div>

                            </div>
                        </div>

                    </div>
                </div>

                <!-- 查询结果 -->
                <div class="tab-content">
                    <div class="tab-pane active" id="table"></div>
                </div>

                </div>

            </div>
        </div>
        <!-- END 页面内容-->

    </div>
</div>

<script>
    //初始加载 : 全部操作
    $("#table").load("pre_sale_manage/listOperation");

    //按类型和时间范围筛选
    $("#search").click(function() {
        $("#table").load("pre_sale_manage/queryOperation"
                , { checkType: $("#checkType").val(),startDate: $.trim($('#startDate').val()),endDate: $.trim($('#endDate').val()) }
        );
    });

    //导出excel
    function operation_download(){

        window.open('pre_sale_manage/operation_download?checkType=' + $("#checkType").val() + '&startDate=' + $.trim($('#startDate').val()) + '&endDate=' + $.trim($('#endDate').val())  );
    }

</script>


