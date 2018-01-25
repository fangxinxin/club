<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp"%>

<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">游戏项管理</h4>
            </div>
            <div class="portlet-body">

                <div id="portlet_content">
                    <!-- 添加 -->
                    <div class="row">
                        <form role="form" action="" method="post" id="form">

                            <div class="row">
                                <div class="col-md-10 col-md-offset-1">

                                    <div class="form-group">
                                        <div class="col-md-3 radio">
                                            <label class="bold">游戏地区：</label>
                                            <label>
                                                <input type="radio" name="parentId" id="p1" value="1">66游戏
                                            </label>
                                            <label>
                                                <input type="radio" name="parentId" id="p2" value="2">来来游戏
                                            </label>
                                            <label>
                                                <input type="radio" name="parentId" id="p3" value="0">其他
                                            </label>
                                        </div>
                                        <div class="col-md-3">
                                            <input type="text" class="form-control" id="area" name="area" placeholder="游戏字符串" />
                                        </div>
                                        <div class="col-md-3">
                                            <input type="text" class="form-control" id="gameId" name="gameId" placeholder="游戏ID" />
                                        </div>
                                        <div class="col-md-2">
                                            <input type="text" class="form-control" id="name" name="name" placeholder="名称" />
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
    $("#Info").load("permission/showArea");

    //添加组
    $("#form").validate({
        debug:true,
        submitHandler:function(form){
            $.post("permission/save_addArea",$(form).serialize(),function (data){
                if(data > 0) {
                    var content = '<h4 class="mt-xlg font-red center">操作成功！</h4>'
                    layerMsg("提示信息", content);
                    reload_content(url);
                } else {
                    var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
                    layerMsg("提示信息", content);
                    reload_content(url);
                }
            });
        },
        rules:{
            area : "required",
            gameId :{
                required : true,
                digits : true
            },
            parentId : "required",
            name : "required"
        },
        messages : {
            area:{
                required : "请填写游戏字符串"
            },
            gameId : {
                required : "请填写游戏ID",
                digits : "请填写数字"
            },
            parentId : {
                required : "请选择游戏地区"
            },
            name:{
                required : "请填写名称"
            }
        }
    });

</script>
