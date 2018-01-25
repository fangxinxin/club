<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="/common/include/lib.jsp" %>

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

<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">俱乐部查询</h4>
            </div>
            <div class="portlet-body">
                <div style="text-align: center;line-height: 28px;">
                    <input type="text" placeholder="通过俱乐部ID查询" id="clubId" style="width: 250px;"/>
                    &nbsp;&nbsp;
                    <input type="button" value="查&nbsp;&nbsp;询" onclick="queryClub();" id="sub"
                           class="btn btn-primary"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="text" placeholder="通过游戏Id查询" id="userId" style="width: 250px;"/>
                    &nbsp;&nbsp;
                    <input type="button" value="查&nbsp;&nbsp;询" onclick="queryPromoter();" class="btn btn-primary"
                           id="sub2"/>
                    <br/><br/><br/>
                </div>
                <c:if test="${club!=null}">
                    <div class="tabbable tabbable-tabdrop">
                        <table width="80%" align="center">
                            <tr>
                                <td width="50%" align="center">
                                    <table border="1" width="80%" style="line-height: 30px;" class="tb1">
                                        <tr style="background-color: #959595">
                                            <th width="50%">信息类型</th>
                                            <th width="50%">信息明细</th>
                                        </tr>
                                        <tr>
                                            <td>代理商昵称</td>
                                            <td>${promoter.nickName}</td>
                                        </tr>
                                        <tr>
                                            <td>代理商ID</td>
                                            <td id="promoter_id">${promoter.id}</td>
                                        </tr>
                                        <tr>
                                            <td>代理商姓名</td>
                                            <td>
                                                    ${promoter.realName}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>手机号码</td>
                                            <td>
                                                <shiro:hasPermission
                                                        name="sys:cellphone:show">${promoter.cellPhone}</shiro:hasPermission>
                                                <shiro:lacksPermission
                                                        name="sys:cellphone:show">*****</shiro:lacksPermission>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>代理商级别</td>
                                            <td>
                                                <c:if test="${promoter.pLevel==-1}">特级</c:if>
                                                <c:if test="${promoter.pLevel==1}">一级</c:if>
                                                <c:if test="${promoter.pLevel==2}">二级</c:if>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>拥有俱乐部</td>
                                            <td>${club.clubName}</td>
                                        </tr>
                                        <tr>
                                            <td>俱乐部转正时间</td>
                                            <td>
                                                <c:if test="${empty logClubModel.createTime}">无</c:if>
                                                <fmt:formatDate value="${logClubModel.createTime}"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>俱乐部ID</td>
                                            <td id="club_id">${club.id}</td>
                                        </tr>
                                        <tr>
                                            <td>俱乐部人数</td>
                                            <td>${club.peopleNum}</td>
                                        </tr>
                                        <tr>
                                            <td>新成员人数</td>
                                            <td>${memberNums}</td>
                                        </tr>
                                        <tr>
                                            <td>俱乐部成员参与对局</td>
                                            <td>${roomNums}</td>
                                        </tr>
                                        <tr>
                                            <td>代理关系</td>
                                            <td><a class="btn btn-success btn-sm" data-toggle="modal"
                                                   id="_relation_detail"
                                                   href="#relation_detail">
                                                关系详情</a></td>
                                        </tr>
                                        <tr>
                                            <td>返钻明细</td>
                                            <td><a class="btn btn-success btn-sm" data-toggle="modal"
                                                   id="_rebate_detail"
                                                   href="#rebate_detail">
                                                返钻明细</a></td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="50%" align="center">
                                    <table width="80%" style="line-height: 30px;" border="1" class="tb1">
                                        <tr style="background-color: #959595">
                                            <th width="50%">信息类型</th>
                                            <th width="50%">信息明细</th>
                                        </tr>
                                        <tr>
                                            <td>充值总额</td>
                                            <td>${promoter.totalPay}元</td>
                                        </tr>
                                        <tr>
                                            <td>到账钻石</td>
                                            <td>${promoter.gameCardTotal}</td>
                                        </tr>
                                        <tr>
                                            <td>钻石发放</td>
                                            <td>${promoter.gameCardSellTotal}</td>
                                        </tr>
                                        <tr>
                                            <td>剩余钻石</td>
                                            <td><span id="gameCard">${promoter.gameCard}</span></td>
                                        </tr>
                                        <tr>
                                            <td>当前可提取返钻数量</td>
                                            <td>${promoter.rebate}</td>
                                        </tr>
                                        <tr>
                                            <td>累计返钻数量</td>
                                            <td>${promoter.rebateTotal}</td>
                                        </tr>
                                            <%-- <tr>
                                                 <td>累计提成金额</td>
                                                 <td>${promoter.depositTotal}元</td>
                                             </tr>
                                             <tr>
                                                 <td>提成购钻石金额</td>
                                                 <td>${requestPay}元</td>
                                             </tr>
                                             <tr>
                                                 <td>当前可用提成</td>
                                                 <td>${promoter.deposit}元</td>
                                             </tr>
                                             <tr>
                                                 <td>已提现金额</td>
                                                 <td>${totalRequest}元</td>
                                             </tr>--%>
                                        <tr>
                                            <td>代理开通人账号</td>
                                            <td>${adminInfoModel.userName}</td>
                                        </tr>
                                        <tr>
                                            <td>代理开通人姓名</td>
                                            <td>${adminInfoModel.realName}</td>
                                        </tr>
                                            <%--<tr>--%>
                                            <%--<td>最后操作钻石时间</td>--%>
                                            <%--<td>--%>
                                            <%--<fmt:formatDate value="${logGamecardModel.createTime}"--%>
                                            <%--pattern="yyyy-MM-dd HH:mm:ss"/>--%>
                                            <%--<c:if test="${empty logGamecardModel.createTime}">无</c:if>--%>

                                            <%--</td>--%>
                                            <%--</tr>--%>
                                        <tr>
                                            <td>创建时间</td>
                                            <td>
                                                <fmt:formatDate value="${promoter.createTime}"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/>
                                            </td>
                                        </tr>
                                            <%--<tr>--%>
                                            <%--<td>最后登录</td>--%>
                                            <%--<td>--%>
                                            <%--<c:if test="${logLogin!=null}">${logLogin.getColumnValue('createTime')}</c:if>--%>
                                            <%--<c:if test="${logLogin==null}">暂无数据</c:if>--%>
                                            <%--</td>--%>
                                            <%--</tr>--%>
                                            <%--<tr>--%>
                                            <%--<td>最后登录IP</td>--%>
                                            <%--<td>--%>
                                            <%--<c:if test="${logLogin!=null}">${logLogin.getColumnValue('loginIp')}</c:if>--%>
                                            <%--<c:if test="${logLogin==null}">暂无数据</c:if>--%>
                                            <%--</td>--%>
                                            <%--</tr>--%>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td width="100%" colspan="2">&nbsp;</td>
                            </tr>
                            <tr>
                                <td width="100%" colspan="2">&nbsp;</td>
                            </tr>
                        </table>
                        <table width="100%" align="center">
                            <tr>
                                <td width="33%">
                                    <shiro:hasPermission name="sys:queryRoomStatis:show">
                                        <a class="btn btn-primary btn-lg" data-toggle="modal" id="_game_count"
                                           href="#game_count">
                                            对局统计</a>
                                    </shiro:hasPermission>
                                </td>
                                <td width="34%">
                                    <shiro:hasPermission name="sys:queryDiamondStatis:show">
                                        <a class="btn btn-primary btn-lg" data-toggle="modal" id="_economic_count"
                                           href="#economic_count">
                                            流水统计</a>
                                    </shiro:hasPermission>
                                </td>
                                <td width="33%">
                                    <shiro:hasPermission name="sys:membersDetail:show">
                                        <a class="btn btn-primary btn-lg" data-toggle="modal" id="_member_detail"
                                           href="#member_detail">
                                            成员明细</a>
                                    </shiro:hasPermission>
                                </td>
                            </tr>
                        </table>
                    </div>
                </c:if>
                <c:if test="${club==null}">
                    <div style="text-align: center;">
                        <h3 style="color: red;">${isEmpty}</h3>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>


