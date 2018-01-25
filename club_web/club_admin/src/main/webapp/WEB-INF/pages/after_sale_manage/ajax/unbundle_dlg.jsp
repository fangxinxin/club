<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${clubUser!=null}">
    <table class="table table-hover">
        <tbody>
            <tr>
                <td>玩家昵称：<span id="gameNickName">${clubUser.gameNickName}</span></td>
                <%--<td>所在俱乐部：${club.clubName}</td>--%>
                <td>
                    所在俱乐部：
                    <select id="clubName" name="clubName">
                        <c:forEach items="${clubList}" var="club">
                            <option  <c:if test="${club.getColumnValue('clubId') == clubUser.clubId}">selected</c:if> value="${club.getColumnValue('clubId')}">
                                    ${club.getColumnValue('clubName')}
                            </option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>玩家ID：<span id="userId">${clubUser.gameUserId}</span></td>
                <td>所在俱乐部ID：<span id="clubId">${clubUser.clubId}</span></td>
            </tr>
            <tr>
                <td>当前钻石数：${diamond}</td>
                <td>
                    所在俱乐部代理商：${promoter.nickName}
                    <span>
                        <c:if test="${promoter.pLevel==-1}">(特级代理商)</c:if>
                        <c:if test="${promoter.pLevel==1}">(一级代理商)</c:if>
                        <c:if test="${promoter.pLevel==2}">(二级代理商)</c:if>
                    </span>
                </td>
            </tr>
            <tr>
                <td>创建日期：
                    <fmt:formatDate value="${clubUser.createTime}" pattern="yyyy-MM-dd h:m:s"/>
                </td>
                <td>所在俱乐部人数：${userNum}</td>
            </tr>
            <tr>
                <td>所在俱乐部代理商ID：<span id="parentId">${club.promoterId}</span></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <a id="check" class="btn red sbold" onclick="check()" data-toggle="modal" <%--href="#div1"--%>>
                        解除绑定
                    </a>
                </td>
            </tr>
        </table>

</c:if>

<c:if test="${empty clubUser}">
    <div class="row">
        <div class="form-group col-md-12 col-xs-12 mt-lg">
            <div class="heading heading-border heading-middle-border heading-middle-border-center">
                <h4>查询信息不存在!</h4>
            </div>
        </div>
    </div>
</c:if>

<div id="div1" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">解除绑定确认</h4>
            </div>
            <div class="modal-body">
                <div class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12 center mt-lg">
                            <h5 class="font-black font-lg">玩家昵称：${clubUser.gameNickName}</h5>
                            <h5 class="font-black font-lg">玩家ID：${clubUser.gameUserId}</h5>
                            <h5 class="font-black font-lg">脱离俱乐部：${club.clubName}</h5>
                        </div>
                        <div class="col-md-12 center mt-lg">
                            <b class="font-red font-lg">将在&lt;${gameName}&gt;将使&lt;${clubUser.gameNickName}&gt;脱离&lt;${club.clubName}&gt;，请确认信息是否正确</b>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer center">
                <button id="unbundle" data-dismiss="modal" onclick="removePlayer(${clubUser.gameUserId},${clubUser.clubId})" type="button" class="btn green"> 确&nbsp;&nbsp;认 </button>
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>
            </div>
        </div>
    </div>
</div>



<script>

    $("#clubName").change(function(){
        var clubId=$("#clubName").val();
        $('#promoterInfo').load('after_sale_manage/searchPlayer?gameUserId='+'${clubUser.gameUserId}'+'&clubId='+clubId);
    });


    function check(){
        $('#div1').modal('show');
    }

    function removePlayer(gameUserId,clubId){
        $.post("after_sale_manage/removePlayer", {gameUserId:gameUserId,clubId:clubId}, function(data) {
            var content;
            if(data == 1) {
                content =  '<h4 class="mt-xlg font-black center">解除绑定成功！</h4>';
                layerMsg_area_r2('解除绑定',content,'500px','300px','after_sale_manage/unbundle');
            }else if(data == 0) {
                content =  '<h4 class="mt-xlg font-black center">解除绑定失败！</h4>';
                layerMsg_area_r2('解除绑定',content,'500px','300px','after_sale_manage/unbundle');
            }else if(data == 'qz'){
                content =  '<h4 class="mt-xlg font-black center">不能解绑群主！</h4>';
                layerMsg_area_r2('解除绑定',content,'500px','300px','after_sale_manage/unbundle');
            }
        });
    }





</script>


