<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--代理升级中的查看详情--%>
<div id="dlg_check_detail" class="easyui-dialog" style="padding:20px 6px;width:90%;"
     data-options="inline:true,modal:true,closed:true,title:'代理信息'">
    <ul class="list list-unstyled ml-md">
        <li class="mb-none">二级代理商维持条件：</li>
        <li class="mb-none">一周内俱乐部成员打满20场。</li>
        <li class="mb-none">&nbsp;</li>
        <li class="mb-none">当前状态：</li>
        <li class="mb-none">俱乐部成员本周累计参与对局数：14局（未达成）</li>
        <%--  <li class="mb-none">代理商级别：<c:if test="${promoter.getColumnValue('pLevel')==1}">一级代理商</c:if>
              <c:if test="${promoter.getColumnValue('pLevel')==2}">二级代理商</c:if>
              <c:if test="${promoter.getColumnValue('pLevel')==-1}">特级代理商</c:if></li>--%>
    </ul>
    <div class="dialog-button">
        <a href="javascript:void(0)" class="btn btn-success btn-block" style="height:35px"
           onclick="$('#dlg_check_detail').dialog('close')">确定</a>
    </div>
</div>