<%--
  Created by IntelliJ IDEA.
  User: ds
  Date: 2017/4/6
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<section class="page-not-found">
  <div class="row center">
    <div class="col-md-12"><!-- col-md-offset-1 -->
      <div class="page-not-found-main">
        <h3>错误信息 <i class="fa fa-file"></i></h3>
        <%--<p>We're sorry, but the page you were looking for doesn't exist.</p>--%>
        <p>${error}</p>
      </div>
    </div>
  </div>
  <br/>
</section>


</body>
</html>
