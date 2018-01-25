<!-- 封停帐号-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>

<div class="row">
  <div class="col-md-12">

    <!-- BEGIN 页面内容-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="col-md-4">
          <a id="Excel" class='btn green btn-outline' href="after_sale_manage/LockPromoterExcel">导出excel</a>
        </div>
        <div class="col-md-4">
          <h4 class="center bold">封停帐号</h4>
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
                <input type="text" class="form-control center" id="promoterId" placeholder="请输入需要封停/解封的代理商ID"
                       onkeyup='this.value=this.value.replace(/\D/gi,"")' maxlength="5" />
              </div>
              <input type="button" class="btn btn-primary" id="search" value="查&nbsp;&nbsp;询">&nbsp;&nbsp;
              <input type="button" class="btn btn-success" onclick="listLockPromoter();" id="list" value="停封列表">
            </div>

          </div>
        </div>

        <!-- 查询结果 -->
        <div class="row">
          <div id="promoterInfo" class="col-md-12 mt-md"></div>
        </div>


      </div>
    </div>
    <!-- END 页面内容-->

  </div>
</div>


<script>
  $("#Excel").hide();

  $("#search").click(function() {
    $("#promoterInfo").load("after_sale_manage/ajax/queryLockPromoter"
            , { promoterId: $("#promoterId").val() }
    );
  });

  function listLockPromoter() {
    $("#Excel").show();
    $("#promoterInfo").load("after_sale_manage/ajax/listLockPromoter");
  }

</script>
