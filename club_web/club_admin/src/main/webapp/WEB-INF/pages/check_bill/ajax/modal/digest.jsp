<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>
<c:if test="${not empty digestTable}">
    <div class="table-responsive">
        <div>
            <table width="100%">
                <tr>
                    <td style="text-align: left;"><input type="button" class="btn green btn-outline" value="导出excel"
                                                         onclick="downLoad()"></td>
                    <td style="text-align: right;">${date}</td>
                </tr>
            </table>
            <br/>
        </div>
        <table class="table table-hover table-striped table-bordered">
            <thead>
            <tr>
                <th>代理商ID</th>
                <th>直属提成笔数</th>
                <th>直属提成金额</th>
                <th>非直属提成笔数</th>
                <th>非直属提成金额</th>
                <th>提成总额</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${digestTable}" var="row">
                <c:set var="parentBonus" value="${row.getColumnValue('parentBonus')}"/>
                <c:set var="nonParentBonus" value="${row.getColumnValue('nonParentBonus')}"/>
                <tr>
                    <td>${row.getColumnValue("promoterId")}</td>
                    <td>${row.getColumnValue('parentBonusTime')}</td>
                    <td>
                        <c:if test="${not empty parentBonus}">
                            ${row.getColumnValue("parentBonus")}
                        </c:if>
                        <c:if test="${empty parentBonus}">
                            -
                        </c:if>
                    </td>
                    <td>${row.getColumnValue('nonParentBonusTime')}</td>
                    <td>
                        <c:if test="${not empty nonParentBonus}">
                            ${row.getColumnValue("nonParentBonus")}
                        </c:if>
                        <c:if test="${empty nonParentBonus}">
                            -
                        </c:if>
                    </td>
                    <td>${row.getColumnValue("totalBonus")}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>
<c:if test="${empty digestTable}">
    <div class="row">
        <div class="col-md-12" style="height: 300px">
            <h4 class="center center-vertical">暂无数据</h4>
        </div>
    </div>
</c:if>

<script>
    //导出
    function downLoad() {
        var payCreateDate = '${date}';
        var bonusId = '${bonusId}';
        window.location.href = "bonusQuery/digestDownload?bonusId=" + bonusId + "&payCreateDate=" + payCreateDate;
    }

</script>
