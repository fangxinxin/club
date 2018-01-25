<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

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
                <h4 class="center bold">俱乐部操作</h4>
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
                                <td width="40%" align="center">
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
                                            <td>${promoter.realName}</td>
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
                                <td width="40%" align="center">
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
                                <td width="20%" align="center">
                                    <table width="80%">
                                        <tr>
                                            <td><a class="btn btn-primary btn-lg" data-toggle="modal"
                                                   id="_game_count" href="#game_count">
                                                对局统计</a></td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td><a class="btn btn-primary btn-lg" data-toggle="modal"
                                                   id="_economic_count" href="#economic_count">
                                                流水统计</a></td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td><a class="btn btn-primary btn-lg" data-toggle="modal"
                                                   id="_member_detail" href="#member_detail">
                                                成员明细</a></td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td><a class="btn btn-primary btn-lg" data-toggle="modal"
                                                <%-- href="javascript:sellList();" --%> onclick="sellList();">
                                                销售明细</a></td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td><a class="btn btn-primary btn-lg" data-toggle="modal"
                                                   href="#modify_info">
                                                修改信息</a></td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td><a class="btn btn-warning btn-lg" data-toggle="modal"
                                                   onclick="giveCard();">
                                                赠送钻石</a></td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td><a class="btn btn-warning btn-lg" data-toggle="modal"
                                                   onclick="deleteCard();">
                                                删除钻石</a></td>
                                        </tr>
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
                                <c:if test="${gameId!=10039 || gameId!=10007}">
                                    <td width="35%">
                                        <a class="btn btn-warning btn-lg" data-toggle="modal"
                                           onclick="giveClubCard(1);">
                                            俱乐部开房专属钻石赠送</a>
                                    </td>
                                    <td width="35%">
                                        <a class="btn btn-warning btn-lg" data-toggle="modal"
                                           onclick="giveClubCard(2);">
                                            俱乐部开房专属钻石删除</a>
                                    </td>
                                </c:if>

                                <input type="text" hidden="hidden" id="giveType" value="1"/>

                                <td width="50%">
                                    &nbsp;</td>
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
                <%--<div class="scroller mt-lg" data-always-visible="1" data-rail-visible1="1">--%>
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
                        <div class="col-md-1" style="margin-left: -5px;">
                            <input type="submit" value="查&nbsp;&nbsp;询" class="btn btn-primary btn-sm "
                                   onclick="queryRoomStatis()">
                        </div>

                    </div>
                    <br/>
                    <div id="game_count_content" class="col-md-12">

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
                            <input style="margin-left: -15px;margin-top: -2px" type="button" class="btn green btn-outline" value="导出excel"
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
                            <input type="submit" value="查&nbsp;&nbsp;询" class="btn btn-primary btn-sm "
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

<%--赠送钻石--%>
<div id="give" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">赠送钻石</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 30px;" align="center">
            <tr>
                <td>代理商昵称：${promoter.nickName}</td>
                <td>拥有俱乐部：${club.clubName}</td>
            </tr>
            <tr>
                <td>代理商ID：<span id="promoter">${promoter.id}</span></td>
                <td>俱乐部ID：${club.id}</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center" colspan="2" style="line-height: 30px;">
                    请输入赠送数量：
                    <input type="text" name="create" width="200px;" placeholder="请输入赠送数量" id="giveNum"
                           onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="确&nbsp;&nbsp;定" class="btn btn-success btn-lg" onclick="give2();">
                </td>
                <td>
                    <input type="button" value="取&nbsp;&nbsp;消" class="btn btn-warning btn-lg"
                           onclick="cancel();">
                </td>
            </tr>
        </table>
    </form>
</div>
<%--赠送钻石确认--%>
<div id="give2" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">赠送钻石确认</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 30px;" align="center">
            <tr>
                <td>代理商昵称：${promoter.nickName}</td>
                <td>拥有俱乐部：${club.clubName}</td>
            </tr>
            <tr>
                <td>代理商ID：${promoter.id}</td>
                <td>俱乐部ID：${club.id}</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center" colspan="2" style="line-height: 30px;color:red;">
                    将在&lt;${gameName}&gt;赠送
                    &lt;${promoter.realName}&gt;<span id="giveNum2"></span>个钻石，请确认信息是否正确。
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="确&nbsp;&nbsp;定" class="btn btn-success btn-lg" onclick="giveAjax();">
                </td>
                <td>
                    <input type="button" value="取&nbsp;&nbsp;消" class="btn btn-warning btn-lg"
                           onclick="cancel();">
                </td>
            </tr>
        </table>
    </form>
