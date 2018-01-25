<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/include/lib.jsp"%>
<link rel="stylesheet" href="http://download.stevengame.com/webResource/common/vendor/plugins/jstree/dist/themes/default/style.css">

<div class="row">
    <div class="col-md-10 ml-lg">
        <label>配置【${role.name}】的权限：</label>
        <button type="button" class="btn btn-sm btn-primary" onclick="cfg_power();">确 定</button>
        <a href="javascript:;" class="btn btn-sm btn-primary ml-sm" onclick="getContent('permission/index')">返回</a>
    </div>

    <input id="areaIds" type="text" hidden="hidden" value="">
    <input id="menuIds" type="text" hidden="hidden" value="">
    <input id="permissionIds" type="text" hidden="hidden" value="">
</div>

<div class="row mt-lg">

    <div class="col-md-4">
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <%--<i class="icon-bubble font-green-sharp"></i>--%>
                    <span class="caption-subject font-green-sharp bold uppercase center">游戏项</span>
                </div>
            </div>
            <div class="portlet-body">

                <div id="tree_1" class="tree-demo"> </div>
                <span id="event_id_1"></span>

                <div class="alert alert-info no-margin margin-top-10">
                    备注.
                </div>

            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <%--<i class="icon-bubble font-green-sharp"></i>--%>
                    <span class="caption-subject font-green-sharp bold uppercase center">菜单项</span>
                </div>
            </div>
            <div class="portlet-body">

                <div id="tree_2" class="tree-demo"> </div>
                <span id="event_id_2"></span>

                <div class="alert alert-info no-margin margin-top-10">
                    备注.
                </div>

            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <%--<i class="icon-bubble font-green-sharp"></i>--%>
                    <span class="caption-subject font-green-sharp bold uppercase center">权限操作项</span>
                </div>
            </div>
            <div class="portlet-body">

                <div id="tree_3" class="tree-demo"> </div>
                <span id="event_id_3"></span>

                <div class="alert alert-info no-margin margin-top-10">
                    备注.
                </div>

            </div>
        </div>
    </div>
</div>

<script charset="utf-8">

    var UITree = function () {

        var gameTree = function() {

            $.post("permission/getGameList", {roleId: '${role.id}'}, function(data) {
                $("#tree_1").jstree({
                    "core" : {
                        "themes" : {
                            "responsive": false
                        },
                        "check_callback" : true,
                        'data' : JSON.parse(data)
                    },
                    "types" : {

                        "default" : {
                            "icon" : "fa icon-state-warning icon-lg"
                        },//fa fa-dot-circle-o icon-state-warning icon-lg
                        "file" : {
                            "icon" : ""//fa fa-file icon-state-warning icon-lg   folder
                        }
                    },
                    "state" : { "key" : "demo3" },
                    "plugins" : [ "wholerow", "types", "checkbox" ]
                });


                $("#tree_1")
                        .on('changed.jstree', function (e, data) {
                            var i, j, r = [];
                            for(i = 0, j = data.selected.length; i < j; i++) {
                                r.push(data.instance.get_node(data.selected[i]).id);
                            }//get_path (obj [, glue, ids])
//                            $('#event_id_1').html('Selected: ' + r.join(', '));
                            $("#areaIds").attr('value', r.join(','));
                        })
                        /*.on("loaded.jstree", function (event, data) {
                            //这两句化是在loaded所有的树节点后，然后做的选中操作，这点是需要注意的，loaded.jstree 这个函数
                            //取消选中，然后选中某一个节点
                            $("#tree_1").jstree("deselect_all",true);
                            //$("#keyKamokuCd").val()是选中的节点id，然后后面的一个参数 true表示的是不触发默认select_node.change的事件
                            $('#tree_1').jstree('select_node',$("#keyKamokuCd").val(),true);
                        })*/


                .jstree();

            });

        }

        var menuTree = function() {

            $.post("permission/getMenuList", {roleId: '${role.id}'}, function(data) {
                $("#tree_2").jstree({
                    "core" : {
                        "themes" : {
                            "responsive": false
                        },
                        "check_callback" : true,
                        'data' : JSON.parse(data)
                    },
                    "types" : {

                        "default" : {
                            "icon" : "fa icon-state-warning icon-lg"
                        },
                        "file" : {
                            "icon" : ""
                        }
                    },
                    "state" : { "key" : "demo3" },
                    "plugins" : [ "wholerow", "types", "checkbox" ]
                });
                $("#tree_2")
                        .on('changed.jstree', function (e, data) {
                            var i, j, r = [];

                            for(i = 0, j = data.selected.length; i < j; i++) {
                                r.push(data.instance.get_node(data.selected[i]).id);
                            }
                            $("#menuIds").attr('value', r.join(','));
                        })
                        .jstree();
            });


        }

        var permissionTree = function() {

            $.post("permission/getPermissionList", {roleId: '${role.id}'}, function(data) {
                $("#tree_3").jstree({
                    "core" : {
                        "themes" : {
                            "responsive": false
                        },
                        "check_callback" : true,
                        'data' : JSON.parse(data)
                    },
                    "types" : {

                        "default" : {
                            "icon" : "fa icon-state-warning icon-lg"
                        },
                        "file" : {
                            "icon" : ""
                        }
                    },
                    "state" : { "key" : "demo3" },
                    "plugins" : [ "wholerow", "types", "checkbox" ]
                });
                $("#tree_3")
                        .on('changed.jstree', function (e, data) {
                            var i, j, r = [];
                            for(i = 0, j = data.selected.length; i < j; i++) {
                                r.push(data.instance.get_node(data.selected[i]).id);
                            }
                            $("#permissionIds").attr('value', r.join(','));
                        })
                        .jstree();
            });

        }


        return {
            //main function to initiate the module
            init: function () {

                gameTree();
                menuTree();
                permissionTree();

            }

        };

    }();

    if (App.isAngularJsApp() === false) {
        jQuery(document).ready(function() {
            UITree.init();
        });
    }

    var url = '${url}';

    function cfg_power() {

        $.post("permission/cfg_power"
                , {
                    roleId: ${role.id},
                    areaIds: $("#areaIds").val(),
                    menuIds: $("#menuIds").val(),
                    permissionIds: $("#permissionIds").val()
                }
                , function(data) {
                    if(data > 0) {
                        var content = '<h4 class="mt-xlg font-red center">操作成功！</h4>'
                        layerMsg("提示信息", content);
                        load_portlet(url);
                    } else {
                        var content = '<h4 class="mt-xlg font-red center">操作失败！</h4>'
                        layerMsg("提示信息", content);
                        load_portlet(url);
                    }

        });
    }

</script>