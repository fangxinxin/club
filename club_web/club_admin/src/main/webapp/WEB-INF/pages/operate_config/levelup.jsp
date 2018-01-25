<%@include file="/common/include/lib.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div>
    <h4 align="center"><b>升降级条件配置</b></h4>
    <hr />

    <div class="col-md-6">
        <label><b>二级代理商—>一级代理商</b></label>
        <table class="table table-hover table-striped table-bordered" cellspacing="0" cellpadding="0">
            <tbody>
            <tr>
                <th>条件序列</th>
                <th>升级条件</th>
            </tr>
            <tr>
                <td>1</td>
                <td>
                    <c:if test="${upFirst.totalPromoter ==null}">名下拥有超过__名代理商(<b style="color: red">待配置</b>)</c:if>
                    <c:if test="${upFirst.totalPromoter !=null}"> 名下拥有超过${upFirst.totalPromoter}名代理商</c:if>
                </td>
            </tr>
            <tr>
                <td>2</td>
                <td>
                    <c:if test="${upFirst.totalPay ==null}">一个月内购买钻石超过__元(<b style="color: red">待配置</b>)</c:if>
                    <c:if test="${upFirst.totalPay !=null}">一个月内购买钻石超过${upFirst.totalPay}元</c:if>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="col-md-12">
        <label>名下代理商人数要求：</label>
        <button class="btn btn-primary" style="margin-top: -2px" onclick="subtraction_1(this)"><strong>-</strong></button>
        <input id="two_nums" class="center" maxlength="3" min="0" type="number" oninput="if(value.length>3)value=value.slice(0,3)" style="height: 34px;" onblur="number_1(this)" value="<c:if test="${upFirst.totalPromoter ==null}">${upFirst_totalPromoter}</c:if>${upFirst.totalPromoter}"/>
        <button class="btn btn-primary" style="margin-top: -2px" onclick="add_1(this)"><strong>+</strong></button>
    </div>

    <div class="col-md-12 mt-md">
        <label>代理商充值金额要求：</label>
        <button class="btn btn-primary" style="margin-top: -2px" onclick="subtraction_100(this)"><strong>-</strong></button>
        <input id="twoPay" maxlength="6" min="0" type="number" oninput="if(value.length>6)value=value.slice(0,6)" class="center" style="height: 34px;" onblur="number_100(this)" value="<c:if test="${upFirst.totalPay ==null}">${toFirst_totalPay}</c:if>${upFirst.totalPay}"/>
        <button class="btn btn-primary" style="margin-top: -2px" onclick="add_100(this)"><strong>+</strong></button>   &nbsp;&nbsp;&nbsp;&nbsp;
        <a id="check1" class="btn green sbold" data-toggle="modal" href="#div1"> 保存修改 </a>
        <hr />
    </div>

    <div class="col-md-6 mt-md">
        <label><b>一级代理商—>特级代理商</b></label>
        <table class="table table-hover table-striped table-bordered" cellspacing="0" cellpadding="0">
            <tbody>
            <tr>
                <th>条件序列</th>
                <th>升级条件</th>
            </tr>
            <tr>
                <td>1</td>
                <td>
                    <c:if test="${upSuper.totalPromoter ==null}">名下拥有超过__名代理商(<b style="color: red">待配置</b>)</c:if>
                    <c:if test="${upSuper.totalPromoter !=null}"> 名下拥有超过${upSuper.totalPromoter}名代理商</c:if>
                </td>
            </tr>
            <tr>
                <td>2</td>
                <td>
                    <c:if test="${upSuper.totalPay ==null}">名下代理商一个月内充值总额超过__元(<b style="color: red">待配置</b>)</c:if>
                    <c:if test="${upSuper.totalPay !=null}">名下代理商一个月内充值总额超过${upSuper.totalPay}元</c:if>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="col-md-12">
        <label>名下代理商人数要求：</label>
        <button class="btn btn-primary" style="margin-top: -2px" onclick="subtraction_1(this)"><strong>-</strong></button>
        <input id="nums" class="center" maxlength="3" min="0" type="number" oninput="if(value.length>3)value=value.slice(0,3)" style="height: 34px;" onblur="number_1(this)" value="<c:if test="${upSuper.totalPromoter ==null}">${toSuper_totalPromoter}</c:if>${upSuper.totalPromoter}"/>
        <button class="btn btn-primary" style="margin-top: -2px" onclick="add_1(this)"><strong>+</strong></button>
    </div>

    <div class="col-md-12 mt-md">
        <label>名下代理商充值要求：</label>
        <button class="btn btn-primary" style="margin-top: -2px" onclick="subtraction_100(this)"><strong>-</strong></button>
        <input id="onePay" class="center" maxlength="6" min="0" type="number" oninput="if(value.length>6)value=value.slice(0,6)" style="height: 34px;" onblur="number_100(this)"  value="<c:if test="${upSuper.totalPay ==null}">${toSuper_totalPay}</c:if>${upSuper.totalPay}"/>
        <button class="btn btn-primary" style="margin-top: -2px" onclick="add_100(this)"><strong>+</strong></button>   &nbsp;&nbsp;&nbsp;&nbsp;
        <a id="check2" class="btn green sbold" data-toggle="modal" href="#div2"> 保存修改 </a>
        <hr />
    </div>

    <%--    <div class="col-md-6 mt-md">
            <label><b>特级代理商—>一级代理商</b></label>
            <table class="table table-hover table-striped table-bordered" cellspacing="0" cellpadding="0">
                <tbody>
                <tr>
                    <th>条件序列</th>
                    <th>降级条件</th>
                </tr>
                <tr>
                    <td>1</td>
                    <td>
                        <c:if test="${downFirst.totalPay ==null}">代理商一个月内充值未满__元(<b style="color: red">待配置</b>)</c:if>
                        <c:if test="${downFirst.totalPay !=null}">代理商一个月内充值未满${downFirst.totalPay}元</c:if>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="col-md-12">
            <label>代理商充值金额要求：</label>
            <button class="btn btn-primary" style="margin-top: -2px" onclick="subtraction3()"><strong>-</strong></button>
            <input id="superPay" class="center" maxlength="6" type="number" oninput="if(value.length>6)value=value.slice(0,6)" style="height: 34px;" onblur="number()" value="<c:if test="${downFirst.totalPay ==null}">${superToFirst_totalPay}</c:if>${downFirst.totalPay}"/>
            <button class="btn btn-primary" style="margin-top: -2px" onclick="add3()"><strong>+</strong></button>   &nbsp;&nbsp;&nbsp;&nbsp;
            <a id="check3" class="btn green sbold" data-toggle="modal" href="#div3"> 保存修改 </a>
            <br/><br/>
        </div>--%>

