<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
    <h4 align="center"><b>钻石售价配置</b></h4>
    <hr />
    <input type="hidden" id="hidde" value="${length}">

    <%--转正代理可购商品--%>
    <c:if test="${not empty dtPriceList}">
        <label><b style="font-size: medium">转正代理可购商品:</b></label>
        <br/><br/>
        <c:forEach items="${dtPriceList}" var="row">
            <div id="${row.getColumnValue("id")}">
                <label>基本钻石数量：</label><input id="${row.getColumnValue("id")}_goodNum" maxlength="6" type="number" oninput="if(value.length>6)value=value.slice(0,6)" class="center" value="${row.getColumnValue("goodNum")}"
                                           onkeyup="this.value=this.value.replace(/\D/g,'')"
                                           onafterpaste="this.value=this.value.replace(/\D/g,'')"/>&nbsp;&nbsp;个&nbsp;&nbsp;
                <label>赠送钻石数量：</label><input id="${row.getColumnValue("id")}_giftNum" maxlength="6" type="number" oninput="if(value.length>6)value=value.slice(0,6)" class="center" value="${row.getColumnValue("giftNum")}"
                                           onkeyup="this.value=this.value.replace(/\D/g,'')"
                                           onafterpaste="this.value=this.value.replace(/\D/g,'')"/>&nbsp;&nbsp;个&nbsp;&nbsp;
                <label>现金价格：</label><input id="${row.getColumnValue("id")}_cashPrice" maxlength="6" type="number" oninput="if(value.length>6)value=value.slice(0,6)" class="center" value="${row.getColumnValue("cashPrice")}"
                                           onkeyup="this.value=this.value.replace(/\D/g,'')"
                                           onafterpaste="this.value=this.value.replace(/\D/g,'')"/>&nbsp;&nbsp;￥&nbsp;&nbsp;
                    <a  class="btn blue sbold" data-toggle="modal"
                        onclick="save(
                        ${row.getColumnValue("id")},
                        ${row.getColumnValue("goodNum")},
                        ${row.getColumnValue("giftNum")},
                        ${row.getColumnValue("cashPrice")})" <%--href="#div1"--%>> 保存修改 </a>
                    <a  class="btn red sbold" data-toggle="modal"  onclick="delet(${row.getColumnValue("id")})" href="#div2"> 删除此项 </a><br/>
            </div><br/>
        </c:forEach>
    </c:if>
    <button class="btn btn-primary">+</button>&nbsp;&nbsp;<label>添加转正代理商购买项</label>
    <br/><br/>
    <div >
        <label>基本钻石数量：</label><input id="add_goodNum" value="600" class="center" maxlength="6" type="number" oninput="if(value.length>6)value=value.slice(0,6)"
                                     onkeyup="this.value=this.value.replace(/\D/g,'')"
                                     onafterpaste="this.value=this.value.replace(/\D/g,'')"/>&nbsp;&nbsp;个&nbsp;&nbsp;
        <label>赠送钻石数量：</label><input id="add_giftNum" value="66" class="center" maxlength="6" type="number" oninput="if(value.length>6)value=value.slice(0,6)"
                                     onkeyup="this.value=this.value.replace(/\D/g,'')"
                                     onafterpaste="this.value=this.value.replace(/\D/g,'')"/>&nbsp;&nbsp;个&nbsp;&nbsp;
        <label>现金价格：</label><input id="add_cashPrice" value="300" class="center" maxlength="6" type="number" oninput="if(value.length>6)value=value.slice(0,6)"
                                   onkeyup="this.value=this.value.replace(/\D/g,'')"
                                   onafterpaste="this.value=this.value.replace(/\D/g,'')"/>&nbsp;&nbsp;￥&nbsp;&nbsp;
        <a  class="btn blue sbold" data-toggle="modal" onclick="addItem()" <%--href="#div3"--%>> 新添购买项 </a>
    </div>
    <hr />

    <%--待转正代理可购商品--%>
        <label><b style="font-size: medium">待转正代理商可购商品:</b></label>
        <c:if test="${empty dictGoodPrice}">
            <label style="color: red" >待转正代理商售价待配置</label>
        </c:if>
        <br/><br/>
            <div >
                <input type="hidden" id="pre_id" value="${dictGoodPrice.id}">
                <label>基本钻石数量：</label><input id="pre_goodNum" maxlength="6" type="number" oninput="if(value.length>6)value=value.slice(0,6)" class="center" value="${dictGoodPrice == null ? 100: dictGoodPrice.goodNum}"
                                             onkeyup="this.value=this.value.replace(/\D/g,'')"
                                             onafterpaste="this.value=this.value.replace(/\D/g,'')"/>&nbsp;&nbsp;个&nbsp;&nbsp;
                <label>赠送钻石数量：</label><input id="pre_giftNum" maxlength="6" type="number" oninput="if(value.length>6)value=value.slice(0,6)" class="center" value="${dictGoodPrice == null ? 100: dictGoodPrice.giftNum}"
                                             onkeyup="this.value=this.value.replace(/\D/g,'')"
                                             onafterpaste="this.value=this.value.replace(/\D/g,'')"/>&nbsp;&nbsp;个&nbsp;&nbsp;
                <label>现金价格：</label><input id="pre_cashPrice" maxlength="6" type="number" oninput="if(value.length>6)value=value.slice(0,6)" class="center" value="${dictGoodPrice == null ? 100: dictGoodPrice.cashPrice}"
                                           onkeyup="this.value=this.value.replace(/\D/g,'')"
                                           onafterpaste="this.value=this.value.replace(/\D/g,'')"/>&nbsp;&nbsp;￥&nbsp;&nbsp;
                <a id="pre_check" class="btn blue sbold" data-toggle="modal"
                     href="#p_save"> 保存修改 </a>
            </div><br/>


