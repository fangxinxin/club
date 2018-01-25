<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>
<style>
    .fa-lg {  font-size: 1.2em;  line-height: 24px;  vertical-align: -15%; }
    blockquote.with-borders {  padding: 15px 12px;  }
</style>

<%--
<c:if test="${isConfig==false}">
    <div class="center-vertical">
        <h4 class="black bold center">暂无数据</h4>
    </div>
</c:if>
--%>

<input type="hidden" id="level" value="${pLevel}"/>
<input type="hidden" id="totalPromoter" value="${totalPromoter}"/>
<input type="hidden" id="totalPayCondition" value="${totalPayCondition}"/>
<input type="hidden" id="totalPay" value="${totalPay}"/>
<input type="hidden" id="dNums" value="${dNums}"/>
<input type="hidden" id="dTotalPay" value="${dTotalPay}"/>

<ul class="list list-unstyled mt-sm">
        <li class=""><h4 class="ml-md">
            当前代理级别：
            <c:if test="${pLevel==-1}">特级代理商</c:if>
            <c:if test="${pLevel==1}">一级代理商</c:if>
            <c:if test="${pLevel==2}">二级代理商</c:if>
        </h4></li>
        <li class="rmt-sm"><h4 class="ml-md">
         <%--   <c:if test="${pLevel==-1}">降级为：一级代理商</c:if>--%>
            <c:if test="${pLevel==1}">下一级别：特级代理商</c:if>
            <c:if test="${pLevel==2}">下一级别：一级代理商</c:if>
        </h4></li>
        <li class="mt-sm">
            <h5 class="blue ml-md">
          <%--      <c:if test="${pLevel==-1}">
                    降级条件：一个月内特级代理及名下代理商充值总额未满${totalPayCondition}元自动降级。降级考核为成为特级代理商之后的自然月算起，当月不算做考核的时间节点。
                </c:if>--%>
                <c:if test="${pLevel==1}">
                    升级条件：名下必须有${totalPromoter}名以上代理商，且一个月内名下代理商购买钻石超过${totalPayCondition}元。
                </c:if>
                <c:if test="${pLevel==2}">
                    升级条件：一个月内购买钻石超过${totalPayCondition}元,且名下必须有${totalPromoter}名以上代理商。
                </c:if>
            </h5>
        </li>

        <li class="mt-lg">
            <hr class="short solid">
        </li>

        <li class="m-md">
            <blockquote class="with-borders col-md-10 col-xs-12 col-md-offset-2">
                <div class="col-md-12 col-xs-12">
                    <c:if test="${pLevel==-1}">
                        <h4 class="center fa-lg bold">代理商及名下代理商本月充值数：<span class="red"> ${dTotalPay}</span> 元</h4>
                    </c:if>
                    <c:if test="${pLevel==1}">
                        <h4 class="center bold">名下代理商个数: ${dNums} 名</h4>
                        <h4 class="center fa-lg bold">名下代理商本月充值数：<span class="red"> ${dTotalPay}</span> 元</h4>
                    </c:if>
                    <c:if test="${pLevel==2}">
                        <h4 class="center bold">名下代理商个数: ${dNums} 名</h4>
                        <h4 class="center fa-lg bold">一个月内购买钻石：<span class="red">${totalPay}</span> 元</h4>
                    </c:if>
                </div>
                <div class="col-md-12 col-xs-12">
                    <%--<h4 class="center" style="font-size: 80%;line-height: 1.42857143;color: #777">备注：<c:if test="${pLevel==-1}"> ${firstDate}</c:if><c:if test="${pLevel==1}">${startDate}</c:if><c:if test="${pLevel==2}">${startDate}</c:if> - ${endDate}</h4>--%>
                    <h4 class="center" style="font-size: 80%;line-height: 1.42857143;color: #777">备注：${startDate} - ${endDate}</h4>
                </div>
            </blockquote>
        </li>


        <%--<li class="mt-xlg">--%>
            <%--<div class="col-md-12 col-xs-12">--%>
                <%--<div class="dialog-button col-xs-6 col-xs-offset-3">--%>
                    <%--<c:if test="${pLevel==-1}"></c:if>--%>
                    <%--<c:if test="${pLevel==1}">--%>
                        <%--<button  id="level1" class="btn btn-primary btn-block" style="height:35px"--%>
                           <%--onclick="checkLevelUP(${dNums},${dTotalPay},${totalPromoter},${totalPayCondition})">确认升级</button>--%>
                    <%--</c:if>--%>
                    <%--<c:if test="${pLevel==2}">--%>
                        <%--<button id="level2" class="btn btn-primary btn-block" style="height:35px"--%>
                           <%--onclick="checkLevelTwo(${dNums},${totalPay},${totalPromoter},${totalPayCondition})">确认升级</button>--%>
                    <%--</c:if>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</li>--%>
        <%--<li class="mt-lg">--%>
                <%--<p class="mb-xs" style="padding: 10px 14px;">--%>
                    <%--<c:if test="${pLevel==1 && gameId != 5539 && gameId!=4156}">--%>
                        <%--备注：升级为特级代理商后，名下代理商每一次购买钻石，你可以获得其购买钻石数量<c:if test="${dictBonus ==null}">${_nonDirectPercent}</c:if><c:if test="${dictBonus !=null}">${nonDirectPercent}</c:if>%的返钻奖励！--%>
                    <%--</c:if>--%>
                    <%--<c:if test="${pLevel==2 && gameId != 5539 && gameId!=4156}">--%>
                        <%--备注：升级为一级代理商后，名下代理商每一次购买钻石，你可以获得其购买钻石数量<c:if test="${dictBonus ==null}">${_directPercent}</c:if><c:if test="${dictBonus !=null}">${directPercent}</c:if>%的返钻奖励！--%>
                    <%--</c:if>--%>
                <%--</p>--%>
        <%--</li>--%>
    </ul>