</div>

<div id="div1" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">升级条件确认</h4>
            </div>
            <div class="modal-body">
                <div class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12 center mt-lg">
                            <h4 class="font-black font-lg">名下代理商人数要求：
                                <c:if test="${upFirst.totalPromoter!=null}">${upFirst.totalPromoter}人——></c:if>
                                <span id="uf_totalP"></span> 人</h4>
                            <h4 class="font-balck font-lg">一个月内购买钻石要求：
                                <c:if test="${upFirst.totalPay!=null}">${upFirst.totalPay}元——></c:if>
                                <span id="uf_totalpay"></span>元</h4>
                        </div>
                        <div class="col-md-12 center mt-lg">
                            <b class="font-red font-lg">即将修改&lt;${gameName}&gt;升级条件，请确认信息是否正确</b>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer center">
                <button id="save1" data-dismiss="modal" onclick="" type="button" class="btn green"> 确认修改 </button>
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>
            </div>
        </div>
    </div>
</div>

<div id="div2" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">升级条件确认</h4>
            </div>
            <div class="modal-body">
                <div class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12 center mt-lg">
                            <h4 class="font-black font-lg">名下代理商人数要求：
                                <c:if test="${upSuper.totalPromoter!=null}">${upSuper.totalPromoter}人——></c:if>
                                <span id="us_totalP"></span> 人</h4>
                            <h4 class="font-black font-lg">名下代理商充值要求：
                                <c:if test="${upSuper.totalPay!=null}">${upSuper.totalPay}元——></c:if>
                                <span id="us_totalPay"></span> 元</h4>
                        </div>
                        <div class="col-md-12 center mt-lg">
                            <b class="font-red font-lg">即将修改&lt;${gameName}&gt;升级条件，请确认信息是否正确</b>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer center">
                <button id="save2" data-dismiss="modal" onclick="" type="button" class="btn green"> 确认修改 </button>
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>
            </div>
        </div>
    </div>
</div>

<div id="div3" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">升级条件确认</h4>
            </div>
            <div class="modal-body">
                <div class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12 center mt-lg">
                            <h4 class="font-black font-lg">名下代理商一个月内充值要求：
                                <c:if test="${downFirst.totalPay!=null}">${downFirst.totalPay}元——></c:if>
                                <span id="df_totalPay"></span> 元</h4>
                        </div>
                        <div class="col-md-12 center mt-lg">
                            <b class="font-red font-lg">即将修改&lt;${gameName}&gt;升级条件，请确认信息是否正确</b>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer center">
                <button id="save3" data-dismiss="modal" onclick="" type="button" class="btn green"> 确认修改 </button>
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>
            </div>
        </div>
    </div>
