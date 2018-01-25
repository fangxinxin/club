<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div id="memberDetail">

  <div class="mt-lg">

    <div class="col-md-3">
      <div class="form-group">
        <select class="form-control" id="memberType">
          <option value="0">全部</option>
          <option value="1" <c:if test="${memberType == 1}">selected="selected" </c:if> >新玩家</option>
          <option value="2" <c:if test="${memberType == 2}">selected="selected" </c:if>>老玩家</option>
        </select>
      </div>
    </div>

    <div class="col-md-12">
      <c:if test="${not empty memberList}">
        <div class="table-responsive">
          <table class="table table-hover table-striped table-bordered">
            <thead>
            <tr>
              <th>ID</th>
              <th>玩家ID</th>
              <th>玩家昵称</th>
              <th>是否为新玩家</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${memberList}" var="row" varStatus="i">
              <c:set value="${row.getColumnValue('isNewMember')==1 ? true : false}" var="isNewMember"/>
              <c:set value="${row.getColumnValue('isNewMember')==1 ? '是' : '否'}" var="memberType"/>
              <tr>
                <td>${i.index+1}</td>
                <td>${row.getColumnValue("gameUserId")}</td>
                <td>${row.getColumnValue("gameNickName")}</td>
                <td>${memberType}</td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </c:if>
      <c:if test="${empty memberList}">
        <div class="col-md-12 mt-lg">
          <div class="heading heading-border heading-middle-border heading-middle-border-center">
            <h4>暂无数据</h4>
          </div>
        </div>
      </c:if>
    </div>

  </div>

  <div class="col-md-12 center mt-lg">
    <button type="button" class="btn green" onclick="layer.close(layer.index)"> 确&nbsp;&nbsp;认 </button>
  </div>

</div>

<script>
  var url = "pre_sale_manage/ajax/memberDetail";
  var refresh = function() {
    var memberType = $('#memberType').val();
    $('#memberDetail').load(url, {clubId: ${clubId}, memberType: memberType});
  }

  $(function(){
    $('#memberType').change(function() {
      refresh();
    })
  });

</script>

