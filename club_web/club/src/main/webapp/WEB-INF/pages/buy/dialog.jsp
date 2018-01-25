<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${!empty goodPrice and dlgId == 1}">
  <ul class="list list-unstyled ml-md" >
    <c:if test="${isNormalClub}">
      <c:set var="goodNum" value="${goodPrice.goodNum}"/>
      <c:set var="cashPrice" value="${goodPrice.cashPrice}"/>
    </c:if>
    <c:if test="${isNormalClub == false}">
      <c:set var="goodNum" value="${goodPrice.goodNum * gameCardNum}"/>
      <c:set var="cashPrice" value="${goodPrice.cashPrice * gameCardNum}"/>
    </c:if>
    <li class="mb-none" >
      本次购买钻石：${goodNum}个
    </li>
    <%--<c:if test="${pLevel != 2}">--%>
      <%--<li class="mb-none">当前可提现金额：${deposit}元</li>--%>
    <%--</c:if>--%>
  </ul>
  <div class="dialog-button">
    <a href="javascript:void(0)" class="btn btn-success " style="width:100%;height:35px" onclick="window.open('buy/pay_weixin?id=${goodPrice.id}&payType=2&gameCardNum=${goodNum}&isNormalClub=${isNormalClub}');">
      微信支付（￥ ${cashPrice}）
    </a>
  </div>
  <div class="dialog-button">
    <a href="javascript:void(0)" class="btn btn-success " style="width:100%;height:35px" onclick="window.open('buy/pay_weixin?id=${goodPrice.id}&payType=3&gameCardNum=${goodNum}&isNormalClub=${isNormalClub}');">
      支付宝支付（￥ ${cashPrice}）
    </a>
  </div>
  <%--<c:if test="${pLevel != 2}">--%>
    <%--<div class="dialog-button">--%>
      <%--<a href="javascript:void(0)" class="btn btn-success " style="width:100%;height:35px" onclick="openDialog('2', ${goodPrice.id});">--%>
        <%--提成金购买（￥${goodPrice.nonCashDiscount * goodPrice.cashPrice}）--%>
        <%--<div class="red rotating" style="margin: -28px 0 0 11em; font-size: 18px; "><b>${goodPrice.remark}购买</b></div>--%>
      <%--</a>--%>
    <%--</div>--%>
  <%--</c:if>--%>
</c:if>

<c:if test="${!empty goodPrice and dlgId == 2}">
  <c:if test="${canBuy}">
    <ul class="list list-unstyled ml-md" >
      <li class="mb-none" >您的账号提成余额：${deposit}元</li>
      <li class="mb-none" >本次购买钻石所需：${goodPrice.cashPrice * goodPrice.nonCashDiscount}元</li>
      <li class="mb-none">购买后账户余额：${deposit - (goodPrice.cashPrice * goodPrice.nonCashDiscount)}元</li>
    </ul>
    <div class="dialog-button">
      <a href="javascript:void(0)" class="btn btn-success " style="width:100%;height:35px" onclick="buyGameCard('${goodPrice.id}')">
        确认购买
      </a>
    </div>
  </c:if>
  <c:if test="${canBuy == false}">
    <!-- 余额不足 -->
    <h4 class="center red mt-xlg">账户余额不足!</h4>
  </c:if>
</c:if>

<div class="dialog-button">
  <a href="javascript:void(0)" class="btn btn-default" style="width:100%;height:35px" onclick="$('#dlg1').dialog('close')">取消</a>
</div>

<script>
  function buyGameCard(goodId) {

    $.post("buy/buyGameCard", {goodId: goodId}, function(data){
      var json = JSON.parse(data);
      if(json.isSuccess) {
        $.messager.alert('提示信息'
                , '<h4 class="center bold red mt-md">购买成功!</h4>'
                , ''  //图标
                , function() {
                  setTimeout(function() {
                    location.reload();
                  }, 1500);
        });
      } else {
        $.messager.alert('提示信息','<h5 class="center bold red mt-lg">购买失败!原因:'+ json.remark +'</h5>');
      }
    });

    //关闭上级弹窗
    $('#dlg1').dialog('close');
  }
</script>
