<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- R_head -->
<%--<%@include file="/common/include/R_head.jsp" %>--%>


<style type="text/css">
    .tb1 tr {
        background: #f1f1f1;
    }

    .tb1 tr:nth-child(even) {
        background: #ccc;
    }

    td, th {
        text-align: center;
        border: 0px;
    }

    .tb1 td, th {
        border-left: 1px solid black;
    }
</style>

<div class="row" style="height:100%;width:100%;background-color: white;padding:0px;margin:0px;">
    <div class="col-md-12 col-xs-12">
        <%--<div class="form-group">--%>
        <div class="col-md-3 col-xs-3">
            <input style="text-align: left;" type="button" class="btn green btn-outline" value="导出excel" onclick="diamondChange_download()">
            <input type="hidden" id="promoterId" value="${promoterId}">
            <input type="hidden" id="joinClubTime" value="${joinClubTime}">
            <input type="hidden" id="userId" value="${userId}">
        </div>
        <div class="col-md-8 col-xs-8">
            <%--<div class="form-group">--%>
            <div class="col-md-4 col-md-offset-2  col-xs-4">
                <div class="input-group date form_date" >
                    <input type="text" size="16" style="width: 100px;" class="form-control input-sm" placeholder="开始时间"
                           id="startDate3" value="${startDate}">
                                                                    <span class="input-group-btn">
                                                                        <button class="btn default date-set input-sm" type="button">
                                                                            <i class="fa fa-calendar"></i>
                                                                        </button>
                                                                    </span>
                </div>
            </div>

            <div class="col-md-4 col-md-offset-1 col-xs-4">
                <div class="input-group date form_date" >
                    <input type="text" size="16" style="width: 100px;" class="form-control input-sm" placeholder="结束时间" name="end"
                           id="endDate3"  value="${endDate}">
                                                                    <span class="input-group-btn">
                                                                        <button class="btn default date-set input-sm" type="button">
                                                                            <i class="fa fa-calendar"></i>
                                                                        </button>
                                                                    </span>
                </div>
            </div>
            <%--</div>--%>

            <div class="col-md-1 col-xs-1" style="padding-left: 20px">
                <input type="submit" value="查&nbsp;&nbsp;询" class="btn btn-primary btn-sm " onclick="queryChangeInfo()">
            </div>
        </div>
        <%--</div>--%>
    </div>
    <table class="table table-hover table-striped table-bordered tb1"  width="100%"  align="center" >
        <tr >
            <th style="text-align: center">成员ID</th>
            <th style="text-align: center">成员昵称</th>
            <th style="text-align: center">钻石变动</th>
            <th style="text-align: center">变动缘由</th>
            <th style="text-align: center">剩余钻石</th>
            <th style="text-align: center">时间</th>
        </tr>
        <tbody id="tb_1">
        <c:if test="${not empty memberDiamondsListByDate}">
            <c:forEach items="${memberDiamondsListByDate}" var="l">
                <tr>
                    <td align="center">${l.userId}</td>
                    <td align="center">${l.userNickName}</td>
                    <td align="center">${l.diamondChangeNums}</td>
                    <td align="center">${l.changeReason}</td>
                    <td align="center">${l.remainDiamondNums}</td>
                    <td align="center">
                        <fmt:formatDate value="${l.createTime}"
                                        pattern="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <div style="width: 100%;text-align: center;line-height: 35px;">
        <br/>
        <input type="button" value="确&nbsp;&nbsp;定" name="create"
               style="width: 150px;"
               class="btn btn-md btn-success" onclick="parent.layer.closeAll();">
    </div>
</div>
<!-- R_foot -->
<%--<%@include file="/common/include/R_foot.jsp"%>--%>
<script>
     function queryChangeInfo(){

         var startDate = $("#startDate3").val();
         var endDate = $("#endDate3").val();
         var promoterId = $("#promoterId").val();
         var userId = $("#userId").val();
         var joinClubTime = $("#joinClubTime").val();

         if (startDate > endDate) {
             layer.alert('开始日期不能大于结束日期，请重新选择！');
         } else {
             $("#tb_1").empty();
             $.get("pre_sale_manage/queryChangeInfoAjax",
                     {
                         startDate: startDate,
                         endDate: endDate,
                         promoterId: promoterId,
                         userId: userId,
                         joinClubTime: joinClubTime
                     },
                     function (data) {
                         $('#tb_1').html(data);
                     },
                     'JSON');
         }

     }

    function diamondChange_download(){
        var startDate = $("#startDate3").val();
        var endDate = $("#endDate3").val();
        var promoterId = $("#promoterId").val();
        var userId = $("#userId").val();
        var joinClubTime = $("#joinClubTime").val();

        window.open('pre_sale_manage/diamondChange_download?startDate=' + startDate + '&endDate=' + endDate + '&promoterId=' + promoterId + '&userId='+ userId + '&joinClubTime=' +joinClubTime  );

    }


</script>



<!-- 时间插件 :: 初始化 -->
<script src="http://download.stevengame.com/webResource/common/js/plugins_init/components-datetimepicker-0.0.5.js"></script>
<!-- 时间插件 :: 选择中文语言 -->
<script charset="utf-8"
        src="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN-1.0.js"></script>





