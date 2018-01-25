<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp" %>
<style>
    .table td, .table th {
        text-align: center;
    }
</style>

<!-- 组管理 -->
<c:if test="${isGroup}">
    <c:if test="${not empty groups}">
        <table class="table table-bordered table-striped table-condensed flip-content" id="sample_editable_1">
            <thead class="flip-content">
            <tr>
                <th>ID</th>
                <th>用户组名</th>
                <th>显示顺序</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="group" items="${groups}">
                <c:set var="id" value="${group.getColumnValue('id')}"/>
                <c:set var="name" value="${group.getColumnValue('name')}"/>
                <c:set var="priority" value="${group.getColumnValue('priority')}"/>
                <tr>

                    <td>${id}</td>
                    <td>
                        <span id="name${id}">${name}</span>
                    </td>
                    <td>
                        <span id="priority${id}">${priority}</span>
                    </td>
                    <td class="">
                        <div id="op${id}">
                            <a href="javascript:;" onclick="editGroup('${id}', '${name}', '${priority}')">
                                <i class="fa fa-edit">编辑</i>
                            </a>|
                            <a href="javascript:;" onclick="userGroup('${id}')">
                                <i class="fa fa-users">成员管理</i>
                            </a>|
                            <a href="javascript:;" onclick="roleGroup('${id}')">
                                <i class="fa fa-lock">角色管理</i>
                            </a>
                                <%--<a href="javascript:;" onclick="removeGroup('${id}')">--%>
                                <%--<i class="fa fa-edit">移除</i>--%>
                                <%--</a>--%>
                        </div>
                    </td>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</c:if>

<!-- 管理员信息 -->
<c:if test="${isUserGroup}">

    <!-- 添加 -->
    <form role="form" action="" method="post" id="user_form">

        <div class="form-group">
            <div class="col-md-4">
                <input type="text" class="form-control" id="userName" name="userName" placeholder="用户名"/>
            </div>
            <input type="submit" class="btn btn-primary" value="添加">
            <a href=""></a>
        </div>

    </form>

    <c:if test="${not empty adminInfo}">
        <table class="table table-hover table-striped table-bordered mt-md">
            <thead>
            <tr>
                <th>管理员ID</th>
                <th>用户名</th>
                <th>真实姓名</th>
                <th>电话号码</th>
                <th>邮件</th>
                <th>创建时间</th>
                <th>remark</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="row" items="${adminInfo}">
                <c:set var="userId" value="${row.getColumnValue('id')}"/>
                <c:set var="userName" value="${row.getColumnValue('userName')}"/>
                <c:set var="realName" value="${row.getColumnValue('realName')}"/>
                <c:set var="cellphone" value="${row.getColumnValue('cellphone')}"/>
                <c:set var="email" value="${row.getColumnValue('email')}"/>
                <c:set var="createTime" value="${row.getColumnValue('createTime')}"/>
                <c:set var="remark" value="${row.getColumnValue('remark')}"/>
                <tr>
                    <td>${userId}</td>
                    <td>${userName}</td>
                    <td>${realName}</td>
                    <td>${cellphone}</td>
                    <td>${email}</td>
                    <td>${createTime}</td>
                    <td>${remark}</td>
                    <td>
                        <input type="button" class="btn blue btn-xs" value="分配权限" onclick="setPower('${userId}');">|
                        <a href="javascript:;" onclick="removeUser('${userId}')">
                            <i class="fa fa-edit">移除</i>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

</c:if>

