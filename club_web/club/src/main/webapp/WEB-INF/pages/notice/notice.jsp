<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<style>
    .title {
        line-height: 1.4em;
        font-size: 13px;
    }
</style>

<!-- 公告列表 -->
<div class="easyui-navpanel mt-md">
<c:if test="${length==0}">
    <div class="center-vertical">
        <h4 class="black bold center">您暂无消息</h4>
    </div>
</c:if>
<c:if test="${dtNoticeList != null}">
    <c:forEach items="${dtNoticeList}" var="row">
            <blockquote class="with-borders col-md-10 col-xs-10 col-md-offset-1 col-xs-offset-1">
                <div class="col-md-12 col-xs-12 sample-icon">
                    <input type="text" id="${row.getColumnValue("dId")}_content" hidden="hidden" value="${row.getColumnValue('content')}">
                    <a href="javascript:;" style="text-decoration-line: none" onclick="checkMessage(
                            ${row.getColumnValue('isRead')}
                            ,'${row.getColumnValue("dId")}'
                            , '${row.getColumnValue("title")}'
                            , '${row.getColumnValue("createTime")}')" >
                        <c:if test="${row.getColumnValue('isRead')==false}">
                            <span id="${row.getColumnValue("dId")}" >
                                <i class="fa fa-lg fa-envelope gray">
                                    <span class="title" >${row.getColumnValue("title")}</span>
                                </i>
                                <i class="fa fa-circle red" style="font-size: 1px;"></i>
                            </span>
                        </c:if>
                        <c:if test="${row.getColumnValue('isRead')==true}">
                            <i class="fa fa-lg fa-envelope-o gray">
                                <span class="title" >${row.getColumnValue("title")}</span>
                            </i>
                        </c:if>
                        <span class="ml-md mt-xs gray" style="font-size: 80%;line-height: 1.42857143;">${row.getColumnValue("createTime")}</span>
                    </a>
                </div>
        </blockquote>
    </c:forEach>
</c:if>
</div>

<div id="win">
    <div id="content" style="margin: 12px 12px"></div>
</div>


<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp"%>

<script>
    function checkMessage(isRead,dId, title, createTime){
        //图标改变
        $("#"+dId).empty();
        $("#"+dId).append("<i class='fa fa-lg fa-envelope-o gray'><span class='title' >"+title+"</span></i>");
        var url= 'noticeList';
        var content = document.getElementById(dId+"_content").value;
        if(isRead==false){
            $.ajax({
                type: "get",
                url: "checkNotice",
                data:{"dId":dId},
                async: false,
                dataType: "text",
                success: function (data) {
                    if(data==1){
                        DiaLog(title, content, createTime);
                    }else{
                        $.messager.alert('提示信息', '<h5 class="center">系统繁忙，请稍后再查看!</h5>', '', function() {
                            location.reload(url);
                        });
                    }
                }
            });
        }else if(isRead==true){
            DiaLog(title, content, createTime);
        }

    }

    function DiaLog(title, content, createTime){
        var _width = window.screen.width * 0.94;
        var _height = window.screen.height;
        var _content =
                '<div class="pull-left"><h5 class="mb-none">各位代理商:</h5></div>' +
                '<br style="line-height: 3em">' +
                    //信息内容
                '<p><!--首行空两格-->' +
                '<span style="padding-left: 26px;word-wrap:break-word;white-space:pre-wrap;overflow:hidden;">' + content + '</span>' +
                '</p>' +
                    //消息发送方
                '<div class="pull-right">' +
                '<h5 class="mb-none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;官方公告</h5>' +
                '<h5>' + createTime + '</h5>' +
                '</div>' +
                '<button class="btn btn-default btn-block" onclick="close_win()">确&nbsp;&nbsp;定</button>';
        $("#content").append(_content);

        //创建window
        $('#win').window({
            title: title,
            width: _width,
            shadow: true,
            draggable:false,
            modal:true
        });

//        alert(parseInt($('#content').get(0).offsetHeight) > 300);
        if(parseInt($('#content').get(0).offsetHeight) > 330) {
            $('#win').window({
                width: _width,
                height: 330,
                shadow: true,
                draggable:false,
                modal:true,
                top:Math.abs(($(window).height() - 330) * 0.5)
//                left:($(window).width() - _width) * 0.5
            });
        }

        $('#win').window('open');
    };

    function close_win(){
        $('#win').window('close');
        $("#content").empty();
    }

</script>