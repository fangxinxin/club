<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="easyui-panel scroll" id="upPart">
    <%-- 大赢家报表 --%>
    <c:if test="${isGameRecordWinner}">
        <c:if test="${not empty winnerList}">
            <div class="table-responsive" id="winnerPart">
                <table class="table table-bordered table-striped mb-none" style="font-size: 14px;">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>玩家</th>
                        <th>大赢家次数</th>
                        <th>合计消耗</th>
                        <th>对局明细</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${winnerList}" var="dt">
                        <tr>
                            <td>${dt.getColumnValue('no')}</td>
                            <td
                                    <c:if test="${dt.getColumnValue('winnerNums')>dt.getColumnValue('winnerTime')}">style="color: red;" </c:if> >
                                    ${dt.getColumnValue('winnerName')}<br/>
                                ID:${dt.getColumnValue('winnerId')}
                            </td>
                            <td>${dt.getColumnValue('winnerTime')}</td>
                            <td>${dt.getColumnValue('gameCard')}钻</td>
                            <td>
                                <div class="center">
                                    <input type="button" class="btn btn-xs btn-primary" value="查&nbsp;&nbsp;询"
                                           onclick="layer_Win(this);">
                                    <input value="${dt.getColumnValue('winnerId')}" type="hidden">
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${empty winnerList}">
            <div class="form-group col-md-12 col-xs-12 mt-xl">
                <div class="heading heading-border heading-middle-border heading-middle-border-center">
                    <h4>暂无数据</h4>
                </div>
            </div>
        </c:if>
    </c:if>

    <%-- 房主报表 --%>
    <c:if test="${isGameRecordHost}">
        <c:if test="${not empty ownerList}">
            <div class="table-responsive">
                <table class="table table-bordered table-striped mb-none" style="font-size: 14px;">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>玩家</th>
                        <th>房主次数</th>
                        <th>合计消耗</th>
                        <th>对局明细</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${ownerList}" var="dt">
                        <tr>
                            <td>
                                    ${dt.getColumnValue('no')}

                            </td>
                            <td>
                                    ${dt.getColumnValue('ownerName')}<br/>
                                ID:${dt.getColumnValue('ownerId')}
                            </td>
                            <td>${dt.getColumnValue('ownerNum')}</td>
                            <td>${dt.getColumnValue('gameCard')}钻</td>
                            <td>
                                <div class="center">
                                    <input type="button" class="btn btn-xs btn-primary" value="查&nbsp;&nbsp;询"
                                           onclick="layer_Host(this);">
                                    <input value="${dt.getColumnValue('ownerId')}" type="hidden">
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${empty ownerList}">
            <div class="form-group col-md-12 col-xs-12 mt-xl">
                <div class="heading heading-border heading-middle-border heading-middle-border-center">
                    <h4>暂无数据</h4>
                </div>
            </div>
        </c:if>
    </c:if>

    <%--战绩明细--%>
    <c:if test="${isGameRecordDetail}">
        <div class="table-responsive">
            <table class="table table-bordered table-striped mb-none" style="font-size: 14px;">
                <thead>
                <tr>
                    <th>房号</th>
                    <th>参与者</th>
                    <th>玩家ID</th>
                    <th>积分</th>
                    <th>时间</th>
                </tr>
                </thead>
                <tbody>

                <c:if test="${not empty dtRecordDetail}">
                    <c:forEach var="dt" items="${dtRecordDetail}">
                        <c:if test="${dt.getColumnValue('winnerNum')==1}">
                            <tr>
                                    <%--房间号--%>
                                <td>
                                        ${dt.getColumnValue('roomId')}<br/>
                                    <c:if test="${dt.getColumnValue('isRead')}">
                                        <a href="javascript:;" class="btn btn-xs btn-default">已&nbsp;阅</a>
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('isRead')==false}">
                                        <a href="javascript:;updateReadStatus(${dt.getColumnValue('id')})"
                                           class="btn btn-xs btn-success">待&nbsp;阅</a>
                                    </c:if>

                                </td>
                                    <%--参与者--%>
                                <td style="text-align:left;">
                                    <c:if test="${dt.getColumnValue('userId1')!=0}">${dt.getColumnValue('userName1')}
                                        <c:if test="${dt.getColumnValue('userId1')==dt.getColumnValue('ownerId')}"><span
                                                style="color: red;">&nbsp;房主</span></c:if>
                                        <c:if test="${dt.getColumnValue('userId1')==dt.getColumnValue('winnerId')}"><span
                                                style="color: red;">&nbsp;大赢家</span></c:if>
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId2')!=0}"><br/>${dt.getColumnValue('userName2')}
                                        <c:if test="${dt.getColumnValue('userId2')==dt.getColumnValue('ownerId')}"><span
                                                style="color: red;">&nbsp;房主</span></c:if>
                                        <c:if test="${dt.getColumnValue('userId2')==dt.getColumnValue('winnerId')}"><span
                                                style="color: red;">&nbsp;大赢家</span></c:if>
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId3')!=0}"><br/>${dt.getColumnValue('userName3')}
                                        <c:if test="${dt.getColumnValue('userId3')==dt.getColumnValue('ownerId')}"><span
                                                style="color: red;">&nbsp;房主</span></c:if>
                                        <c:if test="${dt.getColumnValue('userId3')==dt.getColumnValue('winnerId')}"><span
                                                style="color: red;">&nbsp;大赢家</span></c:if>
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId4')!=0}"><br/>${dt.getColumnValue('userName4')}
                                        <c:if test="${dt.getColumnValue('userId4')==dt.getColumnValue('ownerId')}"><span
                                                style="color: red;">&nbsp;房主</span></c:if>
                                        <c:if test="${dt.getColumnValue('userId4')==dt.getColumnValue('winnerId')}"><span
                                                style="color: red;">&nbsp;大赢家</span></c:if>
                                    </c:if>
                                </td>
                                    <%--玩家ID--%>
                                <td style="text-align:left;">
                                    <c:if test="${dt.getColumnValue('userId1')!=0}">${dt.getColumnValue('userId1')}</c:if>
                                    <c:if test="${dt.getColumnValue('userId2')!=0}"><br/>${dt.getColumnValue('userId2')}</c:if>
                                    <c:if test="${dt.getColumnValue('userId3')!=0}"><br/>${dt.getColumnValue('userId3')}</c:if>
                                    <c:if test="${dt.getColumnValue('userId4')!=0}"><br/>${dt.getColumnValue('userId4')}</c:if>
                                </td>
                                    <%--积分--%>
                                <td style="text-align:left;">
                                    <c:if test="${dt.getColumnValue('userId1')!=0}"><c:if
                                            test="${fn:substring(dt.getColumnValue('userCashDiff1'), 0, 1) != '-'}">+</c:if>${dt.getColumnValue('userCashDiff1')}
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId2')!=0}"><br/><c:if
                                            test="${fn:substring(dt.getColumnValue('userCashDiff2'), 0, 1) != '-'}">+</c:if>${dt.getColumnValue('userCashDiff2')}
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId3')!=0}"><br/><c:if
                                            test="${fn:substring(dt.getColumnValue('userCashDiff3'), 0, 1) != '-'}">+</c:if>${dt.getColumnValue('userCashDiff3')}
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId4')!=0}"><br/><c:if
                                            test="${fn:substring(dt.getColumnValue('userCashDiff4'), 0, 1) != '-'}">+</c:if>${dt.getColumnValue('userCashDiff4')}
                                    </c:if>
                                </td>
                                    <%--时间--%>
                                <td>
                                        ${fn:substring(dt.getColumnValue('gameStopTime'), 0, 10)}<br/>
                                        ${fn:substring(dt.getColumnValue('gameStopTime'), 11, 19)}
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${dt.getColumnValue('winnerNum') > 1}">
                            <tr>
                                    <%--房间号--%>
                                <td>
                                        ${dt.getColumnValue('roomId')}<br/>
                                    <c:if test="${dt.getColumnValue('isRead')}">
                                        <a href="javascript:;" class="btn btn-xs btn-default">已&nbsp;阅</a>
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('isRead')==false}">
                                        <a href="javascript:;updateReadStatus(${dt.getColumnValue('id')})"
                                           class="btn btn-xs btn-success">待&nbsp;阅</a>
                                    </c:if>

                                </td>
                                    <%--参与者--%>
                                <td style="text-align:left;">
                                    <c:if test="${dt.getColumnValue('userId1')!=0}">${dt.getColumnValue('userName1')}
                                        <c:if test="${dt.getColumnValue('userId1')==dt.getColumnValue('ownerId')}"><span
                                                style="color: red;">&nbsp;房主</span></c:if>
                                        <c:if test="${dt.getColumnValue('userCashDiff1')==dt.getColumnValue('winnerId')}"><span
                                                style="color: red;">&nbsp;大赢家</span></c:if>
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId2')!=0}"><br/>${dt.getColumnValue('userName2')}
                                        <c:if test="${dt.getColumnValue('userId2')==dt.getColumnValue('ownerId')}"><span
                                                style="color: red;">&nbsp;房主</span></c:if>
                                        <c:if test="${dt.getColumnValue('userCashDiff2')==dt.getColumnValue('winnerId')}"><span
                                                style="color: red;">&nbsp;大赢家</span></c:if>
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId3')!=0}"><br/>${dt.getColumnValue('userName3')}
                                        <c:if test="${dt.getColumnValue('userId3')==dt.getColumnValue('ownerId')}"><span
                                                style="color: red;">&nbsp;房主</span></c:if>
                                        <c:if test="${dt.getColumnValue('userCashDiff3')==dt.getColumnValue('winnerId')}"><span
                                                style="color: red;">&nbsp;大赢家</span></c:if>
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId4')!=0}"><br/>${dt.getColumnValue('userName4')}
                                        <c:if test="${dt.getColumnValue('userId4')==dt.getColumnValue('ownerId')}"><span
                                                style="color: red;">&nbsp;房主</span></c:if>
                                        <c:if test="${dt.getColumnValue('userCashDiff4')==dt.getColumnValue('winnerId')}"><span
                                                style="color: red;">&nbsp;大赢家</span></c:if>
                                    </c:if>
                                </td>
                                    <%--玩家ID--%>
                                <td style="text-align:left;">
                                    <c:if test="${dt.getColumnValue('userId1')!=0}">${dt.getColumnValue('userId1')}</c:if>
                                    <c:if test="${dt.getColumnValue('userId2')!=0}"><br/>${dt.getColumnValue('userId2')}</c:if>
                                    <c:if test="${dt.getColumnValue('userId3')!=0}"><br/>${dt.getColumnValue('userId3')}</c:if>
                                    <c:if test="${dt.getColumnValue('userId4')!=0}"><br/>${dt.getColumnValue('userId4')}</c:if>
                                </td>
                                    <%--积分--%>
                                <td style="text-align:left;">
                                    <c:if test="${dt.getColumnValue('userId1')!=0}"><c:if
                                            test="${fn:substring(dt.getColumnValue('userCashDiff1'), 0, 1) != '-'}">+</c:if>${dt.getColumnValue('userCashDiff1')}
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId2')!=0}"><br/><c:if
                                            test="${fn:substring(dt.getColumnValue('userCashDiff2'), 0, 1) != '-'}">+</c:if>${dt.getColumnValue('userCashDiff2')}
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId3')!=0}"><br/><c:if
                                            test="${fn:substring(dt.getColumnValue('userCashDiff3'), 0, 1) != '-'}">+</c:if>${dt.getColumnValue('userCashDiff3')}
                                    </c:if>
                                    <c:if test="${dt.getColumnValue('userId4')!=0}"><br/><c:if
                                            test="${fn:substring(dt.getColumnValue('userCashDiff4'), 0, 1) != '-'}">+</c:if>${dt.getColumnValue('userCashDiff4')}
                                    </c:if>
                                </td>
                                    <%--时间--%>
                                <td>
                                        ${fn:substring(dt.getColumnValue('gameStopTime'), 0, 10)}<br/>
                                        ${fn:substring(dt.getColumnValue('gameStopTime'), 11, 19)}
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </c:if>

                </tbody>
            </table>
        </div>
    </c:if>

    <%-- 对局明细 --%>
    <c:if test="${isPlayDetail}">
        <c:if test="${not empty playDetail}">
            <div class="table-responsive" id="winnerPart">
                <table class="table table-bordered table-striped mb-none" style="font-size: 14px;">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>玩家</th>
                        <th>参与场数</th>
                        <th>应承担钻石数</th>
                        <th>对局明细</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${playDetail}" var="dt">
                        <tr>
                            <td>${dt.getColumnValue('no')}</td>
                            <td>
                                    ${dt.getColumnValue('nickName')}<br/>
                                ID:${dt.getColumnValue('userID')}
                            </td>
                            <td>${dt.getColumnValue('times')}</td>
                            <td>${dt.getColumnValue('price')}</td>
                            <td>
                                <div class="center">
                                    <input type="button" class="btn btn-xs btn-primary" value="查&nbsp;&nbsp;询"
                                           onclick="layer_playDetail(this);">
                                    <input value="${dt.getColumnValue('userID')}" type="hidden">
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${empty playDetail}">
            <div class="form-group col-md-12 col-xs-12 mt-xl">
                <div class="heading heading-border heading-middle-border heading-middle-border-center">
                    <h4>暂无数据</h4>
                </div>
            </div>
        </c:if>
    </c:if>

    <div id="dlgWin" class="easyui-panel" style="overflow: auto;padding:20px 6px; width:90%;"></div>
    <div id="dlgHost" class="easyui-panel" style="overflow: auto;padding:20px 6px; width:90%;"></div>
    <div id="dlgPlayDetail" class="easyui-panel" style="overflow: auto;padding:20px 6px; width:90%;"></div>

