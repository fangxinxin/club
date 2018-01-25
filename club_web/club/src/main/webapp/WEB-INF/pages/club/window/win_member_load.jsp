<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
    .table > tbody > tr > td, .table > tbody > tr > th, .table > tfoot > tr > td, .table > tfoot > tr > th, .table > thead > tr > td, .table > thead > tr > th {
        padding: 3px;
        line-height: 1.42857143;
        vertical-align: top;
        border-top: 1px solid #ddd;
    }
</style>
<div class="center" style="height: 100%;">
    <!-- Start member_ -->
    <c:if test="${empty joinMember and empty quitRequestList}">
        <div class="form-group col-md-12 col-xs-12 mt-xl">
            <div class="heading heading-border heading-middle-border heading-middle-border-center">
                <h4>暂无数据</h4>
            </div>
        </div>
    </c:if>
    <c:if test="${not empty joinMember}">
        <div class="table-responsive">
            <table class="table table-bordered table-striped mb-none" style="font-size: 10px;">
                <thead>
                <tr>
                    <th>玩家ID</th>
                    <th>玩家昵称</th>
                    <th>申请时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${joinMember}" var="dt">
                    <tr>
                        <td>${dt.getColumnValue('gameUserId')}</td>
                        <td>
                            <c:if test="${dt.getColumnValue('gameNickName').length()>7}">${fn:substring(dt.getColumnValue('gameNickName'), 0, 6)}..</c:if>
                            <c:if test="${dt.getColumnValue('gameNickName').length()<8}">${dt.getColumnValue('gameNickName')}</c:if>
                        </td>
                        <td>${dt.getColumnValue('createTime')}</td>
                        <td>
                            <div class="center">
                                <input type="hidden" value="${dt.getColumnValue('id')}">
                                <input type="hidden" value="1">
                                <input type="button" class="btn btn-xs btn-success" value="同意"
                                       onclick="agree_join_confirm(this);">
                                <input type="hidden" value="${dt.getColumnValue('gameNickName')}">
                                <input type="hidden" value="${dt.getColumnValue('gameUserId')}">
                                <input type="hidden" value="${dt.getColumnValue('id')}">
                                <input type="hidden" value="2">
                                <input type="button" class="btn btn-xs btn-secondary" value="拒绝"
                                       onclick="refuse_join_confirm(this);">
                                <input type="hidden" value="${dt.getColumnValue('gameNickName')}">
                                <input type="hidden" value="${dt.getColumnValue('gameUserId')}">
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

    <c:if test="${not empty quitRequestList}">
        <div class="table-responsive">
            <table class="table table-bordered table-striped mb-none" style="font-size: 10px;">
                <thead>
                <tr>
                    <th>玩家ID</th>
                    <th>玩家昵称</th>
                    <th>申请时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${quitRequestList}">
                    <tr>
                        <td>${list.getColumnValue("gameUserId")}</td>
                        <td>
                            <c:if test="${list.getColumnValue('gameNickName').length()>7}">${fn:substring(list.getColumnValue('gameNickName'), 0, 6)}..</c:if>
                            <c:if test="${list.getColumnValue('gameNickName').length()<8}">${list.getColumnValue('gameNickName')}</c:if>
                        </td>
                        <td>${list.getColumnValue("createTime")}</td>
                        <td>
                            <div class="center">
                                <input type="hidden" value="${list.getColumnValue("id")}">
                                <input type="hidden" value="${list.getColumnValue("clubId")}">
                                <input type="hidden" value="${list.getColumnValue("gameUserId")}">
                                <input type="hidden" value="${list.getColumnValue("gameNickName")}">
                                <a href="javascript:;"
                                   onclick="agree_quit_confirm(this)"
                                   class="btn btn-xs btn-success">同意</a>
                                <a href="javascript:;"
                                   onclick="refuse_quit_confirm(this)"
                                   class="btn btn-xs btn-secondary">拒绝</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>
    </c:if>

