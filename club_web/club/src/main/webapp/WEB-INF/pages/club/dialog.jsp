<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${!empty club and dlgId == 1}">
    <ul class="list list-unstyled ml-md">
        <li class="mb-none">玩家名称：${club.getColumnValue("gameNickName")}</li>
        <li class="mb-none">玩家ID：${club.getColumnValue("gameUserId")}</li>
        <li class="mb-none">当前钻石：${point}</li>
        <li class="mb-none">累计购买钻石：${totalSellNum}</li>
    </ul>
    <div class="dialog-button">
        <a href="javascript:void(0)" class="btn btn-secondary btn-block" style="height:35px"
           onclick="removeClubUser('${club.getColumnValue('gameUserId')}')">踢出俱乐部</a>
    </div>
</c:if>
<c:if test="${!empty club and dlgId == 2}">
    <c:set value="${club.getColumnValue('gameNickName')}" var="gameNickName"/>
    <input id="gameNickName" hidden="hidden" value="${gameNickName}">
    <ul class="list list-unstyled ml-md">
        <li class="mb-none">玩家名称：${gameNickName}</li>
        <li class="mb-none">玩家ID：${club.getColumnValue("gameUserId")}</li>
        <li class="mb-none">对方剩余钻石：${point}</li>
    </ul>
    <div class="dialog-button mt-sm">
        <input type="number" min="1" max="${gameCard}" onkeyup='this.value=this.value.replace(/\D/gi,"")'
               class="input-sm center" id="sellNum" placeholder="请输入发货数量" style="width: 100%">
    </div>
    <div class="dialog-button mt-md">
        <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
           onclick="sellGameCard('${club.getColumnValue("gameUserId")}', $('#sellNum').val())">确认</a>
    </div>
</c:if>
<c:if test="${empty club}">
    <div class="form-group col-md-12 col-xs-12 mt-xl">
        <div class="heading heading-border heading-middle-border heading-middle-border-center">
            <h4>暂无数据</h4>
        </div>
    </div>
</c:if>
<div class="dialog-button">
    <a href="javascript:void(0)" class="btn btn-default btn-block" style="height:35px"
       onclick="$('#dlgId').dialog('close')">取消</a>
</div>

<script>

    function sellGameCard(gameUserId, _sellNum) {
        if (_sellNum == "") {
            _sellNum = 0;
        }
        var sellNum = parseInt(_sellNum);
        var gameNickName = $("#gameNickName").val().replace(/"([^"]*)"/g, "$1").replace(/'([^']*)"/g, "$1");
        $.messager.confirm('销售钻石'
                , '<h4 class="center bold">确认向'+ gameNickName +'销售 '+ sellNum +' 钻石?</h4>' +
                '<h5 class="center">ID: '+ gameUserId +'</h5>'
                , function (data) {
                    //确定
                    if(data){
                        var gameCard = parseInt('${gameCard}');
                        if(sellNum <= 0){
                            $.messager.alert('提示信息','<h4 class="center bold red mt-lg">出售钻石数目必须大于0</h4>');
                            return;
                        }

                        if(gameCard >= sellNum ) {
                            $.post("sellGameCard", {sellNum: sellNum, gameUserId: gameUserId}, function(data){
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
                        } else {
                            $.messager.alert('提示信息','<h4 class="center bold red mt-lg">代理商“钻石不足”！</h4>');
                        }
                    }
                    //取消
                    else{

                    }

                }
        );

        //关闭上级弹窗
        $('#dlgId').dialog('close');

    };

    function removeClubUser(gameUserId) {
        $.post("removeClubUser", {gameUserId: gameUserId}, function (data) {
            var json = JSON.parse(data);
            if(json.isSuccess) {
                $.messager.alert('提示信息'
                        , '<h4 class="center bold red mt-lg">'+ json.remark +'</h4>'
                        , ''  //图标
                        , function() {
                            setTimeout(function () {
                                reloadTb();
                            }, 1500);
                        });
            } else {
                $.messager.alert('提示信息','<h4 class="center bold red mt-lg">'+ json.remark +'</h4>');
            }
        })

        //关闭上级弹窗
        $('#dlgId').dialog('close');

    };

</script>
