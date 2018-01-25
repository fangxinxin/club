<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp"%>

<!-- BEGIN 代理商信息-->
<c:if test="${not empty promoter}">
  <form id="lockForm" method="post" action="">
    <table class="table table-hover">
      <tbody>
      <tr>
        <td> 玩家昵称： </td>
        <td> ${promoter.nickName} </td>
        <td> 俱乐部昵称： </td>
        <td> ${club.clubName} </td>
      </tr>
      <tr>
        <td> 玩家ID： </td>
        <td> ${promoter.gameUserId} </td>
        <td> 俱乐部ID： </td>
        <td> ${club.id} </td>
      </tr>
      <tr>
        <td> 当前钻石数： </td>
        <td> ${promoter.gameCard} 颗 </td>
        <td> 俱乐部人数： </td>
        <td> ${club.peopleNum}人 </td>
      </tr>
      <tr>
        <td> 创建日期： </td>
        <td> <fmt:formatDate value="${promoter.createTime}" type="both"/> </td>
        <td> 上级代理商昵称： </td>
        <td>
          <c:if test="${not empty parentNickName}">
            ${parentNickName} &nbsp; (<b>${ppLevel}</b>)
          </c:if>
          <c:if test="${empty parentNickName}">
            无
          </c:if>
        </td>
      </tr>
      <tr>
        <%--<td> 最后登录： </td>--%>
        <%--<td>${loginTime}</td>--%>
        <td> 上级代理商ID： </td>
        <td>
          <c:if test="${not empty parentNickName}">
            ${promoter.parentId} <input id="promoterId" class="input-xs" type="text" hidden="hidden" value="${promoter.parentId}">
          </c:if>
        </td>
      </tr>
      <c:if test="${not empty promoterLock}">
        <tr>
          <td> 封停结束时间： </td>
          <td> <fmt:formatDate value="${promoterLock.expireTime}" type="both"/> </td>

          <td> 封停开始时间： </td>
          <td> <fmt:formatDate value="${promoterLock.createTime}" type="both"/> </td>

        </tr>
      </c:if>
      <tr>
        <td> 封停账号事由： </td>
        <td>
          <c:if test='${empty promoterLock}'>
            <input id="lockReason" name="lockReason" type="text" maxlength="15" class="form-control input-sm" style="width: 80%">
          </c:if>
          <c:if test='${not empty promoterLock}'>
            <input id="lockReason_unlock" value="${promoterLock.remark}" class="input-xs" type="text" hidden="hidden">
            ${promoterLock.remark}
          </c:if>
        </td>
        <td> 封停时间：（天） </td>
        <td>
          <c:if test='${empty promoterLock}'>
            <input id="lockDay" name="lockDay" type="number" class="form-control input-sm onlyNum" min="1"
                   max="999" maxlength="3" style="width: 80px" onkeyup='this.value=this.value.replace(/\D/gi,"")' />
          </c:if>
          <c:if test='${not empty promoterLock}'>
            <input id="lockDay_unlock" value="${promoterLock.lockDay}" class="input-xs" type="text" hidden="hidden">
            ${promoterLock.lockDay}&nbsp;天
          </c:if>
        </td>
      </tr>
      </tbody>
    </table>

    <c:if test="${not empty promoterLock}">
      <div class="col-md-10 col-md-offset-1 center">
        <a id="unlock" class="btn red sbold" data-toggle="modal" href="#unlockInfo"> 解封账号 </a>
      </div>
    </c:if>
    <c:if test="${empty promoterLock}">
      <div class="col-md-10 col-md-offset-1 center">
        <input id="form_check" type="submit" class="btn red sbold" value="封停账号">
      </div>
    </c:if>
  </form>
</c:if>

<!-- 列出当前游戏所有 -->
<c:if test="${not empty lockPromoterList}">
  <div class="table-responsive">
    <table class="table table-hover table-striped table-bordered">
      <thead>
      <tr>
        <th>代理商ID</th>
        <th>代理商昵称</th>
        <th>代理商级别</th>
        <th>俱乐部成员</th>
        <th>封停时间</th>
        <th>操作时间</th>
        <th>操作人员</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${lockPromoterList}" var="row">
        <tr>
          <td>${row.getColumnValue("promoterId")}</td>
          <td>${row.getColumnValue("nickName")}</td>
          <td>
            <c:if test="${row.getColumnValue('pLevel') == -1}">特级代理商</c:if>
            <c:if test="${row.getColumnValue('pLevel') == 1}">一级代理商</c:if>
            <c:if test="${row.getColumnValue('pLevel') == 2}">二级代理商</c:if>
          </td>
          <td>${row.getColumnValue("peopleNum")}</td>
          <td>${row.getColumnValue("lockDay")}</td>
          <td>${row.getColumnValue("createTime")}</td>
          <td>${row.getColumnValue("editAdmin")}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</c:if>

