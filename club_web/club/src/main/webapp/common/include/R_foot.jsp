<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- Vendor -->
<script src="http://download.stevengame.com/webResource/common/vendor/bootstrap/js/bootstrap.min.js"></script>

<!-- 表单验证 -->
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>

<!-- Theme Base, Components and Settings -->
<script src="http://download.stevengame.com/webResource/common/js/theme.js"></script>

<!-- EasyUI -->
<script src="http://download.stevengame.com/webResource/common/vendor/jquery-easyui/js/jquery.easyui.min.js"></script>
<script src="http://download.stevengame.com/webResource/common/vendor/jquery-easyui/js/jquery.easyui.mobile.js"></script>
<script src="http://download.stevengame.com/webResource/common/vendor/jquery-easyui/themes/locale/easyui-lang-zh_CN.js"></script>

<!-- Theme myLayer-->
<script src="https://cdn.bootcss.com/layer/3.0.1/layer.js"></script>
<script src="http://download.stevengame.com/webResource/dsqp_config/js/jquery.layer.js"></script>
<script charset="utf-8" src="http://download.stevengame.com/webResource/common/js/plugins_init/layer/layer.js"></script>

<script>
    $(function () {
        function hengshuping(){
            window.location.reload();
        }
        window.addEventListener("onorientationchange" in window ? "orientationchange" : "resize", hengshuping, false);
    })
</script>

</body>
</html>
