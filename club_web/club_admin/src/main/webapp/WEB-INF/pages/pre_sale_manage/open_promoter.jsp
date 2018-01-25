<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<style type="text/css">
    select {
        height: 28px;
        border-radius: 5px;
        border-color: lightseagreen;
        width: 160px;
    }

    .tb1 tr {
        background: #f1f1f1;
    }

    .tb1 tr:nth-child(even) {
        background: #e6e6e6;
    }

    .tb1 td, th {
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
                <h4 class="center bold">开通代理</h4>
                <div class="caption center-pill">
                </div>
            </div>
            <div class="portlet-body">
                <div style="text-align: center;line-height: 28px;">
                    <input type="text" placeholder="请输入需要开通代理的玩家ID" name="gameUserId" id="playId"
                           style="width: 250px;"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" value="查询" onclick="query();" class="btn btn-primary" style="width: 100px;"
                           id="sub"/>
                    <br/>
                    <c:if test="${not empty dtPro}">
                        <div style="text-align: center;">
                            <br/><br/><br/>
                            <h3 style="color: red;">该玩家已经是代理商</h3>
                        </div>
                    </c:if>
                    <c:if test="${dtLength=='0'}">
                        <div style="text-align: center;">
                            <br/><br/><br/>
                            <h3 style="color: red;">该玩家ID不存在，请重新输入</h3>
                        </div>
                    </c:if>
                    <br/>
                    <c:if test="${not empty gameUserId}">
                        <div class="row">
                            <div style="margin: auto">
                                <table class="table table-hover table-striped table-bordered tb2" width="100%">
                                    <tr>
                                        <td width="25%">玩家昵称：</td>
                                        <td width="25%">
                                            <span id="gameNickName">${gameNickName}</span>
                                        <td width="25%">玩家ID：</td>
                                        <td width="25%">${gameUserId}</td>
                                    </tr>
                                    <tr>
                                        <td>当前钻石数：</td>
                                        <td>${diamond}</td>
                                    </tr>
                                    <tr>
                                        <td>登记手机号：&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                        <td><input type="text" placeholder="请输入代理商手机号" maxlength="11"
                                                   id="inCellPhone"></td>
                                        <td>代理商等级:</td>
                                        <td>
                                            <select id="se1">
                                                <option value="2">二级代理商</option>
                                                <option value="1">一级代理商</option>
                                                <option value="-1">特级代理商</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>推荐人（选填）：</td>
                                        <td>
                                            <input type="text" placeholder="请输入推荐人俱乐部Id" id="parentClubId">
                                        </td>
                                        <td>代理商姓名：</td>
                                        <td><input type="text" placeholder="请输入代理商真实姓名" id="inRealName"></td>
                                    </tr>
                                    <tr>
                                        <td>客服微信号：</td>
                                        <td>
                                            <input type="text" placeholder="请输入客服微信号" maxlength="11"
                                                   id="staffWechat">
                                        </td>
                                        <td>备注：</td>
                                        <td><input type="text" placeholder="请输入备注" id="remark"></td>
                                    </tr>
                                    </tr>
                                </table>

                            </div>
                            <div style="width: 100%;text-align: center;line-height: 35px;">
                                <br/>
                                <input type="button" value="开通代理"
                                       style="background-color: #2acd9a;width:200px;"
                                       onclick="create('fake');" class="btn btn-lg">
                                <shiro:hasAnyRoles name="yunyingzongjian,admin">
                                    <input type="button" value="开通正式代理"
                                           style="background-color: #2acd9a;width:200px;"
                                           onclick="create('formal');" class="btn btn-lg">
                                </shiro:hasAnyRoles>

                                <c:if test="${not empty clubList}">
                                    <input type="button" value="俱乐部清单"
                                           style="background-color: #0099cc;width:200px;"
                                           onclick="openDetail();" class="btn btn-lg">
                                </c:if>
                            </div>

                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

<!--代理开通确认 -->
<div id="div1" style="display:none;font-size: 18px;">
    <br/>
    <div style="text-align: center"><h3>代理开通确认</h3></div>
    <input hidden id="promoter_type">
    <hr/>
    <form method="post">
        <table border="0" width="80%" style="line-height: 33px;" align="right">
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;
                    <input type="hidden" id="pLevel3">
                </td>
            </tr>
            <tr>
                <td>玩家昵称：${gameNickName}</td>
                <td>推荐人俱乐部ID：<span id="spClub"></span></td>
            </tr>
            <tr>
                <td>玩家ID：${gameUserId}</td>
                <td>
                    推荐人代理商ID：<span id="upPromoterId"></span>
                </td>
            </tr>
            <tr>
                <td>开通：<span id="pLevel">二级代理商</span></td>
                <td>
                    手机号：<span id="spPhone"></span>
                </td>
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
                <td colspan="2" style="color: #22c6e6;">
                    将在&lt;${gameName}&gt;开通&lt;<span id="spName3"></span>&gt;的代理商身份，请确认信息是否正确
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
        </table>
        <table width="100%">
            <tr>
                <td style="text-align: right;"><input type="button" value="确&nbsp;&nbsp;定"
                                                      class="btn btn-success btn-lg"
                                                      style="width: 120px;" onclick="ajax();">
                </td>
                <td align="center"><input type="button" value="取&nbsp;&nbsp;消" style="width: 120px;"
                                          class="btn btn-warning btn-lg" onclick="cancel();">
                </td>
            </tr>
        </table>
    </form>
</div>

<!--俱乐部明细 -->
<div id="clubDetail" class="page-container" style="background-color: white;display:none;">

    <div class="page-content-wrapper" style="background-color: white;">
        <div class="page-content" style="height:100%;background-color: white;margin: auto;">
            <div class="row">
                <c:if test="${not empty clubList}">
                    <div>
                        <h3 style="text-align: center;">俱乐部明细</h3>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-hover table-striped table-bordered tb1">
                            <thead>
                            <tr>
                                <th>俱乐部名称</th>
                                <th>俱乐部ID</th>
                                <th>代理商名称</th>
                                <th>推广员ID</th>
                                <th>在该俱乐部内被发放钻石的总量</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${clubList}" var="row">
                                <tr>
                                    <td>${row.getColumnValue("clubName")}</td>
                                    <td>${row.getColumnValue("clubId")}</td>
                                    <td>${row.getColumnValue("realName")}</td>
                                    <td>${row.getColumnValue("promoterId")}</td>
                                    <td>${row.getColumnValue("sellNum")}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>

                    </div>
                </c:if>
                <div style="width: 100%;text-align: center;line-height: 35px;">
                    <br/>
                    <input type="button" value="确&nbsp;&nbsp;定" name="create"
                           style="width: 150px;"
                           class="btn btn-lg btn-success" onclick="parent.layer.closeAll();">
                    <input type="button" value="取&nbsp;&nbsp;消" name="create"
                           style="width: 150px;"
                           class="btn btn-lg btn-primary" onclick="parent.layer.closeAll();">
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 代理开通成功，且送短信通知！-->
<div id="div2" style="display:none;font-size: 18px;text-align: center;">
    <br/>
    <div style="text-align: center">代理开通成功</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 33px;" align="center">
            <tr>
                <td>已开通俱乐部：<span id="spName4"></span>俱乐部</td>
            </tr>
            <tr>
                <td>玩家已成为：<span id="pLevel2">二级代理商</span></td>
            </tr>
            <tr>
                <td>俱乐部ID：<span id="spClubId"></span></td>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td style="color: #22c6e6;">后台账号：<span id="spPhone2"></span></td>
            </tr>
            <tr>
                <td style="color: #22c6e6;">
                    已发送短信通知该代理商
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center"><input type="button" value="确定" name="create" style="width: 180px"
                                          class="btn btn-success btn-lg"
                                          onclick="layer.closeAll();$('#content').load('pre_sale_manage/open');">
                </td>
            </tr>
        </table>
    </form>
</div>
<!-- 代理开通成功，未发送短信！-->
<div id="div3" style="display:none;font-size: 18px;text-align: center;">
    <br/>
    <div style="text-align: center">代理开通成功</div>
    <hr/>
    <br/>
    <form method="post">
        <table border="0" width="70%" style="line-height: 33px;" align="center">
            <tr>
                <td>已开通俱乐部：<span id="spName6"></span>俱乐部</td>
            </tr>
            <tr>
                <td>玩家已成为：<span id="pLevel4">二级代理商</span></td>
            </tr>
            <tr>
                <td>俱乐部ID：<span id="spClubId2"></span></td>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td style="color: #22c6e6;">后台账号：<span id="spPhone3"></span></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center"><input type="button" value="确定" name="create" style="width: 180px"
                                          class="btn btn-success btn-lg"
                                          onclick="layer.closeAll();$('#content').load('pre_sale_manage/open');">
                </td>
            </tr>
        </table>
    </form>
</div>

<script>
    function openDetail() {
        layer.open({
            type: 1,
            closeBtn: 0,
            anim: -1,
            title: '',
            area: ['800px', '520px'],
            content: $("#clubDetail")
        });
    }
    function query() {
        var gameUserId = $.trim($("#playId").val());
        if (gameUserId == '') {
            layer.alert("请输入玩家ID！");
        } else if (isNaN(gameUserId)) {
            layer.alert("请输入纯数字ID！");
        } else {
            $('#content').load('pre_sale_manage/queryByUserId?gameUserId=' + gameUserId);
        }
    }
    function create(type) {
        var mobile = /^1(3|4|5|6|7|8|9)\d{9}$/;
        var cellphone = $.trim($("#inCellPhone").val());
        var name = $.trim($("#inRealName").val());
        var gameNickName = '${gameNickName}';
        var parentId = $.trim($("#parentClubId").val());
        var gameUserId = '${gameUserId}';
        var staffWechat = $('#staffWechat').val();

        if (type == 'formal') {
            $("#promoter_type").val(1);
        } else if (type == 'fake') {
            $("#promoter_type").val(2);
        }

        if (cellphone == '') {
            layer.alert("手机号不能为空！");
        } else if (!mobile.test(cellphone)) {
            layer.alert("请输入正确的手机号！");
        } else if (name == '') {
            layer.alert("请输入代理商姓名！");
        } else if (staffWechat == '') {
            layer.alert("请输入客服微信号！");
        } else {
            $("#spClub").text(parentId);

            $("#spName").text(name);
            $("#spName2").text(name);
            $("#spName3").text(gameNickName);
            $("#spPhone").text(cellphone);
            $('#spName5').text(gameNickName.substr(0, 14));
            $('#spName4').text(gameNickName.substr(0, 14));
            $('#spName6').text(gameNickName.substr(0, 14));

            var pLevel = $("#se1").val();
            if (pLevel == 2) {
                $("#pLevel").text('二级代理商');
                $("#pLevel2").text('二级代理商');
                $("#pLevel4").text('二级代理商');
            } else if (pLevel == 1) {
                $("#pLevel").text('一级代理商');
                $("#pLevel2").text('一级代理商');
                $("#pLevel4").text('一级代理商');
            } else {
                $("#pLevel").text('特级代理商');
                $("#pLevel2").text('特级代理商');
                $("#pLevel4").text('特级代理商');
            }

            if (parentId != null && parentId.length > 0) { //判断俱乐部id的正确性
                if (isNaN(parentId)) {
                    layer.alert("推荐人ID必须为纯数字，请重新输入！");
                    return;
                }
                $.post("pre_sale_manage/queryUpPromoter",
                        {clubId: parentId},
                        function (date) {
                            if (date == "0") {
                                layer.alert("推荐人输入错误，请重新输入！");
                                return;
                            } else {
                                parentId = date;
                                $('#upPromoterId').text(parentId);
                                layer.open({
                                    type: 1,
                                    closeBtn: 0,
                                    anim: -1,
                                    title: '',
                                    area: ['800px', '520px'],
                                    content: $("#div1")
                                });
                            }
                        });
            } else {
                $('#upPromoterId').text('');
                layer.open({
                    type: 1,
                    closeBtn: 0,
                    anim: -1,
                    title: '',
                    area: ['800px', '520px'],
                    content: $("#div1")
                });
            }
        }

    }
    function ajax() {
        var realName = $.trim($("#inRealName").val());
        var cellPhone = $.trim($("#inCellPhone").val());
        var parentId = $.trim($("#upPromoterId").text());
        var gameNickName = '${gameNickName}';
        var gameUserId = '${gameUserId}';
        var clubId = $.trim($("#upClubId").val());
        var pLevel = $.trim($("#se1").val());
        var type = $("#promoter_type").val();
        var staffWechat = $('#staffWechat').val();
        var remark = $('#remark').val();

        $.post("pre_sale_manage/createPromoter",
                {
                    pLevel: pLevel,
                    realName: realName,
                    cellPhone: cellPhone,
                    parentId: parentId,
                    gameNickName: gameNickName,
                    gameUserId: gameUserId,
                    clubId: clubId,
                    type: type,
                    staffWechat: staffWechat,
                    remark: remark
                },
                function (da) {
                    var data = da.split(",")[0];
                    var isFirst = da.split(",")[1];
                    if (da.length > 100) {
                        layer.open({
                            type: 1,
                            closeBtn: 0,
                            anim: -1,
                            title: '',
                            area: ['800px', '520px'],
                            content: (da)
                        });
                        return;
                    }
                    if (data == 'NO') {
                        layer.alert("开通失败，请重试！");
                    } else if (data == 'REPEAT') {
                        layer.closeAll();
                        layer.alert("该手机号码已开通代理！");
                    } else {
                        if (isFirst == '1') {
                            $("#spClubId").text(data);
                            $("#spPhone2").text(cellPhone);
                            layer.closeAll();
                            layer.open({
                                type: 1,
                                closeBtn: 0,
                                anim: -1,
                                title: '',
                                area: ['800px', '520px'],
                                content: $("#div2")
                            });
                        } else {
                            $("#spClubId2").text(data);
                            $("#spPhone3").text(cellPhone);
                            layer.closeAll();
                            layer.open({
                                type: 1,
                                closeBtn: 0,
                                anim: -1,
                                title: '',
                                area: ['800px', '520px'],
                                content: $("#div3")
                            });
                        }

                    }
                },
                "text");
    }
    function cancel() {
        layer.closeAll();
    }
</script>

