<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp"%>

<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">管理员权限配置</h4>
            </div>
            <div class="portlet-body">

                <div id="portlet_content">
                    <!-- 添加 -->
                    <div class="row">
                        <form role="form" action="" method="post" id="form">

                            <div class="row">
                                <div class="col-md-10 col-md-offset-2">

                                    <div class="form-group col-md-offset-1">
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" id="name" name="name" placeholder="用户组名" />
                                        </div>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" id="priority" name="priority" placeholder="排列序号" />
                                        </div>
                                        <input type="submit" class="btn btn-primary" id="add" value="添加">
                                    </div>

                                </div>
                            </div>

                        </form>

                    </div>

                    <!-- 查询结果 -->
                    <div class="row">
                        <div id="Info" class="col-md-10 col-md-offset-1 mt-md"></div>
                    </div>
                </div>

            </div>
        </div>
        <!-- END 页面内容-->

    </div>
</div>


<script charset="utf-8">

    var url = "permission/addArea";

    //初始化加载
    $("#Info").load("permission/showGroup");

    //添加组
    $("#form").validate({
        debug:true,
        submitHandler:function(form){
            $.post("permission/addGroup",$(form).serialize(),function (data){
                if(data > 0) {
                    var content = '<h4 class="mt-xlg font-red center">操作成功！</h4>'
                    layerMsg("提示信息", content);
                    reload_content(url)
                } else {
                    var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
                    layerMsg("提示信息", content);
                    reload_content(url);
                }
            });
        },
        rules:{
            name : "required",
            priority :{
                required : true,
                digits : true
            }
        },
        messages : {
            name:{
                required : "请填写用户组名"
            },
            priority : {
                required : "填写显示顺序",
                digits : "请填写数字"
            }
        }
    });

    /** load 刷新 **/
    function reload_content(url){
        setTimeout(function() {
            $("#content").load(url,function(responseTxt,statusTxt,xhr){
                if(statusTxt=="error"){
                    location.reload();
                }
            });
        }, 2000);
    }

</script>
