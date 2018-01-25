<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
    <h4 align="center"><b>俱乐部转正条件</b></h4>
    <hr /><br />

    <div class="row">
        <div class="col-md-12">
            <c:if test="${empty dictFormal}">
                <p style="color: red" align="center">转正条件待配置</p>
            </c:if>
            <div class="form-group">
                <div class="col-md-5">
                    <h5 class="control-label text-right">创建后转正时间：</h5>
                </div>
                <div class="col-md-7">
                    <button class="btn btn-primary" style="margin-top: -2px" onclick="_subtraction(this)">-</button>
                    <input id="expire_day" maxlength="4" min="1" oninput="if(value.length>4)value=value.slice(0,4)" type="number"  class="center" style="height: 34px;" value="<c:if test="${dictFormal.expireDay ==null}">${expireDay}</c:if>${dictFormal.expireDay}" onblur="_number(this)"/>
                    <button class="btn blue" onclick="_add(this)" style="margin-top: -2px">+</button>&nbsp;&nbsp; 天
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-5">
                    <h5 class="control-label text-right">俱乐部人数：</h5>
                </div>
                <div class="col-md-7">
                    <button class="btn btn-primary" onclick="subtraction_(this)" style="margin-top: -2px">-</button>
                    <input id="people_num" maxlength="3" min="0" type="number"  oninput="if(value.length>3)value=value.slice(0,3)"  class="center"  style="height: 34px;" value="<c:if test="${dictFormal.peopleNum ==null}">${peopleNum}</c:if>${dictFormal.peopleNum}" onblur="number_(this)"/>
                    <button class="btn btn-primary" onclick="add(this)" style="margin-top: -2px">+</button>&nbsp;&nbsp; 人
                </div>
            </div>

            <hr/>

            <div class="form-group">
                <div class="col-md-5">
                    <h5 class="control-label text-right">俱乐部参与对局新玩家人数：</h5>
                </div>
                <div class="col-md-7">
                    <button class="btn btn-primary" onclick="subtraction(this)" style="margin-top: -2px">-</button>
                    <input id="new_people_num" maxlength="3" min="0" type="number"  oninput="if(value.length>3)value=value.slice(0,3)"  class="center"  style="height: 34px;" value="<c:if test="${dictFormal.newPeopleNum ==null}">${newPeopleNum}</c:if>${dictFormal.newPeopleNum}" onblur="number(this)"/>
                    <button class="btn btn-primary" onclick="add(this)" style="margin-top: -2px">+</button>&nbsp;&nbsp; 人
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-5">
                    <h5 class="control-label text-right">代开房局数：</h5>
                </div>
                <div class="col-md-7">
                    <button class="btn btn-primary" style="margin-top: -2px" onclick="subtraction(this)">-</button>
                    <input id="pyj_room_num" maxlength="3" min="0" type="number" oninput="if(value.length>3)value=value.slice(0,3)" class="center" style="height: 34px;" value="<c:if test="${dictFormal.pyjRoomNum ==null}">${pyjRoomNum}</c:if>${dictFormal.pyjRoomNum}" onblur="number(this)"/>
                    <button class="btn blue" onclick="add(this)" style="margin-top: -2px">+</button>&nbsp;&nbsp; 局
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-5">
                    <h5 class="control-label text-right">非代开房局数：</h5>
                </div>
                <div class="col-md-7">
                    <button class="btn btn-primary" onclick="subtraction(this)" style="margin-top: -2px">-</button>
                    <input id="non_pyj_room_num" maxlength="3" min="0" type="number"  oninput="if(value.length>3)value=value.slice(0,3)"  class="center"  style="height: 34px;" value="<c:if test="${dictFormal.nonPyjRoomNum ==null}">${nonPyjRoomNum}</c:if>${dictFormal.nonPyjRoomNum}" onblur="number(this)"/>
                    <button class="btn btn-primary" onclick="add(this)" style="margin-top: -2px">+</button>&nbsp;&nbsp; 局
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-5">
                    <h5 class="control-label text-right">转正后赠送专属钻石：</h5>
                </div>
                <div class="col-md-7">
                    <button class="btn btn-primary" onclick="subtraction(this)" style="margin-top: -2px">-</button>
                    <input id="award" maxlength="3" min="0" type="number"  oninput="if(value.length>3)value=value.slice(0,3)"  class="center"  style="height: 34px;" value="<c:if test="${dictFormal.award ==null}">${award}</c:if>${dictFormal.award}" onblur="number(this)"/>
                    <button class="btn btn-primary" onclick="add(this)" style="margin-top: -2px">+</button>&nbsp;&nbsp; 个
                </div>
            </div>

            <%--<div class="form-group">--%>
                <%--<div class="col-md-5">--%>
                    <%--<h5 class="control-label text-right">刷新时间：</h5>--%>
                <%--</div>--%>
                <%--<div class="col-md-7">--%>
                    <%--<button class="btn btn-primary" onclick="subtraction_(this)" style="margin-top: -2px">-</button>--%>
                    <%--<input id="refresh_day" maxlength="3" min=1 type="number"  oninput="if(value.length>3)value=value.slice(0,3)"  class="center"  style="height: 34px;" value="<c:if test="${dictFormal.refreshDay ==null}">${refreshDay}</c:if>${dictFormal.refreshDay}" onblur="number_(this)"/>--%>
                    <%--<button class="btn btn-primary" onclick="add(this)" style="margin-top: -2px">+</button>&nbsp;&nbsp; 天--%>
                <%--</div>--%>
            <%--</div>--%>

            <p style="color: red" align="center">备注：代开房局数和非代开房局数满足其中一个即可</p>

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
                <h4 class="modal-title center">俱乐部转正条件确认</h4>
            </div>
            <div class="modal-body">
                <div class="scroller"  data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12 center mt-lg">
                            <h4 class="font-black font-lg">创建后转正时间要求：<c:if test="${not empty dictFormal}">${dictFormal.expireDay} 天——></c:if><span id="_expire_day"></span> 天</h4>
                            <h4 class="font-black font-lg">俱乐部新玩家人数：<c:if test="${not empty dictFormal}">${dictFormal.newPeopleNum} 人——></c:if><span id="_new_people_num"></span> 人</h4>
                            <h4 class="font-black font-lg">俱乐部人数：<c:if test="${not empty dictFormal}">${dictFormal.peopleNum} 人——></c:if><span id="_people_num"></span> 人</h4>
                            <h4 class="font-black font-lg">代开房局数：<c:if test="${not empty dictFormal}">${dictFormal.pyjRoomNum} 局——></c:if><span id="_pyj_room_num"></span> 局</h4>
                            <h4 class="font-black font-lg">非代开房局数：<c:if test="${not empty dictFormal}">${dictFormal.nonPyjRoomNum} 局——></c:if><span id="_non_pyj_room_num"></span> 局</h4>
                            <h4 class="font-black font-lg">转正后赠送钻石：<c:if test="${not empty dictFormal}">${dictFormal.award} 个——></c:if><span id="_award"></span> 个</h4>
                            <%--<h4 class="font-black font-lg">刷新时间：<c:if test="${not empty dictFormal}">${dictFormal.refreshDay} 天——></c:if><span id="_refresh_day"></span> 天</h4>--%>
                        </div>
                        <div class="col-md-12 center mt-lg">
                            <b class="font-red font-lg">即将修改&lt;${gameName}&gt;转正条件，请确认信息是否正确</b>
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
        $("#_expire_day").text($("#expire_day").val());
        $("#_new_people_num").text($("#new_people_num").val());
        $("#_people_num").text($("#people_num").val());
        $("#_pyj_room_num").text($("#pyj_room_num").val());
        $("#_non_pyj_room_num").text($("#non_pyj_room_num").val());
        $("#_award").text($("#award").val());
