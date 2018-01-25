<!--提现审核-->
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
                <h4 class="center bold">提现审核</h4>
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
                            <table width="100%" border="1px" class="tb1" style="line-height: 30px;font-size: 16px;">
                                <tr style="background-color: #e6e6e6">
                                    <th>提现笔数</th>
                                    <th>提现人数</th>
                                    <th>提现金额</th>
                                    <th>可提现总余额</th>
                                    <th>时间</th>
                                    <th>导入订单</th>
                                    <th>明细</th>
                                    <th>打款状态</th>
                                </tr>
                                <c:forEach items="${dt}" var="d">
                                    <tr>
                                        <td>${d.getColumnValue("requestNum")}</td>
                                        <td>${d.getColumnValue("peopleNum")}</td>
                                        <td>${d.getColumnValue("withdrawTotal")!="" ? d.getColumnValue("withdrawTotal"):"0"}</td>
                                        <td>${d.getColumnValue("remainTotal")!="" ? d.getColumnValue("remainTotal"):"0"}</td>
                                        <td>${d.getColumnValue("createTime")}</td>

                                        <td>
                                            <form method="post" enctype="multipart/form-data"
                                                  action="check_bill/withdrawCheck/upload">
                                                <div class="fileinput fileinput-new" data-provides="fileinput">
                                                        <span class="btn green btn-sm btn-file" style="margin-top: 8px">
                                                            <c:if test="${d.getColumnValue('receiptNum')=='' && d.getColumnValue('exceptionNum')==''}">导&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;入</c:if>
                                                            <c:if test="${d.getColumnValue('receiptNum')!='' || d.getColumnValue('exceptionNum')!=''}">重新导入</c:if>
                                                            <input type="file" name="file" class="btn btn-sm">
                                                        </span>
                                                </div>
                                                <input type="button" class="btn btn-sm" style="margin-top: 8px"
                                                       value="确定"
                                                       onclick="upLoad(this);">
                                                <input type="hidden" value="${d.getColumnValue("createTime")}"
                                                       name="createTime">
                                            </form>
                                        </td>

                                        <td><input type="button" value="查&nbsp;&nbsp;询" class="btn btn-success btn-sm"
                                                   style="width: 60px;" onclick="openA(this)"></td>
                                        <td>
                                            <c:if test="${d.getColumnValue('exceptionNum')!=''}"><span
                                                    style="color: red">订单异常</span></c:if>
                                            <c:if test="${d.getColumnValue('receiptNum')=='' && d.getColumnValue('exceptionNum')==''}">未导入订单</c:if>
                                            <c:if test="${d.getColumnValue('receiptNum')!='' && d.getColumnValue('exceptionNum')==''}"><span
                                                    style="color: green">订单正常</span></c:if>
                                        </td>
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

    function upLoad(obj) {
        var form = $(obj).parent();
        var file = $(obj).prev().children('span').children('input').eq(1);
        if ($.trim(file.val()) == '') {
            layer.alert("请选择文件!");
        } else {
            $.ajax({
                url: 'check_bill/withdrawCheck/upload',
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
        window.open('check_bill/withdrawCheck/download?startDate=' + startDate + '&endDte=' + endDate);
    }
    /*function send(obj) {
     var gameName = $('#gameName').val();
     var txt = $(obj).parent().prev().prev().text();
     var txtOne = txt.split("-")[0] + "年" + parseInt(txt.split("-")[1]) + "月" + parseInt(txt.split("-")[2]) + "日";
     var txtTwo = $(obj).parent().prev().prev().prev().prev().text();
     var txtFour = parseInt(txt.split("-")[1]) + "月" + parseInt(txt.split("-")[2]) + "日";
     layer.open({
     type: 1,
     title: ['确认打款', 'text-align:center;font-size:20px;'],
     closetBtn: 0,
     isOutAnim: false,
     anim: -1,
     shadeClose: false, //点击遮罩关闭层
     area: ['800px', '500px'],
     content: "<div><table width='100%' style='line-height: 35px;'><tr><td>&nbsp;</td><td>&nbsp;</td></tr>" +
     "<tr><td>&nbsp;</td><td>&nbsp;</td></tr>" +
     "<tr><td colspan='2' style='color: dodgerblue;font-size: 18px;' align='center'>" + txtOne + "：需打款" + txtTwo + "元</td></tr>" +
     "<tr><td>&nbsp;</td><td>&nbsp;</td></tr>" +
     "<tr><td>&nbsp;</td><td>&nbsp;</td></tr>" +
     "<tr><td colspan='2' style='color: red;font-size: 18px;' align='center'>注：即将确认&lt;" + gameName + "&gt;" + txtFour + "提成金为已打款状态，请确认相关款项已打入代理商账户。</td></tr>" +
     "<tr><td>&nbsp;</td><td>&nbsp;</td></tr>" +
     "<tr><td>&nbsp;</td><td>&nbsp;</td></tr>" +
     "<tr><td>&nbsp;</td><td>&nbsp;</td></tr>" +
     "<tr><td align='center'>" +
     "<input type='hidden' value=" + txt + ">" +
     "<input type='button' value='确认已打款' name='create' class='btn btn-lg' style='width: 150px;background-color: #00cc99;' onclick='openB(this);'></td>" +
     "<td align='center'><input type='button' value='取&nbsp;&nbsp;消' name='create' style='width: 150px;background-color: #cccccc;' class='btn btn-lg' onclick='layer.closeAll();'></td></tr></table></div>"
     });
     }*/
    function openA(obj) {
        layer.open({
            type: 2,
            closeBtn: 1,
            anim: -1,
            title: ['提现明细', 'text-align:center;'],
            area: ['1350px', '620px'],
            content: "check_bill/withdrawCheck/queryDayDetail?date=" + $(obj).parent().prev().prev().text()
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
        now = new Date(now.getTime() - 86400000);
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
            $("#content").load("check_bill/withdrawCheck/queryWithdrawCheck?startDate=" + $("#startDate").val() + "&endDate=" + $("#endDate").val());
        }
    }

</script>
<!-- 时间插件 :: 初始化 -->
<script src="http://download.stevengame.com/webResource/common/js/plugins_init/components-datetimepicker-0.0.5.js"></script>
<!-- 时间插件 :: 选择中文语言 -->
<script charset="utf-8"
        src="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN-1.0.js"></script>