</div>
<div class="text-left col-md-12">
    <c:if test="${isGameRecordWinner}">
        <span style="color: red;">
            *玩家名称为红色时，表示该玩家所参与的对局中包含多个大赢家或平局情况。
        </span>
    </c:if>&nbsp;
    <input type="button" value="关闭" class="btn btn-default btn-md btn-block"
           onclick="parent.window.location.reload();">
</div>


<script>
    $(function () {
        var nContentH = document.body.clientHeight - 195;
        $('#upPart').css('height', nContentH + 'px');
    });

    <%--layer 大赢家 对局明细--%>
    function layer_Win(obj) {
        var userId = $(obj).next().val();
        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();
        $.post('win/gameRecord_query_winDetail?userId=' + userId + '&startDate=' + startDate + '&endDate=' + endDate, {}, function (str) {
            layer.open({
                type: 1
                , title: false
                , shade: 0.6 //遮罩透明度
                , shadeClose: true
                , offset: '50px'
                , area: ['98%', '80%']
                , closeBtn: 0
                , btnAlign: 'c'
                , anim: 1 //0-6的动画形式，-1不开启
                , content: str //注意，如果str是object，那么需要字符拼接。
            });
        });
    }

    <%--layer 房主 对局明细--%>
    function layer_Host(obj) {
        var userId = $(obj).next().val();
        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();
        $.post('win/gameRecord_query_hostDetail?userId=' + userId + '&startDate=' + startDate + '&endDate=' + endDate, {}, function (str) {
            layer.open({
                type: 1
                , title: false
                , shade: 0.6 //遮罩透明度
                , shadeClose: true
                , offset: '50px'
                , area: ['98%', '80%']
                , closeBtn: 0
                , btnAlign: 'c'
                , anim: 1 //0-6的动画形式，-1不开启
                , content: str //注意，如果str是object，那么需要字符拼接。
            });
        });
    }

    <%--layer 参与局数 对局明细--%>
    function layer_playDetail(obj) {
        var userId = $(obj).next().val();
        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();
        $.post('win/gameRecord_query_playDetail?userId=' + userId + '&startDate=' + startDate + '&endDate=' + endDate, {}, function (str) {
            layer.open({
                type: 1
                , title: false
                , shade: 0.6 //遮罩透明度
                , shadeClose: true
                , offset: '50px'
                , area: ['98%', '80%']
                , closeBtn: 0
                , btnAlign: 'c'
                , anim: 1 //0-6的动画形式，-1不开启
                , content: str //注意，如果str是object，那么需要字符拼接。
            });
        });
    }

    function updateReadStatus(id) {
        url = 'win/gameRecord_detail';
        $.post("win/gameRecord_detail_updateStatus", {id: id}, function (data) {
            if (data > 0) {
            } else {
                parent.$.messager.alert("提示", "系统繁忙，请稍后再试！");
            }
            $("#load_content").load(url, {startDate: $('#startDate').val(), endDate: $('#endDate').val()});
        });
    }


    function updateReadStatus_detail(obj) {
        var id = $(obj).next().val();
        $.post("win/gameRecord_detail_updateStatus", {id: id}, function (data) {
            if (data > 0) {
                $(obj).removeClass('btn-success');
                $(obj).addClass('btn-default');
                $(obj).val("已 阅");
            } else {
                content = '<h5 class="mt-xlg font-black center">系统繁忙，请稍后再试！</h5>';
                layer.alert(content);
            }
        });
    }
</script>
