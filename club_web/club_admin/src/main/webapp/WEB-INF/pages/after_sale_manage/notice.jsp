<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/lib.jsp"%>
<style>
    textarea::-webkit-input-placeholder {text-align: center;padding-top:25px}
</style>
<div class="row">
    <div class="col-md-12">

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center bold">发布公告</h4>
            </div>
            <div class="portlet-body form">

                <!-- 查询 -->
                <div class="row">
                    <div class="col-md-12">

                        <div class="form-group">
                            <label class="col-md-2 control-label center bold">公告标题：</label>
                            <div class="col-md-8">
                                <input type="text" id="_title" maxlength="15" class="form-control center" placeholder="请输入公告标题"
                                      <%-- 只能输入中文--%>
                                    <%--   onkeyup="value=value.replace(/[^\u4E00-\u9FA5]/g,'')"
                                       onpaste="value=value.replace(/[^\u4E00-\u9FA5]/g,'')"
                                       oncontextmenu = "value=value.replace(/[^\u4E00-\u9FA5]/g,'')"--%>

                                       onblur="stripScript(this)"
                                />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label bold center">公告正文：</label>
                            <div class="col-md-8">
                                <textarea class="form-control" maxlength="300" id="_content" placeholder="请输入公告正文"  cols="60" rows="15"></textarea>
                            </div>
                        </div>

                    </div>
                </div>

                <div class="row">
                    <div class="col-md-10 col-md-offset-1 center">
                        <a id="check" class="btn blue sbold" data-toggle="modal"  <%--href="#checkInfo"--%>> 发布公告 </a>
                    </div>
                </div>

            </div>
        </div>
        <!-- END 页面内容-->

    </div>
</div>

<!-- Start 模态 -->
<div id="checkInfo" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title center"><span id="p_title"></span></h4>
            </div>
            <div class="modal-body">
                <div class="pull-left"><h5 class="mb-none">各位代理商:</h5></div>
                <%--<br style="line-height: 3em">--%>
                <p><!--首行空两格--><span id="p_content" style="padding-left: 26px;word-wrap:break-word;white-space:pre-wrap;overflow:hidden;"></span></p>
                <div class="pull-right">
                    <h5 class="mb-none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;官方公告</h5>
                    <h5><span id="p_time"></span></h5>
                    <br/>
                </div><br/><br/>
                <div class="modal-footer">
                    <div class="col-md-12 center">
                        <button id="publish" onclick="publish()" type="button" data-dismiss="modal" class="btn btn-primary btn-block"> 确认发布 </button><br/>
                        <button type="button" data-dismiss="modal" class="btn btn-primary btn-block" style="background-color: #cccccc;border-color: #cccccc;"> 取&nbsp;&nbsp;消 </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
    <!-- End 模态 -->

<script>

    $("#check").click(function() {
        var content;
        var _title = $.trim($("#_title").val());
        var _content = $.trim($("#_content").val());
        $("#p_title").text($("#_title").val());
        $("#p_content").text($("#_content").val());

        //非空校验
        if(_title!="" && _content!=""&&_title.length<=15&&_content.length<=300){
            $('#checkInfo').modal('show')

            $.post("after_sale_manage/ajax/getCurrentTime", function(data) {
                if(data != null) {
                    var json = JSON.parse(data);
                    $("#p_time").text(json.currentTime);
                }
            });
        }else if(_title==""&&_content!=""){
            content =  '<h4 class="mt-xlg font-black center">公告标题不能为空！</h4>';
            layerMsg('提示信息',content);
        }else if(_title!=""&&_content==""){
            content =  '<h4 class="mt-xlg font-black center">公告正文不能为空！</h4>';
            layerMsg('提示信息',content);
        }else if(_title==""&&_content=="") {
            content =  '<h4 class="mt-xlg font-black center">公告标题和正文不能为空！</h4>';
            layerMsg('提示信息',content);
        }else if(_title.length>15) {
            content =  '<h4 class="mt-xlg font-black center">公告标题不能超过15个字符！</h4>';
            layerMsg('提示信息',content);
        }else if(_content.length>300) {
            content =  '<h4 class="mt-xlg font-black center">公告正文不能超过300个字符！</h4>';
            layerMsg('提示信息',content);
        }



    });

    function publish(){
        var title = $("#_title").val();
        var content = $("#_content").val();
        var url = "index";
        $.post("after_sale_manage/noticePublish", {title:title,content:content}, function(data) {
            if(data == 1) {
                content =  '<h4 class="mt-xlg font-black center">公告发布成功！</h4>';
                layerMsg_area_r2('公告发布',content,'500px','300px','after_sale_manage/notice');

            }else {
                content =  '<h4 class="mt-xlg font-black center">公告发布失败！</h4>';
                layerMsg('公告发布',content);
            }

        });
    }
    function stripScript(str) {
        var pattern = new RegExp("[%--`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——| {}【】‘；：”“'。，、？]")        //格式 RegExp("[在中间定义特殊过滤字符]")  
        var s = str.value;
        var rs = "";
        for (var i = 0; i < s.length; i++) {
            rs = rs+s.substr(i, 1).replace(pattern, '');
        }
        str.value = rs;
    }


</script>