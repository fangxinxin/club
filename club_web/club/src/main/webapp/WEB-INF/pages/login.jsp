<%@ page import="java.net.URLDecoder" %>
<%@ page import="dsqp.util.DesUtil" %>
<%@ page import="dsqp.util.CommonUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>
<%
    String name = "";
    String psw = "";
    String checked = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length > 0) {
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if ("name".equals(cookie.getName())) {
                name = URLDecoder.decode(cookie.getValue(), "utf-8");
                checked = "checked";
            }
            if ("psw".equals(cookie.getName())) {
                try {
                    psw = DesUtil.decrypt(cookie.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
%>

<title>登录</title>
<%--<script src="http://apps.bdimg.com/libs/jquery/1.10.2/jquery.min.js"></script>--%>

<!-- header -->
<header>
    <section class="page-header page-header-center page-header-more-padding page-header-no-title-border">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1> 代理商管理后台 </h1>
                </div>
            </div>
        </div>
    </section>
</header>

<!-- main -->
<div role="main" class="main">

    <div class="col-md-12">
        <form action="login" id="form_login" method="post">

            <div class="form-group">
                <div class="col-md-10 col-md-offset-1">
                    <div class="input-group input-group-icon">
													<span class="input-group-addon">
														<span class="icon"><i class="fa fa-user"></i></span>
													</span>
                        <input type="text" class="form-control input-lg" placeholder="手机号码" name="cellPhone"
                               value="<%=name %>" id="cellPhone">
                    </div>
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-10 col-md-offset-1">
                    <div class="input-group input-group-icon">
                        <span class="input-group-addon">
                            <span class="icon"><i class="fa fa-lock"></i></span>
                        </span>
                        <input id="loginPassword" type="password" class="form-control input-lg" placeholder="密码"
                               name="loginPassword" value="<%=psw %>">
                    </div>
                    <div class="col-md-12 center">
                        <p style="color: red" id="msg">${msg}</p>
                    </div>
                </div>

                <div class="col-md-10 col-md-offset-1 mt-md">
                    <input type="checkbox" name="save"
                            <%=checked %> /><span>&nbsp;</span>保存密码
                    <a class="ml-md" href="forget">忘记密码</a>
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-10 text-center col-md-offset-1">
                    <input type="submit" value="登&nbsp;&nbsp;录" class="btn btn-primary btn-lg btn-block btn-3d">
                </div>
            </div>

            <!-- 协议 -->
            <div class="form-group">
                <div style="text-align: center;">
                    <a id="agreement" href="javascript:void(0);" onclick="$('#win').window('open');">
                        <input type="checkbox" name="agree" checked="checked" disabled><span>&nbsp;</span>我已阅读并同意代理协议
                    </a>
                </div>
            </div>
        </form>
    </div>

</div>

<div id="win" class="easyui-window" disabled="true" title="<h4 class='center bold'>代理商后台用户协议</h4>"
     data-options="modal:true,closed:true" style="width:98%;height:94%;padding:10px;display: none;">
    <p>
        《66麻将》为本协议内容涉及的标的产品。鉴于甲方拥有的互联网技术与应用服务技术，为大力宣传、推广《66麻将》，
        甲乙双方就合作对标的产品进行网络推广事宜达成以下共识。
    </p>
    <h5>一、各方的权利和责任：</h5>
    <p>
        第一项 甲方权利及责任<br/>
        1、甲方保证所提供的标的产品具有完全的、独立的知识产权以及各种相关权利，并承诺此权利为甲方单独所有，
        并排除与第三方共有的情况，对于标的产品的权利是甲方独创或经过合法授权取得；<br/>
        2、在本协议履行期间，如甲方就标的产品的知识产权或使用权发生转移、变更，
        甲方必须提前一个月以书面通知、电子邮件等乙方可察觉的方式告知乙方；<br/>
        3、甲方应以自己的名义对外处理正常的玩家关于“平台”上的游戏钻石商品的建议与投诉，
        并提供“平台”运营所必须的线上和线下客服支持业务；<br/>
        4、甲方负责“平台”系统的平稳、可控性运营；<br/>
        5、甲方有权根据游戏官方的价格调整情况对 “平台”上的商品种类及价格进行相应的调整，
        并在调整后的2个工作日内以公告、书面或通知等形式告知乙方。<br/>
        6、甲乙双方系平等的合作关系，并无隶属或管理与被管理关系，双方亦不存在劳动及劳务关系。
        但双方均有权按照协议约定监督另一方的协议履行情况。<br/>

        第二项 乙方权利和责任<br/>
        1、乙方应当利用自己的人脉资源或网络资源，通过合法渠道对标的产品进行宣传，并推荐他人下载APP进行游戏。
        乙方在宣传及网络推广时，不得进行虚假宣传，不得采用违反国家法律法规、损害社会公序良俗的表述。
        如违反前述内容而产生的任何法律责任及法律制裁，乙方应当自担，与甲方无关。<br>
        2、乙方有权购买钻石并组织玩家进行游戏，但需按约定及时足额向甲方支付各项产品费用，并且以不得低于向甲方的采购价销售；<br/>
        3、 为保证良好的市场运行环境，乙方在销售甲方产品时必须严格遵守甲方销售策略。未经甲方书面授权或许可情况下，
        乙方不得私自开展销售相关活动;对于乙方依法、如约进行的推介及宣传行为，甲方会酌情予以奖励，奖励方式由双方另行协商议定。<br/>
        4、 乙方同意并认可甲方发行的可以在“平台”使用的相关产品;<br/>
        5、 乙方在标的产品的游戏活动应遵守中华人民共和国法律、法规，遵守甲方的相关管理规定（包括但不限于管理办法、禁止性和限制性行为等），
        并自行承担因游戏活动直接或间接引起的一切法律责任;<br/>
        6、 乙方可依照游戏设定的方式和程序规则，自主选择进行竞赛或游戏，并对在游戏活动产生后果负责，承担相应责任和损失，
        包括经济损失和精神损害。乙方承诺不利用标的产品的使用、游戏、活动、交易进行的非法活动。甲方对乙方得此类活动也不承担任何责任。<br/>
        7、 禁止利用标的产品进行法律、法规禁止的活动。乙方不得自行组织或容许他人利用标的产品从事赌博等违法活动。
        如发现他人利用标的产品从事赌博等违法活动，应及时制止、拒绝或立即向甲方报告，并协助甲方通过注销用户ID、警告或移送司法机关等方式，
        与甲方共同维护国家法律并防止违法犯罪行为发生。<br/>
        8、乙方为推介及宣传需要采取微信群或QQ群进行宣传的，目的应为鼓励玩家进行良性的线下互动，正面宣传甲方公司及游戏形象，
        乙方应注意引导正确的舆论及宣传导向，必须严格按照法律规定及公司管理规定进行管理，不得在宣传中有容许他人进行赌博等违法犯罪行为的内容。
        如一旦发现此类情况，代理商应立即制止，必要时将违规人员做退群处理，情节严重的，应立即向公司或有关部门反映。<br/>
        9、乙方不得有损害甲方及其他公司和玩家形象、利益的行为。<br/>
    </p>

    <h5>二、其它</h5>
    <p>
        第一项 保密条款<br/>
        1、 合作双方均有保密的义务，不得向任何第三方披露或者透露有关本协议内容项下的任何信息及另一方的商业秘密。
        不得直接或间接利用该保密信息为自己的商业目的或其他目的使用；<br/>
        2、 如果本协议因任何原因终止或不再履行，合作双方不得将任何有关本协议内容的信息披露或透露给任何第三方；<br/>
        3、 上述条款不因本协议的提前终止、无效或撤销而无效；<br/>

        第二项 违约责任<br/>
        1、本协议有其它条款对违约责任约定的，从其约定。其它条款没有约定的，违约方应依照有关法规或公平诚信原则赔偿守约方的损失。
        乙方违反本协议约定或违反有关法律法规规定的，甲方有权终止与乙方合作关系，一切后果由乙方承担。<br/>

        第三项 纠纷处理<br/>
        1、合作双方在本协议执行过程中遇有纠纷，首先应当协商解决，协商不成，可向甲方所在地法院提起诉讼。<br/>

        第四项 不可抗力及其他<br/>
        1、任何由于黑客攻击、计算机病毒侵入或发作、电信部门技术调整导致之影响、因政府管制而造成的暂时性关闭、由于第三方原因(包括不可抗力，
        例如国际出口的主干链路及国际出口电信提供商一方出现故障、火灾、水灾、雷击、地震、洪水、台风、龙卷风、火山爆发、瘟疫和传染病流行、
        罢工、战争或暴力行为或类似事件等)造成“平台”不能正常发布，以及其它影响网络正常经营之不可抗力及其他任何非因甲方过错而造成的个人资料泄露、
        丢失、被盗用或被窜改等的情况发生时，双方各不承担责任，各自的损失后果各自承担。<br/>
        2、 若因不可抗拒力因素导致本协议无法继续执行，本协议终止；<br/>
        3、 如确定因一方管理及工作失误所造成的一切损失，由失误方独立承担一切损失。<br/>
    </p>

    <button class="btn btn-default btn-block" onclick="$('#win').window('close');">确&nbsp;&nbsp;定</button>
</div>

<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp" %>

<%--<script src="../common/js/login.js"></script>--%>
<script>
    //表单验证
    $(function () {
        $("#form_login").validate({
//            submitHandler:function(form){
//                $.post("login", $(form).serialize(), function(data) {
//                    //callBack
//                });
//            },
            rules: {
                cellPhone: {
                    required: true,
                    digits: true,
                    rangelength: [11, 11],
                    min: 0
                },
                loginPassword: {
                    required: true
                }
            },
            messages: {
                cellPhone: {
                    required: "手机号不能为空!",
                    digits: "请输入正确的手机号!",
                    rangelength: "请输入正确的手机号!",
                    min: "请输入正确的手机号!"
                },
                loginPassword: {
                    required: "密码不能为空!"
                }
            }
        });
    });

    //    function checkCellPhone() {
    //        var mobile = /^1(3|4|5|6|7|8|9)\d{9}$/;
    //        var uid = document.getElementById("cellPhone");
    //        if (!mobile.test(uid.value)) {
    //            return "请输入正确的手机号!";
    //        }
    //    }

    //    function regCheck() {
    //        var mobile = /^1(3|4|5|6|7|8|9)\d{9}$/;
    //        var uid = document.getElementById("cellPhone");
    //        var upwd = document.getElementById("passWord");
    //        var flag = true;
    //        if (uid.value == '') {
    //            document.getElementById("msg2").innerHTML = '手机号不能为空!';
    //            flag = false;
    //        } else if (!mobile.test(uid.value)) {
    //            document.getElementById("msg2").innerHTML = '请输入正确的手机号!';
    //            flag = false;
    //        }
    //        if (upwd.value == '') {
    //            document.getElementById("msg1").innerHTML = '密码不能为空!';
    //            flag = false;
    //        }
    //        return flag;
    //    };
    //
    //    function testCellphone() {
    //        var mobile = /^1(3|4|5|6|7|8|9)\d{9}$/;
    //        var cellphone = $.trim($("#cellPhone").val());
    //        $("#msg").html("");
    //        $("#msg1").html("");
    //        if (cellphone == '') {
    //            $("#msg2").html("手机号不能为空!");
    //        } else if (!mobile.test(cellphone)) {
    //            $("#msg2").html("请输入正确的手机号!");
    //        } else {
    //            $("#msg2").html("");
    //        }
    //    };
    //
    //    function testPass() {
    //        var pass = $.trim($("#passWord").val());
    //        if (pass == '') {
    //            $("#msg1").html("密码不能为空!");
    //        } else {
    //            $("#msg1").html("");
    //        }
    //    };
</script>