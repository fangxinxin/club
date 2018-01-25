<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<c:if test="${promoter.pLevel == 2}">
    <%--<div class="center-vertical">--%>
        <br/><br/><br/><br/><br/><br/><br/>
        <h3 class="red bold center">您暂无权限访问此项</h3>
    <%--</div>--%>
</c:if>

<div class="easyui-navpanel bd">

    <c:if test="${promoter.pLevel != 2}">
        <!-- 账户信息 -->
        <div class="easyui-layout" id="userInfo" style="width:100%; height:110px;"></div>

        <!-- 搜索条件 -->
        <div id="tb" class="bd" style="padding:4px 5px;">
            <input id="promoterId" type="text" class="input-sm ml-xs center" placeholder="请输入代理商ID" style="width:50%">
            <input type="button" id="search" class="btn btn-xs btn-primary ml-sm" value="搜索" style="width:50px">
            <a id="reload" class="btn btn-xs btn-primary ml-xs">所有代理商</a>
        </div>

        <!-- 表格 -->
        <table id="tt"></table>
<%--        <table id="tt" class="easyui-datagrid" toolbar="#tb" header="#HeaderTableMember"  style="display: none;"
               singleSelect="true" border="false" fit="true" fitColumns="true"
               pagination="true" scrollbarSize="0" pageList="[10]" pageSize="10" loadMsg="正在读取数据"
               rownumbers="false" iconCls="icon-save">
            <thead>
            <tr>
                <th field="id" width="20%" align="center">代理商ID</th>
                <th field="nickname" width="25%" align="center" formatter="formatNickName">昵称</th>
                <th field="gamecard" width="20%" align="center">充值钻石</th>
                <th field="relation" width="15%" align="center">关系</th>
                <th field="message" width="20%" align="center" formatter="formatMes">信息</th>
            </tr>
            </thead>
        </table>--%>
    </c:if>
</div>

<!-- Dialog -->
<%@include file="/common/include/dialog/clubDialog.jsp" %>
<div id="dlgId" style="padding:20px 6px;width:90%;"></div>

<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp" %>

<script type="text/javascript">

    $(document).ready(function() {

        //加载代理信息
        $('#userInfo').load("getUserInfo");

        var tt = $('#tt').datagrid({
            url: 'getPromoterDataGrid',
            pagination: true,
            fit: true,
            scrollbarSize:"0",
            pageList:[10],
            pageSize:'10',
            loadMsg:"正在读取数据",
            border:"false",
            toolbar:'#tb',
            header:'#userInfo',
            rownumbers: false,
            columns:[[
                {field:'cellphone',title:'代理手机',width:'25%', align:'center'},
                {field:'nickname',title:'昵称',width:'25%', align:'center',
                    formatter: function(value,row,index){
                        var nickName = row.nickname;
                        var length = nickName.length;
                        if(length > 6) {
                            return nickName.substr(0,5)+"..";
                        }else {
                            return nickName;
                        }
                    }
                },
                {field:'gamecardtotal',title:'充值钻石',width:'25%', align:'center'},
                {field:'message',title:'信息',width:'25%',align:'center',
                    formatter: function(value,row,index){
                        var promoterId = row["id"];
                        return '<a class="btn btn-xs btn-primary ml-xs" onclick="openDlg('+promoterId+');">查询</a>';
                    }
                }
            ]]
        });

        tt.datagrid('getPager').pagination({
            layout:['list','sep','first','prev','sep',$('#p-style').val(),'sep','next','last','sep','refresh','info']
        });

    });

    $("#search").click(function () {
        $('#tt').datagrid('reload', {"promoterId": $("#promoterId").val()});
    });

    function reloadTb() {
        $('#tt').datagrid('reload');
        $('#userInfo').load("getUserInfo");
    }


    function openDlg(promoterId) {
        $('#dlgId').dialog({
            title: '代理商信息',
            height: 310,
            closed: false,
            cache: false,
            href: 'promoterDialog?promoterId='+promoterId,
            modal: true
        });
    };

</script>