<%--对局统计--%>
<div id="game_count" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog" style="width: 800px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">对局统计</h4>
            </div>
            <div class="modal-body" style="max-height:400px;overflow-y:auto;">
                <div class="row">
                    <div class="col-md-12 ">
                        <div class="col-md-3 ">
                            <input style="margin-left: -15px;margin-top: -2px" type="button" class="btn green btn-outline" value="导出excel"
                                   onclick="roomStatis_download()">
                        </div>
                        <div class="col-md-8">
                            <div class="col-md-4 col-md-offset-2 ">
                                <div class="input-group date form_date">
                                    <input type="text" size="16" style="width: 100px;" class="form-control input-sm"
                                           placeholder="开始时间"
                                           id="startDate1" value="${startDate}">
                                                        <span class="input-group-btn">
                                                            <button class="btn default date-set input-sm" type="button">
                                                                <i class="fa fa-calendar"></i>
                                                            </button>
                                                        </span>
                                </div>
                            </div>

                            <div class="col-md-4 col-md-offset-2 ">
                                <div class="input-group date form_date">
                                    <input type="text" size="16" style="width: 100px;" class="form-control input-sm"
                                           placeholder="结束时间" name="end"
                                           id="endDate1" value="${endDate}">
                                                        <span class="input-group-btn">
                                                            <button class="btn default date-set input-sm" type="button">
                                                                <i class="fa fa-calendar"></i>
                                                            </button>
                                                        </span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-1 " style="margin-left: -5px;">
                            <input type="submit" value="查&nbsp;&nbsp;询" class="btn btn-primary btn-sm "
                                   onclick="queryRoomStatis()">
                        </div>

                    </div>
                    <br/>
                    <div id="game_count_content" class="col-md-12">

                    </div>

                </div>
            </div>
            <div class="modal-footer center">
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 确&nbsp;&nbsp;认</button>
            </div>
        </div>
    </div>
