<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center">添加管理员</h4>
            </div>
            <div class="portlet-body">

                <div class="featured-boxes">
                    <div class="row">
                        <div class="col-md-6 col-md-offset-3">

                            <div class="featured-box featured-box-primary align-left mt-xlg">
                                <div class="box-content">

                                    <form id="createAdmin" method="post" autocomplete="off">

                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <label>用户名</label>
                                                    <input type="text" value="" class="form-control" id="userName" name="userName">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <label>真实姓名</label>
                                                    <input type="text" value="" class="form-control" id="realmName" name="realmName">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <label>登录密码</label>
                                                    <input type="password" value="" class="form-control" id="loginPassword" name="loginPassword" autocomplete="new-password">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <label>电话号码</label>
                                                    <input type="text" value="" class="form-control" id="cellphone" name="cellphone">
                                                    <%--<p id="errorInfo"></p>--%>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <label>邮件</label>
                                                    <input type="text" value="" class="form-control" id="email" name="email">
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <input type="submit" value="确&nbsp;&nbsp;认" class="btn btn-primary pull-right mb-xl">
                                                </div>
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
        <!-- END 页面内容-->

    </div>
</div>

<script>
    $(function(){
        var email = $("#email").val();
        $("#createAdmin").validate({
            debug:true,
            submitHandler:function(form){
                $.post("permission/save_createAdmin", $(form).serialize(),function (data){
                    if(data > 0) {
                        var content = '<h4 class="mt-xlg font-red center">操作成功！</h4>';
                    } else {
                        var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
                    }
                    layerMsg("提示信息", content);
                    reload_content("permission/createAdmin");
                });
            },
            rules:{
                userName : "required",
                realName : {
                    required: true
                },
                loginPassword : "required",
                cellphone : {
                    digits:true,
                    min: 0,
                    rangelength:[11,11]
                },
                email: {
                    email:true
                }
            },
            messages : {
                userName:{
                    required: "该选项必填"
                },
                realName:{
                    required: "该选项必填"
                },
                loginPassword:{
                    required: "该选项必填"
                },
                cellphone:{
                    rangelength: "请输入正确手机号",
                    digits: "请输入正确手机号",
                    min: "请输入正确手机号"
                },
                email:{
                    email : "必须输入正确格式的电子邮件"
                }
            }
        });

    });

</script>
