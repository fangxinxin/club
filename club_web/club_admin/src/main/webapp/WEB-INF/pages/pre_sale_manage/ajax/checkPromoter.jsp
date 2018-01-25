<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp"%>


<!-- BEGIN 推广员信息-->
<c:if test="${not empty promoter and not empty club}">
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
      <td> ${promoter.gameCard}颗 </td>
      <td> 俱乐部人数： </td>
      <td>
           ${club.peopleNum}人&nbsp;
           <button class="btn btn-default btn-sm" onclick="memberDetail(${club.id})">成员明细</button>
      </td>
    </tr>
    <tr>
      <td> 俱乐部新成员人数： </td>
      <td> ${newMemberNum}人 </td>
      <td> 成员参与总局数： </td>
      <td> ${totalPlayGameNum}局 </td>
    </tr>
    <tr>
      <td> 创建日期： </td>
      <td> <fmt:formatDate value="${promoter.createTime}" type="both"/> </td>
      <td> 新成员参与局数： </td>
      <td> ${totalNewPlayGameNum}局 </td>
    </tr>
    <tr>
      <%--<td> 最后登录： </td>--%>
      <%--<td>${loginTime}</td>--%>
        <td> 截至考核时间： </td>
        <td> <fmt:formatDate value="${club.expireTime}" type="both"/> </td>
      <td> 俱乐部开通日期： </td>
      <td> <fmt:formatDate value="${club.createTime}" type="both"/> </td>
    </tr>
    </tbody>
  </table>

  <!-- 延长考核时间 -->
  <div class="col-md-10 col-md-offset-1 center mt-sm">
    <div class="form-group">
      <div class="col-md-12 mt-md">
        <label class="control-label"> 延长期限： </label>
        <input id="addTime" type="number" class="form-control input-inline input-medium center" min="1" value="24"
               onkeyup='this.value=this.value.replace(/\D/gi,"")' />
        <span class="help-inline"> 小时</span>
        <input type="button" class="btn btn-success ml-sm" value="延长期限"
               onclick="addCheckPromoterTime('${promoter.id}', '${promoter.nickName}', $('#addTime').val());">
        <h5 class="mt-md font-red bold">注：代理商审核条件: 1.审核时间${expireDay}天; 2.新玩家参与对局人数：${newPeopleNum}人;3.俱乐部人数：${peopleNum}人; 4.新玩家参与代开房局数${pyjRoomNum}、非代开房局数${nonPyjRoomNum};</h5>
      </div>
    </div>
  </div>
</c:if>

<!-- 列出当前游戏所有未转正 -->
<c:if test="${flag == 1}">

  <hr/>

  <div class="table-responsive">

    <div class="col-md-12">
      <div class="form-group">
        <div class="col-md-2 col-md-offset-6">
          <div class="input-group date form_date">
            <input id="startDate" type="text" size="16" class="form-control"
                   placeholder="开始时间" name="startDate" value="${startDate}">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button">
                                              <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
          </div>
        </div>

        <div class="col-md-2">
          <div class="input-group date form_date" id="end">
            <input id="endDate" type="text" size="16" class="form-control"
                   placeholder="结束时间" name="endDate" value="${endDate}">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button">
                                              <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
          </div>

        </div>

        <input  type="button" value="查&nbsp;&nbsp;询" class="btn btn-primary" onclick="queryConversionListByDate()">
      </div>

    </div>

    <c:if test="${not empty checkPromoterList}">
      <table class="table table-hover table-striped table-bordered">
        <thead>
        <tr>
          <th>玩家ID</th>
          <th>玩家昵称</th>
          <th>俱乐部ID</th>
          <th>代理ID</th>
            <%--<th>俱乐部名称</th>--%>
          <th>人数</th>
          <th>新成员</th>
          <th>参与局数</th>
          <th>新成员局数</th>
          <th>创建时间</th>
          <th>转正截止时间</th>
          <th>转正剩余时间</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${checkPromoterList}" var="row">
          <c:set value="${row.getColumnValue('id')}" var="id"/>
          <c:set value="${row.getColumnValue('promoterId')}" var="promoterId"/>
          <tr>
            <td>${row.getColumnValue("gameUserId")}</td>
            <td>${row.getColumnValue("gameNickName")}</td>
            <td>${id}</td>
            <td>${promoterId}</td>
              <%--<td>${row.getColumnValue("clubName")}</td>--%>
            <td>${row.getColumnValue("peopleNum")}</td>
            <td>${row.getColumnValue("newMemberNum")}</td>
            <td>${row.getColumnValue("totalPlayGameNum")}</td>
            <td>${row.getColumnValue("totalNewPlayGameNum")}</td>
            <td>${row.getColumnValue("createTime")}</td>
            <td>${row.getColumnValue("expireTime")}</td>
            <td>${row.getColumnValue("restTime")}</td>
            <td>
              <a href="javascript:;" class="btn grey btn-xs" onclick="refuseCheckClub_confirm('${id}', '${promoterId}')">取消审核</a>
              <a href="javascript:;" class="btn blue btn-xs" onclick="becomeFormal_confirm('${id}', ${promoterId})">立即转正</a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:if>
  </div>
</c:if>

