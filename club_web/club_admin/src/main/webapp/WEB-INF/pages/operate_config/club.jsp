<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
    <h4 align="center"><b>俱乐部配置</b></h4>
    <hr /><br />

    <div class="row">
        <div class="col-md-12">

            <div class="form-group">

                <div class="col-md-6 col-md-offset-4">
                    <label class="control-label" style="margin-left:28px">是否允许售钻给任意玩家：</label>
                    <%--开关--%>
                    <input class="btn" id="OFF" type="button" value="OFF" onclick="choose_off()" ><input class="btn" id="ON" type="button" value="ON"  onclick="choose_on()"> &nbsp;&nbsp;&nbsp;&nbsp;
                    <span id="isAllowSell" style="color: red">${dictClub.isAllowSell=="true" ? "开启中":"关闭中"}</span>
                </div>

            </div>

        </div>
    </div>


    <div class="row">
        <div class="col-md-12 center mt-lg">
            <a id="check" class="btn green sbold" data-toggle="modal" href="#checkInfo"> 保存修改 </a>
        </div>
    </div>
</div>

<div id="checkInfo" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">俱乐部配置修改确认</h4>
            </div>
            <div class="modal-body">
                <div class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12 center mt-lg">
                            <h4 class="font-black font-lg">是否允许售钻给任意玩家：${dictClub.isAllowSell=='true' ? "开启中":"关闭中"} ——><span id="_isAllowSell"></span> </h4>
                        </div>
                        <div class="col-md-12 center mt-lg">
                            <b class="font-red font-lg">即将修改&lt;${gameName}&gt;俱乐部配置，请确认信息是否正确</b>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer center">
                <button id="save" data-dismiss="modal" onclick="" type="button" class="btn green"> 确认修改 </button>
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>
            </div>
        </div>
    </div>
</div>


<script>


    $("#check").click(function() {
        $("#_isAllowSell").text($("#isAllowSell").text());
    });

    $("#save").click(function() {
        var isAllowSell = $("#isAllowSell").text();
        $.post("operate_config/modification",{isAllowSell:isAllowSell}, function(data) {
            if(data > 0) {
                content =  '<h4 class="mt-xlg font-black center">更新俱乐部配置成功！</h4>';
                layerMsg_area_r2('俱乐部配置修改确认',content,'500px','300px','operate_config/clubConfig');
            }else {
                content =  '<h4 class="mt-xlg font-black center">更新俱乐部配置失败！</h4>';
                layerMsg_area_r2('俱乐部配置修改确认',content,'500px','300px','operate_config/clubConfig');
            }
        });
    });

    $(function(){
        var isAllowSell = ${dictClub.isAllowSell};
        if(isAllowSell){
            $("#ON").addClass('btn-primary');
        }else{
            $("#OFF").addClass('btn-primary');
        }
    });

    function choose_on(){
        $("#ON").addClass('btn-primary');
        $("#OFF").removeClass('btn-primary');
        $("#isAllowSell").empty();
        $("#isAllowSell").text("开启中")
    }
    function choose_off(){
        $("#ON").removeClass('btn-primary');
        $("#OFF").addClass('btn-primary');
        $("#isAllowSell").empty();
        $("#isAllowSell").text("关闭中")
    }


</script>