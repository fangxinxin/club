<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${isSell}">
    <div class="col-xs-12 form-group mt-lg" style="padding:0 5px 5px 5px;">
        <div class="col-xs-8">
            <input id="gameUserId" type="text" maxlength="10" onkeyup='this.value=this.value.replace(/\D/gi,"")'
                   class="form-control center ml-xs" placeholder="请输入要售钻的玩家ID">
        </div>
        <div class="col-xs-4">
            <input type="button" id="search" class="btn btn-block btn-primary" value="查询" onclick="search_sell_clubUser()"/>
        </div>
    </div>

    <div class="mt-xs" style="height: 190px">
        <div id="load_content"></div>
    </div>

    <c:if test="${isAllowSell}">
        <div class="col-xs-12 form-group">
            <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
               onclick="sellCheck($('#gameUserId').val(), $('#sellNum').val())">确认</a>
        </div>
    </c:if>
    <c:if test="${isAllowSell==false}">
        <div class="col-xs-12 form-group">
            <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
               onclick="sellCheck($('#gameUserId').val(), $('#_sellNum').val())">确认</a>
        </div>
    </c:if>

    <div class="col-xs-12 form-group">
        <a href="javascript:close_layer();" class="btn btn-default btn-block" style="height:35px">取消</a>
    </div>
</c:if>




<script charset="utf-8">

    function search_sell_clubUser() {
        var pattern = /\D/gi, gameUserId = $("#gameUserId").val();

        if(pattern.test(gameUserId)) {
            layer.close(layer.index);

            $.messager.alert('', '<h4 class="center bold red mt-md">该玩家不存在</h4>', '');
        } else {
            $("#load_content").load("win/sell_clubUser", { gameUserId: gameUserId });
        }
    }

</script>