</div>

<%--流水统计--%>
<div id="economic_count" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog" style="width: 800px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">流水统计</h4>
            </div>
            <div class="modal-body" style="max-height:400px;overflow-y:auto;">
                <%--<div class="scroller mt-lg"  data-always-visible="1" data-rail-visible1="1">--%>
                <div class="row">
                    <div class="col-md-12 ">
                        <div class="col-md-3 ">
                            <input  style="margin-left: -15px;margin-top: -2px" type="button" class="btn green btn-outline" value="导出excel"
                                   onclick="diamondStatis_download()">
                        </div>
                        <div class="col-md-8 ">
                            <div class="col-md-4 col-md-offset-2">
                                <div class="input-group date form_date">
                                    <input type="text" size="16" class="form-control input-sm" placeholder="开始时间"
                                           id="startDate2" value="${startDate}">
                                                            <span class="input-group-btn">
                                                                <button class="btn default date-set input-sm"
                                                                        type="button">
                                                                    <i class="fa fa-calendar"></i>
                                                                </button>
                                                            </span>
                                </div>
                            </div>

                            <div class="col-md-4 col-md-offset-2">
                                <div class="input-group date form_date">
                                    <input type="text" size="16" class="form-control input-sm" placeholder="结束时间"
                                           name="end"
                                           id="endDate2" value="${endDate}">
                                                            <span class="input-group-btn">
                                                                <button class="btn default date-set input-sm"
                                                                        type="button">
                                                                    <i class="fa fa-calendar"></i>
                                                                </button>
                                                            </span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-1" style="margin-left: -5px;">
                            <input  type="submit" value="查&nbsp;&nbsp;询" class="btn btn-primary btn-sm "
                                   onclick="queryDiamondStatis()">
                        </div>
                    </div>
                    <br/>
                    <div id="economic_count_content" class="col-md-12">

                    </div>
                </div>
                <%--</div>--%>
            </div>
            <div class="modal-footer center">
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 确&nbsp;&nbsp;认</button>
            </div>
        </div>
    </div>
</div>


<%--成员明细--%>
<div id="member_detail" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog" style="width:1100px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">成员明细</h4>
            </div>
            <div class="modal-body" style="max-height:550px;overflow-y:auto;">
                <%--<div class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">--%>
                <div class="row">
                    <div class="col-md-12 ">
                        <input style="text-align: left;" type="button" class="btn green btn-outline" value="导出excel"
                               onclick="memberDetail_download()">
                    </div>
                    <div id="member_detail_content" class="col-md-12">
                    </div>
                </div>
                <%--</div>--%>
            </div>
            <div class="modal-footer center">
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 确&nbsp;&nbsp;认</button>
            </div>
        </div>
    </div>
</div>


