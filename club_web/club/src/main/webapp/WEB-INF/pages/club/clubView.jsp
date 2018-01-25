<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>
<style>

    .notice {
        width: 10px;
        height: 10px;
        line-height: 10px;
        font-size: 10px;
        color: #fff;
        text-align: center;
        background-color: #f00;
        border-radius: 50%;
        position: relative;
        right: 5px;
        top: -3px;
    }

</style>

<div class="easyui-navpanel bd">

    <%--账户信息--%>
    <div class="easyui-layout" id="userInfo" style="width:100%; height:110px;"></div>

    <%--搜索条件--%>
    <div id="tb" class="bd center" style="padding:5px 5px; display:none;">

        <a href="javascript:;" id="member_manage" class="btn btn-sm btn-default ml-md pull-left"
           onclick="club_win('会员管理','win/member')">会员管理</a>
    <span class="pull-left" id="span">
        &nbsp;
    </span>

        <a href="javascript:;" class="btn btn-sm btn-default"
           onclick="club_win2('销售钻石','win/sell')">销售钻石</a>

        <a class="btn btn-sm btn-default mr-md pull-right"
           onclick="club_iframe('战绩查询','win/gameRecord')">战绩查询</a>

    </div>

    <%--表格--%>
    <table id="tt"></table>

</div>

<!-- Dialog -->
<%@include file="/common/include/dialog/clubDialog.jsp" %>
<div id="dlgId" style="padding:20px 6px; width:90%;"></div>

<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp" %>
<%--<script src="https://cdn.bootcss.com/layer/3.0.3/mobile/layer.js"></script>--%>

<script type="text/javascript">
    function club_iframe(title, url) {
        $("#HeadTitle", parent.document.body).html(title);
        $("#Content", parent.document.body).attr("src", url);
    }

    //点击之后去掉红点
    $("#member_manage").click(function () {
        $("#span").removeClass("notice");
    });

    //加载代理信息
    $('#userInfo').load("getUserInfo");

    $(document).ready(function () {

        var hasRequest = '${hasRequest}';
        if (hasRequest == 'true') {
            $('#span').addClass('notice');
        }

        var tt = $('#tt').datagrid({
            url: 'getClubDataGrid',
            pagination: true,
            fit: true,
            scrollbarSize: "0",
            pageList: [10],
            pageSize: '10',
            loadMsg: "正在读取数据",
            border: "false",
            toolbar: '#tb',
            header: '#userInfo',
            rownumbers: false,
            columns: [[
                {field: 'gameuserid', title: '玩家ID', width: '25%', align: 'center'},
                {
                    field: 'gamenickname', title: '昵称', width: '19%', align: 'center',
                    formatter: function (value, row, index) {
                        var nickName = row.gamenickname;
                        var length = nickName.length;
                        if (length > 6) {
                            return nickName.substr(0, 5) + "..";
                        } else {
                            return nickName;
                        }

                    }
                },
                {field: 'privateroomdiamond', title: '钻石', width: '14%', align: 'center'},
                {field: 'pyjroomnum', title: '周局数', width: '13%', align: 'center'},
                {
                    field: 'formatOpera', title: '身份', width: '14%', align: 'center',
                    formatter: function (value, row, index) {
                        var gameUserId = row["gameuserid"];
                        if (row["promoterid"] == 0) {
                            return '<a href="javascript:;" class="btn btn-xs btn-link">群主</a>';
                        } else {
                            return '<a class="btn btn-xs btn-success" onclick="openDlg(1, ' + gameUserId + ');">群员</a>';
                        }
                    }
                },
                {
                    field: 'sell', title: '售钻', width: '16%', align: 'center',
                    formatter: function (value, row, index) {
                        var gameUserId = row["gameuserid"];
                        return '<a class="btn btn-xs btn-success ml-xs" onclick="openDlg(2,' + gameUserId + ');">出售</a>';
                    }
                }
            ]]
        });

        tt.datagrid('getPager').pagination({
            layout: ['list', 'sep', 'first', 'prev', 'sep', $('#p-style').val(), 'sep', 'next', 'last', 'sep', 'refresh', 'info']
        });

    });

    $("#search").click(function () {
        $('#tt').datagrid('reload', {"userId": $("#userId").val()});
    });

    function reloadTb() {
        $('#tt').datagrid('reload');
        $('#userInfo').load("getUserInfo");
    }


    function openDlg(dlgId, gameUserId) {
        //踢出俱乐部
        if (dlgId == 1) {
            $('#dlgId').dialog({
                title: '成员管理',
                height: 265,
                closed: false,
                cache: false,
                href: 'clubDialog?dlgId=' + dlgId + '&gameUserId=' + gameUserId,
                modal: true
            });
        }

        //销售钻石
        else if (dlgId == 2) {
            $('#dlgId').dialog({
                title: '发货',
                height: 305,
                closed: false,
                cache: false,
                href: 'clubDialog?dlgId=' + dlgId + '&gameUserId=' + gameUserId,
                modal: true
            });
        }
    }
    ;

    function club_win(title, url) {
        layer_win_url(title, url);
    }

    function club_win2(title, url) {
        layer_win_url2(title, url);
    }

    function layer_win_url(title, url) {
        $.post(url, {}, function (str) {
            layer.open({
                type: 1
                , title: [title, 'text-align: center; padding-left: 78px; font-size: 16px;']
                , shade: 0.6 //遮罩透明度
                , shadeClose: true
                , area: ['98%', '90%']
                , closeBtn: 0
                , btnAlign: 'c'
                , btn: ['关闭']
                , yes: function (index) {
                    layer.close(index);
                    reloadTb();
                }
                , anim: 1 //0-6的动画形式，-1不开启
                , content: str //注意，如果str是object，那么需要字符拼接。
            });
        });
    }

    function layer_win_url2(title, url) {
        $.post(url, {}, function (str) {
            layer.open({
                type: 1
                , title: [title, 'text-align: center; padding-left: 78px; font-size: 16px;']
                , shade: 0.6 //遮罩透明度
                , shadeClose: true
                , area: ['96%', '360px']
//                ,closeBtn:0
                , anim: 1 //0-6的动画形式，-1不开启
                , content: str //注意，如果str是object，那么需要字符拼接。
            });
        });
    }

    function close_layer() {
        layer.close(layer.index);
    }

</script>