//        $("#_refresh_day").text($("#refresh_day").val());
    });

    $("#save").click(function() {
        var expireDay = $("#expire_day").val();
        var newPeopleNum = $("#new_people_num").val();
        var peopleNum = $("#people_num").val();
        var pyjRoomNum = $("#pyj_room_num").val();
        var nonPyjRoomNum = $("#non_pyj_room_num").val();
        var award = $("#award").val();
//        var refreshDay = $("#refresh_day").val();
        $.post("operate_config/convertCondition",{
                expireDay:expireDay,
                newPeopleNum:newPeopleNum,
                peopleNum:peopleNum,
                pyjRoomNum:pyjRoomNum,
                nonPyjRoomNum:nonPyjRoomNum,
                award:award
//                , refreshDay:refreshDay
                },
        function(data) {
            if(data > 0) {
                content =  '<h4 class="mt-xlg font-black center">更新转正条件成功！</h4>';
                layerMsg_area_r2('转正条件修改确认',content,'500px','300px','operate_config/convert');
            }else {
                content =  '<h4 class="mt-xlg font-black center">更新转正条件失败！</h4>';
                layerMsg_area_r2('转正条件修改确认',content,'500px','300px','operate_config/convert');
            }
        });
    });


    function subtraction(thisObj){
        var m =$(thisObj).next().val();
        if (m<1) {
            //如果文本框的值小于1,则设置值为0
            $(thisObj).next().val(0);
        }else {
            $(thisObj).next().val($(thisObj).next().val()-1);
        }
    }

    function add(thisObj){
        var n = $(thisObj).prev().val();
        //parseInt() 将数值型字符串转换为数值
        $(thisObj).prev().val(parseInt(n)+1);
    }

    function number(thisObj){
        var num = $(thisObj).val();
        //如果文本值为空,设置为1
        if (parseInt(num)=="") {
            $(thisObj).val(0);
        }
        //如果文本值为非纯数字,设置为1
        //isNaN()是否为非法数字
        if (isNaN(parseInt(num))) {
            $(thisObj).val(0);
        }
        //如果文本值小于1,设置为1
        if (parseInt(num)<1) {
            $(thisObj).val(0);
        }
    }

    function subtraction_(thisObj){
        var m =$(thisObj).next().val();
        if (m<=1) {
            //如果文本框的值小于1,则设置值为1
            $(thisObj).next().val(1);
        }else {
            $(thisObj).next().val($(thisObj).next().val()-1);
        }
    }
    function number_(thisObj){
        var num = $(thisObj).val();
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
    }

    function _subtraction(thisObj){
        var m =parseFloat($(thisObj).next().val());
        if (m<=1) {
            //如果文本框的值小于1,则设置值为1
            $(thisObj).next().val(1);
        }else {
            $(thisObj).next().val(parseFloat((m-1).toFixed(2)));
        }
    }

    function _add(thisObj){
        var n = $(thisObj).prev().val();
        //parseInt() 将数值型字符串转换为数值
        $(thisObj).prev().val(parseFloat(n)+1);
    }



    function _number(thisObj){
        var num = $(thisObj).val();
        //如果文本值为空,设置为1
        if (parseFloat(num)=="") {
            $(thisObj).val(1);
        }
        //如果文本值为非纯数字,设置为1
        //isNaN()是否为非法数字
        if (isNaN(parseFloat(num))) {
            $(thisObj).val(1);
        }
        //如果文本值小于1,设置为0
        if (parseFloat(num)<=1) {
            $(thisObj).val(1);
        }
    }


</script>