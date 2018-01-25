<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .notice {
        width: 10px;
        height: 10px;
        line-height: 10px;
        font-size: 10px;
        color: #fff;
        text-align: center;
        background-color: #f00;
        border-radius: 50%;
        position: relative;
        right: -75px;
        top: -3px;
    }
</style>

<c:if test="${isMember}">
    <div class="datagrid-toolbar bd center">
        <ul class="pagination pagination-sm" style="margin: 6px 0;">
            <li id="join_li" onclick="tab('win/member_joinMsg');"><a style="width: 100px" href="javascript:;">入会申请
                    <%--<span class=" pull-left" id="join">&nbsp;</span>--%> </a></li>
            <li id="quit_li" onclick="tab('win/member_leaveMsg');"><a style="width: 100px" href="javascript:;">离会申请<span
                    class=" pull-left" id="quit">&nbsp;</span></a></li>
            <%--<c:if test="${joinMax>1}">--%>
                <%--<li><a style="width: 100px" href="javascript:add();">添加会员</a></li>--%>
            <%--</c:if>--%>
        </ul>
    </div>

    <div id="t" class="datagrid-toolbar bd center mt-xs">
        <div id="load_content"></div>

    </div>
</c:if>


<script charset="utf-8">

    $("#load_content").load("win/member_joinMsg");

    //点击之后去掉红点
    //    $("#join_li").click(function(){
    //        $("#join").removeClass("notice");
    //    });
    $("#quit_li").click(function () {
        $("#quit").removeClass("notice");
    });

    function tab(url) {
        $("#load_content").load(url);
    }
    function add() {
        var str = "this.value=this.value.replace(/\\D/gi,\"\")";
        $("#load_content").html("<div><br/>" +
                "<input type='text' placeholder='请输入要添加的玩家ID' maxlength='10' id='userId' onkeyup='" + str + "'>&nbsp;&nbsp;" +
                "<input type='button' class='btn btn-sm btn-success' value='查询' onclick='queryInfo();'>" +
                "<br/><br/>" +
                "<div id='addUserInfo'></div>" +
                "</div>");
    }

    function queryInfo() {
        var userId = $('#userId').val();
        $.post('win/queryUserInfo',
                {userId: userId},
                function (data) {
                    if (data == '0') {
                        $('#addUserInfo').html("<h6 align='center' style='color:red;'>玩家ID输入错误，请重新输入！</h6>");
                    } else if (data == '1') {
                        $('#addUserInfo').html("<h6 align='center' style='color:red;'>该玩家已经是代理商，无法添加！</h6>");
                    } else {
                        $('#addUserInfo').html("<table class='table table-hover table-striped table-bordered tb1'>" +
                                "<tr><td>玩家ID</td><td>玩家昵称</td><td>操作</td></tr>" +
                                "<tr><td id='gameUserId'>" + userId + "</td><td id='nickName'>" + data + "</td><td><input type='button' class='btn btn-sm btn-success' value='添加入会' onclick='join();'></td></tr>" +
                                "</table>");
                    }
                }
        );
    }
    function join() {
        var gameUserId = $('#gameUserId').text();
        var nickName = $('#nickName').text();
        $.post('win/member_joinMsg_handle',
                {gameUserId: gameUserId, gameNickName: nickName, handle: 3},
                function (data) {
                    if (data == 'OK') {
                        layer.alert('成功添加玩家' + nickName + '！');
                    } else {
                        layer.alert(data);
                    }
                    add();
                }
        );
    }
    $(document).ready(function () {
        var hasJoinRequest = '${hasJoinRequest}';
        var hasQuitRequest = '${hasQuitRequest}';

//        if (hasJoinRequest == 'true') {
//            $('#join').addClass('notice');
//        }
        if (hasQuitRequest == 'true') {
            $('#quit').addClass('notice');
        }
    });

</script>