<!-- 提示信息 -->
<c:if test="${empty promoter and empty lockPromoterList}">
  <div class="row">
    <div class="form-group col-md-12 col-xs-12 mt-lg">
      <div class="heading heading-border heading-middle-border heading-middle-border-center">
        <h4>查询信息不存在!</h4>
      </div>
    </div>
  </div>
</c:if>
<!-- END 代理商信息-->

<!-- Start 模态 -->
<!-- 封停 -->
<div id="lockInfo" class="modal fade" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h4 id="modal_title_lock" class="modal-title center">账号封停确认</h4>
      </div>
      <div class="modal-body">
        <div id="modal_content_lock" class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
          <div class="row" id="modal_row_lock">
            <div class="col-md-6">
              <h5>封停代理商：<span class="text-capitalize">${promoter.nickName}</span></h5>
              <h5>封停账号事由：<span id="r_lockReason_lock"></span></h5>
              <h5>
                封停开始时间：<span id="startTime"></span>
                <input id="startTime_lock" class="input-xs" type="text" hidden="hidden">
              </h5>
            </div>
            <div class="col-md-6">
              <h5>封停俱乐部ID：${club.id}</h5>
              <h5>封停时间：<span id="r_lockDay_lock"></span>天</h5>
              <h5>
                封停结束时间：<span id="endTime"></span>
                <input id="endTime_lock" class="input-xs" type="text" hidden="hidden">
              </h5>
            </div>
            <div class="col-md-12 center mt-lg">
              <b class="font-red font-lg">将在< ${gameName} >封停< ${promoter.nickName} >的代理商身份，请确认信息是否正确!</b>
              <%--<h5 class="bold mt-md"><small>备注：如封停代理商账号，请在后续操作清空代理商的俱乐部成员</small></h5>--%>
            </div>
          </div>
        </div>
      </div>
      <div id="modal_footer_lock" class="modal-footer center">
        <button id="addLockPromoter" type="button" class="btn green"> 确&nbsp;&nbsp;认 </button>
        <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>
      </div>
    </div>
  </div>
</div>

<!-- 解封 -->
<div id="unlockInfo" class="modal fade" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h4 id="modal_title_unlock" class="modal-title center">解除封停确认</h4>
      </div>
      <div class="modal-body">
        <div id="modal_content_unlock" class="scroller mt-lg" style="height:300px" data-always-visible="1" data-rail-visible1="1">
          <div class="row" id="modal_row_unlock">
            <div class="col-md-6">
              <h5>代理商昵称：<span class="text-capitalize">${promoter.nickName}</span></h5>
              <h5>代理商ID：${promoter.id}</h5>
              <h5>代理商级别：${pLevel}</h5>
              <h5>后台剩余钻石数：${promoter.gameCard} 颗</h5>
              <h5>累计充值金额：${promoter.totalPay} 元</h5>
              <h5>封停账号事由：<span id="r_lockReason_unlock"></span></h5>
            </div>
            <div class="col-md-6">
              <h5>拥有俱乐部：<span class="text-capitalize">${club.clubName}</span></h5>
              <h5>俱乐部ID：${club.id}</h5>
              <h5>拥有俱乐部人数：${club.peopleNum}</h5>
              <h5>
                上级代理商：
                <c:if test="${not empty parentNickName}">
                  <span class="text-capitalize">${parentNickName}</span>
                </c:if>
              </h5>
              <h5>
                上级代理商ID：
                <c:if test="${promoter.parentId != 0}">
                  ${promoter.parentId}
                </c:if>
              </h5>
              <h5>封停时间：<span id="r_lockDay_unlock"></span>天</h5>
            </div>
            <div class="col-md-12 center mt-lg">
              <b class="font-red font-lg">将在< ${gameName} >解封< ${promoter.nickName} >的代理商身份，请确认信息是否正确!</b>
            </div>
          </div>
        </div>
      </div>
      <div id="modal_footer_unlock" class="modal-footer center">
        <button id="unlockPromoter" type="button" class="btn green"> 确&nbsp;&nbsp;认 </button>
        <button type="button" data-dismiss="modal" class="btn dark btn-outline"> 取&nbsp;&nbsp;消 </button>
      </div>
    </div>
  </div>
