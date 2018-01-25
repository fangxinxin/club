<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
    select {
        height: 25px;
        border-radius: 5px;
        border-color: lightseagreen;
    }
    .b-hr {
        background-image: -webkit-linear-gradient(left, transparent, #DBDBDB, transparent);
        background-image: linear-gradient(to right, transparent, #DBDBDB, transparent);
        border: 0;
        height: 1px;
        margin: 11px 0;
    }
</style>

<c:set value="${promoter.id}"          var="promoterId"/>
<c:set value="${promoter.cellPhone}"   var="cellPhone"/>
<c:set value="${promoter.nickName}"    var="nickName"/>
<c:set value="${promoter.gameCard}"    var="gameCard"/>
<c:set value="${promoter.pLevel}"      var="pLevel"/>
<c:set value="${promoter.rebate}"      var="rebate" />
<c:set value="${promoter.bankAccount}" var="bankAccount" />
<c:set value="${promoter.deposit}"     var="deposit" />
<c:set value="${club.id}"              var="clubId"/>
<c:set value="${club.shareCard}"       var="shareCard"/>
<c:set value="${club.clubCard}"        var="clubCard"/>
<c:set value="${club.clubName}"        var="clubName"/>

<!-- 弹框 -->
<!-- 个人信息 -->
<div id="dlg1" class="easyui-dialog" style="padding:20px 6px;width:90%;"
     data-options="inline:true,modal:true,closed:true,title:'个人信息'">
    <ul class="list list-unstyled ml-md">
        <li class="mb-none mt-xs">俱乐部名:${clubName}</li>
        <li class="mb-none">玩家名称：${nickName}</li>
        <li class="mb-none">代理商级别：
            <c:if test="${pLevel==1}">一级代理商</c:if>
            <c:if test="${pLevel==2}">二级代理商</c:if>
            <c:if test="${pLevel==-1}">特级代理商</c:if>
        </li>
        <li class="mb-none">钻石余额：${gameCard}</li>
        <li class="">俱乐部人数：${num}</li>
        <li class="mb-none">
            <input type="button" class="btn btn-borders btn-primary btn-sm mr-xs" value="修改密码"
                   onclick="$('#dlg2').dialog('open').dialog('center')">
            <input type="button" class="btn btn-secondary btn-sm mr-xs" value="注&nbsp;&nbsp;销"
                   onclick="logout();">
        </li>
    </ul>
    <div class="dialog-button">
        <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
           onclick="$('#dlg1').dialog('close')">确定</a>
    </div>
</div>
<!-- 修改密码 -->
<div id="dlg2" class="easyui-dialog" style="padding:20px 6px;width:90%;"
     data-options="inline:true,modal:true,closed:true,title:'修改密码'">
    <form id="form" action="/changePass" method="post">
        <table width="100%">
            <tr>
                <td colspan="2" style="color: green;text-align: center;">温馨提示：修改密码后需要重新登录！</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td align="right" width="40%">原密码：</td>
                <td>
                    <input type="password" name="loginPassword" id="loginPassword">
                    <input type="hidden" name="cellPhone" value="${cellPhone}"
                           id="cellPhone">
                    <input type="hidden" name="id" value="${promoterId}" id="id">
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td align="right" width="40%">新密码：</td>
                <td><input type="password" name="rePass" id="rePass" maxlength="10"></td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td align="right" width="40%">确认新密码：</td>
                <td><input type="password" name="agen" id="agen" maxlength="10"></td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
        </table>
        <div class="dialog-button">
            <input type="button" class="btn btn-warning btn-block" style="width:100%;height:35px" value="提交"
                   onclick="ajax()" id="btn">
        </div>
        <div class="dialog-button">
            <a href="javascript:void(0)" class="btn btn-success btn-block" style="width:100%;height:35px"
               onclick="$('#dlg2').dialog('close');">返回</a>
        </div>
    </form>
</div>
<!-- 提成信息 -->
<div id="dlg3" class="easyui-dialog" style="padding:20px 6px;width:90%;"
     data-options="inline:true,modal:true,closed:true,title:'提成信息'">
    <ul class="list list-unstyled ml-md">
        <li><h4>当前可提成金额：</h4></li>
        <li class="mb-none"><h3 class="center fa-2x bold">￥${deposit}&nbsp;元</h3></li>
        <li class="mb-none">
            <h4>
                <small>备注：每周二结算上周的可提成金额</small>
            </h4>
        </li>
        <li class="mb-none">
            <hr>
        </li>
        <li class="mb-none">
            <div class="col-md-6 col-xs-6">
                我的银行卡：<br/><br/><span style="font-size: 24px;" id="spanBankAccount">
                <c:if test="${bankAccount=='0'}">未绑定</c:if>
                <c:if test="${bankAccount!='0'}">${bankAccount}</c:if>
                </span>
            </div>
            <div class="col-md-6 col-xs-6">
                <c:if test="${bankAccount=='0'}"><a
                        class="btn btn-xs btn-primary mb-xs ml-xlg" onclick="bound();">绑定银行卡</a></c:if>
                <c:if test="${bankAccount!='0'}"><a
                        class="btn btn-xs btn-primary mb-xs ml-xlg" onclick="unbound();">解除绑定</a></c:if>
            </div>
        </li>
    </ul>
    <div class="dialog-button">
        <input type="button" class="btn btn-primary btn-block" value="申请提现"
               onclick="requestConfirm();">
    </div>
    <div class="dialog-button">
        <a href="javascript:void(0)" class="btn btn-default btn-block" style="height:35px"
           onclick="$('#dlg3').dialog('close')">取消</a>
    </div>
</div>
<!-- 申请提现-->
<div id="dlg4" class="easyui-dialog" style="padding:20px 6px;width:90%;"
     data-options="inline:true,modal:true,closed:true,title:'我要提现'">
    <form action="/changePass" method="post" onsubmit="return sub();">
        <table width="95%">
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center">当前可提成金额：<span id="totalWithdraw">${deposit}</span>&nbsp;元
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center">
                    <input type="text" name="withdraw" id="withdraw" style="width: 30%;text-align: right;"
                           placeholder="请输入整数">00&nbsp;元
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
        </table>
        <div class="dialog-button">
            <input type="button" class="btn btn-warning btn-block" style="width:100%;height:35px" value="确认提现"
                   onclick="warnWithdraw()">
        </div>
        <div class="dialog-button">
            <a href="javascript:void(0)" class="btn btn-success btn-block" style="width:100%;height:35px"
               onclick="$('#dlg4').dialog('close');">取消</a>
        </div>
    </form>
</div>
<!--提现确认 -->
<div id="dlg5" class="easyui-dialog" style="padding:20px 6px;width:90%;"
     data-options="inline:true,modal:true,closed:true,title:''">
    <form action="/changePass" method="post" onsubmit="return sub();">
        <table width="95%">
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center" style="font-size: 24px;">您本次提现的金额为：</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td style="font-size: 28px;" align="center">￥:&nbsp;<span id="span1"></span></td>
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
        </table>
        <div class="dialog-button">
            <input type="button" class="btn btn-warning btn-block" style="width:100%;height:35px" value="确认提现"
                   onclick="withdrawReuest()" id="btn5">
        </div>
        <br/>
        <div class="dialog-button">
            <a href="javascript:void(0)" class="btn btn-success btn-block" style="width:100%;height:35px"
               onclick="$('#dlg5').dialog('close');">取消</a>
        </div>
    </form>
</div>
<!-- 提现成功提醒-->
<div id="dlg6" class="easyui-dialog" style="padding:20px 6px;width:90%;height: 400px;"
     data-options="inline:true,modal:true,closed:true,title:''">
    <div>
        <br/><br/>
        <h3 align="center">恭喜您提现成功！</h3>
        <br/>
        <p style="font-size: 20px;color: black;">您的提现预计在5个工作日内打款，如有异常，请联络客服人员！</p>
        <br/><br/><br/>
        <a href="javascript:void(0)" class="btn btn-success btn-block"
           style="width:100%;height:35px;align-self: center;"
           onclick="window.location='index';">确定</a>
    </div>
</div>
<!-- 绑定银行卡 -->
<div id="dlg_bound" class="easyui-dialog" style="padding:20px 6px;width:95%;font-size: 10px;"
     data-options="inline:true,modal:true,closed:true,title:'绑定银行卡'">
    <form method="post" id="form_validate">
        <div class="form-group">
            <div class="col-md-6">
                <div class="input-group input-medium">
                    <span class="input-group-btn">
                            <input type="button" class="form-control" value="&nbsp;姓&nbsp;&nbsp;&nbsp;&nbsp;名&nbsp;&nbsp;" style="font-size: 11px;">
                        </span>
                    <input type="text" class="form-control" name="realName" id="realName" maxlength="20"
                           placeholder="请输入真实姓名...">
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-6">
                <div class="input-group input-medium">
                    <span class="input-group-btn">
                            <input type="button" class="form-control" value="身份证号" style="font-size: 11px;">
                        </span>
                    <input type="text" class="form-control" id="IDCard" maxlength="18" placeholder="请输入身份证号..."
                           name="IDCard">
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-6">
                <div class="input-group input-medium">
                    <span class="input-group-btn">
                            <input type="button" class="form-control" value="银行卡号" style="font-size: 11px;">
                        </span>
                    <input type="text" class="form-control" name="cardNum" id="cardNum" maxlength="20"
                           placeholder="请输入银行卡号...">
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-md-12">
                开户地：
                <select id="selProvince" onchange="provinceChange();"></select>
                <select id="selCity" style="align-content: right;"></select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-12">
                开户银行:
                <select id="bankName">
                    <option value="国家开发银行">国家开发银行</option>
                    <option value="中国进出口银行">中国进出口银行</option>
                    <option value="中国农业发展银行">中国农业发展银行</option>
                    <option value="中国银行">中国银行</option>
                    <option value="中国工商银行">中国工商银行</option>
                    <option value="中国建设银行">中国建设银行</option>
                    <option value="中国农业银行">中国农业银行</option>
                    <option value="中国光大银行">中国光大银行</option>
                    <option value="中国民生银行">中国民生银行</option>
                    <option value="中信银行">中信银行</option>
                    <option value="交通银行">交通银行</option>
                    <option value="华夏银行">华夏银行</option>
                    <option value="招商银行">招商银行</option>
                    <option value="兴业银行">兴业银行</option>
                    <option value="广发银行">广发银行</option>
                    <option value="平安银行">平安银行</option>
                    <option value="上海浦东发展银行">上海浦东发展银行</option>
                    <option value="恒丰银行">恒丰银行</option>
                    <option value="浙商银行">浙商银行</option>
                    <option value="渤海银行">渤海银行</option>
                    <option value="中国邮政储蓄银行">中国邮政储蓄银行</option>
                    <option value="城市商业银行">城市商业银行</option>
                    <option value="北京银行">北京银行</option>
                    <option value="天津银行">天津银行</option>
                    <option value="河北银行">河北银行</option>
                    <option value="沧州银行">沧州银行</option>
                    <option value="唐山市商业银行">唐山市商业银行</option>
                    <option value="承德银行">承德银行</option>
                    <option value="张家口市商业银行">张家口市商业银行</option>
                    <option value="秦皇岛银行">秦皇岛银行</option>
                    <option value="邢台银行">邢台银行</option>
                    <option value="廊坊银行">廊坊银行</option>
                    <option value="保定银行">保定银行</option>
                    <option value="邯郸银行">邯郸银行</option>
                    <option value="衡水银行">衡水银行</option>
                    <option value="晋商银行">晋商银行</option>
                    <option value="大同市商业银行">大同市商业银行</option>
                    <option value="长治银行">长治银行</option>
                </select>
            </div>
        </div>

        <div class="form-group">
            <div class="col-md-6">
                <div class="input-group input-medium">
                    <input type="text" class="form-control" placeholder="请输入验证码" name="boundVerifyCode"
                           style="font-size: 12px;" id="boundVerifyCode">
                        <span class="input-group-btn">
                            <input type="button" class="form-control" value="点击获取验证码"
                                   onclick="sendMes(this);">
                        </span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="dialog-button">
                <input type="button" class="btn btn-success btn-block" style="height:35px" value="确认" id="btnBound"
                       onclick="bundBankCode();">
            </div>
            <div class="dialog-button">
                <a href="javascript:void(0)" class="btn btn-default btn-block" style="height:35px"
                   onclick="$('#dlg_bound').dialog('close')">取消</a>
            </div>
        </div>
    </form>
</div>
<!-- 绑定确认 -->
<div id="dlg_bound_ok" class="easyui-dialog" style="padding:20px 6px;width:90%;height: 450px;"
     data-options="inline:true,modal:true,closed:true,title:''">
    <br/><br/>
    <p style="font-size: 22px;">您所要绑定的银行卡卡号为：</p>
    <p style="text-align: center;font-size: 22px;" id="cardNumOK"></p>
    <p style="font-size: 22px;">持卡人姓名：</p>
    <p style="text-align: center;font-size: 22px;" id="realNameOK"></p>
    <br/>
    <div class="form-group">
        <div class="dialog-button mt-xs">
            <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
               id="btnBoundOk" onclick="updateBankNum();">确认</a>
        </div>
        <br/>
        <div class="dialog-button">
            <a href="javascript:void(0)" class="btn btn-default btn-block" style="height:35px"
               onclick="$('#dlg_bound_ok').dialog('close')">取消</a>
        </div>
    </div>
</div>
<!-- 绑定成功 -->
<div id="dlg_bound_success" class="easyui-dialog" style="padding:20px 6px;width:90%;height: 300px;"
     data-options="inline:true,modal:true,closed:true,title:''">
    <br/><br/>
    <p style="text-align: center;font-size: 22px;color: red;">恭喜您绑定成功银行卡！</p>
    <br/>
    <div class="form-group">
        <div class="dialog-button mt-xs">
            <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
               onclick="window.location='index';">确认</a>
        </div>
        <br/>
    </div>
</div>
<!-- 解除绑定 -->
<div id="dlg_unbound" class="easyui-dialog" style="padding:20px 6px;width:90%;"
     data-options="inline:true,modal:true,closed:true,title:'解绑银行卡'">
    <form method="post">
        <div class="form-group">
            <label class="col-md-3 control-label">您当前绑定的银行卡号：</label>
            <div class="col-md-6">
                <h3 class="bold">${bankAccount}</h3>
            </div>
        </div>
        <div class="form-group rmt-xlg">
            <label class="col-md-3 control-label" for="cancelBoundCode">验证码：</label>
            <div class="col-md-6">
                <div class="input-group input-medium">
                    <input type="text" class="form-control" placeholder="手机收到的验证码" name="verifyCode"
                           id="cancelBoundCode" style="font-size: 12px;">
                        <span class="input-group-btn">
                            <input type="button" class="form-control" value="点击获取验证码"
                                   onclick="unboundSendMes();settime(this);">
                        </span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="dialog-button mt-xs">
                <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
                   id="btnCancel" onclick="cancelBankNum();">确认</a>
            </div>
            <div class="dialog-button">
                <a href="javascript:void(0)" class="btn btn-default btn-block" style="height:35px"
                   onclick="$('#dlg_unbound').dialog('close')">取消</a>
            </div>
        </div>
    </form>
</div>
<!-- 解绑成功 -->
<div id="dlg_cancelbound_success" class="easyui-dialog" style="padding:20px 6px;width:90%;height: 300px;"
     data-options="inline:true,modal:true,closed:true,title:''">
    <br/><br/>
    <p style="text-align: center;font-size: 24px;color: red;">解绑成功！</p>
    <br/>
    <div class="form-group">
        <div class="dialog-button mt-xs">
            <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
               onclick="window.location='index';">确认</a>
        </div>
        <br/>
    </div>
</div>
<!-- 修改 :: 俱乐部开房钻石数 -->
<div id="change_diamond" class="easyui-dialog" style="padding:20px 6px;width:90%;"
     data-options="inline:true,modal:true,closed:true,title:'钻石转换'">
    <ul class="list list-unstyled ml-md">
        <li class="mb-none">
            <span style="font-size: 15px">
                当前剩余钻石:<span id="gameCard">${gameCard}</span><span id="change1" class="ml-md bold"></span>
            </span>
        </li>
        <li class="mt-xs">
            <button class="btn btn-primary" style="margin-top:-2px;" onclick="editGameCard('subtract',1)">
                <strong><i class="fa fa-minus"></i></strong>
            </button>
            <input id="changeGameCard" class="center" type="number" min="1" maxlength="10" style="height:35px; width: 65%;"
                   oninput="editGameCard_in(1)" value="${gameCard}"/>
            <button class="btn btn-primary" style="margin-top: -2px;" onclick="editGameCard('add',1)">
                <strong><i class="fa fa-plus"></i></strong>
            </button>
        </li>
        <li class="mb-none">
            <span style="font-size: 15px">
                俱乐部开房剩余钻石：<span id="shareCard">${shareCard}</span><span id="change2" class="ml-md bold"></span>
            </span>
        </li>
        <li class="mt-xs">
            <button class="btn btn-primary" style="margin-top: -2px" onclick="editGameCard('subtract',2)">
                <strong><i class="fa fa-minus"></i></strong>
            </button>
            <input id="changeShareCard" class="center" type="number" min="1" maxlength="10" style="height: 35px; width: 65%;"
                   oninput="editGameCard_in(2)" value="${shareCard}"/>
            <button class="btn btn-primary" style="margin-top: -2px" onclick="editGameCard('add',2)">
                <strong><i class="fa fa-plus"></i></strong>
            </button>
        </li>
        <li class="mt-xs mb-none">
            <label style="color: gray">俱乐部开房专属钻石： ${clubCard}</label>
        </li>
    </ul>
    <div class="dialog-button">
        <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
           onclick="change_shareCard()">保存</a>
    </div>
    <div class="dialog-button">
        <a href="javascript:void(0)" class="btn btn-default btn-block" style="height:35px"
           onclick="$('#change_diamond').dialog('close');reloadTb();">退出</a>
    </div>
</div>
<!-- 领取红包 -->
<c:if test="${hasRedPacket && dictRedPacket != null}">
    <div id="redPacket" class="easyui-dialog" style="padding:16px 6px;width:90%;"
         data-options="inline:true,modal:true,closed:true,title:'申请红包'">
        <ul class="list list-unstyled ml-md">
            <li><h4 style="margin: 0;">满足条件后，可申请领取红包奖励</h4></li>

            <li class="mb-none">
                <div class="col-xs-5 p-none m-none">
                    <h3 class="text-right bold heading-tertiary" style="margin-bottom: 10px">
                        <i class="fa fa-cny" style="padding-left: 6px;padding-right: 6px;"></i>:
                    </h3>
                </div>
                <div class="col-xs-7 p-none m-none">
                    <h3 class="bold heading-tertiary" style="margin-bottom: 10px">&nbsp;66元</h3>
                </div>
            </li>

            <li class="mb-none">
                <div class="col-xs-5 p-none m-none">
                    <h3 class="text-right bold heading-tertiary" style="margin-bottom: 10px">
                        <i class="fa fa-diamond"></i>:
                    </h3>
                </div>
                <div class="col-xs-7 p-none m-none">
                    <h3 class="bold heading-tertiary" style="margin-bottom: 10px">&nbsp;300颗</h3>
                </div>
            </li>

            <li class="mb-none"><div class="col-xs-12 b-hr"></div></li>

            <li class="mb-none">
                <h4 style="margin-bottom: 8px">领取红包条件：</h4>
            </li>

            <li class="mb-none">
                <label style="margin-bottom: 2px">1.俱乐部转正已成功转正</label>
            </li>
            <li class="mb-none">
                <label>2.转正后48小时内，俱乐部“新玩家”所参与对局数达到20场</label>
            </li>

            <li class="mt-xs mb-none">
                <h4><small>备注：退出其他俱乐部后再加入非新玩家</small></h4>
            </li>
        </ul>

        <div class="dialog-button">
            <input type="button" class="btn btn-primary btn-block" value="申请领取"
                   onclick="request_redPacket();">
        </div>
        <div class="dialog-button">
            <a href="javascript:void(0)" class="btn btn-default btn-block" style="height:35px"
               onclick="$('#redPacket').dialog('close')">取消</a>
        </div>
    </div>
    <!-- 领取红包 :: 提示信息 -->
    <div id="redPacket_prompt" class="easyui-dialog" style="padding:20px 6px;width:90%;"
         data-options="inline:true,modal:true,closed:true,title:'提  示'">
        <h4 class="ml-md p-md">
            您的领取红包申请已提交，我们将于3个工作日内对红包进行审核，并发放。如果延期未到账，请与客服人员联系，谢谢。
        </h4>
        <h4 class="ml-md p-md">
            您的俱乐部尚未转正，还不能申请领取红包。
        </h4>
        <h4 class="ml-md p-md">
            您俱乐部内的新成员转正后48小时内参与的对局数*场，不足20场，无法领取红包，请条件满足后再次申请取。
        </h4>
        <div class="dialog-button">
            <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
               onclick="$('#redPacket_prompt').dialog('close')">确定</a>
        </div>
    </div>
</c:if>
<!-- 钻石返利 -->
<c:if test="${pLevel!=2}">
    <c:set value="${rebate}" var="rebate" />
    <div id="rebate" class="easyui-dialog" style="padding:20px 6px;width:90%;"
         data-options="inline:true,modal:true,closed:true,title:'提钻信息'">
        <ul class="list list-unstyled ml-md">
            <li class="mb-none rmt-md">
                <h4 class="text-right mb-none">
                    <a href="javascrip:;" class="btn btn-sm btn-default"
                       onclick="layer_url_area('返钻信息', 'win/rebateLog', '96%', '80%'), $('#rebate').dialog('close')"
                       style="color: #383f48;font-weight: 400;line-height: 1.2">
                        返钻提取记录
                    </a>
                </h4>
            </li>
            <li class="mb-none">
                <h4>
                    当前可提取返钻数量：
                </h4>
            </li>
            <li class="mb-none">
                <h3 class="center mb-none m-none fa-2x bold">
                    <i class="fa fa-diamond" style="color: #0088cc"></i> ${rebate}&nbsp;颗
                </h3>
            </li>
        </ul>
        <div class="dialog-button">
            <input type="button" class="btn btn-success btn-block"  <c:if test="${rebate == 0}">disabled="disabled" </c:if> value="确定提取"
                   onclick="getRebate();">
        </div>
        <div class="dialog-button">
            <a href="javascript:void(0)" class="btn btn-default btn-block" style="height:35px"
               onclick="$('#rebate').dialog('close')">取消</a>
        </div>

    </div>
</c:if>

<!-- 方法 -->
<script type="text/javascript">

    <%--验证银行卡号--%>
    function testBankCard(value) {
        if (/[^0-9 \-]+/.test(value)) {
            return false;
        }
        var nCheck = 0,
                nDigit = 0,
                bEven = false,
                n, cDigit;

        value = value.replace(/\D/g, "");

        if (value.length < 13 || value.length > 19) {
            return false;
        }

        for (n = value.length - 1; n >= 0; n--) {
            cDigit = value.charAt(n);
            nDigit = parseInt(cDigit, 10);
            if (bEven) {
                if (( nDigit *= 2 ) > 9) {
                    nDigit -= 9;
                }
            }
            nCheck += nDigit;
            bEven = !bEven;
        }

        return ( nCheck % 10 ) === 0;
    }

    <%--验证身份证号--%>
    function testIDCard(value) {
        var reg = /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/;
        return reg.test(value);
    }

    function bundBankCode() {
        var cardNum = $.trim($('#cardNum').val());
        var boundVerifyCode = $.trim($('#boundVerifyCode').val());
        var realName = $.trim($('#realName').val());
        var IDCard = $.trim($('#IDCard').val());//身份证
        var reg = /^[\u4E00-\u9FA5]{2,4}/;
        if (realName.length == 1 || reg.test(realName) == false) {
            $.messager.alert("提示", "请输入真实姓名!");
        } else if (IDCard == '') {
            $.messager.alert('提示', '请输入身份证号！');
        } else if (!testIDCard(IDCard)) {
            $.messager.alert('提示', '请输入正确的身份证号！');
        } else if (cardNum == '') {
            $.messager.alert('提示', '银行卡号不能为空！');
        } else if (!testBankCard(cardNum)) {
            $.messager.alert('提示', '请输入正确的银行卡号！');
        } else if (boundVerifyCode == '') {
            $.messager.alert('提示', '请输入您收到的验证码！');
        } else {
            $('#dlg_bound_ok').dialog('open').dialog('center');
            $('#cardNumOK').text(cardNum);
            $('#realNameOK').text(realName);

        }
    }

    function logout() {
        $.post("logout", function (date, status) {
            if (status == "success") {
                parent.location.reload();
            }
        });
    }

    function requestConfirm() {
        var bank = $.trim($("#spanBankAccount").text());
        if (bank != '未绑定') {
            $('#dlg4').dialog('open').dialog('center');
        } else {
            $.messager.alert('提示', '请先绑定银行卡！');
        }
    }

    function cancelBankNum() {
        var verifyCode = $.trim($("#cancelBoundCode").val());
        if (verifyCode == '') {
            $.messager.alert('提示', '请输入您收到的验证码！');
        } else {
            $.post("cancelBound", {verifyCode: verifyCode}, function (data) {
                if (data == 'YES') {
                    $('#dlg_cancelbound_success').dialog('open').dialog('center');
                } else {
                    $.messager.alert('提示', '验证码错误！');
                }
            });
        }

    }

    function updateBankNum() {
        var bankNum = $("#cardNumOK").text();
        var realName = $("#realNameOK").text();
        var verifyCode = $.trim($("#boundVerifyCode").val());
        var bankName = $.trim($('#bankName').val());//开户行名
        var province = $.trim($('#selProvince').val());//开户省
        var city = $.trim($('#selCity').val());//开户市
        var bankArea = province + '-' + city + '-' + bankName;

        var IDCard = $.trim($('#IDCard').val());//身份证
        $.post("updateBankNum", {
            bankNum: bankNum,
            realName: realName,
            verifyCode: verifyCode,
            IDCard: IDCard,
            bankArea: bankArea
        }, function (data) {
            if (data == 'YES') {
                $('#dlg_bound_success').dialog('open').dialog('center');
            } else {
                $.messager.alert('提示', '验证码错误！');
                $('#dlg_bound_ok').dialog('close');
            }
        });
    }

    function sendMes(obj) {
        var cardNum = $.trim($('#cardNum').val());
        var realName = $.trim($('#realName').val());
        var IDCard = $.trim($('#IDCard').val());//身份证
        var reg = /^[\u4E00-\u9FA5]{2,4}/;
        if (realName.length == 1 || reg.test(realName) == false) {
            $.messager.alert("提示", "请输入真实姓名!");
        } else if (IDCard == '') {
            $.messager.alert('提示', '请输入身份证号！');
        } else if (!testIDCard(IDCard)) {
            $.messager.alert('提示', '请输入正确的身份证号！');
        } else if (cardNum == '') {
            $.messager.alert('提示', '银行卡号不能为空！');
        } else if (!testBankCard(cardNum)) {
            $.messager.alert('提示', '请输入正确的银行卡号！');
        } else {
            var cell = $("#cellPhone").val();
            $.post("verifyCodeTwo", {cellPhone: cell}, function (data) {
            });
            settime(obj);
        }
    }

    function unboundSendMes() {
        var cell = $("#cellPhone").val();
        $.post("verifyCodeTwo", {cellPhone: cell}, function (data) {
        });
    }

    var countdown = 90;

    function settime(obj) {
        if (countdown == 0) {
            obj.removeAttribute("disabled");
            obj.value = "免费获取验证码";
            countdown = 90;
            return;
        } else {
            obj.setAttribute("disabled", true);
            obj.value = "重新发送(" + countdown + ")";
            countdown--;
        }
        setTimeout(function () {
            settime(obj)
        }, 1000)
    }

    function bound() {
        $('#dlg3').dialog('close');
        $('#dlg_bound').dialog('open').dialog('center');
    }

    function unbound() {
        $('#dlg3').dialog('close');
        $('#dlg_unbound').dialog('open').dialog('center');
    }

    function ajax() {
        var loginPassword = $.trim($("#loginPassword").val());
        var rePass = $.trim($("#rePass").val());
        var agen = $.trim($("#agen").val());
        var cellPhone = ${cellPhone};
        if (loginPassword == '') {
            $.messager.alert('提示', '原密码不能为空！');
        } else if (rePass == '') {
            $.messager.alert('提示', '新密码不能为空！');
        } else if (agen == '') {
            $.messager.alert('提示', '确认新密码不能为空！');
        } else if (loginPassword == rePass) {
            $.messager.alert('提示', '新密码不能与旧密码相同！');
        } else if (agen != rePass) {
            $.messager.alert('提示', '两次输入密码不一致！');
        } else {
            $.post("changePass", {
                "cellPhone": cellPhone,
                "rePass": rePass,
                "loginPassword": loginPassword
            }, function (data) {
                if (data == 'NO') {
                    $.messager.alert('提示', '原始密码错误，请重新输入！');
                } else {
                    window.location = 'login';
                }
            }, "text");
        }

    }

    function withdrawReuest() {
        var withdraw = $.trim($("#withdraw").val()) + '00';
        $.post("addWithdrawRequest", {
            "withdraw": withdraw
        }, function (data) {
            if (data != 'OK') {
                $.messager.alert('提示', data);
            } else {
                $('#dlg6').dialog('open').dialog('center');
            }
        }, "text");
    }

    function warn(obj) {
        var value = $.trim($(obj).val());
        if (value == '') {
            $.messager.alert('提示', '该项为不能为空！');
        }
    }

    function warnWithdraw() {
        var test = /^[1-9]\d*$/;
        var now = $.trim($("#withdraw").val());
        var total = $.trim($("#totalWithdraw").html());
        if (now == '') {
            $.messager.alert('提示', '请输入提现金额！');
        } else if (!test.test(now)) {
            $.messager.alert('提示', '请输入大于0的整数！');
        } else if (parseInt(now + '00') > parseInt(total)) {
            $.messager.alert('提示', '提现金额不能大于可提现金额！');
        } else {
            $('#dlg5').dialog('open').dialog('center');
            $('#span1').html($('#withdraw').val() + '00');
        }
    }

</script>

<!-- 修改 :: 俱乐部开房钻石数 -->
<script charset="utf-8">
    var gameCard = parseInt('${gameCard}');
    var shareCard = parseInt('${shareCard}');

    /**转换钻石**/
    //action: 1 增钻,2 减钻
    //cardType: 1 剩余钻石,2 开房剩余钻石
    function editGameCard(action,cardType){
        //parseInt() 将数值型字符串转换为数值
        var changeGameCard = parseInt($("#changeGameCard").val());
        var changeShareCard = parseInt($("#changeShareCard").val());
        var gameCardTotal = parseInt('${gameCard}') + parseInt('${shareCard}');

        var c1 = isNaN(changeGameCard) || isNaN(changeShareCard) || isNaN(gameCardTotal);
        var c2 = changeGameCard+changeShareCard > gameCardTotal ;

        if(c1 || c2) {
            $.messager.alert('', '<h4 class="center bold red mt-md">请输入正确数量！</h4>', '');
        }  else {
            if(action == "add") {
                if(cardType == 1) {
                    changeGameCard += 1;
                    changeShareCard -= 1;
                }
                else if(cardType == 2) {
                    changeShareCard += 1;
                    changeGameCard -= 1;
                }
            }
            else if(action == "subtract") {
                if(cardType == 1) {
                    changeGameCard -= 1;
                    changeShareCard += 1;
                }
                else if(cardType == 2) {
                    changeShareCard -= 1;
                    changeGameCard += 1;
                }
            }

            if(changeGameCard<0 || changeShareCard<0) {
                return;
            }

            //显示变化
            show_change(changeGameCard,changeShareCard);

            $("#changeGameCard").val(changeGameCard);
            $("#changeShareCard").val(changeShareCard);
            return;
        }

    };

    /**手动输入**/
    function editGameCard_in(cardType) {
        var changeGameCard = parseInt($("#changeGameCard").val());
        var changeShareCard = parseInt($("#changeShareCard").val());
        var gameCardTotal = parseInt('${gameCard}') + parseInt('${shareCard}');
        var isNum = /^[1-9]\\d*$/;

        //判断非数字的字符
        if(isNum.test(changeGameCard) || isNum.test(changeShareCard)) {
            $("#changeGameCard").val('${gameCard}');
            $("#changeShareCard").val('${shareCard}');
            return;
        }

        //输入剩余钻石
        if(cardType == 1) {
            if(changeGameCard < 0 || changeGameCard > gameCardTotal) {
                $("#changeGameCard").val(gameCardTotal-changeShareCard);
                return $.messager.alert('', '<h4 class="center bold red mt-md">请输入正确数量！</h4>', '');
            }

            changeShareCard = gameCardTotal - changeGameCard;

            //显示变化
            show_change(changeGameCard,changeShareCard);

            return $("#changeShareCard").val(changeShareCard);
        }
        //输入俱乐部开房剩余钻石
        else if(cardType == 2) {
            if(changeShareCard < 0 || changeShareCard > gameCardTotal) {
                $("#changeShareCard").val(gameCardTotal-changeGameCard);
                return $.messager.alert('', '<h4 class="center bold red mt-md">请输入正确数量！</h4>', '');
            }

            changeGameCard = gameCardTotal - changeShareCard;

            //显示变化
            show_change(changeGameCard,changeShareCard);

            return $("#changeGameCard").val(changeGameCard);
        }

    };

    /**显示变化**/
    function show_change(changeGameCard,changeShareCard) {
        var text1,text2;
        var x1 = changeGameCard-gameCard;
        var x2 = changeShareCard-shareCard;
        if( x1 >= 0) {
            text1 = "+"+x1;
            $("#change1").attr('style', 'color:green');
        }
        else if(x1 < 0) {
            text1 = x1;
            $("#change1").attr('style', 'color:red');
        }
        if(x2 >= 0) {
            text2 = "+"+x2;
            $("#change2").attr('style', 'color:green');
        }
        else if(x2 < 0) {
            text2 = x2;
            $("#change2").attr('style', 'color:red');
        }
        $("#change1").text(text1);
        $("#change2").text(text2);
    }

    /**提交改变**/
    function change_shareCard() {
        //parseInt() 将数值型字符串转换为数值
        var changeGameCard = parseInt($("#changeGameCard").val());
        var changeShareCard = parseInt($("#changeShareCard").val());
        var gameCardTotal = parseInt('${gameCard}') + parseInt('${shareCard}');

        var c1 = isNaN(changeGameCard) || isNaN(changeShareCard) || isNaN(gameCardTotal);
        var c2 = changeGameCard+changeShareCard > gameCardTotal ;

        if(c1 || c2) {
            return $.messager.alert('', '<h4 class="center bold red mt-md">请输入正确数量！</h4>', '');
        } else {
            $.post("updateShareCard", {gameCard: changeGameCard, shareCard: changeShareCard}, function(data){
                if(data==('OK')) {
                    $("#changeGameCard").val(changeGameCard);
                    $("#changeShareCard").val(changeShareCard);
                    $("#gameCard").text(changeGameCard);
                    $("#shareCard").text(changeShareCard);
                    return;
                } else {
                    $.messager.alert('提示信息','<h5 class="center bold red mt-lg">销售失败!原因:'+ data +'</h5>');

                    $("#changeGameCard").val('${gameCard}');
                    $("#changeShareCard").val('${shareCard}');
                }

            });
        }

    }

</script>
<!-- 领取红包 -->
<script charset="utf-8">
    /** 申请红包 :: 提示信息 **/
    function request_redPacket() {

        $.post("requestRedPacket", function(data){
            if(data==('OK')) {
                $("#changeGameCard").val(changeGameCard);
                $("#changeShareCard").val(changeShareCard);
                $("#gameCard").text(changeGameCard);
                $("#shareCard").text(changeShareCard);
                return;
            } else {
                $.messager.alert('提示信息','<h5 class="center bold red mt-lg">销售失败!原因:'+ data +'</h5>');

                $("#changeGameCard").val('${gameCard}');
                $("#changeShareCard").val('${shareCard}');
            }

        });

    }

</script>
<!-- 钻石返利 -->
<script charset="utf-8">
    /** 钻石返利 :: 确定提取 **/
    function getRebate() {

        $.post("win/getRebate", function(data){
            var json = JSON.parse(data);

            $.messager.alert('提示信息','<h4 class="center red mt-lg">'+json.remark+'</h4>', '', function() {
                reloadTb();
            });
        });

    }


</script>

<script type="text/javascript" language="javascript">
    //定义数组，存储省份信息
    var province = ["北京", "上海", "天津", "重庆", "浙江", "江苏", "广东", "福建", "湖南", "湖北", "辽宁",
        "吉林", "黑龙江", "河北", "河南", "山东", "陕西", "甘肃", "新疆", "青海", "山西", "四川",
        "贵州", "安徽", "江西", "云南", "内蒙古", "西藏", "广西", "宁夏", "海南", "香港", "澳门", "台湾"
    ];

    //定义数组,存储城市信息
    var beijing = ["东城区", "西城区", "海淀区", "朝阳区", "丰台区", "石景山区", "通州区", "顺义区", "房山区", "大兴区", "昌平区", "怀柔区", "平谷区", "门头沟区", "延庆县", "密云县"];
    var shanghai = ["浦东新区", "徐汇区", "长宁区", "普陀区", "闸北区", "虹口区", "杨浦区", "黄浦区", "卢湾区", "静安区", "宝山区", "闵行区", "嘉定区", "金山区", "松江区", "青浦区", "南汇区", "奉贤区", "崇明县"];
    var tianjing = ["河东", "南开", "河西", "河北", "和平", "红桥", "东丽", "津南", "西青", "北辰", "塘沽", "汉沽", "大港", "蓟县", "宝坻", "宁河", "静海", "武清"];
    var chongqing = ["渝中区", "大渡口区", "江北区", "沙坪坝区", "九龙坡区", "南岸区", "北碚区", "万盛区", "双桥区", "渝北区", "巴南区", "万州区", "涪陵区", "黔江区", "长寿区", "江津区", "合川区", "永川区", "南川区"];
    var jiangsu = ["南京", "无锡", "常州", "徐州", "苏州", "南通", "连云港", "淮安", "扬州", "盐城", "镇江", "泰州", "宿迁"];
    var zhejiang = ["杭州", "宁波", "温州", "嘉兴", "湖州", "绍兴", "金华", "衢州", "舟山", "台州", "利水"];
    var guangdong = ["广州", "韶关", "深圳", "珠海", "汕头", "佛山", "江门", "湛江", "茂名", "肇庆", "惠州", "梅州", "汕尾", "河源", "阳江", "清远", "东莞", "中山", "潮州", "揭阳"];
    var fujiang = ["福州", "厦门", "莆田", "三明", "泉州", "漳州", "南平", "龙岩", "宁德"];
    var hunan = ["长沙", "株洲", "湘潭", "衡阳", "邵阳", "岳阳", "常德", "张家界", "益阳", "郴州", "永州", "怀化", "娄底", "湘西土家苗族自治区"];
    var hubei = ["武汉", "襄阳", "黄石", "十堰", "宜昌", "鄂州", "荆门", "孝感", "荆州", "黄冈", "咸宁", "随州", "恩施土家族苗族自治州"];
    var liaoning = ["沈阳", "大连", "鞍山", "抚顺", "本溪", "丹东", "锦州", "营口", "阜新", "辽阳", "盘锦", "铁岭", "朝阳", "葫芦岛"];
    var jilin = ["长春", "吉林", "四平", "辽源", "通化", "白山", "松原", "白城", "延边朝鲜族自治区"];
    var heilongjiang = ["哈尔滨", "齐齐哈尔", "鸡西", "牡丹江", "佳木斯", "大庆", "伊春", "黑河", "大兴安岭"];
    var hebei = ["石家庄", "保定", "唐山", "邯郸", "承德", "廊坊", "衡水", "秦皇岛", "张家口"];
    var henan = ["郑州", "洛阳", "商丘", "安阳", "南阳", "开封", "平顶山", "焦作", "新乡", "鹤壁", "许昌", "漯河", "三门峡", "信阳", "周口", "驻马店", "济源"];
    var shandong = ["济南", "青岛", "菏泽", "淄博", "枣庄", "东营", "烟台", "潍坊", "济宁", "泰安", "威海", "日照", "滨州", "德州", "聊城", "临沂"];
    var shangxi = ["西安", "宝鸡", "咸阳", "渭南", "铜川", "延安", "榆林", "汉中", "安康", "商洛"];
    var gansu = ["兰州", "嘉峪关", "金昌", "金川", "白银", "天水", "武威", "张掖", "酒泉", "平凉", "庆阳", "定西", "陇南", "临夏", "合作"];
    var qinghai = ["西宁", "海东地区", "海北藏族自治州", "黄南藏族自治州", "海南藏族自治州", "果洛藏族自治州", "玉树藏族自治州", "海西蒙古族藏族自治州"];
    var xinjiang = ["乌鲁木齐", "奎屯", "石河子", "昌吉", "吐鲁番", "库尔勒", "阿克苏", "喀什", "伊宁", "克拉玛依", "塔城", "哈密", "和田", "阿勒泰", "阿图什", "博乐"];
    var shanxi = ["太原", "大同", "阳泉", "长治", "晋城", "朔州", "晋中", "运城", "忻州", "临汾", "吕梁"];
    var sichuan = ["成都", "自贡", "攀枝花", "泸州", "德阳", "绵阳", "广元", "遂宁", "内江", "乐山", "南充", "眉山", "宜宾", "广安", "达州", "雅安", "巴中", "资阳", "阿坝藏族羌族自治州", "甘孜藏族自治州", "凉山彝族自治州"];
    var guizhou = ["贵阳", "六盘水", "遵义", "安顺", "黔南布依族苗族自治州", "黔西南布依族苗族自治州", "黔东南苗族侗族自治州", "铜仁", "毕节"];
    var anhui = ["合肥", "芜湖", "安庆", "马鞍山", "阜阳", "六安", "滁州", "宿州", "蚌埠", "巢湖", "淮南", "宣城", "亳州", "淮北", "铜陵", "黄山", "池州"];
    var jiangxi = ["南昌", "九江", "景德镇", "萍乡", "新余", "鹰潭", "赣州", "宜春", "上饶", "吉安", "抚州"];
    var yunnan = ["昆明", "曲靖", "玉溪", "保山", "昭通", "丽江", "普洱", "临沧", "楚雄彝族自治州", "大理白族自治州", "红河哈尼族彝族自治州", "文山壮族苗族自治州", "西双版纳傣族自治州", "德宏傣族景颇族自治州", "怒江傈僳族自治州", "迪庆藏族自治州"];
    var neimenggu = ["呼和浩特", "包头", "乌海", "赤峰", "通辽", "鄂尔多斯", "呼伦贝尔", "巴彦淖尔", "乌兰察布"];
    var guangxi = ["南宁", "柳州", "桂林", "梧州", "北海", "防城港", "钦州", "贵港", "玉林", "百色", "贺州", "河池", "崇左"];
    var xizang = ["拉萨", "昌都地区", "林芝地区", "山南地区", "日喀则地区", "那曲地区", "阿里地区"];
    var ningxia = ["银川", "石嘴山", "吴忠", "固原", "中卫"];
    var hainan = ["海口", "三亚"];
    var xianggang = ["中西区", "湾仔区", "东区", "南区", "九龙城区", "油尖旺区", "观塘区", "黄大仙区", "深水埗区", "北区", "大埔区", "沙田区", "西贡区", "元朗区", "屯门区", "荃湾区", "葵青区", "离岛区"];
    var taiwan = ["台北", "高雄", "基隆", "台中", "台南", "新竹", "嘉义"];
    var aomeng = ["澳门半岛", "氹仔岛", "路环岛"];

    //页面加载方法
    $(function () {
        //设置省份数据
        setProvince();

        //设置背景颜色
        setBgColor();
    });

    //设置省份数据
    function setProvince() {
        //给省份下拉列表赋值
        var option, modelVal;
        var $sel = $("#selProvince");

        //获取对应省份城市
        for (var i = 0, len = province.length; i < len; i++) {
            modelVal = province[i];
            option = "<option value='" + modelVal + "'>" + modelVal + "</option>";

            //添加到 select 元素中
            $sel.append(option);
        }

        //调用change事件，初始城市信息
        provinceChange();
    }

    //根据选中的省份获取对应的城市
    function setCity(provinec) {
        var $city = $("#selCity");
        var proCity, option, modelVal;

        //通过省份名称，获取省份对应城市的数组名
        switch (provinec) {
            case "北京":
                proCity = beijing;
                break;
            case "上海":
                proCity = shanghai;
                break;
            case "天津":
                proCity = tianjing;
                break;
            case "重庆":
                proCity = chongqing;
                break;
            case "浙江":
                proCity = zhejiang;
                break;
            case "江苏":
                proCity = jiangsu;
                break;
            case "广东":
                proCity = guangdong;
                break;
            case "福建":
                proCity = fujiang;
                break;
            case "湖南":
                proCity = hunan;
                break;
            case "湖北":
                proCity = hubei;
                break;
            case "辽宁":
                proCity = liaoning;
                break;
            case "吉林":
                proCity = jilin;
                break;
            case "黑龙江":
                proCity = heilongjiang;
                break;
            case "河北":
                proCity = hebei;
                break;
            case "河南":
                proCity = henan;
                break;
            case "山东":
                proCity = shandong;
                break;
            case "陕西":
                proCity = shangxi;
                break;
            case "甘肃":
                proCity = gansu;
                break;
            case "新疆":
                proCity = xinjiang;
                break;
            case "青海":
                proCity = qinghai;
                break;
            case "山西":
                proCity = shanxi;
                break;
            case "四川":
                proCity = sichuan;
                break;
            case "贵州":
                proCity = guizhou;
                break;
            case "安徽":
                proCity = anhui;
                break;
            case "江西":
                proCity = jiangxi;
                break;
            case "云南":
                proCity = yunnan;
                break;
            case "内蒙古":
                proCity = neimenggu;
                break;
            case "西藏":
                proCity = xizang;
                break;
            case "广西":
                proCity = guangxi;
                break;
            case "":
                proCity = xizang;
                break;
            case "宁夏":
                proCity = ningxia;
                break;
            case "海南":
                proCity = hainan;
                break;
            case "香港":
                proCity = xianggang;
                break;
            case "澳门":
                proCity = aomeng;
                break;
            case "台湾":
                proCity = taiwan;
                break;
        }

        //先清空之前绑定的值
        $city.empty();

        //设置对应省份的城市
        for (var i = 0, len = proCity.length; i < len; i++) {
            modelVal = proCity[i];
            option = "<option value='" + modelVal + "'>" + modelVal + "</option>";

            //添加
            $city.append(option);
        }

        //设置背景
        setBgColor();
    }

    //省份选中事件
    function provinceChange() {
        var $pro = $("#selProvince");
        setCity($pro.val());
    }

    //设置下拉列表间隔背景样色
    function setBgColor() {
        var $option = $("select option:odd");
        $option.css({"background-color": "#DEDEDE"});
    }
    var countdown = 90;

    function settime(obj) {
        if (countdown == 0) {
            obj.removeAttribute("disabled");
            obj.value = "免费获取验证码";
            countdown = 90;
            return;
        } else {
            obj.setAttribute("disabled", true);
            obj.value = "重新发送(" + countdown + ")";
            countdown--;
        }
        setTimeout(function () {
            settime(obj)
        }, 1000)
    }
</script>