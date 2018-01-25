<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp"%>
<style>
  .table td, .table th {
    text-align: center;
  }
</style>

<!-- 游戏项信息 -->
<c:if test="${isAreaGroup}">

  <c:if test="${not empty areas}">
    <table class="table table-hover table-striped table-bordered mt-md">
      <thead>
      <tr>
        <th>ID</th>
        <th>角色字符串</th>
        <th>父ID</th>
        <th>游戏ID</th>
        <th>名称</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="row" items="${areas}">
        <c:set var="id" value="${row.getColumnValue('id')}"/>
        <c:set var="area" value="${row.getColumnValue('area')}"/>
        <c:set var="parentId" value="${row.getColumnValue('parentId')}"/>
        <c:set var="gameId" value="${row.getColumnValue('gameId')}"/>
        <c:set var="name" value="${row.getColumnValue('name')}"/>
        <tr>
          <td>${id}</td>
          <td>${area}</td>
          <td>${parentId}</td>
          <td>${gameId}</td>
          <td>${name}</td>
          <td>
            <a href="javascript:;" onclick="removeGame('${gameId}')">
              <i class="fa fa-edit">移除</i>
            </a>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:if>

</c:if>


<!-- 成员 -->
<script charset="utf-8">
  var url = "permission/addArea";

  /** 移除 **/
  function removeGame(gameId) {
    $.post("permission/removeGame", { gameId: gameId }, function(data) {
      reload_content(url);
    });
  }


</script>
<script charset="utf-8">

  function load_portlet(url) {
    $("#portlet_content").load(url);
  };

</script>