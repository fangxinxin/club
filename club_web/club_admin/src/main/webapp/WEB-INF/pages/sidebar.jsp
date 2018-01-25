<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>

<c:if test="${not empty currentMenuList}">
    <div class="page-sidebar navbar-collapse collapse">
        <ul class="page-sidebar-menu  page-header-fixed" data-keep-expanded="false" data-auto-scroll="true"
            data-slide-speed="200">
            <c:forEach items="${currentMenuList}" var="menu">
                <!-- 菜单名 -->
                <%--<li class="heading center">--%>
                    <%--<h3 class="uppercase">${menu.name}</h3>--%>
                <%--</li>--%>

                <!-- 二级菜单 -->
                <c:forEach items="${menu.childMenus}" var="child">
                    <li class="nav-item">
                        <a href="javascript:;" class="nav-link nav-toggle"
                           <c:if test="${empty child.childMenus}">onclick="getContent('${child.url}')"</c:if> >
                            <i class="${child.icon}"></i>
                            <span class="title">${child.name}</span>
                            <c:if test="${fn:length(child.childMenus)>0}">
                                &nbsp;&nbsp;<i class="fa fa-caret-down"></i>
                            </c:if>
                        </a>
                        <ul class="sub-menu">
                            <!-- 三级菜单 -->
                            <c:forEach items="${child.childMenus}" var="sub_child">

                                <li class="nav-item">
                                    <a href="javascript:;" onclick="getContent('${sub_child.url}')" class="nav-link nav-toggle">
                                        <span class="title">${sub_child.name}</span>
                                    </a>
                                </li>

                            </c:forEach>
                        </ul>
                    </li>
                </c:forEach>

            </c:forEach>
        </ul>

    </div>
</c:if>

<!-- Global script -->
<script src="http://download.stevengame.com/webResource/common/vendor/js/layout.min.js"></script>