</div>
<!-- End 模态 -->


<script>
  //表单验证
  $(function() {
    $("#lockForm").validate({
      dubug : false,
      submitHandler:function(form){
        $("#r_lockReason_lock").text($("#lockReason").val());
        $("#r_lockDay_lock").text($("#lockDay").val());

        $.post("after_sale_manage/ajax/checkLockPromoter", {lockDay: $('#lockDay').val()}, function(data) {
          if(data != null) {
            var json = JSON.parse(data);
            $("#startTime").text(json.startTime);
            $("#startTime_lock").val(json.startTime);
            $("#endTime").text(json.endTime);
            $("#endTime_lock").val(json.endTime);
          }
        });

        //打开模态
        $("#lockInfo").modal();
      },
      rules:{
        lockDay : {
          required : true,
          digits:true,
          min:1,
          maxlength: 3
        },
        lockReason : {
          required : true,
          maxlength:15
        }
      },
      messages : {
        lockDay:{
          required : "封停时间必填",
          digits : "输入值必须为整数",
          min : "输入值必须不小于1",
          maxlength: "输入值不可超过三位数"
        },
        lockReason:{
          required : "封停原因必填",
          maxlength: "输入封停原因不能超过15个字符"
        }
      }
    });
  });

  var url = "after_sale_manage/lockPromoter";

  //添加封停账户
  $("#addLockPromoter").click(function() {
    $.post("after_sale_manage/ajax/addLockPromoter"
            , {
              promoterId: $('#promoterId').val()
              , lockDay: $('#lockDay').val()
              , lockReason: $("#lockReason").val()
              , startTime: $("#startTime_lock").val()
              , endTime: $("#endTime_lock").val()
            }, function(data) {
              if(data > 0) {
                $("#addLockPromoter").attr('data-dismiss', 'modal');
                $("#modal_row_lock").remove();
                $("#modal_footer_lock").empty();

                var footer = $('<button>').attr('id', 'close_reload').attr('class', 'btn green').attr('type', 'button')
                        .attr('onclick', 'close_modal(\'#lockInfo\', \''+url+'\')').text('确认');
                $("#modal_title_lock").text("封停账号成功");
                $("#modal_content_lock").append(
                        "<h4 class='center bold'>已封停代理商：${promoter.nickName}</h4>"+
                        "<h4 class='center bold'>俱乐部ID：${club.id}</h4>"
                );
                $("#modal_footer_lock").append(footer);
              }
            });
  });

  //解封二次确认
  $("#unlock").click(function() {
    $("#r_lockReason_unlock").text($("#lockReason_unlock").val());
    $("#r_lockDay_unlock").text($("#lockDay_unlock").val());
  });

  //解除封停账户
  $("#unlockPromoter").click(function() {
    $.post("after_sale_manage/ajax/unlockPromoter"
            , {
              promoterId: $('#promoterId').val()
            }, function(data) {
              if(data) {
                $("#modal_row_unlock").remove();
                $("#modal_footer_unlock").empty();

                $("#modal_title_unlock").text("解封账号成功");
                var footer = $('<button>').attr('id', 'close_reload').attr('class', 'btn green').attr('type', 'button')
                        .attr('onclick', 'close_modal(\'#unlockInfo\', \''+url+'\')').text('确认');
                $("#modal_content_unlock").append(
                        "<h4 class='center bold'>已解除封停代理商：${promoter.nickName}</h4>"+
                        "<h4 class='center bold'>俱乐部ID：${club.id}</h4>"
                );
                $("#modal_footer_unlock").append(footer);
              }
            });
  });

  function close_modal(id, url) {
    $(id).modal('hide');

    setTimeout(function() {
      $("#content").load(url,function(responseTxt,statusTxt,xhr){
        if(statusTxt=="error"){
          location.reload();
        }
      });
    }, 1000)
  }

</script>