<!-- 角色信息 -->
<c:if test="${isRoleGroup}">

    <!-- 添加 -->
    <form role="form" action="" method="post" id="role_form">

        <div class="form-group">
            <div class="col-md-3">
                <input type="text" class="form-control" id="role" name="role" placeholder="角色字符串"/>
            </div>
            <div class="col-md-3">
                <input type="text" class="form-control" id="name" name="name" placeholder="角色名"/>
            </div>
            <div class="col-md-3">
                <input type="text" class="form-control" id="priority" name="priority" placeholder="排列序号"/>
            </div>
            <input type="submit" class="btn btn-primary" id="add" value="添加">
        </div>

    </form>

    <c:if test="${not empty roleGroup}">
        <table class="table table-hover table-striped table-bordered">
            <thead>
            <tr>
                <th>角色ID</th>
                <th>角色字符串</th>
                <th>角色名</th>
                <th>分组ID</th>
                <th>显示顺序</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>

            <c:forEach var="row" items="${roleGroup}">
                <c:set var="id" value="${row.getColumnValue('id')}"/>
                <c:set var="role" value="${row.getColumnValue('role')}"/>
                <c:set var="name" value="${row.getColumnValue('name')}"/>
                <c:set var="groupId" value="${row.getColumnValue('groupId')}"/>
                <c:set var="priority" value="${row.getColumnValue('priority')}"/>
                <tr>
                    <td>${id}</td>
                    <td>
                        <span id="role${id}">${role}</span>
                    </td>
                    <td>
                        <span id="name${id}">${name}</span>
                    </td>
                    <td>${groupId}</td>
                    <td>
                        <span id="priority${id}">${priority}</span>
                    </td>
                    <td>
                        <div id="op${id}">
                            <a href="javascript:;" onclick="editRole('${id}', '${role}', '${name}', '${priority}')">
                                <i class="fa fa-edit">编辑</i>
                            </a>|
                            <a href="javascript:;" onclick="powerGroup('${id}')">
                                <i class="fa fa-lock">权限项管理</i>
                            </a>
                        </div>
                    </td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </c:if>

</c:if>

<!-- back -->
<c:if test="${empty groups}">

    <div class="row center">
        <a href="javascript:;" class="btn btn-primary" onclick="getContent('permission/index')">返回</a>
    </div>

</c:if>

<!-- 用户组 -->
<script charset="utf-8">

    /** 用户组管理 **/
    function userGroup(id) {
        $("#portlet_content").load("permission/userGroup", {groupId: id});
    }

    function editGroup(id, name, priority) {
        var _name = $("#name" + id);
        var _priority = $("#priority" + id);
        var _op = $("#op" + id);
        _name.empty();
        _priority.empty();
        _op.empty();

        _name.append(
                "<div class='col-md-8 col-md-offset-2'>" +
                "<input type='text' id=\'ed_name" + id + "\' class='form-control text-center input-sm' value=\'" + name + "\'/>" +
                "</div>"
        );
        _priority.append(
                "<div class='col-md-8 col-md-offset-2'>" +
                "<input type='text' id=\'ed_priority" + id + "\' class='form-control text-center input-sm' value=\'" + priority + "\'/>" +
                "</div>"
        );
        _op.append("<input type='button' class='btn btn-sm green' value='确定' onclick='editGroupSave(" + id + ");'/>");

    }

    function editGroupSave(id) {
        var name = $("#ed_name" + id).val();
        var priority = $("#ed_priority" + id).val();
        var url = 'permission/index';

        $.post("permission/editGroup", {id: id, name: name, priority: priority}, function (data) {
            if (data > 0) {
                var content = '<h4 class="mt-xlg font-red center">操作成功</h4>'
                layerMsg("提示信息", content);
                reload_content(url)
            } else {
                var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
                layerMsg("提示信息", content);
                reload_content(url);
            }

        });
    }

    //    function removeGroup(id) {
    //        $.post("permission/removeGroup", { id: id }, function(data) {
    //            if(data > 0) {
    //                var content = '<h4 class="mt-xlg font-red center">操作成功！</h4>'
    //                layerMsg("提示信息", content);
    //                reload_content(url)
    //            } else {
    //                var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
    //                layerMsg("提示信息", content);
    //                reload_content(url);
    //            }
    //
    //        });
    //    }

</script>
<!-- 成员 -->
<script charset="utf-8">
    var url = '${url}';

    /** 成员管理 **/
    function removeUser(userId) {
        $.post("permission/removeUser", {userId: userId}, function (data) {

            if (data > 0) {
                var content = '<h4 class="mt-xlg font-red center">操作成功！</h4>'
                layerMsg("提示信息", content);
                load_portlet(url)
            } else {
                var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
                layerMsg("提示信息", content);
                load_portlet(url);
            }

        });
    }

    //添加管理员
    $("#user_form").validate({
        debug: true,
        submitHandler: function (form) {
            $.post("permission/addUser", {groupId: '${groupId}', userName: $("#userName").val()}, function (data) {

                if (data > 0) {
                    var content = '<h4 class="mt-xlg font-red center">操作成功！</h4>'
                    layerMsg("提示信息", content);
                    load_portlet(url)
                } else {
                    var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
                    layerMsg("提示信息", content);
                    load_portlet(url);
                }
            });
        },
        rules: {
            userName: "required"
        },
        messages: {
            userName: {
                required: "请填写用户名"
            }
        }
    });

    //分配权限
    function setPower(userId) {
        var title = "设置权限";
        var _url = 'permission/roleSelect?groupId=${groupId}&userId=' + userId;

        return layer_url_area(title, _url, '460px', '250px');
    }

    //保存权限
    function savePower(userId, roleId) {
        $.post("permission/savePower", {userId: userId, roleId: roleId}, function (data) {

            if (data > 0) {
                var content = '<h4 class="mt-xlg font-red center">操作成功！</h4>';
                layerMsg("提示信息", content);

                $.post("permission/savePower", {userId: userId, roleId: roleId});

                return load_portlet(url);
            } else {
                var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>';
                layerMsg("提示信息", content);
                load_portlet(url);
            }
        });
    }


