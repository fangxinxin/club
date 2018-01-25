<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<style type="text/css">
    .tb1 tr {
        background: #f1f1f1;
    }

    .tb1 tr:nth-child(even) {
        background: #ccc;
    }

    td {
        align: center;
    }
</style>
<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">解散俱乐部</h4>
            </div>
            <div class="portlet-body">
                <div style="text-align: center;line-height: 28px;;">
                    <input type="text" placeholder="请输入需要解散的俱乐部ID" name="inValue" id="inValue" style="width: 250px;"/>
                    &nbsp;&nbsp;
                    <input type="button" value="查询" onclick="query();" class="btn btn-primary" style="width: 100px;"
                           id="sub"/>
                </div>
                <c:if test="${club!=null}">
                    <div class="tabbable tabbable-tabdrop">
                        <table border="0" width="80%" style="line-height: 35px;" align="right">
                            <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td>玩家昵称：${promoter.nickName}</td>
                                <td>俱乐部昵称：${club.clubName}</td>
                            </tr>
                            <tr>
                                <td>玩家ID：${promoter.gameUserId}</td>
                                <td>所在俱乐部ID：<span id="clubId">${club.id}</span></td>
                            </tr>
                            <tr>
                                <td>当前钻石数：${promoter.gameCard}</td>
                                <td>俱乐部人数：${club.peopleNum}
                                </td>
                            </tr>
                            <tr>
                                <td>创建日期：<fmt:formatDate value="${club.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td>
                                    代理商权限：
                                    <c:if test="${promoter.pLevel==-1}">特级代理商</c:if>
                                    <c:if test="${promoter.pLevel==1}">一级代理商</c:if>
                                    <c:if test="${promoter.pLevel==2}">二级代理商</c:if>
                                </td>
                            </tr>
                            <tr>
                                <%--<td>最后登录：--%>
                                    <%--<c:if test="${logLogin!=null}">${logLogin.getColumnValue('createTime')}</c:if>--%>
                                    <%--<c:if test="${logLogin==null}">暂无数据</c:if>--%>
                                <%--</td>--%>
                                <td>
                                    账号是否封停：
                                    <c:if test="${promoter.loginStatus==2}">是</c:if>
                                    <c:if test="${promoter.loginStatus!=2}">否</c:if>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td><input type="button" value="解散俱乐部" width="200px;" onclick="flush();"
                                           class="btn btn-success btn-lg">
                                </td>
                                <td><input type="button" value="俱乐部成员详情" width="200px;" class="btn btn-success btn-lg"
                                           onclick="info();">
                                </td>
                            </tr>


                        </table>

                    </div>
                </c:if>
                <c:if test="${isEmpty=='empty'}">
                    <div style="text-align: center;">
                        <br/><br/><br/>
                        <h3 style="color: red;">该俱乐部ID不存在，请重新输入</h3>
                    </div>
                </c:if>
                <c:if test="${clubStatus==0}">
                    <div style="text-align: center;">
                        <br/><br/><br/>
                        <h3 style="color: red;">该俱乐部正在转正考核中，无法解散</h3>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>


<!--解散俱乐部确认-->
<div id="div1" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">解散俱乐部确认</div>
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
                <td>
                    代理商级别：
                    <c:if test="${promoter.pLevel==-1}">特级代理商</c:if>
                    <c:if test="${promoter.pLevel==1}">一级代理商</c:if>
                    <c:if test="${promoter.pLevel==2}">二级代理商</c:if>
                </td>
                <td>
                    拥有俱乐部人数：${club.peopleNum}
                </td>
            </tr>
            <tr>
                <td>后台剩余钻石数：${promoter.gameCard}</td>
                <td>上线代理商：
                    <c:if test="${upPromoter==null}">-</c:if>
                    <c:if test="${upPromoter!=null}">${upPromoter.nickName}</c:if>
                </td>
            </tr>
            <tr>
                <td>累计充值金额：${promoter.totalPay}元</td>
                <td>上线代理商ID：
                    <c:if test="${promoter.parentId=='0'}">-</c:if>
                    <c:if test="${promoter.parentId!='0'}">${promoter.parentId}</c:if>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2" style="color: red;font-size: 17px;">
                    将在&lt;${gameName}&gt;解散&lt;${promoter.realName}&gt;的俱乐部成员，请确认信息是否正确
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center"><input type="button" value="确&nbsp;&nbsp;定" name="create" class="btn btn-lg"
                                          style="width: 150px;background-color: #00cc99;"
                                          onclick="ajax();">
                </td>
                <td align="center"><input type="button" value="取&nbsp;&nbsp;消" name="create"
                                          style="width: 150px;background-color: #cccccc;"
                                          class="btn btn-lg"
                                          onclick="layer.closeAll();">
                </td>
            </tr>
        </table>
    </form>
