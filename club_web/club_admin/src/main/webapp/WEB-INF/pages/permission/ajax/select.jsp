<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div class="form-group mt-xlg">
  <%--<label class="col-md-3 control-label text-right">分配权限：</label> --%>
<div class="col-md-8 col-md-offset-2">
<select id="role_select" class="form-control"> 
<c:if test="${not empty roleDt}">
  <c:forEach items="${roleDt}" var="row">
    <c:set var="roleId" value="${row.getColumnValue('id')}"/>
    <c:set var="name" value="${row.getColumnValue('name')}"/>
      <option value="${roleId}">${name}</option>
    </c:forEach>
  </c:if>
</select> 
</div> 
</div> 
<div class="col-md-8 col-md-offset-2 center mt-xlg"> 
<input type="button" class="btn btn-block green" value="保&nbsp;&nbsp;存"
       onclick="savePower('${userId}',$('#role_select').val()),layer.close(layer.index);">
</div>
