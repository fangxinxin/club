<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${!empty club and !empty promoter}">
  <ul class="list list-unstyled ml-md">
    <li class="mb-none">代理商昵称：${promoter.getColumnValue("nickName")}</li>
    <li class="mb-none">代理商级别：<c:if test="${promoter.getColumnValue('pLevel')==1}">一级代理商</c:if>
      <c:if test="${promoter.getColumnValue('pLevel')==2}">二级代理商</c:if>
      <c:if test="${promoter.getColumnValue('pLevel')==-1}">特级代理商</c:if></li>
    <li class="mb-none">直属上级：${promoter.getColumnValue("parentid")}</li>
    <li class="mb-none">游戏ID：${promoter.getColumnValue("gameuserid")}</li>
    <li class="mb-none">拥有俱乐部：${club.getColumnValue("clubname")}</li>
    <li class="mb-none">拥有俱乐部ID：${club.getColumnValue("id")}</li>
    <li>拥有俱乐部人数：${num}</li>
    <li class="mb-none">充值累计金额：${promoter.getColumnValue("totalPay")}</li>
  </ul>
</c:if>
<c:if test="${empty club or empty promoter}">
  <div class="form-group col-md-12 col-xs-12 mt-xl">
    <div class="heading heading-border heading-middle-border heading-middle-border-center">
      <h4>暂无数据</h4>
    </div>
  </div>
</c:if>
<div class="dialog-button">
  <a href="javascript:void(0)" class="btn btn-success" style="width:100%;height:35px"
     onclick="$('#dlgId').dialog('close')">确定</a>
</div>
