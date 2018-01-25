<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<div class="easyui-panel" style="overflow: auto; width: 100%; height: 100%">


    <!-- 钻石购买 -->
    <div class="col-md-12 col-xs-12 mt-md center">

        <c:if test="${isNormalClub == false && not empty goodPrice}">
            <%--<div class="center-vertical">--%>
            <%--<h3 class="red bold center">您暂未通过审核，不能购买钻石</h3>--%>
            <%--</div>--%>
            <c:forEach items="${goodPrice}" var="row">
                <blockquote class="with-borders">
                    <c:set value="${row.getColumnValue('cashPrice')}" var="cashPrice"/>
                    <c:set value="${row.getColumnValue('goodNum')}" var="goodNum"/>
                    <c:set value="${row.getColumnValue('giftNum')}" var="giftNum"/>
                    <h4 class="pull-right rmt-xs red rotating bold" style="font-size: 14px;">
                            ${fn:substring(cashPrice/(goodNum+giftNum), 0, 5)}元/钻
                    </h4>
                        <%--<h4 class="pull-right rmt-md ml-xs red rotating bold">${row.getColumnValue("remark")}</h4>--%>
                    <h5>${cashPrice} 元 = ${goodNum+giftNum} 钻石</h5>
                    <c:if test="${giftNum!=0}">
                        <h5>${goodNum} + ${giftNum} <span style="font-size: 10px;">(赠)</span> 钻石</h5>
                    </c:if>
                    <div class="dialog-button center" style="width: 100%">
                        <button class="btn btn-primary" style="margin-top:-2px;" onclick="editGameCard('subtract')">
                            <strong><i class="fa fa-minus"></i></strong>
                        </button>
                        <input id="gameCardNum" class="center" type="number" min="1" maxlength="10" style="height:35px; width: 65%;"
                               oninput="editGameCard_in()" value="1"/>
                        <button class="btn btn-primary" style="margin-top: -2px;" onclick="editGameCard('add')">
                            <strong><i class="fa fa-plus"></i></strong>
                        </button>
                    </div>
                    <div class="dialog-button mt-xs">
                        <a href="javascript:void(0)" class="btn btn-success " style="width:100%;height:35px"
                           onclick="openDialog('1', '${row.getColumnValue('id')}')">
                            购&nbsp;&nbsp;买
                        </a>
                    </div>
                </blockquote>
            </c:forEach>
        </c:if>

        <c:if test="${isNormalClub && not empty goodPrice}">
            <c:forEach items="${goodPrice}" var="row">
                <blockquote class="with-borders" onclick="openDialog('1', '${row.getColumnValue('id')}')">
                    <c:set value="${row.getColumnValue('cashPrice')}" var="cashPrice"/>
                    <c:set value="${row.getColumnValue('goodNum')}" var="goodNum"/>
                    <c:set value="${row.getColumnValue('giftNum')}" var="giftNum"/>
                    <h4 class="pull-right rmt-xs red rotating bold" style="font-size: 14px;">
                        ${fn:substring(cashPrice/(goodNum+giftNum), 0, 4)}元/钻
                    </h4>
                    <%--<h4 class="pull-right rmt-md ml-xs red rotating bold">${row.getColumnValue("remark")}</h4>--%>
                    <h5>${cashPrice} 元 = ${goodNum+giftNum} 钻石</h5>
                    <c:if test="${gameId == 4156 || gameId == 5539}">
                        <h5>活动</h5>
                    </c:if>
                    <c:if test="${giftNum!=0}">
                        <c:if test="${gameId != 4156 && gameId != 5539}">
                            <h5>${goodNum} + ${giftNum} <span style="font-size: 10px;">(赠)</span> 钻石</h5>
                        </c:if>
                    </c:if>
                    <%--<h4>赠送 ${row.getColumnValue("giftNum")} 钻石</h4>--%>
                </blockquote>
            </c:forEach>
        </c:if>
    </div>

    <!-- 弹框 -->
    <div id="dlg1" style="padding:20px 6px;width:80%;"></div>

</div>

<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp"%>

<!-- 购买钻石 :: 输入钻石数控制 -->
<script charset="utf-8">

    //action: add 增钻,subtract 减钻
    //cardType: 1 剩余钻石,2 开房剩余钻石
    function editGameCard(action){
        var gameCardNum = parseInt($.trim($('#gameCardNum').val()));    //购买钻石数量

        if(isNaN(gameCardNum)) {
            $("#gameCardNum").val(1);
            $.messager.alert('', '<h4 class="center bold red mt-md">请输入正确数量！</h4>', '');
        }  else {
            if(action == "add") {
                gameCardNum += 1;
            }
            else if(action == "subtract") {
                gameCardNum -= 1;
            }

            //显示变化
            if(gameCardNum <= 0) {
                return;
            } else {
                $('#gameCardNum').val(gameCardNum);
                return;
            }

        }

    };

    /**手动输入**/
    function editGameCard_in() {
        var gameCardNum = parseInt($.trim($('#gameCardNum').val()));    //购买钻石数量
        var isNum = /^[1-9]\\d*$/;

        //判断非数字的字符
        if(isNum.test(gameCardNum) || isNaN(gameCardNum) || gameCardNum <= 0) {
            $("#gameCardNum").val(1);
            return;
        }

    };


</script>

<!-- 购买钻石 :: 选择购买方式 -->
<script>

    function openDialog(dlgId, id) {
        var link = 'buy/buyDialog?dlgId='+ dlgId +'&id='+ id +"&isNormalClub="+ ${isNormalClub};
        if(${isNormalClub == false}) {
            var gameCardNum = parseInt($.trim($('#gameCardNum').val()));    //购买钻石数量

            if($('#gameCardNum').val().length > 9) {
                $("#gameCardNum").val(1);
                $.messager.alert('', '<h4 class="center bold red mt-md">请输入正确数量！</h4>', '');
                return;
            } else {
                link = link +'&gameCardNum='+ gameCardNum;
            }
        }
        if(dlgId == 1) {
            $('#dlg1').dialog({
                title: '购买方式',
                closed: false,
                height: 260,
                cache: false,
                href: link,
                modal: true
            });
        }
        else if(dlgId == 2) {
            $('#dlg1').dialog({
                title: '提成购买',
                closed: false,
                height: 260,
                cache: false,
                href: 'buy/buyDialog?dlgId='+ dlgId +'&id='+ id,
                modal: true
            });
        }

    }

</script>