</div>


<div id="div1" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">钻石售价修改确认</h4>
            </div>
            <div class="modal-body">
                <div class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12 center mt-lg">
                            <input id="hid" type="hidden"  >
                            <h4 class="font-balck font-lg">基本钻石数量：<span id="goodNum"></span>个——><span id="_goodNum"></span>个</h4>
                            <h4 class="font-balck font-lg">赠送钻石数量：<span id="giftNum"></span>个——><span id="_giftNum"></span>个</h4>
                            <h4 class="font-balck font-lg">现金价格：<span id="cashPrice"></span>元——><span id="_cashPrice"></span>元</h4>
                        </div>
                        <div class="col-md-12 center mt-lg">
                            <b class="font-red font-lg">请确认信息是否正确</b>
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

<div id="div2" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">钻石售价删除确认</h4>
            </div>
            <div class="modal-body">
                <div class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12 center mt-lg">
                            <input id="hidd" type="hidden"  >
                        </div>
                        <div class="col-md-12 center mt-lg">
                            <br/><br/>
                            <b class="font-red font-lg">请确认是否删除？</b>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer center">
                <button id="delete" data-dismiss="modal" onclick="" type="button" class="btn green"> 确认删除 </button>
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>
            </div>
        </div>
    </div>
</div>

<div id="div3" class="modal fade" tabindex="-1" aria-hidden="true">
<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h4 class="modal-title center">钻石售价修改确认</h4>
        </div>
        <div class="modal-body">
            <div class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
                <div class="row">
                    <div class="col-md-12 center mt-lg">
                        <h4 class="font-balck font-lg">新增钻石售价项</h4>
                        <h4 class="font-balck font-lg">基本钻石数量：<span id="addGoodNum"></span>个</h4>
                        <h4 class="font-balck font-lg">赠送钻石数量：<span id="addGiftNum"></span>个</h4>
                        <h4 class="font-balck font-lg">现金价格：<span id="addCashPrice"></span>元</h4>
                    </div>
                    <div class="col-md-12 center mt-lg">
                        <b class="font-red font-lg">请确认信息是否正确</b>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-footer center">
            <button id="add" data-dismiss="modal" onclick="" type="button" class="btn green"> 确认新增 </button>
            <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>
        </div>
    </div>
</div>
</div>


<div id="p_save" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center">待转正钻石售价修改确认</h4>
            </div>
            <div class="modal-body">
                <div class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
                    <div class="row">
                        <div class="col-md-12 center mt-lg">
                            <h4 class="font-balck font-lg">基本钻石数量：<c:if test="${not empty dictGoodPrice}">${dictGoodPrice.goodNum} 个——></c:if><span id="_pre_goodNum"></span> 个</h4>
                            <h4 class="font-balck font-lg">赠送钻石数量：<c:if test="${not empty dictGoodPrice}">${dictGoodPrice.giftNum} 个——></c:if><span id="_pre_giftNum"></span> 个</h4>
                            <h4 class="font-balck font-lg">现金价格：<c:if test="${not empty dictGoodPrice}">${dictGoodPrice.cashPrice} 个——></c:if><span id="_pre_cashPrice"></span> 个</h4>
                        </div>
                        <div class="col-md-12 center mt-lg">
                            <b class="font-red font-lg">请确认信息是否正确</b>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer center">
                <button id="pre_save" data-dismiss="modal" onclick="save_pre()" type="button" class="btn green"> <c:if test="${not empty dictGoodPrice}">确认修改</c:if><c:if test="${empty dictGoodPrice}">确认新增</c:if> </button>
                <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>
            </div>
        </div>
    </div>
</div>