<!-- 列出当前游戏:: 转正失败列表 -->
<c:if test="${not empty FailList}">
  <div class="table-responsive">
    <table class="table table-hover table-striped table-bordered">
      <thead>
      <tr>
        <th>玩家ID</th>
        <th>玩家昵称</th>
        <th>俱乐部ID</th>
        <th>人数</th>
        <th>新成员</th>
        <th>参与局数</th>
        <th>新成员局数</th>
        <th>创建时间</th>
        <th>转正截止时间</th>
        <th>申请状态</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${FailList}" var="row">
        <c:set value="${row.getColumnValue('clubId')}" var="clubId"/>
        <c:set value="${row.getColumnValue('clubType')}" var="clubType"/>
        <tr>
          <td>${row.getColumnValue("gameUserId")}</td>
          <td>${row.getColumnValue("gameNickName")}</td>
          <td>${clubId}</td>
          <td>${row.getColumnValue("peopleNum")}</td>
          <td>${row.getColumnValue("peopleNumNew")}</td>
          <td>${row.getColumnValue("pyjNum")}</td>
          <td>${row.getColumnValue("pyjNumNew")}</td>
          <td>${row.getColumnValue("createTime")}</td>
          <td>${row.getColumnValue("expireTime")}</td>
          <td>
            <c:if test="${clubType == 2}">转正失败</c:if>
            <c:if test="${clubType == 3}">被拒绝</c:if>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</c:if>

<!-- 提示信息 -->
<c:if test="${empty promoter and empty club and empty checkPromoterList and empty FailList and empty flag}">
  <div class="row">
    <div class="col-md-12 mt-lg">
      <div class="heading heading-border heading-middle-border heading-middle-border-center">
        <h4>暂无数据</h4>
      </div>
    </div>
  </div>
</c:if>
<!-- END 推广员信息-->


<script>
  var url = "pre_sale_manager/ajax/listCheckPromoter"

  $(".form_date").datetimepicker({
    language: "zh-CN",
    autoclose: true,
    isRTL: App.isRTL(),
    minView: "month",
    todayBtn: true,
    format: "yyyy-MM-dd",
//    endDate: new Date(),
    pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left")
  });

  //按时间查询待转正列表
  function queryConversionListByDate(){
    var startDate = $.trim($('#startDate').val());
    var endDate = $.trim($('#endDate').val());
    if(endDate<startDate){
      layer.alert('开始日期不能大于结束日期，请重新选择！');
      return;
    }
    $("#_startDate").val(startDate);
    $("#_endDate").val(endDate);
    $("#chooseTab").val("1");
    $("#Excel").show();
    $("#checkPromoterInfo").load('pre_sale_manage/ajax/listCheckPromoter?&startDate=' + startDate + '&endDate=' + endDate);

  }

  /**
   * 立即转正
   * */
  function becomeFormal_confirm(clubId,promoterId) {
    var title ="立即转正确认";
    var url = "pre_sale_manage/ajax/becomeFormalView?clubId="+clubId+"&promoterId="+promoterId;

    layer_url(title,url);
  }

  function becomeFormal(promoterId) {
    $.post("pre_sale_manage/ajax/becomeFormal", { promoterId: promoterId }, function(data) {
      var content;
      if(data) {
        content = '<h4 class="mt-xlg font-red center">操作成功！</h4>'
        layerMsg("提示信息", content);
        reload_content(url)
      } else {
        content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
        layerMsg("提示信息", content);
        reload_content(url);
      }

      layer.close(layer.index-1);
    });
  }


  //为账户延长期限
  function addCheckPromoterTime(promoterId, nickName, addTime) {
    $.post("pre_sale_manage/ajax/addCheckPromoterTime",
            {
              promoterId: promoterId,
              addTime: addTime
            },
            function(data) {
              var content;
              if(data > 0) {
                content = '<h4 class="mt-xlg font-red center">成功为'+nickName+'延长'+addTime+'小时期限！</h4>';
                layerMsg_area_r('提示信息', content, '500px', '300px', 1200);
              } else {
                content =  '<h4 class="mt-xlg font-red center">延长期限操作失败！</h4>';
                layerMsg_area_r('提示信息', content, '500px', '300px', 1200);
              }
            });
  };

  /**
  *取消审核
  * @param clubId
   */
  function refuseCheckClub_confirm(clubId,promoterId) {
    var title ="取消审核确认";
    var url = "pre_sale_manage/ajax/refuseCheckPromoter?clubId="+clubId+"&promoterId="+promoterId;

    layer_url(title,url);
  }

  function refuseCheckClub(clubId) {
    $.post("pre_sale_manage/ajax/refuseCheckClub", { clubId: clubId }, function(data) {
      var content;
      if(data) {
        content = '<h4 class="mt-xlg font-red center">操作成功！</h4>'
        layerMsg("提示信息", content);
        reload_content(url)
      } else {
        content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
        layerMsg("提示信息", content);
        reload_content(url);
      }

      layer.close(layer.index-1);
    });
  }

  function close_layer() {
    layer.close(layer.index);
  }


  /**
   * 新成员详情
   * @param clubId
   */
  function memberDetail(clubId) {
    var title ="成员明细";
    var url = "pre_sale_manage/ajax/memberDetail?clubId="+clubId;

    layer_url(title,url);
  }
</script>
