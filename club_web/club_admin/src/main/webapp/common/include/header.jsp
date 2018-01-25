<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header id="header" class="header-narrow" data-plugin-options='{"stickyEnabled": true, "stickyEnableOnBoxed": true, "stickyEnableOnMobile": true, "stickyStartAt": 1, "stickySetTop": "1"}'>
    <div class="header-body" style="background-color: #383f48">

        <div class="header-top header-top-style-2">
            <div class="row">
                <div class="col-md-12">
                    <p class="pull-left hidden-xs ml-md">
                        <b class="font-lg"><i class="fa fa-home"></i> &nbsp; 史蒂夫游戏管理后台</b>
                    </p>
                    <p class="pull-right mr-md">
                        <span>账户登录：${remark}【${realName}】</span> |
                        <a class="btn blue btn-xs" href="javascript:;" onclick="getContent('user/password')">修改密码</a> |
                        <a class="btn blue btn-xs" href="logout">注销</a>
                    </p>
                </div>
            </div>
        </div>


        <div class="header-container header-nav header-nav-bar header-nav-bar-quaternary">
            <div class="row ml-md">
                <button class="btn header-btn-collapse-nav" data-toggle="collapse" data-target=".header-nav-main">
                    <i class="fa fa-bars"></i>
                </button>
                <div class="header-nav-main header-nav-main-light header-nav-main-effect-2 header-nav-main-sub-effect-1 collapse">
                    <nav>
                        <ul class="nav nav-pills" id="mainNav">
                            <c:if test="${not empty rootMenus}">

                                <c:forEach items="${rootMenus}" var="menu">
                                    <li>

                                        <a class="dropdown" href="javascript:;" onclick="getMenu('${menu.getColumnValue('menu')}');">
                                                ${menu.getColumnValue('name')}
                                        </a>

                                    </li>
                                </c:forEach>

                            </c:if>
                        </ul>
                    </nav>
                </div>

                <div class="mt-md mr-md">
                    <div class="pull-right">
                        <b class="bold">选择游戏：</b>
                        <a class="btn btn-primary" data-toggle="modal" href="#gameList" style="width: 120px">${gameName}</a>
                    </div>
                </div>
            </div>
        </div>

        <%-- 手机支持 --%>
        <%--<div class="page-header navbar navbar-fixed-top visible-xs">--%>
        <%--<!-- Begin Header Inner -->--%>
        <%--<div class="page-header-inner ">--%>

        <%--<!-- Begin Menu -->--%>
        <%--<a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">--%>
        <%--<span></span>--%>
        <%--</a>--%>
        <%--<!-- End Menu -->--%>

        <%--</div>--%>
        <%--<!-- End Header Inner -->--%>
        <%--</div>--%>

    </div>
</header>

<!-- Start 模态 -->
<div id="gameList" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width:860px">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">请选择游戏</h4>
            </div>
            <div class="modal-body">
                <div class="scroller mt-lg" style="height:600px" data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12">

                            <c:if test="${not empty gameList}">
                                <c:forEach items="${gameList}" var="row">
                                    <c:if test="${row.getColumnValue('parentId') != 0}">
                                        <div class="col-md-3">
                                            <button type="button" class="btn btn-primary btn-block" onclick="changeGame('${row.getColumnValue('gameId')}');">
                                                    ${row.getColumnValue('preName')}${row.getColumnValue('name')}
                                            </button>
                                            <div class="col-md-12 mt-lg"></div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </c:if>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- End 模态 -->