<script>


    function save(id,goodNum,giftNum,cashPrice){
        var _goodNum = document.getElementById(id+"_goodNum").value;
        var _giftNum = document.getElementById(id+"_giftNum").value;
        var _cashPrice = document.getElementById(id+"_cashPrice").value;
        $("#hid").text(id);
        $("#_goodNum").text(_goodNum);
        $("#_giftNum").text(_giftNum);
        $("#_cashPrice").text(_cashPrice);
        $("#goodNum").text(goodNum);
        $("#giftNum").text(giftNum);
        $("#cashPrice").text(cashPrice);

        //非空校验
        if(_goodNum !="" && _giftNum!="" && _cashPrice!="" ){
            $('#div1').modal('show')
        }else {
            content =  '<h4 class="mt-xlg font-black center">数据不能为空！</h4>';
            layerMsg('提示信息',content);
        }
    }


    $("#pre_check").click(function() {
        $("#_pre_goodNum").text($("#pre_goodNum").val());
        $("#_pre_giftNum").text($("#pre_giftNum").val());
        $("#_pre_cashPrice").text($("#pre_cashPrice").val());

    });

    //保存待转正代理商售价配置
    function save_pre(){
        var id = $("#pre_id").val();
        var goodNum = $("#pre_goodNum").val();
        var giftNum = $("#pre_giftNum").val();
        var cashPrice = $("#pre_cashPrice").val();
        var type = 2;
        $.post("operate_config/saveCardPriceConditon",{id:id,goodNum: goodNum,giftNum: giftNum,cashPrice:cashPrice,type:type}, function(data) {
            if(data == 1) {
                content =  '<h4 class="mt-xlg font-black center">钻石销售修改成功！</h4>';
                layerMsg_area_r2('钻石销售修改确认',content,'500px','300px','operate_config/cardPrice');
            }else {
                content =  '<h4 class="mt-xlg font-black center">钻石销售修改失败！</h4>';
                layerMsg('钻石销售修改确认', content);
            }
        });

    }

    $("#save").click(function() {
        var id = $("#hid").text();
        var goodNum = $("#_goodNum").text();
        var giftNum = $("#_giftNum").text();
        var cashPrice = $("#_cashPrice").text();
        var type = 1;
        $.post("operate_config/saveCardPriceConditon",{id:id,goodNum: goodNum,giftNum: giftNum,cashPrice:cashPrice,type:type}, function(data) {
            if(data == 1) {
                content =  '<h4 class="mt-xlg font-black center">钻石销售修改成功！</h4>';
                layerMsg_area_r2('钻石销售修改确认',content,'500px','300px','operate_config/cardPrice');
            }else {
                content =  '<h4 class="mt-xlg font-black center">钻石销售修改失败！</h4>';
                layerMsg('钻石销售修改确认', content);
            }
        });
    });


    function  delet(id){
        $("#hidd").text(id);
    }

    $("#delete").click(function() {
        var id = $("#hidd").text();
        $.post("operate_config/deleteCardPriceConditon",{id:id}, function(data) {
            if(data == 1) {
                $("#hidde").val(parseInt($("#hidde").val())-1);
                content =  '<h4 class="mt-xlg font-black center">钻石销售删除成功！</h4>';
                layerMsg_area('钻石销售删除确认',content,'500px','300px','operate_config/cardPrice');

            }else {
                content =  '<h4 class="mt-xlg font-black center">钻石销售删除失败！</h4>';
                layerMsg('钻石销售删除确认', content);
            }
        });
    });

    //增加钻石售价项，不能大于5条
    function addItem(){
            var add_goodNum = $("#add_goodNum").val();
            var add_giftNum = $("#add_giftNum").val();
            var add_cashPrice = $("#add_cashPrice").val();
            $("#addGoodNum").text(add_goodNum);
            $("#addGiftNum").text(add_giftNum);
            $("#addCashPrice").text(add_cashPrice);
            var length = parseInt($("#hidde").val());

            if(length>=5){
                content =  '<h4 class="mt-xlg font-black center">钻石销售项已经达到5条上限，不能再新增！</h4>';
                layerMsg_area_r2('钻石销售新增提示',content,'500px','300px','operate_config/cardPrice');
            }else{
                //非空校验
                if(add_goodNum !="" && add_giftNum!=""  && add_cashPrice!="" ){
                    $('#div3').modal('show');
                }else {
                    content =  '<h4 class="mt-xlg font-black center">数据不能为空！</h4>';
                    layerMsg('提示信息', content);
                }
            }
    }

    $("#add").click(function() {
        var goodNum = $("#add_goodNum").val();
        var giftNum = $("#add_giftNum").val();
        var cashPrice = $("#add_cashPrice").val();
        var type = 1;
        $.post("operate_config/saveCardPriceConditon",{goodNum:goodNum,giftNum:giftNum,cashPrice:cashPrice,type:type}, function(data) {
            if(data == 1) {
                $("#hidde").val(parseInt($("#hidde").val())+1);
                content =  '<h4 class="mt-xlg font-black center">钻石销售新增成功！</h4>';
                layerMsg_area_r2('钻石销售新增确认',content,'500px','300px','operate_config/cardPrice');
            }else {
                content =  '<h4 class="mt-xlg font-black center">钻石销售新增失败！</h4>';
                layerMsg('钻石销售新增确认',content);
            }
        });
    });



</script>