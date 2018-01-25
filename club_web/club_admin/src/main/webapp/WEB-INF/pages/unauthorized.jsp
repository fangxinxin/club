<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/header.jsp"%>

<div class="body coming-soom">

    <header id="header" data-plugin-options="{&quot;stickyEnabled&quot;: false}">
        <div class="header-body">
            <div class="header-top">
                <div class="container">
                    <p class="pull-right">
                        <b>游戏后台</b>
                    </p>
                </div>
            </div>
        </div>
    </header>


    <div role="main" class="main">
        <div class="container">

            <div class="featured-boxes featured-boxes-flat">
            <div class="featured-box featured-box-secondary featured-box-effect-2" style="height: 206px;">
                <div class="box-content">
                    <i class="icon-featured fa fa-lock"></i>
                    <div class="row">
                        <div class="col-md-12 center">
                            <h2 class=" heading-secondary"><strong>您没有权限~！</strong></h2>
                    <span class="lead"><strong>提示信息：</strong>
                        当前页面需相应的权限方能访问，您没有权限访问本页面，请点击下方按钮返回！
                    </span>
                        </div>
                        <div class="col-md-12 center"><br style="line-height: 4em">
                            <input type="button" name="button1" class="btn btn-primary  btn-circle" id="button1" value="返      回" onclick="history.go(-1)">
                        </div>
                    </div>
                </div>
            </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <hr class="tall">
                </div>
            </div>
        </div>
    </div>

</div>


<%@include file="/common/include/footer.jsp"%>