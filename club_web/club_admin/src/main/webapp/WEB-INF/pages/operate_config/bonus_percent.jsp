<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
    <h4 align="center"><b>返钻比例配置</b></h4>
    <hr /><br />

    <div class="row">
        <div class="col-md-12">
            <c:if test="${empty dictBonus}">
                <p style="color: red" align="center">返钻比例待配置</p>
            </c:if>
            <div class="form-group">
                <div class="col-md-6 col-md-offset-4">
                    <label class="control-label" >一级代理商返钻比例：</label>
                    <button class="btn btn-primary" style="margin-top: -2px" onclick="subtraction(this)">-</button>
                    <input type="number" name="directPercent" min="0"  id="directPercent"  maxlength="2" type="number" oninput="if(value.length>3)value=value.slice(0,3)" class="center" style="height: 34px;" value="<c:if test="${directPercent ==null}">${_directPercent}</c:if><c:if test="${directPercent !=null}">${directPercent}</c:if>" onblur="number(this)">
                    <button class="btn blue" onclick="add(this)" style="margin-top: -2px">+</button>&nbsp;&nbsp;%
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-6 col-md-offset-4">
                    <label class="control-label" >特级代理商返钻比例：</label>
                    <button class="btn btn-primary" style="margin-top: -2px" onclick="subtraction(this)">-</button>
                    <input type="number" name="nonDirectPercent" min="0"  id="nonDirectPercent"  maxlength="2" type="number" oninput="if(value.length>3)value=value.slice(0,3)" class="center" style="height: 34px;" value="<c:if test="${nonDirectPercent ==null}">${_nonDirectPercent}</c:if><c:if test="${directPercent !=null}">${nonDirectPercent}</c:if>" onblur="number(this)">
                    <button class="btn blue" onclick="add(this)" style="margin-top: -2px">+</button>&nbsp;&nbsp;%
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
                <h4 class="modal-title center">返钻比例修改确认</h4>
            </div>
            <div class="modal-body">
                <div class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12 center mt-lg">
                            <h4 class="font-black font-lg">一级代理商返钻：<c:if test="${not empty dictBonus}">${directPercent} %——></c:if><span id="_directPercent"></span> %</h4>
                            <h4 class="font-black font-lg">特级代理商返钻：<c:if test="${not empty dictBonus}">${nonDirectPercent} %——></c:if><span id="_nonDirectPercent"></span> %</h4>
                        </div>
                        <div class="col-md-12 center mt-lg">
                            <b class="font-red font-lg">即将修改&lt;${gameName}&gt;返钻比例，请确认信息是否正确</b>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer center">
                <button id="save" data-dismiss="modal" onclick="" type="button" class="btn green"> <c:if test="${not empty dictBonus}">确认修改</c:if><c:if test="${empty dictBonus}">确认新增</c:if> </button>
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>
            </div>
        </div>
    </div>
</div>




<script>



    $("#check").click(function() {

        $("#_directPercent").text($("#directPercent").val());
        $("#_nonDirectPercent").text($("#nonDirectPercent").val());

    });


    $("#save").click(function() {
        var directPercent = $("#directPercent").val();
        var nonDirectPercent = $("#nonDirectPercent").val();
        $.post("operate_config/saveBonusPercent",{directPercent:directPercent,nonDirectPercent:nonDirectPercent}, function(data) {
            if(data > 0) {
                content =  '<h4 class="mt-xlg font-black center">更新返钻比例成功！</h4>';
                layerMsg_area_r2('返钻比例修改确认',content,'500px','300px','operate_config/bonusPercent');
            }else {
                content =  '<h4 class="mt-xlg font-black center">更新返钻比例失败！</h4>';
                layerMsg_area_r2('返钻比例修改确认',content,'500px','300px','operate_config/bonusPercent');
            }
        });
    });


    function subtraction(thisObj){
        var m =$(thisObj).next().val();
        if (m<=1) {
            //如果文本框的值小于1,则设置值为1
            $(thisObj).next().val(1);
        }else {
            $(thisObj).next().val($(thisObj).next().val()-1);
        }
    }

    function add(thisObj){
        var n = $(thisObj).prev().val();
        //parseInt() 将数值型字符串转换为数值
        if(n>=100){
            $(thisObj).prev().val(100);
        }else{
            $(thisObj).prev().val(parseInt(n)+1);
        }
    }

    function number(thisObj){
        var num = $(thisObj).val();

        // ^[1-9]\d*$  只能输入整数而且不能以0开头
//        if (!(/^[1-9]\d*$/.test(num))) {
//            $(thisObj).val(1);
//        }
        //如果文本值为空,设置为1
        if (parseInt(num)=="") {
            $(thisObj).val(1);
        }
        //如果文本值为非纯数字,设置为1
        //isNaN()是否为非法数字
        if (isNaN(parseInt(num))) {
            $(thisObj).val(1);
        }
        //如果文本值小于1,设置为1
        if (parseInt(num)<=1) {
            $(thisObj).val(1);
        }
        //如果文本值大于100,设置为100
        if (parseInt(num)>=100) {
            $(thisObj).val(100);
        }
    }
</script>



