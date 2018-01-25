<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>
<style type="text/css">
    button {
        border-radius: 5px;
        border: 1px solid #e6e6e6;
    }
</style>
<div class="easyui-navpanel bd">
    <div id="tb" class="bd">
        <div class="m-xs">
            <div style="margin-bottom: 5px;padding:5px 0;">
                <button onclick="daily(),changeColor(this);" style="width:20%;height: 35px;" id="btn0">日报</button>
                <button onclick="sale(),changeColor(this);" style="width:25%;height: 35px;" id="btn1">销售记录</button>
                <button onclick="pay(),changeColor(this);" style="width:25%;height: 35px;" id="btn2">进货记录</button>
                <button onclick="bonus(),changeColor(this);" style="width:25%;height: 35px;" id="btn3">返钻明细</button>
                <%--<button onclick="withdraw(),changeColor(this);" style="width:20.5%;height: 35px;" id="btn4">提现记录</button>--%>
            </div>
            从
            <input class="input-sm col-xs-4 easyui-datebox" prompt="开始时间" value="${startDate}"
                   data-options="editable:false,panelWidth:220,panelHeight:240,iconWidth:30,onSelect:onSelect"
                   style="width:104px;height: 30px;padding: 1px 1px;font-size: 12px;line-height: 1.5;border-radius: 3px;"
                   id="startDate">到
            <input class="input-sm col-xs-4 easyui-datebox" prompt="结束时间"
                   data-options="editable:false,panelWidth:220,panelHeight:240,iconWidth:30,onSelect:onSelect"
                   value="${endDate}"
                   style="width:104px;height: 30px;padding: 1px 1px;font-size: 12px;line-height: 1.5;border-radius: 3px;"
                   id="endDate">
            <input type="button" id="search" class="btn btn-xs btn-primary ml-sm" value="查询" style="width:50px">
        </div>
    </div>

    <!-- 表格 -->
    <table id="content"></table>
</div>
<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp" %>

