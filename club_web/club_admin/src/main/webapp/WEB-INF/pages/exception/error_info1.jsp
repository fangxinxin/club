<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<title>错误提示</title>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp"%>

<div class="page-container">

  <!-- Content -->
  <div class="page-content" style="min-height:100%">

    <div class="row">
      <div class="col-md-12" id="content">

        <section class="page-not-found">
          <div class="row center">

            <div class="col-md-12">
              <div class="page-not-found-main" style="margin-top: 5%;">
                <h2>${code} <i class="fa fa-file"></i></h2>
                <p class="mt-lg">${msg}</p>
              </div>
            </div>
            <div class="col-md-12">
              <h4 class="heading-primary">
                <a href="index" class="btn btn-primary">返回上一页</a>
              </h4>
            </div>

          </div><br/>
        </section>

      </div>
    </div>

  </div>

</div>