</div>
<%--赠送成功提示--%>
<div id="give3" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">赠送成功提示</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 30px;" align="center">
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td><b>钻石赠送成功</b></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="确&nbsp;&nbsp;定" class="btn btn-warning btn-lg"
                           onclick="cancel();">
                </td>
            </tr>
        </table>
    </form>
</div>
<%--删除钻石--%>
<div id="delete" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">删除钻石</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 30px;" align="center">
            <tr>
                <td>代理商昵称：${promoter.nickName}</td>
                <td>拥有俱乐部：${club.clubName}</td>
            </tr>
            <tr>
                <td>代理商ID：${promoter.id}</td>
                <td>俱乐部ID：${club.id}</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr style="color: deepskyblue;">
                <td colspan="2">当前钻石：${promoter.gameCard}个</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center" colspan="2" style="line-height: 30px;">
                    请输入删除数量：
                    <input type="text" name="create" width="200px;" placeholder="请输入删除数量" id="deleteNum"
                           onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="确&nbsp;&nbsp;定" class="btn btn-success btn-lg" onclick="delete2();">
                </td>
                <td>
                    <input type="button" value="取&nbsp;&nbsp;消" class="btn btn-warning btn-lg"
                           onclick="cancel();">
                </td>
            </tr>
        </table>
    </form>
</div>
<%--删除钻石确认--%>
<div id="delete2" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">删除钻石确认</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 30px;" align="center">
            <tr>
                <td>代理商昵称：${promoter.nickName}</td>
                <td>拥有俱乐部：${club.clubName}</td>
            </tr>
            <tr>
                <td>代理商ID：${promoter.id}</td>
                <td>俱乐部ID：${club.id}</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center" colspan="2" style="line-height: 30px;color:red;">
                    将在&lt;${gameName}&gt;删除
                    &lt;${promoter.realName}&gt;<span id="deleteNum2"></span>个钻石，请确认信息是否正确。
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="确&nbsp;&nbsp;定" class="btn btn-success btn-lg" onclick="deleteAjax();">
                </td>
                <td>
                    <input type="button" value="取&nbsp;&nbsp;消" class="btn btn-warning btn-lg"
                           onclick="cancel();">
                </td>
            </tr>
        </table>
    </form>
</div>
<%--删除成功提示--%>
<div id="delete3" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">删除成功提示</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 30px;" align="center">
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td><b>钻石删除成功</b></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="确&nbsp;&nbsp;定" class="btn btn-warning btn-lg"
                           onclick="cancel();">
                </td>
            </tr>
        </table>
    </form>
</div>

<%--赠送/删除俱乐部开房专属钻石--%>
<div id="giveClubCard" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center"><span class="giveClubCard_str"></span>俱乐部开房专属钻石</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 30px;" align="center">
            <tr>
                <td>代理商昵称：${promoter.nickName}</td>
                <td>拥有俱乐部：${club.clubName}</td>
            </tr>
            <tr>
                <td>代理商ID：<span id="promoter2">${promoter.id}</span></td>
                <td>俱乐部ID：${club.id}</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr id="clubCard_show" style='color: deepskyblue;' hidden="hidden">
                <td colspan='2'>当前俱乐部开房专属钻石：${club.clubCard}颗</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center" colspan="2" style="line-height: 30px;">
                    请输入<span class="giveClubCard_str"></span>数量：
                    <input type="number" name="create" width="200px;" placeholder="请输入钻石数量" id="giveClubNum"
                           onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="确&nbsp;&nbsp;定" class="btn btn-success btn-lg"
                           onclick="giveClubCard2();">
                </td>
                <td>
                    <input type="button" value="取&nbsp;&nbsp;消" class="btn btn-warning btn-lg"
                           onclick="cancel();">
                </td>
            </tr>
        </table>
    </form>
