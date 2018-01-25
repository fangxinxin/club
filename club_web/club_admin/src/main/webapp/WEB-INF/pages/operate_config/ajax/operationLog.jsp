<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>

<c:if test="${not empty operationList}">
    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered">
            <thead>
            <tr>
                <th>操作类型</th>
                <th>操作内容</th>
                <th>操作账号</th>
                <th>时间</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${operationList}" var="row">
                <tr>
                    <td>${row.getColumnValue("typeName")}</td>
                    <td>${row.getColumnValue("content")}</td>
                    <td>${row.getColumnValue("editAdmin")}</td>
                    <td>${row.getColumnValue("createTime")}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>


<!-- 提示信息 -->
<c:if test="${empty operationList}">
    <div class="row">
        <div class="form-group col-md-12 col-xs-12 mt-lg">
            <div class="heading heading-border heading-middle-border heading-middle-border-center">
                <h4>查询信息不存在!</h4>
            </div>
        </div>
    </div>
</c:if>