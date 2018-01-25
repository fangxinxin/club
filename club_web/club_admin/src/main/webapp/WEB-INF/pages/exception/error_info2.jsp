<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- R_head -->
<%@include file="/common/include/R_head.jsp"%>

<title>错误信息</title>

<div class="row coming-soom">

    <div class=" col-md-offset-2 col-md-8">
        <div class="featured-boxes featured-boxes-flat">
            <div class="featured-box featured-box-secondary featured-box-effect-2">
                <div class="box-content">

                    <i class="icon-featured fa fa-lock"></i>
                    <div class="row">
                        <div class="col-md-12 center">
                            <%--<h2 class="heading-secondary"><strong>您没有权限~！</strong></h2>--%>
                            <div class="page-not-found-main">
                                <h2>${code} <i class="fa fa-file"></i></h2>
                                <p class="bold font-lg">${msg}</p>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <h4 class="heading-primary">
                                <a href="index" class="btn btn-primary">返回上一页</a>
                            </h4>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <div class="col-md-2"></div>


</div>