</div>
<%--赠送/删除俱乐部开房专属钻石确认--%>
<div id="giveClubCard2" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">
        <span class="giveClubCard_str"></span>俱乐部开房专属钻石确认
    </div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 30px;" align="center">
            <tr>
                <td>代理商昵称：${promoter.nickName}</td>
                <td>拥有俱乐部：${club.clubName}</td>
            </tr>
            <tr>
                <td>代理商ID：${promoter.id}</td>
                <td>俱乐部ID：${club.id}</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center" colspan="2" style="line-height: 30px;color:red;">
                    将在&lt;${gameName}&gt;<span class="giveClubCard_str"></span>
                    &lt;${promoter.realName}&gt;<span id="giveClubNum2"></span>颗钻石，请确认信息是否正确。
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="确&nbsp;&nbsp;定" class="btn btn-success btn-lg"
                           onclick="giveClubAjax();">
                </td>
                <td>
                    <input type="button" value="取&nbsp;&nbsp;消" class="btn btn-warning btn-lg"
                           onclick="cancel();">
                </td>
            </tr>
        </table>
    </form>
</div>
<%--赠送/删除专属钻石成功提示--%>
<div id="giveClubCard3" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">
        <span id="giveClubCard3_title">提示信息</span>
    </div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 30px;" align="center">
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td><b>俱乐部开房专属钻石<span class="giveClubCard_str"></span>成功</b></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="确&nbsp;&nbsp;定" class="btn btn-warning btn-lg"
                           onclick="cancel();">
                </td>
            </tr>
        </table>
    </form>
</div>

<%--销售明细--%>
<div id="list" style="display:none;font-size: 14px;">
    <div style="text-align: center">
        <br/>
    </div>
    <div>
        <div class="col-sm-3 ">
            <input type="button" class="btn green btn-outline" value="导出excel" onclick="downLoadSellList()">
        </div>
        <div class="col-sm-4">
            <div class="input-group date form_date" id="date">
                <input type="text" size="16" class="form-control input-sm" placeholder="开始时间" name="start"
                       id="startDate" value="${startDate}">
                                                        <span class="input-group-btn">
                                                            <button class="btn default date-set input-sm" type="button">
                                                                <i class="fa fa-calendar"></i>
                                                            </button>
                                                        </span>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="input-group date form_date" id="date2">
                <input type="text" size="16" class="form-control input-sm" placeholder="结束时间" name="end" id="endDate"
                       value="${endDate}">
                                                        <span class="input-group-btn">
                                                            <button class="btn default date-set input-sm" type="button">
                                                                <i class="fa fa-calendar"></i>
                                                            </button>
                                                        </span>
            </div>
        </div>
        <input type="submit" value="查&nbsp;&nbsp;询" class="btn btn-primary btn-sm " onclick="ajaxSellList();">
    </div>
    <br/>
    <form method="post">
        <table border="1" width="100%" style="line-height: 25px;" align="center" class="tb1">
            <tr style="background-color: #959595">
                <th style="text-align: center">赠送对象</th>
                <th style="text-align: center">对象ID</th>
                <th style="text-align: center">赠送数额</th>
                <th style="text-align: center">是否到账</th>
                <th style="text-align: center">补发</th>
                <th style="text-align: center">时间</th>
            </tr>
            <tbody id="tbody">
            <c:forEach items="${allSellList}" var="l">
                <tr>
                    <td align="center">${l.getColumnValue("gameNickName")}</td>
                    <td align="center">${l.getColumnValue("gameUserId")}</td>
                    <td align="center">${l.getColumnValue("sellNum")}</td>
                    <td align="center">
                        <c:if test="${l.getColumnValue('isSuccess')}">已到账</c:if>
                        <c:if test="${!l.getColumnValue('isSuccess')}">未到账</c:if>
                    </td>
                    <td align="center">
                        <c:if test="${l.getColumnValue('isSuccess')}">&nbsp;</c:if>
                        <c:if test="${!l.getColumnValue('isSuccess')}">
                            <input type="hidden" name="id" value="${l.getColumnValue('id')}">
                            <input type="button" value="补发" class="btn btn-warning btn-xs" onclick="reissueOk(this);">
                            <input type="hidden" name="id" value="${l.getColumnValue('gameNickName')}">
                            <input type="hidden" name="id" value="${l.getColumnValue('sellNum')}">
                        </c:if>
                    </td>
                    <td align="center">${l.getColumnValue("createTime")}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${not empty allSellList}">
            <div class="center mt-md">
                <br/><br/>
                <input type="button" value="确&nbsp;&nbsp;定" name="create" align="center" class="btn btn-success btn-lg"
                       onclick="layer.closeAll();">
            </div>
        </c:if>
    </form>