<script type="text/javascript">
    //选择日期不能大于当前日期

    $(function () {
        daily();
        $('#btn0').css("background-color", "lightblue");
        $('#btn1').css("background-color", "white");
        $('#btn2').css("background-color", "white");
        $('#btn3').css("background-color", "white");
        $('#btn4').css("background-color", "white");
        var now = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());

        $('#startDate').datebox('calendar').calendar({
            validator: function (date) {
                return date <= now;
            }
        });
        $('#endDate').datebox('calendar').calendar({
            validator: function (date) {
                return date <= now;
            }
        });
    });

    function changeColor(obj) {
        $('#btn0').css("background-color", "white");
        $('#btn1').css("background-color", "white");
        $('#btn2').css("background-color", "white");
        $('#btn3').css("background-color", "white");
        $('#btn4').css("background-color", "white");
        $(obj).css("background-color", "lightblue");
    }

    function onSelect() {
        var sd = $('#startDate').datebox('getValue'), ed = $('#endDate').datebox('getValue');
        if (ed < sd) {
            parent.$.messager.alert("提示", "结束日期不能小于开始日期，请重新选择!");
            $('#startDate').datebox('setValue', '${startDate}');
            $('#endDate').datebox('setValue', '${endDate}');
        }
    }

    $("#search").click(function () {
        $('#content').datagrid('reload', {"startDate": $("#startDate").val(), "endDate": $("#endDate").val()});
    });
    $("#reload").click(function () {
        location.reload();
    });
    function daily() {
        $('#a1').css({
            'width': '87px', 'height': '30px', 'background-color': 'rgb(230, 230, 230)'
        });
        $('#content').datagrid({
                    url: 'salelog/promoter_report',
                    pagination: true,
                    fit: true,
                    scrollbarSize: "1",
                    pageList: [10],
                    pageSize: '10',
                    loadMsg: "正在读取数据",
                    border: "true",
                    toolbar: '#tb',
                    header: '#HeaderTableMember',
                    rownumbers: false,
                    columns: [[
                        {field: 'statdate', title: '日期', width: '25%', align: 'center'},
                        {field: 'roomcreatenum', title: '俱乐部开局总数', width: '25%', align: 'center'},
                        {field: 'gamecardconsume', title: '俱乐部钻石消耗', width: '25%', align: 'center'},
                        {field: 'gamecardsell', title: '钻石销售', width: '28%', align: 'center'}
                    ]]
                }
        ).datagrid('getPager').pagination({
            layout: ['list', 'sep', 'first', 'prev', 'sep', $('#p-style').val(), 'sep', 'next', 'last', 'sep', 'refresh', 'info']
        })
    }
    function sale() {
        $('#a1').css({
            'width': '87px', 'height': '30px', 'background-color': 'rgb(230, 230, 230)'
        });
        $('#content').datagrid({
                    url: 'salelog/sell_record',
                    pagination: true,
                    fit: true,
                    scrollbarSize: "1",
                    pageList: [10],
                    pageSize: '10',
                    loadMsg: "正在读取数据",
                    border: "true",
                    toolbar: '#tb',
                    header: '#HeaderTableMember',
                    rownumbers: false,
                    columns: [[
                        {field: 'gameuserid', title: '买家ID', width: '18%', align: 'center'},
                        {
                            field: 'gamenickname', title: '买家帐号', width: '25%', align: 'center',
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
                        {
                            field: 'sellnum', title: '钻石数量', width: '19%', align: 'center',
                            formatter: function (value, row, index) {
                                var sell = row.sellnum;
                                return parseInt(sell) * -1;
                            }
                        },
                        {field: 'createtime', title: '销售时间', width: '41%', align: 'center'}
                    ]]
                }
        ).datagrid('getPager').pagination({
            layout: ['list', 'sep', 'first', 'prev', 'sep', $('#p-style').val(), 'sep', 'next', 'last', 'sep', 'refresh', 'info']
        })
    }
    function pay() {
        var tt = $('#content').datagrid({
                    url: 'salelog/pay_record',
                    pageList: [10],
                    pageSize: '10',
                    fit: true,
                    pagination: true,
                    columns: [[
                        {field: 'goodnum', title: '钻石数量', width: '31%', align: 'center'},
                        {field: 'paytype', title: '支付方式', width: '32%', align: 'center'},
                        {field: 'createtime', title: '操作时间', width: '40%', align: 'center'}
                    ]]
                }
        );
        tt.datagrid('getPager').pagination({
            layout: ['list', 'sep', 'first', 'prev', 'sep', $('#p-style').val(), 'sep', 'next', 'last', 'sep', 'refresh', 'info']
        });
    }
    function bonus() {
        $('#content').datagrid({
                    url: 'salelog/bonus_record',
                    pageList: [10],
                    pageSize: '10',
                    fit: true,
                    pagination: true,
                    columns: [[
                        {field: 'frompromoterid', title: '代理商ID', width: '20%', align: 'center'},
                        {field: 'diamond', title: '充值钻石', width: '20%', align: 'center'},
                        {field: 'rebatepercent', title: '返还比率', width: '20%', align: 'center'},
                        {field: 'rebatediamond', title: '返还钻石', width: '20%', align: 'center'},
                        {field: 'createdate', title: '充值日期', width: '22%', align: 'center'}
                    ]]
                }
        ).datagrid('getPager').pagination({
            layout: ['list', 'sep', 'first', 'prev', 'sep', $('#p-style').val(), 'sep', 'next', 'last', 'sep', 'refresh', 'info']
        })
    }
    function withdraw() {
        $('#content').datagrid({
                    url: 'salelog/withdraw_record',
                    pageList: [10],
                    pageSize: '10',
                    fit: true,
                    pagination: true,
                    columns: [[
                        {field: 'withdrawamount', title: '提现金额', width: '18%', align: 'center'},
                        {field: 'serialno', title: '流水单号', width: '27%', align: 'center'},
                        {
                            field: 'withdrawstatus', title: '提现状态', width: '20%', align: 'center',
                            formatter: function (value, row, index) {
                                var status = row.withdrawstatus;
                                if (status == 0) {
                                    return '正在提现';
                                } else if (status == 1) {
                                    return '提现成功';
                                } else {
                                    return '提现异常';
                                }
                            }
                        },
                        {field: 'createtime', title: '申请时间', width: '38%', align: 'center'}
                    ]]
                }
        ).datagrid('getPager').pagination({
            layout: ['list', 'sep', 'first', 'prev', 'sep', $('#p-style').val(), 'sep', 'next', 'last', 'sep', 'refresh', 'info']
        })
    }

</script>