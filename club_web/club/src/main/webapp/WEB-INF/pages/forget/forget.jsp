<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- R_head -->
<%@ include file="/common/include/R_head.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="http://code.jquery.com/jquery-1.8.0.min.js"></script>
<title>找回密码</title>

<!-- header -->
<header>
    <section class="page-header page-header-center page-header-no-title-border">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <h1> 找回密码 </h1>
                </div>
            </div>
        </div>
    </section>
</header>

<!-- main -->
<div role="main" class="main">

    <div class="col-xs-12">
        <form action="forget" id="form" method="post">

            <div class="form-group">
                <div class=" col-xs-12">
                    <label class="bold">手机号码</label>
                    <input type="text" value="" class="form-control input-lg" name="cellPhone" placeholder="手机号码"
                           id="cellPhone">
                </div>
            </div>

            <div class="form-group">
                <div class=" col-xs-12">
                    <label class="bold">验证码</label>
                </div>
                <div class="col-xs-7">
                    <input type="text" class="form-control input-lg" name="verifyCode" placeholder="验证码"
                           id="verifyCode">
                </div>
                <div class="col-xs-5">
                    <input type="button" class="form-control input-lg btn-primary btn-block" id="btn" value="获取验证码"
                           onclick="ajax(this)" style="font-size: 10px;"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-10 text-center col-md-offset-1">
                    <input type="submit" value="提&nbsp;&nbsp;交" class="btn btn-secondary btn-lg btn-block btn-3d"
                           id="sub">
                </div>
                <div class="col-md-10 text-center col-md-offset-1">
                    <br/>
                    <input type="button" value="返&nbsp;&nbsp;回" class="btn btn-success btn-lg btn-block btn-3d"
                           id="cancel"
                           onclick="window.location = 'login';">
                </div>
            </div>

        </form>
    </div>

</div>

<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp" %>
<script>
    jQuery.validator.addMethod("testCellPhone", function (value, element, param) {
        var mobile = /^1(3|4|5|6|7|8|9)\d{9}$/;
        return mobile.test(value);
    }, $.validator.format("请输入正确的手机号!"));
    $(function () {
        $("#form").validate({
            dubug: false,
            submitHandler: function () {
                document.getElementById('sub').disabled = 'true';
                document.getElementById('cancel').disabled = 'true';
                window.location.href = 'forgetTwo?cellPhone=' + $("#cellPhone").val();
            },
            rules: {
                cellPhone: {
                    required: true,
                    testCellPhone: true,
                    remote: {
                        url: "testCellPhone",     //后台处理程序
                        type: "post",               //数据发送方式
                        dataType: "text",           //接受数据格式
                        data: {//要传递的数据
                            cellPhone: function () {
                                return $("#cellPhone").val();
                            }
                        }
                    }
                },
                verifyCode: {
                    required: true,
                    remote: {
                        url: "testVerifyCode",     //后台处理程序
                        type: "post",               //数据发送方式
                        dataType: "text",           //接受数据格式
                        data: {//要传递的数据
                            cellPhone: function () {
                                return $("#cellPhone").val();
                            },
                            verifyCode: function () {
                                return $("#verifyCode").val();
                            }
                        }
                    }
                }

            },
            messages: {
                cellPhone: {
                    required: "手机号码不能为空",
                    testCellPhone: "请输入正确的手机号!",
                    remote: "手机号码错误"
                },
                verifyCode: {
                    required: "验证码不能为空",
                    remote: "验证码错误"
                }
            }
        });
    });
    var countdown = 90;
    function settime(obj) {
        if (countdown == 0) {
            obj.removeAttribute("disabled");
            obj.value = "免费获取验证码";
            countdown = 90;
            return;
        } else {
            obj.setAttribute("disabled", true);
            obj.value = "重新发送(" + countdown + ")";
            countdown--;
        }
        setTimeout(function () {
            settime(obj)
        }, 1000)
    }


    function ajax(obj) {
        var mobile = /^1(3|4|5|6|7|8|9)\d{9}$/;
        var cell = $("#cellPhone").val();
        if (cell == '' || !mobile.test(cell)) {
            layer.msg('请输入正确的手机号');
        } else {
            $.post("verifyCode", {cellPhone: cell}, function (data) {
                if (data == 'OK') {
                    settime(obj);
                } else {
                    layer.msg(data);
                }
            });
        }
    }


</script>