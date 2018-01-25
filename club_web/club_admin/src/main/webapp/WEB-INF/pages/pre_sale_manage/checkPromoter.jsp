<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp"%>
<%@include file="/common/include/datetimepicker.jsp"%>

<div class="row">
  <div class="col-md-12">

    <!-- BEGIN 页面内容-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="col-md-4">
          <a id="Excel" class='btn green btn-outline'>导出excel</a>
          <input id="chooseTab" type="text" hidden="hidden"/>
        </div>
        <div class="col-md-4">
          <h4 class="center bold">代理审核</h4>
        </div>
        <div class="col-md-4"></div>
      </div>
      <div class="portlet-body">

        <!-- 查询 -->
        <div class="row">
          <div class="col-md-8 col-md-offset-3">

            <div class="form-group ">
              <%--<label class="col-md-3 control-label" for="promoterId">代理商ID</label>--%>
              <div class="col-md-4">
                <input type="text" class="form-control center" id="promoterId" placeholder="请输入代理商ID">
              </div>
              <input type="button" class="btn btn-primary" id="search" value="查&nbsp;&nbsp;询">&nbsp;&nbsp;
              <input type="button" class="btn btn-success" onclick="listCheckPromoter();" value="待转正列表">&nbsp;&nbsp;
              <input type="button" class="btn btn-secondary" onclick="listFail();" value="转正失败列表">
            </div>

          </div>
        </div>

        <!-- 查询结果 -->
        <div class="row">
          <input id="_startDate" value="${startDate}"  type="text" hidden="hidden">
          <input id="_endDate" value="${endDate}"  type="text" hidden="hidden">
          <div id="checkPromoterInfo" class="col-md-12 mt-md"></div>
        </div>

      </div>
    </div>
    <!-- END 页面内容-->

  </div>
</div>


<script>

  $("#Excel").hide();

  $("#search").click(function() {
    $("#checkPromoterInfo").load("pre_sale_manage/ajax/queryCheckPromoter"
            , { promoterId: $("#promoterId").val() }
    );
  });

  function listCheckPromoter() {
    $("#chooseTab").val("1");
    $("#Excel").show();
    $("#checkPromoterInfo").load("pre_sale_manage/ajax/listCheckPromoter");
  }

  function listFail() {
    $("#chooseTab").val("2");
    $("#Excel").show();
    $("#checkPromoterInfo").load("pre_sale_manage/ajax/listFail");
  }

  //导出
  $("#Excel").click(function () {
    var startDate = $.trim($('#_startDate').val());
    var endDate = $.trim($('#_endDate').val());
    var tabId = $("#chooseTab").val();
    window.location.href = "pre_sale_manage/checkPromoterDownload?tabId=" + tabId+'&startDate=' + startDate + '&endDate=' + endDate;
  });

</script>