<%--关系详情--%>
<div id="relation_detail" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog" style="width: 900px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">关系详情</h4>
            </div>
            <div class="modal-body" style="max-height:450px;overflow-y:auto;">
                <%--<div class="scroller mt-lg"  data-always-visible="1" data-rail-visible1="1">--%>
                <div class="row">
                    <div class="col-md-12 ">
                      <table width="100%" >
                          <tr>
                              <td width="50%" style="text-align:left">
                              上级代理昵称：${upPromoter == null ? "无": upPromoter.nickName}
                              </td>
                              <td width="45%" style="text-align:left">
                              上级代理商ID: ${upPromoter == null ? "无": upPromoter.id}
                              </td>
                              <%-- <td width="30%" style="text-align:left">
                                   上级俱乐部ID:   ${club == null ? "无": club.id}
                              </td>--%>
                              <td width="5%">
                                  <input style="margin-left: 20px;margin-bottom: 5px" type="button" class="btn green btn-outline" value="导出excel"
                                         onclick="relationDetail_download()">
                              </td>
                          </tr>
                      </table>
                    </div>
                    <br/>
                    <div id="relation_detail_content" class="col-md-12">

                    </div>
                </div>
                <%--</div>--%>
            </div>
            <div class="modal-footer center">
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 确&nbsp;&nbsp;认</button>
            </div>
        </div>
    </div>
</div>

<%--返钻明细--%>
<div id="rebate_detail" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog" style="width: 900px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">返钻明细</h4>
            </div>
            <div class="modal-body" style="max-height:450px;overflow-y:auto;">
                <div class="row">
                    <div class="col-md-12 ">
                        <div class="col-md-3 ">
                            <input style="margin-left: -15px" type="button" class="btn green btn-outline"
                                   value="导出excel"
                                   onclick="rebateDetail_download()">
                        </div>
                        <div class="col-md-8 ">
                            <div class="col-md-4 col-md-offset-2">
                                <div class="input-group date form_date">
                                    <input type="text" size="16" class="form-control input-sm" placeholder="开始时间"
                                           id="startDate" value="${startDate}">
                                                            <span class="input-group-btn">
                                                                <button class="btn default date-set input-sm"
                                                                        type="button">
                                                                    <i class="fa fa-calendar"></i>
                                                                </button>
                                                            </span>
                                </div>
                            </div>

                            <div class="col-md-4 col-md-offset-2">
                                <div class="input-group date form_date">
                                    <input type="text" size="16" class="form-control input-sm" placeholder="结束时间"
                                           name="end"
                                           id="endDate" value="${endDate}">
                                                            <span class="input-group-btn">
                                                                <button class="btn default date-set input-sm"
                                                                        type="button">
                                                                    <i class="fa fa-calendar"></i>
                                                                </button>
                                                            </span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-1">
                            <input type="submit" value="查&nbsp;&nbsp;询" class="btn btn-primary btn-sm "
                                   onclick="queryRebateDetail()">
                        </div>
                    </div>
                    <div style="margin-top:-5px" id="rebate_detail_content" class="col-md-12">

                    </div>
                </div>
            </div>
            <div class="modal-footer center">
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 确&nbsp;&nbsp;认</button>
            </div>
        </div>
    </div>
</div>

