<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="center" style="height: 100%;">

  <c:if test="${empty list}">
    <div class="form-group col-md-12 col-xs-12 mt-xl">
      <div class="heading heading-border heading-middle-border heading-middle-border-center">
        <h4>暂无数据</h4>
      </div>
    </div>
  </c:if>

  <div style="height: 80%">
    <c:if test="${not empty list}">
      <div class="table-responsive">
        <table class="table table-bordered table-striped mb-none" style="font-size: 10px;">
          <thead>
          <tr>
            <th>返钻数量</th>
            <th>返钻提取时间</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach items="${list}" var="dt">
            <tr>
              <td>${dt.getColumnValue('getDiamond')}</td>
              <td>${dt.getColumnValue('createTime')}</td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </c:if>

  </div>

  <div class="col-xs-12 mt-xlg form-group">
    <a href="javascript:close_layer();" class="btn btn-default btn-block" style="height:35px">取消</a>
  </div>

</div>
