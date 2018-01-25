<!--代理查询-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
                <h4 class="center bold">代理查询</h4>
                <div class="caption center-pill">
                </div>
            </div>
            <div class="portlet-body">
                <div style="text-align: center;line-height: 28px; ">
                    <input type="text" placeholder="通过俱乐部ID查询" name="inValue" id="clubId" style="width: 250px;"/>
                    &nbsp;&nbsp;
                    <input type="button" value="查&nbsp;&nbsp;询" onclick="queryClub();" class="btn btn-primary"
                           id="sub"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="text" placeholder="通过游戏Id查询" name="inValue" id="userId" style="width: 250px;"/>
                    &nbsp;&nbsp;
                    <input type="button" value="查&nbsp;&nbsp;询" onclick="queryPromoter();"
                           class="btn btn-primary" id="sub2"/>
                    <br/><br/><br/>
                </div>
                <c:if test="${club!=null}">
                    <div>
                        <table width="100%" align="center">
                            <tr>
                                <td width="50%" align="center">
                                    <table border="1" width="80%" align="center" style="line-height: 30px;" class="tb1">
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
                                            <td>${promoter.id}</td>
                                        </tr>
                                        <tr>
                                            <td>代理商姓名</td>
                                            <td>${promoter.realName}</td>
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
                                            <td>俱乐部ID</td>
                                            <td>${club.id}</td>
                                        </tr>
                                        <tr>
                                            <td>俱乐部人数</td>
                                            <td>${club.peopleNum}</td>
                                        </tr>
                                        <tr>
                                            <td>上线代理商</td>
                                            <td>${upPromoter==null? "无":upPromoter.nickName}</td>
                                        </tr>
                                        <tr>
                                            <td>上线代理商ID</td>
                                            <td>${upPromoter==null? "无":upPromoter.id}</td>
                                        </tr>
                                        <tr>
                                            <td>手机号</td>
                                            <td>
                                                <shiro:hasPermission
                                                        name="sys:cellphone:show">${promoter.cellPhone}</shiro:hasPermission>
                                                <shiro:lacksPermission
                                                        name="sys:cellphone:show">*****</shiro:lacksPermission>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>代理开通人</td>
                                            <td>${promoter.editAdmin}</td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="50%" align="center">
                                    <table width="80%" align="center" style="line-height: 30px;" border="1" class="tb1">
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
                                            <%--  <tr>
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
                                            <td>当前可提取返钻数量</td>
                                            <td>${promoter.rebate}</td>
                                        </tr>
                                        <tr>
                                            <td>累计返钻数量</td>
                                            <td>${promoter.rebateTotal}</td>
                                        </tr>
                                        <tr>
                                            <td>创建时间</td>
                                            <td>
                                                <fmt:formatDate value="${promoter.createTime}"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/>

                                            </td>
                                        </tr>
                                            <%--<tr>--%>
                                            <%--<td>最后登录</td>--%>
                                            <%--<td>${logLogin==null? "暂无数据":logLogin.getColumnValue("createTime")}</td>--%>
                                            <%--</tr>--%>
                                            <%--<tr>--%>
                                            <%--<td>最后登录IP</td>--%>
                                            <%--<td>${logLogin==null? "暂无数据":logLogin.getColumnValue("loginIp")}</td>--%>
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
                            <c:if test="${promoter.pLevel < 2}">
                                <tr>
                                    <td width="50%"><input type="button" value="购买明细" class="btn btn-primary btn-lg"
                                                           onclick="queryPayList();">
                                    </td>
                                    <td width="50%"><input type="button" value="返钻明细" class="btn btn-primary btn-lg"
                                                           onclick="queryWithdraw();">
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${promoter.pLevel==2}">
                                <tr>
                                    <td colspan="2" width="50%" align="center">
                                        <input type="button" value="购买明细" class="btn btn-primary btn-lg"
                                               onclick="queryPayList();">
                                    </td>
                                </tr>
                            </c:if>
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

    <!--购买明细 -->
    <div id="buy" style="display:none;font-size: 16px;">
        <div style="text-align: center">
            <br/>
        </div>
        <div>
            <div class="col-sm-3 ">
                <input type="button" class="btn green btn-outline" value="导出excel" onclick="downLoadBuy()">
            </div>
            <div class="col-sm-4">
                <div class="input-group date form_date" id="date">
                    <input type="text" size="16" class="form-control input-sm" placeholder="开始时间" name="start"
                           id="start"
                           value="${startDate}">
                                                        <span class="input-group-btn">
                                                            <button class="btn default date-set input-sm" type="button">
                                                                <i class="fa fa-calendar"></i>
                                                            </button>
                                                        </span>
                </div>
            </div>
            <div class="col-sm-4 ">
                <div class="input-group date form_date" id="date2">
                    <input type="text" size="16" class="form-control input-sm" placeholder="结束时间" name="end" id="end"
                           value="${endDate}">
                                                        <span class="input-group-btn">
                                                            <button class="btn default date-set input-sm" type="button">
                                                                <i class="fa fa-calendar"></i>
                                                            </button>
                                                        </span>
                </div>
            </div>
            <input type="hidden" id="promoter" value="${promoter.id}">
            <input type="button" value="查&nbsp;&nbsp;询" class="btn btn-primary btn-sm " onclick="queryByDate();">
        </div>
        <br/>
        <form method="post">
            <table border="1" width="100%" style="line-height: 30px;" align="center" class="tb1">
                <tr style="background-color: #959595">
                    <th style="text-align: center">购买金额</th>
                    <th style="text-align: center">支付方式</th>
                    <th style="text-align: center">购买钻石总额</th>
                    <th style="text-align: center">基本钻石</th>
                    <th style="text-align: center">赠予钻石</th>
                    <th style="text-align: center">钻石到账</th>
                    <th style="text-align: center">时间</th>
                </tr>
                <tbody id="tb2">
                <c:forEach items="${list}" var="l">
                    <tr>
                        <td align="center">${l.getColumnValue("price")}</td>
                        <td align="center">
                            <c:if test="${l.getColumnValue('payType')=='1'}">提成金支付</c:if>
                            <c:if test="${l.getColumnValue('payType')=='2'}">微信支付</c:if>
                            <c:if test="${l.getColumnValue('payType')=='3'}">支付宝支付</c:if>
                        </td>
                        <td align="center">${l.getColumnValue('goodNum')+l.getColumnValue('goodGivingNum')}</td>
                        <td align="center">${l.getColumnValue('goodNum')}</td>
                        <td align="center">${l.getColumnValue('goodGivingNum')}</td>
                        <td align="center">
                            <c:if test="${l.getColumnValue('isSuccess')}">是</c:if>
                            <c:if test="${!l.getColumnValue('isSuccess')}">否</c:if>
                        </td>
                        <td align="center">${l.getColumnValue('createTime')}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div style="text-align: center;line-height: 30px;">
                <br/><br/>
                <input type="button" value="确&nbsp;&nbsp;定" name="create" align="center" class="btn btn-success btn-lg"
                       onclick="layer.closeAll();" style="width: 150px;">
            </div>
        </form>
    </div>
    <!--提成明细 -->
    <div id="bonus" style="display:none;font-size: 16px;">
        <br/>
        <div>
            <div class="col-sm-10">
                &nbsp;&nbsp;
                <input type="button" value="按日期" width="150px" id="atDate"
                       style="background-color: #22c6e6;width: 150px;height: 32px;border:0px;"
                       onclick="clickDate();"><input type="button" value="按下属代理商" width="150px" id="atPro"
                                                     onclick="clickPro();"
                                                     style="width: 150px;height: 32px;border:0px;">
            </div>
            <div class="col-sm-2 ">
                <input type="button" class="btn green btn-outline" value="导出excel" onclick="downLoadBonusByDate()"
                       id="btnByDate">
                <input type="button" class="btn green btn-outline" value="导出excel" onclick="downLoadBonusByUnder()"
                       id="btnByUnder" style="display: none;">
            </div>
            <br/><br/>
        </div>
        <table width="100%" border="1" id="tbDate" style="text-align: center;line-height: 30px;" class="tb1">
            <tr style="background-color: #959595">
                <th>提成金额</th>
                <th>累计提成金额</th>
                <th>时间</th>
            </tr>
            <c:forEach items="${totalWeek}" var="l">
                <tr>
                    <td>${l.getColumnValue('weekTotal')!="" ? l.getColumnValue('weekTotal') :"0"}</td>
                    <td>${l.getColumnValue('bonusTotal')!="" ? l.getColumnValue('bonusTotal') :"0"}</td>
                    <td>${l.getColumnValue('startDate')}~${l.getColumnValue('endDate')}</td>
                </tr>
            </c:forEach>
        </table>
        <table width="100%" border="1" id="tbPro" style="text-align: center;display: none;line-height: 30px;"
               class="tb1">
            <tr style="background-color: #959595">
                <th>代理商ID</th>
                <th>代理商昵称</th>
                <th>关系</th>
                <th>充值金额</th>
                <th>计入提成</th>
                <th>成为代理商时间</th>
            </tr>
            <c:forEach items="${bonusList}" var="l">
                <tr>
                    <td>${l.getColumnValue('promoterId')}</td>
                    <td>${l.getColumnValue('nickName')}</td>
                    <td>${l.getColumnValue('relation')}</td>
                    <td>${l.getColumnValue('payTotal')!="" ? l.getColumnValue('payTotal') : "0"}</td>
                    <td>${l.getColumnValue('bonusTotal')!="" ? l.getColumnValue('bonusTotal') : "0"}</td>
                    <td>${l.getColumnValue('createTime')}</td>
                </tr>
            </c:forEach>
        </table>
        <br/><br/><br/>
        <div style="text-align: center;">
            <input type="button" value="确&nbsp;&nbsp;定" name="create" align="center" class="btn btn-success btn-lg"
                   onclick="layer.closeAll();" style="width: 150px;">
        </div>
    </div>
    <!--返钻明细 -->
    <div id="withdraw" style="display:none;font-size: 16px;">
        <div style="text-align: center">
            <br/>
        </div>
        <div>
            <div class="col-sm-3 ">
                <input type="button" class="btn green btn-outline" value="导出excel" onclick="downLoadBonusByDate()">
            </div>
            <div class="col-sm-4">
            </div>
            累计提取返钻数量：<span id="span">${rebateGetTotal}</span>
        </div>
        <br/>
        <form method="post">
            <table border="1" width="100%" style="line-height: 30px;" align="center" class="tb1">
                <tr style="background-color: #959595">
                    <th style="text-align: center" width="50%">提取返钻数量</th>
                    <th style="text-align: center" width="50%">时间</th>
                </tr>
                <tbody id="tb1">
                <c:forEach items="${rebate}" var="l">
                    <tr>
                        <td align="center">${l.getColumnValue("getDiamond")}</td>
                        <td align="center">${l.getColumnValue('createTime')}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div style="text-align: center;line-height: 30px;">
                <br/><br/>
                <input type="button" value="确&nbsp;&nbsp;定" name="create" align="center" class="btn btn-success btn-lg"
                       onclick="layer.closeAll();" style="width: 150px;">
            </div>
        </form>
    </div>
    <!--下线明细 -->
    <div id="under" style="display:none;font-size: 16px;">
        <div>
            <br/>
            <div class="col-sm-3 ">
                <input type="button" class="btn green btn-outline" value="导出excel" onclick="downLoadUnder()">
            </div>
        </div>
        <div style="text-align: center">
            <br/><br/>
            <table border="1" width="100%" style="line-height: 30px;" align="center" class="tb1">
                <tr style="background-color: #959595">
                    <th style="text-align: center">代理商ID</th>
                    <th style="text-align: center">昵称</th>
                    <th style="text-align: center">代理商等级</th>
                    <th style="text-align: center">累计充值</th>
                    <th style="text-align: center">关系</th>
                    <th style="text-align: center">俱乐部</th>
                    <th style="text-align: center">俱乐部ID</th>
                    <th style="text-align: center">俱乐部人数</th>
                </tr>
                <c:forEach items="${under}" var="l">
                    <tr>
                        <td align="center">${l.getColumnValue("promoterId")}</td>
                        <td align="center">${l.getColumnValue("nickName")}</td>
                        <td align="center">
                            <c:if test="${l.getColumnValue('pLevel')==-1}">特级</c:if>
                            <c:if test="${l.getColumnValue('pLevel')==1}">一级</c:if>
                            <c:if test="${l.getColumnValue('pLevel')==2}">二级</c:if>
                        </td>
                        <td align="center">${l.getColumnValue("totalPay")}</td>
                        <td align="center">${l.getColumnValue("relation")}</td>
                        <td align="center">${l.getColumnValue("clubName")}</td>
                        <td align="center">${l.getColumnValue("clubId")}</td>
                        <td align="center">${l.getColumnValue("peopleNum")}</td>
                    </tr>
                </c:forEach>
            </table>
            <div style="text-align: center;line-height: 30px;">
                <br/><br/>
                <input type="button" value="确&nbsp;&nbsp;定" name="create" align="center" class="btn btn-success btn-lg"
                       onclick="layer.closeAll();" style="width: 150px;">
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    function downLoadBuy() {
        var promoterId = '${promoter.id}';
        var start = $('#start').val();
        var end = $('#end').val();
        window.open('check_bill/personalCheck/downLoadBuy?promoterId=' + promoterId + '&start=' + start + '&end=' + end);
    }

    function downLoadBonusByDate() {
        var promoterId = '${promoter.id}';
        var start = $('#startDate').val();
        var end = $('#endDate').val();
        window.open('check_bill/personalCheck/downLoadWithdraw?promoterId=' + promoterId + '&start=' + start + '&end=' + end);
    }

    function downLoadUnder() {
        var promoterId = '${promoter.id}';
        window.open('check_bill/personalCheck/downLoadUnder?promoterId=' + promoterId);
    }

    function downLoadBonusByDate() {
        var promoterId = '${promoter.id}';
        window.open('check_bill/personalCheck/downLoadBonusByDate?promoterId=' + promoterId);
    }

    function downLoadBonusByUnder() {
        var promoterId = '${promoter.id}';
        window.open('check_bill/personalCheck/downLoadBonusByUnder?promoterId=' + promoterId);
    }

    function testNaN(obj) {
        if (isNaN(obj.value)) {
            layer.msg("请输入纯数字ID!");
        }
    }
    function testSubOne() {
        var playId = $.trim($('#clubId').val());
        if (isNaN(playId) || playId == '') {
            $('#sub').attr('disabled', 'true');
        } else {
            $('#sub').removeAttr('disabled');
        }
    }
    function testSubTwo() {
        var playId = $.trim($('#promoterId').val());
        if (isNaN(playId) || playId == '') {
            $('#sub2').attr('disabled', 'true');
        } else {
            $('#sub2').removeAttr('disabled');
        }
    }

    function clickDate() {
        $('#atDate').css('background-color', '#22c6e6');
        $('#atPro').css('background-color', '');
        $('#btnByDate').css('display', 'block');
        $('#btnByUnder').css('display', 'none');
        $('#tbPro').hide();
        $('#tbDate').show();
    }
    function clickPro() {
        $('#atPro').css('background-color', '#22c6e6');
        $('#atDate').css('background-color', '');
        $('#btnByDate').css('display', 'none');
        $('#btnByUnder').css('display', 'block');
        $('#tbDate').hide();
        $('#tbPro').show();
    }

    function queryClub() {
        var clubId = $.trim($("#clubId").val());
        if (isNaN(clubId)) {
            layer.alert('请输入纯数字ID');
        } else if (clubId == '') {
            layer.alert('请输入俱乐部ID');
        } else {
            $('#content').load('check_bill/personalCheck/queryByClub?clubId=' + clubId);
        }
    }

    function queryByDate() {
        var promoterId = $('#promoter').val();
        var start = $('#start').val();
        var end = $('#end').val();
        if (start > end) {
            layer.alert('开始日期不能大于结束日期，请重新选择！');
        } else {
            $.post('check_bill/personalCheck/buyListAjax',
                    {promoterId: promoterId, start: start, end, end},
                    function (data) {
                        $('#tb2').html(data);
                    },
                    'json');
        }
    }

    function queryWithdrawByDate() {
        var promoterId = $('#promoter').val();
        var start = $('#startDate').val();
        var end = $('#endDate').val();
        if (start > end) {
            layer.alert('开始日期不能大于结束日期，请重新选择！');
        } else {
            $.post('check_bill/personalCheck/withdrawListAjax',
                    {promoterId: promoterId, start: start, end: end},
                    function (data) {
                        $('#tb1').html(data);
                    },
                    'json');
        }
    }

    function queryPromoter() {
        var userId = $.trim($("#userId").val());
        if (userId == '') {
            layer.alert('请输入游戏ID');
        } else if (isNaN(userId)) {
            layer.alert('请输入纯数字ID');
        } else {
            $('#content').load('check_bill/personalCheck/queryByPromoter?userId=' + userId);
        }
    }

    function queryPayList() {
        layer.open({
            type: 1,
            closeBtn: 1,
            anim: -1,
            title: ['购买明细', 'text-align:center;font-size:24px;'],
            area: ['800px', '520px'],
            content: $('#buy'),
            cancel: function (index, layero) {
                var cellphone = '${promoter.cellPhone}';
                layer.closeAll();
                $('#content').load('check_bill/personalCheck/queryByPromoter?cellphone=' + cellphone);
            }
        });
    }
    function queryBonusList() {
        layer.open({
            type: 1,
            closeBtn: 1,
            anim: -1,
            title: ['提成明细', 'font-size:24px;text-align:center;'],
            area: ['800px', '520px'],
            content: $('#bonus'),
            cancel: function (index, layero) {
                var cellphone = '${promoter.cellPhone}';
                layer.closeAll();
                $('#content').load('check_bill/personalCheck/queryByPromoter?cellphone=' + cellphone);
            }
        });
    }

    function queryUnder() {
        layer.open({
            type: 1,
            closeBtn: 1,
            anim: -1,
            title: ['下线明细', 'text-align:center;font-size:24px;'],
            area: ['800px', '520px'],
            content: $('#under'),
            cancel: function (index, layero) {
                var cellphone = '${promoter.cellPhone}';
                layer.closeAll();
                $('#content').load('check_bill/personalCheck/queryByPromoter?cellphone=' + cellphone);
            }
        });
    }

    function queryWithdraw() {
        layer.open({
            type: 1,
            closeBtn: 1,
            anim: -1,
            title: ['返钻明细', 'font-size:24px;text-align:center;'],
            area: ['800px', '520px'],
            content: $('#withdraw'),
            cancel: function (index, layero) {
                var cellphone = '${promoter.cellPhone}';
                layer.closeAll();
                $('#content').load('check_bill/personalCheck/queryByPromoter?cellphone=' + cellphone);
            }
        });
    }
    function giveAjax() {
        var changeNum = $.trim($("#giveNum").val());
        var promoterId = $.trim($("#promoter").text());
        var changeBefore = $.trim($("#gameCard").text());
        var changeAfter = parseInt(changeBefore) + parseInt(changeNum);
        $.post("after_sale_manage/promoterHandle/giveGameCard",
                {
                    gameId: 10007,
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
                            area: ['800px', '520px'],
                            content: $("#give3")
                        });
                    } else {
                        layer.alert("赠送失败，请重试！");
                    }
                },
                "text");
    }

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
                    gameId: 10007,
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
                            area: ['800px', '520px'],
                            content: $("#delete3")
                        });
                    } else {
                        layer.alert("删除失败，请重试！");
                    }
                },
                "text");
    }
</script>

<!-- 时间插件 :: 初始化 -->
<script src="http://download.stevengame.com/webResource/common/js/plugins_init/components-datetimepicker-0.0.5.js"></script>
<!-- 时间插件 :: 选择中文语言 -->
<script src="http://download.stevengame.com/webResource/common/vendor/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN-1.0.js"></script>
