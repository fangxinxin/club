<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- R_head -->
<%@include file="/common/include/R_head.jsp" %>

<div class="easyui-navpanel" style="position:relative">
    <!-- head -->
    <header class="bd">
        <div class="m-toolbar">
            <div id="HeadTitle" class="m-title"></div>
            <div class="m-left">
                <a href="goSelect" class="easyui-linkbutton m-back" plain="true" outline="true">
                    返回 </a>
            </div>
            <div class="m-right">
                <a href="javascript:;" class="easyui-linkbutton" iconCls="" plain="true"
                   outline="true"> ${preName}${gameName} </a>
            </div>
        </div>
    </header>

    <iframe id="Content" class="bd" scrolling="no" frameborder="0" marginwidth="0" marginheight="0"
            src="clubView" width="100%" height="100%" allowTransparency="true"></iframe>

    <!-- footer -->
    <div id="TabMenu" class="m-buttongroup m-buttongroup-justified" style="width:100%;"></div>

</div>

<!-- R_foot -->
<%@include file="/common/include/R_foot.jsp" %>

<script>
    $(function () {
        var bIsMobile = false;
        var oBrowser = {
            versions: function () {
                var u = navigator.userAgent;
                return { //移动终端浏览器版本信息
                    trident: u.indexOf('Trident') > -1, //IE内核
                    presto: u.indexOf('Presto') > -1, //opera内核
                    webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                    gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                    mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
                    ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                    android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                    iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
                    iPad: u.indexOf('iPad') > -1, //是否iPad
                    webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
                };
            }(),
            language: (navigator.browserLanguage || navigator.language).toLowerCase()
        }
        if (oBrowser.versions.iPhone || oBrowser.versions.iPad) {
            bIsMobile = true;
        } else if (oBrowser.versions.android) {
            bIsMobile = true;
        } else {
            bIsMobile = false;
        }

        //菜单
        var aTabData = [
            {
                'id': 'club_id',
                'title': '俱乐部管理',
                'icon': 'http://download.stevengame.com/webResource/common/img/icons/club0814.png',
                'url': 'clubView',
                'home': 1
            }
            , {
                'id': 'promoter_id',
                'title': '代理管理',
                'icon': 'http://download.stevengame.com/webResource/common/img/icons/promoter.png',
                'url': 'promoterView',
                'home': 0
            }
            , {
                'id': 'saleLog_id',
                'title': '销售记录',
                'icon': 'http://download.stevengame.com/webResource/common/img/icons/sellRecord.png',
                'url': 'salelog/index',
                'home': 0
            }
            , {
                'id': 'buy_id',
                'title': '购买钻石',
                'icon': 'http://download.stevengame.com/webResource/common/img/icons/buy.png',
                'url': 'buy/index.do',
                'home': 0
            }
            , {
                'id': 'agentUp_id',
                'title': '代理升级',
                'icon': 'http://download.stevengame.com/webResource/common/img/icons/levelUp.png',
                'tips_icon': 'http://download.stevengame.com/webResource/common/img/icons/levelUp_r.png',
                'url': 'agentUp',
                'home': 0
            }
            , {
                'id': 'notice_id',
                'title': '消息',
                'icon': 'http://download.stevengame.com/webResource/common/img/icons/message.png',
                'tips_icon': 'http://download.stevengame.com/webResource/common/img/icons/message_r.png',
                'url': 'noticeList',
                'home': 0
            }
        ];

        InitView();
        function InitView() {
            for (var i = 0; i < aTabData.length; ++i) {
                var oItemData = aTabData[i];
                var oBtnTab = $('<a>').attr('href', 'javascript:void(0)').attr('class', 'easyui-linkbutton TabItem menu'+i)
                        .attr('style', 'border-width:0;').attr('data-index', i);

                var oMenu_img;
                var f1 = oItemData.title == "消息" && ${noticeTips};
                var f2 = oItemData.title == "代理升级" && false;
                if(f1 || f2) {
                    oMenu_img = $('<img>').attr('class', 'tips mt-sm img-responsive').attr('id', oItemData.id)
                            .attr('src', oItemData.tips_icon).attr('style', 'img:no-repeat; height:50%');
                }
                else {
                    oMenu_img = $('<img>').attr('class', 'mt-sm img-responsive').attr('id', oItemData.id)
                            .attr('src', oItemData.icon).attr('style', 'img:no-repeat; height:50%');
                }

                $('#TabMenu').append(oBtnTab);
                $('.menu'+[i]).append(oMenu_img);


                if (oItemData.home) {
                    $("#Content", parent.document.body).attr("src", oItemData.url);
                    $("#HeadTitle").html(oItemData.title);
                }
            }

            if (bIsMobile) {
                $('.TabItem').on('touchend', OnClickTabItem);
            } else {
                $('.TabItem').on('click', OnClickTabItem);
            }

            var nContentH = document.body.clientHeight - 106;// iframe height
            $('#Content').css('height', nContentH + 'px');
        }

        function OnClickTabItem() {
            var nIndex = $(this).attr('data-index');
            if (nIndex < aTabData.length) {
                var oItemData = aTabData[nIndex];
                $("#Content", parent.document.body).attr("src", oItemData.url);
                $("#HeadTitle").html(oItemData.title);

                var f1 = oItemData.title == '消息' ;
                var f2 = oItemData.title == '代理升级';
                if(f1 || f2) {
                    $("#"+oItemData.id, parent.document.body).attr("src", oItemData.icon);
                }
            }
        }


    }());

</script>