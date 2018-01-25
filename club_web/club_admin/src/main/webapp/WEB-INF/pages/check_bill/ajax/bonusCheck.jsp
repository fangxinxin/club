<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp"%>
<style>
  .table td, .table th {
    text-align: center;
  }
</style>
<!-- BEGIN 上周待审核提成信息-->

<!-- 列出提成简报 -->
<c:if test="${not empty dailyTable}">
  <div class="table-responsive">
    <table class="table table-hover table-striped table-bordered">
      <thead>
      <tr>
        <th>代理商人数</th>
        <th>提成人数</th>
        <th>充值总额</th>
        <th>可提成充值总额</th>
        <th>直属提成</th>
        <th>非直属提成</th>
        <th>时间</th>
        <th>提成摘要</th>
        <th>确认信息</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${dailyTable}" var="row">
        <c:set var="bonusId" value="${row.getColumnValue('bonusId')}"/>
        <c:set var="directBonus" value="${row.getColumnValue('directBonus')}"/>
        <c:set var="nonDirectBonus" value="${row.getColumnValue('nonDirectBonus')}"/>
        <c:set var="payCreateDate" value="${row.getColumnValue('payCreateDate')}"/>
        <c:set var="isPass" value="${row.getColumnValue('isPass')}"/>
        <tr>
          <td>${row.getColumnValue("promoternum")}</td>
          <td>${row.getColumnValue("bonuspeoplenum")}</td>
          <td>${row.getColumnValue("payTotal")}</td>
          <td>${row.getColumnValue("bonusPayTotal")}</td>
          <td>${directBonus}</td>
          <td>${nonDirectBonus}</td>
          <td>${payCreateDate}</td>
          <td><input type="button" id="digest" class="btn green btn-sm" value="查询" onclick="digest('${bonusId}','${payCreateDate}');"></td>
          <td>
            <c:if test="${isPass}"> <span>已确认</span> </c:if>
            <c:if test="${isPass == false}">
              <input type="button" id="ready_check" class="btn blue btn-sm" value="待确认"
                     onclick="checkOneDayBonus('${bonusId}','${directBonus}','${nonDirectBonus}','${payCreateDate}');">
            </c:if>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>

  <!-- Start 模态 -->
  <!-- 摘要 -->
  <div id="modal_check" class="modal fade" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
          <h4 id="modal_title" class="modal-title center"></h4>
        </div>
        <div class="modal-body">
          <div id="modal_content" class="scroller mt-lg" data-always-visible="1" data-rail-visible1="1">
            <div id="modal_content_row" class="row">
              <div class="col-md-12">
                <div id="modal_check_tb_content"></div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer center">
          <div id="modal_footer"></div>
        </div>
      </div>
    </div>
  </div>
  <!-- End 模态 -->
</c:if>

<!-- 列出提成明细 -->
<c:if test="${not empty singleDetail}">
  <div class="table-responsive">
    <table class="table table-hover table-striped table-bordered">
      <thead>
      <tr>
        <th>代理商ID</th>
        <th>充值金额</th>
        <th>直属上线ID</th>
        <th>直属上线提成</th>
        <th>非直属上线ID</th>
        <th>非直属上线提成</th>
        <th>时间</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${singleDetail}" var="row">
        <tr>
          <td>${row.getColumnValue("fromPromoterId")}</td>
          <td>${row.getColumnValue("pay")}</td>
          <c:if test="${row.getColumnValue('parentId') != 0}">
            <td>${row.getColumnValue("parentId")}</td>
            <td>${row.getColumnValue("parentBonus")}</td>
          </c:if>
          <c:if test="${row.getColumnValue('parentId') == 0}">
            <td>无</td>
            <td> - </td>
          </c:if>
          <c:if test="${row.getColumnValue('nonParentId') != 0}">
            <td>${row.getColumnValue("nonParentId")}</td>
            <td>${row.getColumnValue("nonParentBonus")}</td>
          </c:if>
          <c:if test="${row.getColumnValue('nonParentId') == 0}">
            <td>无</td>
            <td> - </td>
          </c:if>
          <td>${row.getColumnValue("payCreateTime")}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</c:if>

<!-- END 提成审核信息-->


<!-- 提示信息 -->
<c:if test="${empty dailyTable and empty singleDetail}">
  <div class="row">
    <div class="form-group col-md-12 col-xs-12 mt-lg">
      <div class="heading heading-border heading-middle-border heading-middle-border-center">
        <h4>暂无数据</h4>
      </div>
    </div>
  </div>
</c:if>

<script>
  var title = $("#modal_title");
  var content = $("#modal_check_tb_content");
  var footer = $("#modal_footer");

  function digest(bonusId,payCreateDate) {
    content.empty();
    footer.empty();

    title.text('提成明细');
    content.load("check_bill/ajax/digestTable", { bonusId: bonusId, payCreateDate: payCreateDate });
    footer.append("<button type='button' data-dismiss='modal' class='btn blue'> 确&nbsp;&nbsp;认 </button>");

    $("#modal_check").modal('show');
  };

  function checkOneDayBonus(bonusId,directBonus,nonDirectBonus,payCreateDate) {
    content.empty();
    footer.empty();

    title.text('提成确认');
    content.append("<h4 class='center font-blue bold'>"+payCreateDate+": 直属提成共计"+directBonus+"元</h4>");
    content.append("<h4 class='center font-blue bold'>"+payCreateDate+": 非直属提成共计"+nonDirectBonus+"元</h4>");
    content.append("<br/><br/>");
    content.append("<h4 class='center font-red'>请确认以上信息是否正确</h4>");
    content.append("<br/>");
    footer.append("<button type='button' data-dismiss='modal' class='btn blue' onclick=\"checkedOneDayBonus("+bonusId+",\'"+payCreateDate+"\')\"> 确&nbsp;&nbsp;认 </button>");
    footer.append("<button type='button' data-dismiss='modal' class='btn default'> 取&nbsp;&nbsp;消 </button>");

    $("#modal_check").modal('show');
  };

  function checkedOneDayBonus(bonusId,payCreateDate) {
    $.post("check_bill/ajax/checkOneDayBonus", { bonusId: bonusId, payCreateDate: payCreateDate },
            function() { reload_content("check_bill/bonusCheck"); }
    );

  };
</script>