<script>

    function queryClub() {
        var clubId = $.trim($("#clubId").val());
        if (clubId == '') {
            layer.alert('请输入俱乐部ID');
        } else if (isNaN(clubId)) {
            layer.alert('请输入纯数字ID');
        } else {
            $('#content').load('pre_sale_manage/queryByClub?clubId=' + clubId);
        }
    }

    function queryPromoter() {
        var userId = $.trim($("#userId").val());
        if (userId == '') {
            layer.alert('请输入游戏ID');
        } else if (isNaN(userId)) {
            layer.alert('请输入纯数字ID');
        } else {
            $('#content').load('pre_sale_manage/queryByPromoter?userId=' + userId + "&start=&end=");
        }

    }

    function queryRoomStatis() {
        var clubId = $.trim($("#club_id").text());
        var start = $("#startDate1").val();
        var end = $("#endDate1").val();
        if (start > end) {
            layer.alert('开始日期不能大于结束日期，请重新选择！');
        } else {
            $.get("pre_sale_manage/queryRoomStatisAjax",
                    {
                        clubId: clubId,
                        start: start,
                        end: end
                    },
                    function (data) {
                        $('#tb1').html(data);
                    },
                    'JSON');
        }

    }

    function queryDiamondStatis() {
        var clubId = $.trim($("#club_id").text());
        var start = $("#startDate2").val();
        var end = $("#endDate2").val();
        if (start > end) {
            layer.alert('开始日期不能大于结束日期，请重新选择！');
        } else {
            $.get("pre_sale_manage/queryDiamondStatisAjax",
                    {
                        clubId: clubId,
                        start: start,
                        end: end
                    },
                    function (data) {
                        $('#tb2').html(data);
                    },
                    'JSON');
        }
    }

    function roomStatis_download() {
        var clubId = $.trim($("#club_id").text());
        var start = $("#startDate1").val();
        var end = $("#endDate1").val();

        window.open('pre_sale_manage/roomStatis_download?start=' + start + '&end=' + end + '&clubId=' + clubId);

    }

    function diamondStatis_download() {
        var clubId = $.trim($("#club_id").text());
        var start = $("#startDate2").val();
        var end = $("#endDate2").val();

        window.open('pre_sale_manage/diamondStatis_download?start=' + start + '&end=' + end + '&clubId=' + clubId);
    }

    function memberDetail_download() {
        var promoterId = $.trim($("#promoter_id").text());

        window.open('pre_sale_manage/memberDetail_download?promoterId=' + promoterId);
    }


    function relationDetail_download() {
        var promoterId = $.trim($("#promoter_id").text());

        window.open('pre_sale_manage/relationDetail_download?promoterId=' + promoterId);
    }

    function rebateDetail_download() {
        var promoterId = $.trim($("#promoter_id").text());
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();

        window.open('pre_sale_manage/rebateDetail_download?promoterId=' + promoterId + '&startDate=' + startDate + '&endDate=' + endDate);
    }



    function check_record(obj, userId) {
        var joinClubTime = $.trim($(obj).parent().prev().text());
        var promoterId = $.trim($("#promoter_id").text());
        var title = "成员钻石流水";
        var url = 'pre_sale_manage/queryDiamondChangeAjax?userId=' + userId + '&promoterId=' + promoterId + '&joinClubTime=' + joinClubTime;
        layer_url(title,url,'760px','426px');

    }

    function layer_url(title,url,width,height) {
        $.post(url, {}, function(str){
            layer.open({
                type: 1
                ,title: [title, 'text-align: center; padding-left: 78px; font-size: 16px;']
                ,shade: 0.6 //遮罩透明度
                ,area:[width, height]
                ,shadeClose :true
                ,btnAlign:'c'
                ,anim: 1 //0-6的动画形式，-1不开启
                ,content: str //注意，如果str是object，那么需要字符拼接。
            });
        });
    }

    //按时间查询返钻明细
    function queryRebateDetail() {
        var promoterId = $.trim($("#promoter_id").text());
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        if (startDate > endDate) {
            layer.alert('开始日期不能大于结束日期，请重新选择！');
        } else {
            $.get("pre_sale_manage/query_rebate_detailAjax",
                    {
                        promoterId: promoterId,
                        startDate: startDate,
                        endDate: endDate
                    },
                    function (data) {
                        $('#rebateDetail').html(data);
                    },
                    'JSON');
        }


    }


    //对局统计
    $("#_game_count").click(function () {
        var clubId = $.trim($("#club_id").text());
        $("#game_count_content").load("pre_sale_manage/query_game_count", {clubId: clubId});
    });


    //流水统计
    $("#_economic_count").click(function () {
        var clubId = $.trim($("#club_id").text());
        $("#economic_count_content").load("pre_sale_manage/query_economic_count", {clubId: clubId});
    });


    //成员明细
    $("#_member_detail").click(function () {
        var promoterId = $.trim($("#promoter_id").text());
        $("#member_detail_content").load("pre_sale_manage/query_member_detail", {promoterId: promoterId});
    });

    //关系详情
    $("#_relation_detail").click(function () {
        var promoterId = $.trim($("#promoter_id").text());
        $("#relation_detail_content").load("pre_sale_manage/query_relation_detail", {promoterId: promoterId});
    });

    //返钻明细
    $("#_rebate_detail").click(function () {
        var promoterId = $.trim($("#promoter_id").text());
        $("#rebate_detail_content").load("pre_sale_manage/query_rebate_detail", {promoterId: promoterId});
    });


</script>


<!-- 时间插件 :: 初始化 -->
<script src="http://download.stevengame.com/webResource/common/js/plugins_init/components-datetimepicker-0.0.5.js"></script>
<!-- 时间插件 :: 选择中文语言 -->
<script src="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN-1.0.js"></script>