</div>



<script>


    $("#check1").click(function() {
        $("#uf_totalP").text($("#two_nums").val());
        $("#uf_totalpay").text($("#twoPay").val());
    });
    $("#check2").click(function() {
        $("#us_totalP").text($("#nums").val());
        $("#us_totalPay").text($("#onePay").val());
    });
    $("#check3").click(function() {
        $("#df_totalPay").text($("#superPay").val());
    });

    $("#save1").click(function() {

        var totalPay = $("#twoPay").val();
        var totalPromoter =  $("#two_nums").val();
        var levelUpType = 2;
        $.post("operate_config/levelUpConditon",{totalPay:totalPay,totalPromoter:totalPromoter,levelUpType:levelUpType}, function(data) {
            if(data == 1) {
                content =  '<h4 class="mt-xlg font-black center">升级条件成功！</h4>';
                layerMsg_area_r2('升级条件修改确认',content,'500px','300px','operate_config/levelUp');
            }else {
                content =  '<h4 class="mt-xlg font-black center">升级条件失败！</h4>';
                layerMsg('升级条件修改确认', content);
            }
        });
    });

    $("#save2").click(function() {

        var totalPay = $("#onePay").val();
        var totalPromoter =  $("#nums").val();
        var levelUpType = 1;
        $.post("operate_config/levelUpConditon",{totalPay:totalPay,totalPromoter:totalPromoter,levelUpType:levelUpType}, function(data) {
            if(data == 1) {
                content =  '<h4 class="mt-xlg font-black center">升级条件成功！</h4>';
                layerMsg_area_r2('升级条件修改确认',content,'500px','300px','operate_config/levelUp');
            }else {
                content =  '<h4 class="mt-xlg font-black center">升级条件失败！</h4>';
                layerMsg('升级条件修改确认', content);
            }
        });
    });

    $("#save3").click(function() {

        var totalPay = $("#superPay").val();
        var levelUpType = -1;
        $.post("operate_config/levelUpConditon",{totalPay:totalPay,levelUpType:levelUpType}, function(data) {
            if(data == 1) {
                content =  '<h4 class="mt-xlg font-black center">升级条件成功！</h4>';
                layerMsg_area_r2('升级条件修改确认',content,'500px','300px','operate_config/levelUp');

            }else {
                content =  '<h4 class="mt-xlg font-black center">升级条件失败！</h4>';
                layerMsg('升级条件修改确认',content);
            }
        });
    });

    function subtraction_1(thisObj){
        var m =$(thisObj).next().val();
        if (m<=1) {
            //如果文本框的值小于1,则设置值为0
            $(thisObj).next().val(0);
        }else {
            $(thisObj).next().val($(thisObj).next().val()-1);
        }
    }
    function subtraction_100(thisObj){
        var m =$(thisObj).next().val();
        if (m<=100) {
            //如果文本框的值小于1,则设置值为1
            $(thisObj).next().val(0);
        }else {
            $(thisObj).next().val($(thisObj).next().val()-100);
        }
    }

    function add_1(thisObj){
        var n = $(thisObj).prev().val();
        //parseInt() 将数值型字符串转换为数值
        $(thisObj).prev().val(parseInt(n)+1);
    }
    function add_100(thisObj){
        var n = $(thisObj).prev().val();
        //parseInt() 将数值型字符串转换为数值
        $(thisObj).prev().val(parseInt(n)+100);
    }

    function number_1(thisObj){
        var num = $(thisObj).val();
        //如果文本值为空,设置为1
        if (parseInt(num)=="") {
            $(thisObj).val(1);
        }
        //如果文本值为非纯数字,设置为1
        //isNaN()是否为非法数字
        if (isNaN(parseInt(num))) {
            $(thisObj).val(1);
        }
        //如果文本值小于1,设置为1
        if (parseInt(num)<=1) {
            $(thisObj).val(1);
        }
    }

    function number_100(thisObj){
        var num = $(thisObj).val();
        //如果文本值为空,设置为0
        if (parseInt(num)=="") {
            $(thisObj).val(0);
        }
        //如果文本值为非纯数字,设置为1
        //isNaN()是否为非法数字
        if (isNaN(parseInt(num))) {
            $(thisObj).val(0);
        }
        //如果文本值小于1,设置为1
        if (parseInt(num)<=100) {
            $(thisObj).val(0);
        }
    }

</script>