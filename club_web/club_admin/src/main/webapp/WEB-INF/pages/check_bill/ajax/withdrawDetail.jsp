<!--提现明细（提现审核和提现查询内的子功能）-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Vendor -->
<script src="http://download.stevengame.com/webResource/common/vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="http://download.stevengame.com/webResource/common/vendor/common/common.min.js"></script>

<!-- Theme Base, Components and Settings -->
<script src="http://download.stevengame.com/webResource/common/js/theme.js"></script>
<script src="http://download.stevengame.com/webResource/common/js/theme.init.js"></script>

<!-- Global script -->
<script src="http://download.stevengame.com/webResource/common/vendor/js/app.min.js"></script>
<script src="http://download.stevengame.com/webResource/common/vendor/js/layout.min.js"></script>

<!-- 表单验证 -->
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>

<!-- layer -->
<script src="https://cdn.bootcss.com/layer/3.0.1/layer.js"></script>
<script charset="utf-8"
        src="http://download.stevengame.com/webResource/common/vendor/plugins/layer/layer.1.0.2.js"></script>

<!-- Favicon -->
<link rel="shortcut icon" href="http://download.stevengame.com/webResource/common/img/favicon.ico"/>
<!-- Mobile Metas -->
<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>

<!-- Vendor CSS -->
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.css">
<link rel="stylesheet" href="//cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.css"/>
<link rel="stylesheet" href="//cdn.bootcss.com/simple-line-icons/2.4.1/css/simple-line-icons.css">

<!-- Theme CSS -->
<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/theme.css">
<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/theme-elements.css">
<%--<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/theme-animate.css">--%>

<!-- Skin CSS -->
<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/css/skins/skin-corporate-3.css">
<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/metronic/layout.min.css">
<link rel="stylesheet"
      href="http://download.stevengame.com/webResource/common/vendor/css/metronic/themes/darkblue.min.css">
<!-- Theme Custom CSS -->
<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/css/custom.css">
<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/skins/default.css">

<!-- Theme Global CSS -->
<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/components.min.css">
<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/components.min.0.0.1.css">
<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/css/plugins.min.css">

<!-- Jquery -->
<script src="http://download.stevengame.com/webResource/common/vendor/jquery/jquery.min.js"></script>
<style type="text/css">
    .tb1 tr {
        background: #f1f1f1;
    }

    .tb1 tr:nth-child(even) {
        background: #ccc;
    }

    td, th {
        text-align: center;
        border: 0px;
    }

    .tb1 td, th {
        border-left: 1px solid black;
    }
</style>
<!-- 提现明细 -->
<div class="page-container" style="background-color: white;">

    <div class="page-content-wrapper" style="background-color: white;">
        <div class="page-content" style="height:100%;background-color: white;margin: auto;">
            <div class="row">
                <div>
                    <table width="100%">
                        <tr>
                            <td style="text-align: left;">
                                <input type="button" class="btn green btn-outline" value="导出excel" onclick="downLoad()">
                                <input type="hidden" id="date" value="${date}">
                            </td>
                            <td style="text-align: right;">${date2}</td>
                        </tr>
                    </table>
                    <br/>
                </div>
                <c:if test="${not empty dt}">
                    <div class="table-responsive">
                        <table class="table table-hover table-striped table-bordered tb1">
                            <thead>
                            <tr>
                                <th>代理商ID</th>
                                <th>昵称</th>
                                <th>提现金额</th>
                                <th>姓名</th>
                                <th>开户地</th>
                                <th>开户银行</th>
                                <th>银行卡号</th>
                                <th>身份证号</th>
                                <th>手机号</th>
                                <th>流水单号</th>
                                <th>是否成功</th>
                                <th>时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${dt}" var="row">
                                <c:if test="${row.getColumnValue('withdrawStatus')=='2'}">
                                    <tr style="color: red;">
                                        <td>${row.getColumnValue("promoterId")}</td>
                                        <td>${row.getColumnValue("nickName")}</td>
                                        <td>${row.getColumnValue("withdrawAmount")}</td>
                                        <td>${row.getColumnValue("realName")}</td>
                                        <td>${row.getColumnValue("bankArea").split("-")[0]}-${row.getColumnValue("bankArea").split("-")[1]}</td>
                                        <td>
                                            <c:if test="${row.getColumnValue('bankArea')==''}">-</c:if>
                                            <c:if test="${row.getColumnValue('bankArea')!=''}">${row.getColumnValue("bankArea").split("-")[2]}</c:if>
                                        </td>
                                        <td>${row.getColumnValue("bankAccount")}</td>
                                        <td>${row.getColumnValue("IDCard")}</td>
                                        <td>${row.getColumnValue("cellPhone")}</td>
                                        <td>${row.getColumnValue("serialno")}</td>
                                        <td>${row.getColumnValue("withdrawStatus")=='1' ? '是':'否'}</td>
                                        <td>${row.getColumnValue("createTime")}</td>
                                    </tr>
                                </c:if>
                                <c:if test="${row.getColumnValue('withdrawStatus')!='2'}">
                                    <tr>
                                        <td>${row.getColumnValue("promoterId")}</td>
                                        <td>${row.getColumnValue("nickName")}</td>
                                        <td>${row.getColumnValue("withdrawAmount")}</td>
                                        <td>${row.getColumnValue("realName")}</td>
                                        <td>${row.getColumnValue("bankArea").split("-")[0]}-${row.getColumnValue("bankArea").split("-")[1]}</td>
                                        <td>
                                            <c:if test="${row.getColumnValue('bankArea')==''}">-</c:if>
                                            <c:if test="${row.getColumnValue('bankArea')!=''}">${row.getColumnValue("bankArea").split("-")[2]}</c:if>
                                        </td>
                                        <td>${row.getColumnValue("bankAccount")}</td>
                                        <td>${row.getColumnValue("IDCard")}</td>
                                        <td>${row.getColumnValue("cellPhone")}</td>
                                        <td>${row.getColumnValue("serialno")}</td>
                                        <td>${row.getColumnValue("withdrawStatus")=='1' ? '是':'否'}</td>
                                        <td>${row.getColumnValue("createTime")}</td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            </tbody>
                        </table>

                    </div>
                </c:if>
                <div style="width: 100%;text-align: center;line-height: 35px;">
                    <br/>
                    <input type="button" value="确&nbsp;&nbsp;定" name="create"
                           style="width: 150px;"
                           class="btn btn-lg btn-success" onclick="parent.layer.closeAll();">
                </div>
            </div>
        </div>
    </div>
</div>

<script>

    function downLoad() {
        var date = $('#date').val();
        window.open('downloadDetail?date=' + date);
    }

</script>
