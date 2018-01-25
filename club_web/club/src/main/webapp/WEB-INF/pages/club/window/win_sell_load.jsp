<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="center">

    <!-- Start sell -->
    <c:if test="${isAllowSell}">
        <c:if test="${not empty clubUser}">
            <c:set var="gameUserId" value="${clubUser.getColumnValue('userId')}"/>
            <c:set var="gameNickName" value="${clubUser.getColumnValue('nickName')}"/>

            <c:if test="${isClubUser}">
                <ul class="list list-unstyled text-left mt-md ml-lg">
                    <li class="mb-none">玩家名称：${gameNickName}</li>
                    <li class="mb-none">玩家ID：${gameUserId}</li>
                    <li class="mb-none">对方剩余钻石：${gameCard}</li>
                </ul>
                <div class="col-xs-12 form-group mt-sm">
                    <input type="number" min="1" maxlength="10" max="${promoterGameCard}" onkeyup='this.value=this.value.replace(/\D/gi,"")'
                           class="form-control center" id="sellNum" placeholder="请输入发货数量" style="width: 100%">
                    <input type="text" id="gameUserId" hidden="hidden" value="${gameUserId}">
                </div>

            </c:if>
            <c:if test="${isSellCheck}">
                <div class="col-xs-12 mt-lg center">
                    <h4 class="bold">确认向${gameNickName}销售${sellNum}钻石?</h4>
                    <h5>ID: ${gameUserId} </h5>
                </div>
                <div class="col-xs-12 form-group">
                    <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
                       onclick="sellGameCard('${gameUserId}', '${sellNum}')">确认</a>
                    <a href="javascript:close_layer();" class="btn btn-default btn-block" style="height:35px">取消</a>
                </div>
            </c:if>

        </c:if>

        <c:if test="${empty clubUser}">
            <div class="form-group col-md-12 col-xs-12 mt-xl">
                <div class="heading heading-border heading-middle-border heading-middle-border-center">
                    <h4>该玩家不存在</h4>
                </div>
            </div>
        </c:if>
    </c:if>


    <%--不允许销售给任意玩家--%>
    <c:if test="${isAllowSell == false}">
        <!-- Start sell -->
        <c:if test="${not empty clubUser and not empty clubUserModel}">
            <c:set var="gameUserId" value="${clubUser.getColumnValue('userId')}"/>
            <c:set var="gameNickName" value="${clubUser.getColumnValue('nickName')}"/>

            <c:if test="${isClubUser}">
                <ul class="list list-unstyled text-left mt-md ml-lg">
                    <li class="mb-none">玩家名称：${gameNickName}</li>
                    <li class="mb-none">玩家ID：${gameUserId}</li>
                    <li class="mb-none">对方剩余钻石：${gameCard}</li>
                </ul>
                <div class="col-xs-12 form-group mt-sm">
                    <input type="number" min="1" maxlength="10" max="${promoterGameCard}" onkeyup='this.value=this.value.replace(/\D/gi,"")'
                           class="form-control center" id="_sellNum" placeholder="请输入发货数量" style="width: 100%">
                    <input type="text" id="_gameUserId" hidden="hidden" value="${gameUserId}">
                </div>

            </c:if>
            <c:if test="${isSellCheck}">
                <div class="col-xs-12 mt-lg center">
                    <h4 class="bold">确认向${gameNickName}销售${sellNum}钻石?</h4>
                    <h5>ID: ${gameUserId} </h5>
                </div>
                <div class="col-xs-12 form-group">
                    <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
                       onclick="sellGameCard1('${gameUserId}', '${sellNum}')">确认</a>
                    <a href="javascript:close_layer();" class="btn btn-default btn-block" style="height:35px">取消</a>
                </div>
            </c:if>

        </c:if>

        <c:if test="${empty clubUser or empty clubUserModel}">
            <c:if test="${empty clubUser}">
                <div class="form-group col-md-12 col-xs-12 mt-xl">
                    <div class="heading heading-border heading-middle-border heading-middle-border-center">
                        <h4>该玩家不存在</h4>
                    </div>
                </div>
            </c:if>
            <c:if test="${empty clubUserModel and not empty clubUser}">
                <div class="form-group col-md-12 col-xs-12 mt-xl">
                    <div class="heading heading-border heading-middle-border heading-middle-border-center">
                        <h4>只能销售给本俱乐部成员</h4>
                    </div>
                </div>
            </c:if>
        </c:if>

    </c:if>


    <!-- End sell -->



</div>


<script charset="utf-8">

    function sellCheck(gameUserId,sellNum) {
        layer.close(layer.index);
        var pNum = parseInt('${promoterGameCard}');
        if(sellNum <= 0 || pNum < sellNum) {
            $.messager.alert('', '<h4 class="center bold red mt-md">请正确输入发货数量！</h4>', '');
        } else {
            layerMsg_url("提示信息", "win/sell_check?gameUserId="+gameUserId+"&sellNum="+sellNum);
        }

    };

    function layerMsg_url(title,url) {
        $.post(url, {}, function(str){
            layer.open({
                type: 1
                ,title: [title, 'text-align: center; padding-left: 78px; font-size: 16px;']
                ,shade: 0.6 //遮罩透明度
                ,shadeClose :true
                ,btnAlign:'c'
                ,anim: 1 //0-6的动画形式，-1不开启
                ,content: str //注意，如果str是object，那么需要字符拼接。
            });
        });
    }

    function sellGameCard(gameUserId,sellNum) {
        layer.close(layer.index);
        $.post("win/sellAllGameCard", {sellNum: sellNum, gameUserId: gameUserId}, function(data){
            var json = JSON.parse(data);
            if(json.isSuccess) {
                $.messager.alert(''
                        , '<h4 class="center bold red mt-md">销售成功!</h4>'
                        , ''  //图标
                        , function() {
                            setTimeout(function() {
                                reloadTb();
                            }, 1500);
                        }
                );
            } else {
                $.messager.alert('提示信息','<h5 class="center bold red mt-lg">销售失败!原因:'+ json.remark +'</h5>');
            }

        });
    }


    function sellGameCard1(gameUserId,sellNum) {
        layer.close(layer.index);
        $.post("sellGameCard", {sellNum: sellNum, gameUserId: gameUserId}, function(data){
            var json = JSON.parse(data);
            if(json.isSuccess) {
                $.messager.alert(''
                        , '<h4 class="center bold red mt-md">销售成功!</h4>'
                        , ''  //图标
                        , function() {
                            setTimeout(function() {
                                location.reload();
                            }, 1500);
                        }
                );
            } else {
                $.messager.alert('提示信息','<h5 class="center bold red mt-lg">销售失败!原因:'+ json.remark +'</h5>');
            }

        });
    }

</script>