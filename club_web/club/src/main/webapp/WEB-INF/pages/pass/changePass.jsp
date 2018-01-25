<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<title>登录</title>

<!-- main -->
<div class="row">
    <div class="col-md-12">
        <div class="portlet-body">
            <div class="form-group col-xs-10 col-xs-offset-1">
                <br/>
                <h2 align="center">首次登陆修改密码</h2>
                <hr/>
                <div>
                    <p style="color: red">大圣掌游:</p>
                    <p style="color: red;">
                        亲爱的代理商，为保障您的利益不受侵害。请您在首次登录时修改登录密码。密码修改只支持数字和字母的组合，且不超过10位。请务必不要告知他人密码，以免造成财务损失。</p>
                </div>
                <hr/>

            </div>

            <div class="col-xs-10 col-xs-offset-1">
                <form id="form" action="changePass" method="post">
                    <table width="100%">
                        <tr>
                            <td>原始密码：&nbsp;</td>
                        </tr>
                        <tr>
                            <td>
                                <input type="password" name="loginPassword" id="loginPassword" style="width: 100%;">
                                <input type="hidden" name="cellPhone" value="${cellPhone}" id="cellPhone">
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>新密码：&nbsp;</td>
                        </tr>
                        <tr>
                            <td><input type="password" name="rePass" id="rePass"
                                       onpaste="return false;" oncopy="return false;" oncut="return false;"
                                       style="width: 100%;"></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>确认新密码：&nbsp;</td>
                        </tr>
                        <tr>
                            <td><input type="password" name="agen" id="agen"
                                       onpaste="return false;"
                                       oncopy="return false;" oncut="return false;" style="width: 100%;"></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                <input type="submit" value="确&nbsp;&nbsp;认"
                                       class="btn btn-primary btn-md btn-block btn-3d"
                                       id="btn" style="width: 80px;">
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp" %>

<script>
    jQuery.validator.addMethod("notEqualTo", function (value, element, param) {
        return value != $(param).val();
    }, $.validator.format("两次输入不能相同!"));
    $(function () {
        $("#form").validate({
            dubug: false,
            submitHandler: function (form) {
                ajax();
            },
            rules: {
                loginPassword: {
                    required: true
                },
                rePass: {
                    required: true,
                    notEqualTo: '#loginPassword',
                    maxlength: 10
                },
                agen: {
                    required: true,
                    equalTo: '#rePass'
                }
            },
            messages: {
                loginPassword: {
                    required: "原始密码不能为空"
                },
                rePass: {
                    required: "新密码不能为空",
                    notEqualTo: "新密码不能与旧密码相同",
                    maxlength: "密码长度不能超过10位"
                },
                agen: {
                    required: "确认新密码不能为空",
                    equalTo: "两次输入密码不一致"
                }
            }
        });
    });

    function ajax() {
        var loginPassword = $.trim($("#loginPassword").val());
        var rePass = $.trim($("#rePass").val());
        var agen = $.trim($("#agen").val());
        var cellPhone = $.trim($("#cellPhone").val());
        $.post("changePass", {
            "cellPhone": cellPhone,
            "rePass": rePass,
            "loginPassword": loginPassword
        }, function (data) {
            if (data == 'NO') {
                $.messager.alert('提示', '原始密码输入错误，请重新输入！');
            } else {
                window.location = 'login';
            }
        });
    }
</script>

