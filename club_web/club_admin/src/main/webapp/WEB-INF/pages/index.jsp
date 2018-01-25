<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<title>史蒂夫游戏管理后台</title>

<!-- Header -->
<%@include file="/common/include/header.jsp"%>

<!-- Main -->
<div class="page-container">

    <!-- Sidebar -->
    <div class="page-sidebar-wrapper bold" id="sidebar"></div>

    <!-- Content -->
    <div class="page-content-wrapper">
        <div class="page-content" style="min-height:760px">
            <div class="row">
                <div class="col-md-12" id="content"></div>
            </div>
        </div>
    </div>

</div>


<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp"%>

<script>
    //初始化侧边栏
    $("#sidebar").load("getSidebar");

    //加载侧边栏
    function getMenu(menu) {
        $("#sidebar").load("getSidebar", { menu: menu});
    }

    //加载页面内容
    function getContent(url) {
        $("#content").load(url,function(responseTxt,statusTxt,xhr){
            if(statusTxt=="error"){
                location.reload();
            }
        });
    }

    function changeGame(gameId) {
        $.post("changeGame", { gameId: gameId });
        reload(500);
    }

    //刷新页面
    function reload(time) {
        setTimeout(function() {
            location.reload();
        }, time);
    }

    /** load 刷新 **/
    function reload_content(url) {
        setTimeout(function() {
            $("#content").load(url,function(responseTxt,statusTxt,xhr){
                if(statusTxt=="error"){
                    location.reload();
                }
            });
        }, 2000)
    }

</script>