</div>
<%--补发钻石确认--%>
<div id="reissue" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">补发钻石确认</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 30px;" align="center">
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center" colspan="2" style="line-height: 30px;color:red;">
                    将在&lt;${gameName}&gt;补发
                    &lt;<span id="spName"></span>&gt;<span id="spNum"></span>个钻石，请确认信息是否正确。
                    <input type="hidden" id="spId">
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="确定" class="btn btn-success btn-lg" onclick="reissue2();">
                </td>
                <td>
                    <input type="button" value="取消" class="btn btn-warning btn-lg" onclick="layer.close(layer.index);">
                </td>
            </tr>
        </table>
    </form>
</div>
<%--补发成功提示--%>
<div id="reissue2" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">补发成功提示</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 30px;" align="center">
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td><b>钻石补发成功</b></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <input type="button" value="确定" class="btn btn-warning btn-lg"
                           onclick="queryByDate();">
                </td>
            </tr>
        </table>
    </form>
</div>

<%--修改信息--%>
<div id="modify_info" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog" style="width: 700px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">修改信息</h4>
            </div>
            <div class="modal-body">
                <div class="scroller" style="height:500px" data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12 mt-lg">
                            <label>俱乐部昵称：<span id="clubName">${club.clubName}</span></label><br/>
                            <label>修改昵称：&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="text" placeholder="请输入新的俱乐部昵称"
                                                                               id="newClubName">&nbsp;&nbsp;
                            <button class="btn green" onclick="changeClubName();">确认修改</button>
                            <br/>
                            <br/>
                            <label>代理商手机号：<span id="cellphone">${promoter.cellPhone}</span></label><br/>
                            <label>修改手机号：</label><input type="text" placeholder="请输入新的手机号码" id="newCellphone">&nbsp;&nbsp;
                            <button class="btn green" onclick="changeCellphone();">确认修改</button>
                            <br/>
                            <br/>
                            <label>代理商姓名：<span id="realName">${promoter.realName}</span></label><br/>
                            <label>修改姓名：&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="text" placeholder="请输入新的代理商姓名"
                                                                               id="newRealName">&nbsp;&nbsp;
                            <button class="btn green" onclick="changeRealName();">确认修改</button>
                            <br/>
                            <br/>
                            <label>重置代理商密码</label><br/>
                            <label>修改密码：&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="text" placeholder="请输入新的代理商密码"
                                                                               id="password">&nbsp;&nbsp;
                            <button class="btn green" onclick="changePass();">确认修改</button>
                            <br/>
                            <br/>
                            <label>代理商备注：<span id="remark">${promoter.remark}</span></label><br/>
                            <label>修改备注：&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="text" placeholder="请输入新的代理商备注"
                                                                               id="newRemark">&nbsp;&nbsp;
                            <button class="btn green" onclick="changeRemark();">确认修改</button>
                            <br/>
                            <br/>
                            <label>员工微信号：<span id="staffWechat">${promoter.staffWechat}</span></label><br/>
                            <label>修改微信：&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="text" placeholder="请输入新的员工微信号"
                                                                               id="newStaffWechat">&nbsp;&nbsp;
                            <button class="btn green" onclick="changeWechat();">确认修改</button>
                            <br/>
                            <br/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer center">
                <%--<button id="save" data-dismiss="modal" onclick="" type="button" class="btn green"> 确认修改 </button>--%>
                <%--<button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>--%>
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 确&nbsp;&nbsp;定</button>
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
                        <table width="100%">
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
                                    <input style="margin-left: 20px;margin-bottom: 5px" type="button"
                                           class="btn green btn-outline" value="导出excel"
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
                                           id="startDate_f" value="${startDate}">
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
                                           id="endDate_f" value="${endDate}">
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

    <%--修改手机号码--%>
    function changeCellphone() {
        var cellphone = $('#cellphone').text();
        var newCellphone = $('#newCellphone').val();
        var mobile = /^1(3|4|5|6|7|8|9)\d{9}$/;
        if (!mobile.test(newCellphone)) {
            layer.alert("请输入正确的手机号!");
        } else {
            $.post("after_sale_manage/promoterHandle/updateCellphone",
                    {
                        cellphone: cellphone,
                        newCellphone: newCellphone
                    },
                    function (data) {
                        if (data == 'OK') {
                            $('#cellphone').text(newCellphone);
                            $('#newCellphone').val('');
                            layer.open({
                                title: '修改信息成功提示'
                                , content: '手机号已改为：' + newCellphone
                            });
                        } else {
                            layer.alert("修改失败，请重试！");
                        }
                    },
                    "text");
        }

    }

    <%--修改代理商真实姓名--%>
    function changeRealName() {
        var cellphone = $('#cellphone').text();
        var newRealName = $('#newRealName').val();
        if (newRealName == '' || newRealName == null) {
            layer.alert("姓名不能为空！");
            return;
        }
        $.post("after_sale_manage/promoterHandle/updateRealName",
                {
                    cellphone: cellphone,
                    realName: newRealName
                },
                function (data) {
                    if (data == 'OK') {
                        $('#realName').text(newRealName);
                        $('#newRealName').val('');
                        layer.open({
                            title: '修改信息成功提示'
                            , content: '代理商姓名已改为：' + newRealName
                        });
                    } else {
                        layer.alert("修改失败，请重试！");
                    }
                },
                "text");
    }

    <%--修改密码--%>
    function changePass() {
        var cellphone = $('#cellphone').text();
        var password = $('#password').val();
        if (password == '' || password == null) {
            layer.alert("密码不能为空！");
            return;
        }
        $.post("after_sale_manage/promoterHandle/updatePass",
                {
                    cellphone: cellphone,
                    password: password
                },
                function (data) {
                    if (data == 'OK') {
                        $('#password').val('');
                        layer.open({
                            title: '修改信息成功提示'
                            , content: '代理商密码已改为：' + password
                        });
                    } else {
                        layer.alert("修改失败，请重试！");
                    }
                },
                "text");
    }


    <%--修改俱乐部名称--%>
    function changeClubName() {
        var clubId = '${club.id}';
        var newClubName = $('#newClubName').val();
        if (newClubName == '' || newClubName == null) {
            layer.alert("密码不能为空！");
            return;
        }
        $.post("after_sale_manage/promoterHandle/updateClubName",
                {
                    clubId: clubId,
                    clubName: newClubName
                },
                function (data) {
                    if (data == 'OK') {
                        $('#clubName').text(newClubName);
                        $('#newClubName').val('');
                        layer.open({
                            title: '修改信息成功提示'
                            , content: '俱乐部名称已成功修改为：' + newClubName
                        });
                    } else {
                        layer.alert("修改失败，请重试！");
                    }
                },
                "text");
    }

    <%--修改员工微信号--%>
    function changeWechat() {
        var promoterId = '${club.promoterId}';
        var staffWechat = $('#newStaffWechat').val();
        if (staffWechat == '' || staffWechat == null) {
            layer.alert("微信号不能为空！");
            return;
        }
        $.post("after_sale_manage/promoterHandle/updateWechat",
                {
                    promoterId: promoterId,
                    staffWechat: staffWechat
                },
                function (data) {
                    if (data == 'OK') {
                        $('#staffWechat').text(staffWechat);
                        $('#newStaffWechat').val('');
                        layer.open({
                            title: '修改信息成功提示'
                            , content: '员工微信号已成功修改为：' + staffWechat
                        });
                    } else {
                        layer.alert("修改失败，请重试！");
                    }
                },
                "text");
    }

    <%--修改备注--%>
    function changeRemark() {
        var promoterId = '${club.promoterId}';
        var remark = $('#newRemark').val();
        if (remark == '' || remark == null) {
            layer.alert("备注不能为空！");
            return;
        }
        $.post("after_sale_manage/promoterHandle/updateRecord",
                {
                    promoterId: promoterId,
                    remark: remark
                },
                function (data) {
                    if (data == 'OK') {
                        $('#remark').text(remark);
                        $('#newRemark').val('');
                        layer.open({
                            title: '修改信息成功提示'
                            , content: '备注已成功修改为：' + remark
                        });
                    } else {
                        layer.alert("修改失败，请重试！");
                    }
                },
                "text");
    }

    function downLoadSellList() {
        var promoterId = '${promoter.id}';
        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();
        window.open('after_sale_manage/promoterHandle/downLoadSellList?promoterId=' + promoterId + '&startDate=' + startDate + '&endDate=' + endDate);
    }

    function queryClub() {
        var clubId = $.trim($("#clubId").val());
        if (clubId == '') {
            layer.alert('请输入俱乐部ID');
        } else if (isNaN(clubId)) {
            layer.alert('请输入纯数字ID');
        } else {
            $('#content').load('after_sale_manage/promoterHandle/queryByClub?clubId=' + clubId);
        }
    }

    function queryPromoter() {
        var userId = $.trim($("#userId").val());
        if (userId == '') {
            layer.alert('请输入游戏ID');
        } else if (isNaN(userId)) {
            layer.alert('请输入纯数字ID');
        } else {
            $('#content').load('after_sale_manage/promoterHandle/queryByPromoter?userId=' + userId + "&start=&end=");
        }

    }

    function queryRoomStatis() {
        var clubId = $.trim($("#club_id").text());
        var start = $("#startDate1").val();
        var end = $("#endDate1").val();
        if (start > end) {
            layer.alert('开始日期不能大于结束日期，请重新选择！');
        } else {
            $.get("after_sale_manage/promoterHandle/queryRoomStatisAjax",
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
            $.get("after_sale_manage/promoterHandle/queryDiamondStatisAjax",
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

        window.open('after_sale_manage/promoterHandle/roomStatis_download?start=' + start + '&end=' + end + '&clubId=' + clubId);

    }

    function diamondStatis_download() {
        var clubId = $.trim($("#club_id").text());
        var start = $("#startDate2").val();
        var end = $("#endDate2").val();

        window.open('after_sale_manage/promoterHandle/diamondStatis_download?start=' + start + '&end=' + end + '&clubId=' + clubId);
    }

    function memberDetail_download() {
        var promoterId = $.trim($("#promoter_id").text());

        window.open('after_sale_manage/promoterHandle/memberDetail_download?promoterId=' + promoterId);
    }


    function check_record(obj, userId) {
        var joinClubTime = $.trim($(obj).parent().prev().text());
        var promoterId = $.trim($("#promoter_id").text());
        var title = "成员钻石流水";
        var url = 'after_sale_manage/promoterHandle/queryDiamondChangeAjax?userId=' + userId + '&promoterId=' + promoterId + '&joinClubTime=' + joinClubTime;
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



    function queryByDate() {
        var cellphone = '${promoter.cellPhone}';
        var start = $("#startDate").val();
        var end = $("#endDate").val();
        if (start > end) {
            layer.alert('开始日期不能大于结束日期，请重新选择！');
        } else {
            layer.closeAll();
            $('#content').load('after_sale_manage/promoterHandle/queryByPromoter?cellphone=' + cellphone + '&start=' + start + '&end=' + end);
        }
    }

    <%--按日期查询销售明细--%>
    function ajaxSellList() {
        var promoterId = $.trim($("#promoter").text());
        var start = $("#startDate").val();
        var end = $("#endDate").val();
        if (start > end) {
            layer.alert('开始日期不能大于结束日期，请重新选择！');
        } else {
            $.post("after_sale_manage/promoterHandle/ajaxSellList",
                    {
                        promoterId: promoterId,
                        startDate: start,
                        endDate: end
                    },
                    function (data) {
                        $('#tbody').html(data);
                    });
        }
    }
    <%--显示销售明细--%>
    function sellList() {
        layer.open({
            type: 1,
            closeBtn: 1,
            anim: -1,
            title: ['销售明细', 'text-align:center;font-size:24px;'],
            area: ['800px', '520px'],
            content: $("#list"),
            cancel: function (index, layero) {
                var promoterId = $.trim($("#promoter").text());
                layer.closeAll();
            }
        });
    }
    function giveCard() {
        layer.open({
            type: 1,
            closeBtn: 0,
            anim: -1,
            title: '',
            area: ['800px', '520px'],
            content: $("#give")
        });
    }
    function deleteCard() {
        layer.open({
            type: 1,
            closeBtn: 0,
            anim: -1,
            title: '',
            area: ['800px', '520px'],
            content: $("#delete")
        });
    }
    function give2() {
        var giveNum = $("#giveNum").val();
        if (parseInt(giveNum) == 0) {
            layer.alert("赠送钻石数必须大于0！");
        } else if (giveNum == '') {
            layer.alert("请输入赠送钻石数！");
        } else {
            $("#giveNum2").text(parseInt(giveNum));
            layer.open({
                type: 1,
                closeBtn: 0,
                anim: -1,
                title: '',
                area: ['800px', '520px'],
                content: $("#give2")
            });
        }
    }
    function delete2() {
        var changeBefore = parseInt(${promoter.gameCard});
        var deleteNum = $.trim($('#deleteNum').val());
        if (deleteNum == 0) {
            layer.alert("删除钻石数必须大于0！");
        } else if (deleteNum == '') {
            layer.alert("请输入删除钻石数！");
        } else if (changeBefore < parseInt(deleteNum)) {
            layer.alert("删除钻石数不得大于当前钻石数！");
        } else {
            $("#deleteNum2").text(parseInt(deleteNum));
            layer.open({
                type: 1,
                closeBtn: 0,
                anim: -1,
                title: '',
                area: ['800px', '520px'],
                content: $("#delete2")
            });
        }
    }
    <%--执行赠送房卡--%>
    function giveAjax() {
        var changeNum = $.trim($("#giveNum").val());
        var promoterId = $.trim($("#promoter").text());
        var changeBefore = $.trim($("#gameCard").text());
        var changeAfter = parseInt(changeBefore) + parseInt(changeNum);
        $.post("after_sale_manage/promoterHandle/giveGameCard",
                {
                    promoterId: promoterId,
                    changeNum: changeNum,
                    changeBefore: changeBefore,
                    changeAfter: changeAfter
                },
                function (data) {
                    if (data == 'YES') {
                        layer.open({
                            type: 1,
                            closeBtn: 0,
                            anim: -1,
                            title: '',
                            shadeClose: true,
                            area: ['800px', '520px'],
                            content: $("#give3")
                        });
                    } else {
                        layer.alert("赠送失败，请重试！");
                    }
                },
                "text");
    }
    <%--执行删除房卡--%>
    function deleteAjax() {
        var adminId = $.trim($("#adminId").val());
        var adminName = $.trim($("#adminName").val());
        var changeNum = "-" + $.trim($("#deleteNum").val());
        var promoterId = $.trim($("#promoter").text());
        var changeBefore = $.trim($("#gameCard").text());
        var changeAfter = parseInt(changeBefore) + parseInt(changeNum);
        $.post("after_sale_manage/promoterHandle/giveGameCard",
                {
                    adminId: adminId,
                    adminName: adminName,
                    promoterId: promoterId,
                    changeNum: changeNum,
                    changeBefore: changeBefore,
                    changeAfter: changeAfter
                },
                function (data) {
                    if (data == 'YES') {
                        layer.open({
                            type: 1,
                            closeBtn: 0,
                            anim: -1,
                            title: '',
                            shadeClose: true,
                            area: ['800px', '520px'],
                            content: $("#delete3")
                        });
                    } else {
                        layer.alert("删除失败，请重试！");
                    }
                },
                "text");
    }
    function cancel() {
        layer.closeAll();
        $('#content').load('after_sale_manage/promoterHandle/queryByPromoter?userId=' + '${promoter.gameUserId}');
    }
    <%--补发确认--%>
    function reissueOk(obj) {
        var id = $(obj).prev().val();
        var gameNickName = $(obj).next().val();
        var sellNum = $(obj).next().next().val();
        $('#spName').text(gameNickName);
        $('#spNum').text(sellNum);
        $('#spId').val(id);
        layer.open({
            type: 1,
            closeBtn: 0,
            anim: -1,
            title: '',
            shadeClose: true,
            area: ['800px', '520px'],
            content: $("#reissue")
        });
    }
    <%--执行补发--%>
    function reissue2() {
        var id = $('#spId').val();
        $.post('after_sale_manage/promoterHandle/reissue',
                {id: id},
                function (data) {
                    if (data == 'OK') {
                        layer.open({
                            type: 1,
                            closeBtn: 0,
                            anim: -1,
                            title: '',
                            area: ['800px', '520px'],
                            content: $("#reissue2")
                        });
                    } else {
                        layer.msg('补发失败，请刷新重试！');
                    }
                }, 'text');
    }


</script>

<%-- 赠送/删除俱乐部专属房卡 --%>
<script>

    function giveClubCard(type) {
        $('#giveType').val(parseInt(type));

        var str = '';
        if (type == 1) {
            str = '赠送';
        }
        else {
            str = '删除';
            $('#clubCard_show').show();
        }
        $(".giveClubCard_str").text(str);

        layer.open({
            type: 1,
            closeBtn: 0,
            anim: -1,
            title: '',
            shadeClose: true,
            area: ['800px', '520px'],
            content: $("#giveClubCard")
        });
    }
    function giveClubCard2() {
        var type = $('#giveType').val();
        var changeNum = $("#giveClubNum").val();
        var changeBefore = '${club.clubCard}';

        if (parseInt(giveClubNum) <= 0) {
            layer.alert("输入数值必须大于0！");
        }
        else if (giveClubNum == '') {
            layer.alert("输入钻石数不能为空！");
        }
        else if (type == 2 && changeBefore - changeNum < 0) {
            $("#giveClubNum").val('${club.clubCard}');
            layer.alert("删除钻石数不能超过俱乐部开房专属钻石数量");
            return;
        }
        else {
            $("#giveClubNum2").text(parseInt(changeNum));
            layer.open({
                type: 1,
                closeBtn: 0,
                anim: -1,
                title: '',
                shadeClose: true,
                area: ['800px', '520px'],
                content: $("#giveClubCard2")
            });
        }
    }
    function giveClubAjax() {
        var type = $('#giveType').val();
        var changeBefore = '${club.clubCard}';
        var changeNum = $.trim($("#giveClubNum").val());
        var promoterId = $.trim($("#promoter2").text());

        $.post("after_sale_manage/promoterHandle/giveClubGameCard",
                {
                    type: parseInt(type),
                    promoterId: promoterId,
                    changeNum: changeNum,
                    changeBefore: parseInt(changeBefore)
                },
                function (data) {
                    if (data == 'YES') {
                        layer.open({
                            type: 1,
                            closeBtn: 0,
                            anim: -1,
                            title: '',
                            shadeClose: true,
                            area: ['800px', '520px'],
                            content: $("#giveClubCard3")
                        });
                    } else {
                        layer.alert("操作失败，请重试！");
                    }
                },
                "text");
    }

    function relationDetail_download() {
        var promoterId = $.trim($("#promoter_id").text());

        window.open('pre_sale_manage/relationDetail_download?promoterId=' + promoterId);
    }

    function rebateDetail_download() {
        var promoterId = $.trim($("#promoter_id").text());
        var startDate = $("#startDate_f").val();
        var endDate = $("#endDate_f").val();

        window.open('pre_sale_manage/rebateDetail_download?promoterId=' + promoterId + '&startDate=' + startDate + '&endDate=' + endDate);
    }

    //按时间查询返钻明细
    function queryRebateDetail() {
        var promoterId = $.trim($("#promoter_id").text());
        var startDate = $("#startDate_f").val();
        var endDate = $("#endDate_f").val();
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
