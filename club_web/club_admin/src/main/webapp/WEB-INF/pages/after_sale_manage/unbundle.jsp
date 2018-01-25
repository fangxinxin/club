<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">解绑玩家</h4>
            </div>
            <div class="portlet-body">
                <!-- 查询 -->
                <div class="row">
                    <div class="col-md-8 col-md-offset-3">

                        <div class="form-group ">
                            <div class="col-md-6">
                                <input  id="gameUserId" class="form-control center" type="text" placeholder="请输入需要解绑的玩家ID"/>
                            </div>
                                <input type="button" value="查询" class="btn blue sbold" onclick="query();"/>
                        </div>

                    </div>
                </div>


                <!-- 查询结果 -->
                <div class="row">
                    <div id="promoterInfo" class="col-md-12 mt-md"></div>
                </div>

            </div>
        </div>
        <!-- END 页面内容-->

    </div>
</div>


<script>
    function query() {
        var gameUserId = $.trim($("#gameUserId").val());
        $('#promoterInfo').load('after_sale_manage/searchPlayer?gameUserId=' + gameUserId);
    }
</script>