<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <link href="http://download.stevengame.com/webResource/common/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet"
          type="text/css">

    <link href="http://download.stevengame.com/webResource/common/vendor/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet"
          type="text/css">

    <!-- END PAGE LEVEL STYLES -->
    <link rel="shortcut icon" href="http://download.stevengame.com/webResource/common/img/favicon.ico">

    <style type="text/css">
        .row{vertical-align:middle;height:200px;line-height:200px;width:100%;}
    </style>

</head>

<!-- BEGIN 页面内容-->
<c:if test="${isWeiXin}">
<body onload="callMpPay()">
</c:if>

<c:if test="${!isWeiXin}">
<body>
<div class="text-center container theme-showcase row"  role="main">

    <div class="page-header">
        <div class="" id="tab1">
            <div class="">
                <img  src="ajax/create?code_url=${code_url}">
            </div>
            <h1 class="text-center">
                <b>方法1：长按二维码，选择“识别图中二维码”进行支付</b>
                <br/>
                <b>（苹果用户需放大下图片）</b>
                <br/>
                <b>方法2：手机保存二维码截图到相册（<b style="color:red">5分钟有效时间</b>）</b>
                <br/>
                <b>使用微信扫一扫扫取相册中该二维码付款</b>
            </h1>
        </div>

        <a href="${successUrl}"><h1 style="color:red">如果支付完成点击这里跳转</h1></a>

    </div>

</div>
</c:if>

<script type="text/javascript" src="http://lib.sinaapp.com/js/jquery/1.8.3/jquery.min.js"></script>
<script>

    function callMpPay(){
        if (typeof WeixinJSBridge == "undefined"){
            if( document.addEventListener ){
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            }else if (document.attachEvent){
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
        }else{
            onBridgeReady();
        }
    }

    function onBridgeReady(){
        WeixinJSBridge.invoke(
                'getBrandWCPayRequest', {
                    "appId":"${appId}",     //公众号名称，由商户传入
                    "timeStamp":"${timeStamp}",         //时间戳，自1970年以来的秒数
                    "nonceStr":"${nonceStr}", //随机串
                    "package":"${pkg}",
                    "signType":"MD5",         //微信签名方式：
                    "paySign":"${paySign}" //微信签名
                },
                function(res){
//                    for( key in res ) {
//                        alert( "key is " + [key] + ", value is " + res[key] );
//                    }
                    if(res.err_msg == "get_brand_wcpay_request:ok" ) {
                        location.href = '${successUrl}';
                    }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                }
        );
    }
</script>
</body>
</html>
