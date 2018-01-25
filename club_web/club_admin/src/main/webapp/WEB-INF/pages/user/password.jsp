<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center">修改密码</h4>
            </div>
            <div class="portlet-body">

                <!-- 修改密码 -->
                <div class="featured-boxes">
                    <div class="row">
                        <div class="col-md-6 col-md-offset-3">

                            <div class="featured-box featured-box-primary align-left mt-xlg">
                                <div class="box-content">

                                    <form action="/" id="updateForm" method="post">
                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <label>旧密码</label>
                                                    <input type="password" value="" class="form-control" id="loginPass" name="loginPass">
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-6">
                                                    <label>新密码</label>
                                                    <input type="password" value="" class="form-control" id="newPass" name="newPass">
                                                    <p id="errorNewPassword"></p>
                                                </div>
                                                <div class="col-md-6">
                                                    <label>重复新密码</label>
                                                    <input type="password" value="" class="form-control" id="rePass" name="rePass">
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
        $("#updateForm").validate({
            debug:true,
            submitHandler:function(form){
                $.post("user/changePass",$(form).serialize(),function (data){
                    if(data > 0) {
                        var content = '<h4 class="mt-xlg font-red center">密码修改成功！</h4>'
                        layerMsg_area_r("提示信息", '600px', '360px', content, 1200);
                        location.href="logout";
                    } else {
                        var content = '<h4 class="mt-xlg font-red center">密码修改失败！</h4>'
                        layerMsg_area_r2("提示信息", '600px', '360px', content, 'user/password');
                    }
                });
            },
            rules:{
                loginPass : {
                    required : true,
                    remote:{
                        type:"post",
                        url : "user/search",
                        data :{
                            loginPass:function(){
                                return $("#loginPass").val();
                            }
                        },
                        dataFilter : function(data,type){
                            if(data == 1)
                                return true;
                            else
                                return false;
                        }
                    }
                },
                newPass : "required",
                rePass : {
                    required : true,
                    equalTo : "#newPass"
                }
            },
            messages : {
                loginPass:{
                    required : "旧密码必填",
                    remote : "旧密码输入不正确"
                },
                newPass:{
                    required : "新密码必填"
                },
                rePass:{
                    required : "重复密码必填",
                    equalTo : "两次输入密码不一致"
                }
            }
        });

    });

</script>
<!-- R_foot -->
