<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="http://download.stevengame.com/webResource/common/vendor/jquery/jquery.min.js"></script>
</head>
<body>

    <form method="post" action="pay">
        输入代理商id：<input  type="number"  name="promoterId" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        输入充值时间: <input name="createTime" id="createTime" value="${startTime}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        输入价格:    <input type="number" name="price">
        输入钻石数量: <input type="number" name="goodNum">
        <input type="submit" />
    </form>

<script>
    $(function(){
        var start = {
            elem : '#createTime',
            format : 'YYYY-MM-DD hh:mm:ss',
            //min : laydate.now(), //设定最小日期为当前日期
            max : '2099-06-16 23:59:59', //最大日期
            istime : true,
            istoday : false,
            isclear : false,
            choose : function(datas) {
            }
        };
        laydate(start);
    });

</script>
<script src="http://download.stevengame.com/webResource/common/vendor/laydate-master/laydate.js"></script>
</body>
</html>