</div>

<script>
    function agree_quitRequest(id, clubId, gameUserId, gameNickName) {

        url = "win/member_leaveMsg";
        $.post("win/agree_quit_request", {
            "id": id,
            "clubId": clubId,
            "gameUserId": gameUserId,
            "gameNickName": gameNickName
        }, function (data) {
            if (data == '1') {
                content = '<h5 class="mt-xlg font-black center">玩家' + gameNickName + '退出成功！</h5>';
                layer.alert(content);

            } else {
                content = '<h5 class="mt-xlg font-black center">玩家' + gameNickName + '退会失败！</h5>';
                layer.alert(content);

            }

            $("#load_content").load("win/member_leaveMsg");

        });
    }

    function refuse_quitRequest(id, clubId, gameUserId, gameNickName) {

        $.post("win/refuse_quit_request", {
            "id": id,
            "clubId": clubId,
            "gameUserId": gameUserId,
            "gameNickName": gameNickName
        }, function (data) {
            if (data == '1') {
                content = '<h5 class="mt-xlg font-black center">已拒绝玩家' + gameNickName + '退会！</h5>';
                layer.alert(content);
            } else {
                content = '<h5 class="mt-xlg font-black center">拒绝玩家' + gameNickName + '退会失败！</h5>';
                layer.alert(content);
            }

            $("#load_content").load("win/member_leaveMsg");

        });
    }

    function handle_joinRequest(id, gameUserId, gameNickName, handle) {
        $.post('win/member_joinMsg_handle',
                {id: id, gameUserId: gameUserId, gameNickName: gameNickName, handle: handle},
                function (data) {
                    if (data == 'OK') {
                        layer.alert('成功添加玩家' + gameNickName + '！');//拒绝加入
                    } else if (data == 'REFUSE') {
                        layer.alert('已拒绝玩家' + gameNickName + '入会！');//拒绝加入
                    } else {
                        layer.alert(data);//加入失败
                    }
                    $("#load_content").load("win/member_joinMsg");
                }
        );
    }

    function agree_join_confirm(obj) {
        var id = $(obj).prev().prev().val();
        var handle = $(obj).prev().val();
        var gameNickName = $(obj).next().val();
        var gameUserId = $(obj).next().next().val();

        layer.confirm('确定同意该玩家入会?',
                {title: '提示'},
                function () {
                    handle_joinRequest(id, gameUserId, gameNickName, handle);
                });
    }

    function refuse_join_confirm(obj) {
        var id = $(obj).prev().prev().val();
        var handle = $(obj).prev().val();
        var gameNickName = $(obj).next().val();
        var gameUserId = $(obj).next().next().val();

        layer.confirm('确定拒绝该玩家入会?',
                {title: '提示'},
                function () {
                    handle_joinRequest(id, gameUserId, gameNickName, handle);
                });
    }

    function agree_quit_confirm(thisObj) {
        var id = $(thisObj).prev().prev().prev().prev().val();
        var clubId = $(thisObj).prev().prev().prev().val();
        var gameUserId = $(thisObj).prev().prev().val();
        var gameNickName = $(thisObj).prev().val();

        layer.confirm('确定同意该玩家离开俱乐部?',
                {title: '提示'},
                function () {
                    agree_quitRequest(id, clubId, gameUserId, gameNickName);
                });
    }

    function refuse_quit_confirm(thisObj) {
        var id = $(thisObj).prev().prev().prev().prev().prev().val();
        var clubId = $(thisObj).prev().prev().prev().prev().val();
        var gameUserId = $(thisObj).prev().prev().prev().val();
        var gameNickName = $(thisObj).prev().prev().val();

        layer.confirm('确定拒绝该玩家退出俱乐部?',
                {title: '提示'},
                function () {
                    refuse_quitRequest(id, clubId, gameUserId, gameNickName);
                });
    }


</script>