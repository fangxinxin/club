<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp"%>


<!-- header -->
<header>
<br style="line-height: 5em">
</header>

<!-- main -->
<div role="main" class="main">

    <div class="container">

        <div class="row">
            <div class="col-md-12">

                <div class="featured-boxes">
                    <div class="row">
                        <div class="col-sm-3"></div>
                        <div class="col-sm-6">
                            <div class="featured-box featured-box-quaternary align-left mt-xlg">
                                <div class="box-content">
                                    <h4 class="heading-quaternary text-uppercase mb-md center"><strong>网站后台管理</strong></h4>
                                    <form action=" " id="frmSignIn" method="post">
                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <label class="bold">用户名</label>
                                                    <input type="text" value="" class="form-control input-lg" name="userName">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <a class="pull-right" href="#">(忘记密码?)</a>
                                                    <label class="bold">密码</label>
                                                    <input type="password" value="" class="form-control input-lg" name="password">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-12 center">
                                                <p style="color: red">${msg}</p>
                                            </div>
                                            <div class="col-md-6">
															<span class="remember-box checkbox">
																<label for="rememberme">
                                                                    <input type="checkbox" id="rememberme" name="rememberme" checked>记住密码
                                                                </label>
															</span>
                                            </div>
                                            <div class="col-md-6 text-center" >
                                                <input type="submit" value="登&nbsp;&nbsp;录" class="btn btn-primary mb-xl pull-right" data-loading-text="Loading...">
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

            </div>
        </div>

    </div>

</div>



<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp"%>