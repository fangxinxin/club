<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div class="mt-xlg">
  <%--<label class="col-md-3 control-label text-right">分配权限：</label> --%>
  <div class="col-md-12">
    <c:if test="${not empty club && not empty promoter}">
      <div class="" id="modal_row_lock">
        <div class="col-md-6">
          <h5>玩家昵称：${promoter.nickName}</h5>
          <h5>玩家ID：${promoter.gameUserId}</h5>
          <h5>手机号：${promoter.cellPhone}</h5>
        </div>
        <div class="col-md-6">
          <h5>创建俱乐部：${club.clubName}</h5>
          <h5>取消：${level}</h5>
          <h5>代理商姓名：${promoter.realName}</h5>
        </div>
        <div class="col-md-12 center mt-lg">
          <b class="font-red font-lg2">
            将在< ${gameName} >取消< ${promoter.nickName} >的待转正代理商身份，请确认信息是否正确
          </b>
        </div>
      </div>
    </c:if>
  </div>
</div>

<div class="col-md-12 center mt-xlg">
  <button type="button" class="btn green" onclick="refuseCheckClub('${clubId}')"> 确&nbsp;&nbsp;认 </button>&nbsp;&nbsp;&nbsp;&nbsp;
  <button type="button" class="btn dark btn-outline" onclick="close_layer();"> 取&nbsp;&nbsp;消 </button>
</div>

