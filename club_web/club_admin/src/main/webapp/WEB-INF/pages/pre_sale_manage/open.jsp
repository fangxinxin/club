<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">开通代理</h4>
                <div class="caption center-pill">
                </div>
            </div>
            <div class="portlet-body">
                <div style="text-align: center;line-height: 28px;">
                    <input type="text" placeholder="请输入需要开通代理的玩家ID" name="gameUserId" id="playId"
                           style="width: 250px;"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" value="查询" onclick="query();" class="btn btn-primary" style="width: 100px;"
                           id="sub"/>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function query() {
        var gameUserId = $.trim($("#playId").val());
        if (gameUserId == '') {
            layer.alert("请输入玩家ID！");
        } else if (isNaN(gameUserId)) {
            layer.alert("请输入纯数字ID！");
        } else {
            $('#content').load('pre_sale_manage/queryByUserId?gameUserId=' + gameUserId);
        }
    }
</script>