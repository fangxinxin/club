<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${not empty playDetail}">
    <div>
        <table style="width: 100%;line-height: 40px;">
            <tr>
                <td width="20%"></td>
                <td width="20%"></td>
                <td width="20%" style="font-size: 14px"><b>对局明细</b></td>
                <td width="20%"></td>
                <td width="20%">
                    <c:if test="${noReadNum>0}">
                        <input class="btn btn-xs btn-success" value="全部已阅" style="width:100%;"
                               onclick="updateAllReadStatus_detail('${userId}')">
                    </c:if>
                    <c:if test="${noReadNum==0}">
                        <input class="btn btn-xs btn-default" value="全部已阅" style="width:100%;">
                    </c:if>
                </td>
            </tr>
        </table>
    </div>
    <div class="table-responsive">
        <table class="table table-bordered table-striped mb-none" style="font-size: 10px;">
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
            <c:forEach var="dt" items="${playDetail}">
                <c:if test="${dt.getColumnValue('winnerNum')==1}">
                    <tr>
                            <%--房间号--%>
                        <td>
                                ${dt.getColumnValue('roomId')}<br/>
                            <c:if test="${dt.getColumnValue('isRead')}">
                                <a href="javascript:;" class="btn btn-xs btn-default">已&nbsp;阅</a>
                            </c:if>
                            <c:if test="${dt.getColumnValue('isRead')==false}">
                                <input class="btn btn-xs btn-success" value="待&nbsp;阅" style="width: 39.13px;"
                                       onclick="updateReadStatus_detail(this)">
                                <input type="hidden" value="${dt.getColumnValue('id')}">
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
                                <input class="btn btn-xs btn-success" value="待&nbsp;阅" style="width: 39.13px;"
                                       onclick="updateReadStatus_detail(this)">
                                <input type="hidden" value="${dt.getColumnValue('id')}">
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
            </tbody>
        </table>
    </div>
</c:if>
<c:if test="${empty playDetail}">
    <div>
        <table style="width: 100%;line-height: 40px;">
            <tr>
                <td width="20%"></td>
                <td width="20%"></td>
                <td width="20%" style="font-size: 14px"><b>对局明细</b></td>
                <td width="20%"></td>
                <td width="20%"></td>
            </tr>
        </table>
    </div>
    <div class="form-group col-md-12 col-xs-12 mt-xl">
        <div class="heading heading-border heading-middle-border heading-middle-border-center">
            <h4>暂无数据</h4>
        </div>
    </div>
</c:if>

<div class="dialog-button">
    <a href="javascript:layer.closeAll();" class="btn btn-default"
       style="width:100%;height:35px">确定</a>
</div>

<script>
    function reloadPlayDetail() {
        $('#dlgPlayDetail').dialog('destroy');
        $("#load_content").load("win/gameRecord_query_playDetail?startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val());
    }

    <%--更新所有状态为已读--%>
    function updateAllReadStatus_detail(userId) {
        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();
        $.post("win/gameRecord_detail_updateAllStatus", {
            userId: userId,
            startDate: startDate,
            endDate: endDate
        }, function () {
            layer.closeAll();
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
        });
    }
</script>