</div>
<!--解散俱乐部成功！-->
<div id="div2" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center">解散俱乐部成功</div>
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
                <td align="center">已解散俱乐部：${club.clubName}</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center">俱乐部ID：${club.id}</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td style="color: #22c6e6;" align="center">
                    &lt;${promoter.realName}&gt;俱乐部玩家可重新选择加入俱乐部
                </td>
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
                <td align="center"><input type="button" value="确定" name="create" style="width: 150px;"
                                          class="btn btn-success btn-lg"
                                          onclick="layer.closeAll();$('#content').load('after_sale_manage/flush');">
                </td>
            </tr>
        </table>
    </form>
</div>
<!--俱乐部详情-->
<div id="div3" style="display:none;font-size: 16px;">
    <br/>
    <div style="text-align: center">俱乐部详情</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="1" width="100%" style="line-height: 30px;" align="center" class="tb1">
            <tr style="background-color: #959595">
                <th style="text-align: center">玩家ID</th>
                <th style="text-align: center">玩家昵称</th>
                <th style="text-align: center">加入时间</th>
                <th style="text-align: center">最后上线</th>
                <th style="text-align: center">累计购买钻石</th>
                <th style="text-align: center">剩余钻石</th>
            </tr>
            <c:forEach items="${list}" var="l">
                <tr>
                    <td align="center">${l.getColumnValue("gameUserId")}</td>
                    <td align="center">${l.getColumnValue("gameNickName")}</td>
                    <td align="center">
                            ${l.getColumnValue('createTime')}
                    </td>
                    <td align="center">${l.getColumnValue('loginTime')=="" ? "暂无数据":l.getColumnValue('loginTime')}</td>
                    <td align="center">
                        <c:if test="${l.getColumnValue('totalPay')==''}">0</c:if>
                        <c:if test="${l.getColumnValue('totalPay')!=''}">${l.getColumnValue('totalPay')}</c:if>
                    </td>
                    <td align="center">${l.getColumnValue("privateRoomDiamond")}</td>
                </tr>
            </c:forEach>
        </table>
        <div style="text-align: center;line-height: 30px;">
            <br/>
            <input type="button" value="确&nbsp;&nbsp;定" name="create" align="center" class="btn btn-success btn-lg"
                   onclick="layer.closeAll();">
        </div>
    </form>
</div>
<script>

    function flush() {
        $("#spName").text($.trim($("#inRealName").val()));
        $("#spName2").text($.trim($("#inRealName").val()));
        $("#spName3").text($.trim($("#inRealName").val()));
        $("#spPhone").text($.trim($("#inCellPhone").val()));
        layer.open({
            type: 1,
            closeBtn: 0,
            anim: -1,
            title: '',
            area: ['800px', '520px'],
            content: $("#div1")
        });
    }
    function info() {
        layer.open({
            type: 1,
            closeBtn: 0,
            anim: -1,
            title: '',
            area: ['800px', '520px'],
            content: $("#div3")
        });
    }
    function query() {
        var inValue = $.trim($("#inValue").val());
        if (inValue == '') {
            layer.alert('请输入俱乐部ID');
        } else if (isNaN(inValue)) {
            layer.alert('请输入纯数字ID');
        } else {
            $('#content').load('after_sale_manage/queryClub?inValue=' + inValue);
        }
    }
    function ajax() {
        var clubId = $.trim($("#clubId").text());
        $.post("after_sale_manage/flushUser",
                {
                    clubId: clubId
                },
                function (data) {
                    if (data) {
                        layer.open({
                            type: 1,
                            closeBtn: 0,
                            anim: -1,
                            title: '',
                            area: ['800px', '520px'],
                            content: $("#div2")
                        });
                    } else {
                        layer.alert("操作失败，请重试！");
                    }
                },
                "text");
    }
</script>