</script>
<!-- 角色 -->
<script charset="utf-8">
    var url = '${url}';

    /** 角色管理 **/
    function roleGroup(id) {
        $("#portlet_content").load("permission/roleGroup", {groupId: id});
    }

    //添加角色
    $("#role_form").validate({
        debug: true,
        submitHandler: function (form) {
            $.post("permission/addRole?groupId=${groupId}", $(form).serialize(), function (data) {

                if (data > 0) {
                    var content = '<h4 class="mt-xlg font-red center">操作成功！</h4>'
                    layerMsg("提示信息", content);
                    load_portlet(url)
                } else {
                    var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
                    layerMsg("提示信息", content);
                    load_portlet(url);
                }
            });
        },
        rules: {
            role: "required",
            name: "required",
            priority: {
                required: true,
                digits: true
            }
        },
        messages: {
            role: {
                required: "请填写角色字符串"
            },
            name: {
                required: "请填写角色名"
            },
            priority: {
                required: "填写显示顺序",
                digits: "请填写数字"
            }
        }
    });

    //编辑角色
    function editRole(id, role, name, priority) {
        var _role = $("#role" + id);
        var _name = $("#name" + id);
        var _priority = $("#priority" + id);
        var _op = $("#op" + id);
        _role.empty();
        _name.empty();
        _priority.empty();
        _op.empty();

        _role.append(
                "<div class='col-md-8 col-md-offset-2'>" +
                "<input type='text' id=\'ed_role" + id + "\' class='form-control text-center input-sm' value=\'" + role + "\'/>" +
                "</div>"
        );
        _name.append(
                "<div class='col-md-8 col-md-offset-2'>" +
                "<input type='text' id=\'ed_name" + id + "\' class='form-control text-center input-sm' value=\'" + name + "\'/>" +
                "</div>"
        );
        _priority.append(
                "<div class='col-md-8 col-md-offset-2'>" +
                "<input type='text' id=\'ed_priority" + id + "\' class='form-control text-center input-sm' value=\'" + priority + "\'/>" +
                "</div>"
        );
        _op.append("<input type='button' class='btn btn-sm green' value='确定' onclick='editRoleSave(" + id + ");'/>");
        _op.append("<input type='button' class='btn btn-sm red ml-xs' value='删除' onclick='deleteRole(" + id + ");'/>");

    }

    //保存编辑角色
    function editRoleSave(id) {
        var role = $("#ed_role" + id).val();
        var name = $("#ed_name" + id).val();
        var priority = $("#ed_priority" + id).val();

        $.post("permission/editRole", {id: id, role: role, name: name, priority: priority}, function (data) {
            if (data > 0) {
                var content = '<h4 class="mt-xlg font-red center">操作成功！</h4>'
                layerMsg("提示信息", content);
                load_portlet(url)
            } else {
                var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
                layerMsg("提示信息", content);
                load_portlet(url);
            }

        });
    }

    //删除角色
    function deleteRole(id) {

        $.post("permission/deleteRole", {id: id}, function (data) {
            if (data > 0) {
                var content = '<h4 class="mt-xlg font-red center">操作成功！</h4>'
                layerMsg("提示信息", content);
                load_portlet(url)
            } else {
                var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
                layerMsg("提示信息", content);
                load_portlet(url);
            }

        });
    }

</script>
<!-- 权限 -->
<script charset="utf-8">

    function powerGroup(id) {
        $("#portlet_content").load("permission/powerGroup", {roleId: id});
    }

</script>
<script charset="utf-8">

    function load_portlet(url) {
        $("#portlet_content").load(url);
    }
    ;

</script>