<!-- Dialog -->
<%@include file="/common/include/dialog/checkDetail.jsp" %>

<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp"%>

<script>

    $(document).ready(function(){
        var level = parseInt($("#level").val());
        var totalPay = parseFloat($("#totalPay").val()) ;
        var totalPromoter = parseInt($("#totalPromoter").val()) ;
        var totalPayCondition = parseFloat($("#totalPayCondition").val()) ;
        var dNums = parseInt($("#dNums").val()) ;
        var dTotalPay = parseFloat($("#dTotalPay").val()) ;

        if(level==2){
            if(dNums<totalPromoter || totalPay<totalPayCondition){
                $("#level2").attr('disabled', true);
            }
        }else if(level==1){
            if(dNums<totalPromoter || dTotalPay<totalPayCondition){
                $("#level1").attr('disabled', true);
            }
        }

    });

        function checkLevelTwo(dNums,dTotalPay,totalPromoter,totalPayCondition){
        if(dNums>=totalPromoter && dTotalPay>=totalPayCondition){
            $.ajax({
                type: "get",
                url: "agentUpCheck",
                async: false,
                dataType: "text",
                success: function (data) {
                    if(data==1) {
                        DiaLog(true, "恭喜您升级成功！", "agentUp");
                    }else{
                        DiaLog(false, "系统繁忙，请稍后再试！", "agentUp");
                    }
                }
            });
        }else{
            if (dNums < totalPromoter && dTotalPay < totalPayCondition) {
                DiaLog(false, "您没达成升级条件，暂时不能升级，再接再厉，加油升级哦！", "agentUp");
            } else if (dNums > totalPromoter && dTotalPay < totalPayCondition) {
                DiaLog(false, "代理商人数不足"+totalPromoter+"人，请再接再厉！", "agentUp");
            } else {
                DiaLog(false, "名下代理商充值数额不足"+totalPayCondition+"元，请再接再厉哦！", "agentUp");
            }

        }
    }

    function checkLevelUP(dNums,dTotalPay,totalPromoter,totalPayCondition) {
        if(dNums>=totalPromoter && dTotalPay>=totalPayCondition){

            $.ajax({
                type: "get",
                url: "agentUpCheck",
                async: false,
                dataType: "text",
                success: function (data) {
                    if(data==1) {
                        DiaLog(true, "恭喜您升级成功！", "agentUp");
                    }else{
                        DiaLog(false, "系统繁忙，请稍后再试！", "agentUp");
                    }
                }
            });

        }else {
            if (dNums < totalPromoter && dTotalPay < totalPayCondition) {
                DiaLog(false, "您没达成升级条件，暂时不能升级，再接再厉，加油升级哦！", "agentUp");
            } else if (dNums > totalPromoter && dTotalPay < totalPayCondition) {
                DiaLog(false, "代理商人数不足"+totalPromoter+"人，请再接再厉！", "agentUp");
            } else {
                DiaLog(false, "名下代理商充值数额不足"+totalPayCondition+"元，请再接再厉哦！", "agentUp");
            }

        }
    }


    function DiaLog(success,msg,url){
        if(success){
            //页面层
            layer.open({
                type: 1,
                area : [ '85%', '20em' ],
                title : '',
//                skin: 'layui-layer-rim', //加上边框
                closeBtn: 0, //不显示关闭按钮
                content:
                '<div class="col-md-2 col-xs-2"></div><div class="col-md-8 col-xs-8 center"><br style="line-height: 6em;"/>'+
                '<h6 class="text-center"><b>'+ msg + '</b></h6>'+
                '<div class="col-md-12 col-xs-12 form-group"><br/>'+
                '<a href="' + url + '" class="btn btn-link btn-block btn-primary">确认</a>'+
                '</div>'+
                '</div>'
            });
        }else{
            layer.open({
                type: 1,
                area : [ '85%', '20em' ],
                title : '',
//                skin: 'layui-layer-rim', //加上边框
                skin: '',
                closeBtn: 1, //不显示关闭按钮
//			content: '<h3 class="text-center"><b>' + msg + '</b></h3>'
                content:
                '<div class="col-md-2 col-xs-2"></div><div class="col-md-8 col-xs-8 center"><br style="line-height: 6em;"/>'+
                '<h6 class="text-center"><b>'+ msg + '</b></h6>'+
                '<div class="col-md-12 col-xs-12 form-group"><br/>'+
                '<a href="' + url + '" class="btn btn-link btn-block btn-primary">确认</a>'+
                '</div>'+
                '</div>'
            });
        }
    }

</script>