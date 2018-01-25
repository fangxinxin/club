<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!-- R_head -->
<%@include file="/include/R_head.jsp" %>


<body>

<div class="row">
    <div class="col-md-12">

        <div class="clearfix mt-md"></div>

        <!-- BEGIN 页面内容-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <h4 class="center">API测试</h4>
            </div>
            <div class="portlet-body">

                <div class="featured-boxes">
                    <div class="row">
                        <div class="col-md-6 col-md-offset-3">

                            <div class="featured-box featured-box-primary align-left mt-xlg">
                                <div class="box-content">

                                    <!-- 参数： gameId, clubId, roomId, cardConsume, gameUserNum, roomRound, gameUserInfo, createTime -->
                                    <form action="log/henanmjquanji/roomCardConsume" method="post">

                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <label>gameId</label>
                                                    <input type="text" value="" class="form-control" name="gameId">
                                                </div>
                                                <div class="col-md-12">
                                                    <label>clubId</label>
                                                    <input type="text" value="" class="form-control" name="clubId">
                                                </div>
                                                <div class="col-md-12">
                                                    <label>roomId</label>
                                                    <input type="text" value="" class="form-control" name="roomId">
                                                </div>
                                                <div class="col-md-12">
                                                    <label>cardConsume</label>
                                                    <input type="text" value="" class="form-control" name="cardConsume">
                                                </div>
                                                <div class="col-md-12">
                                                    <label>gameUserNum</label>
                                                    <input type="text" value="" class="form-control" name="gameUserNum">
                                                </div>
                                                <div class="col-md-12">
                                                    <label>roomRound</label>
                                                    <input type="text" value="" class="form-control" name="roomRound">
                                                </div>
                                                <div class="col-md-12">
                                                    <label>gameUserInfo</label>
                                                    <input type="text" value="" class="form-control" name="gameUserInfo">
                                                </div>
                                                <div class="col-md-12">
                                                    <label>createTime</label>
                                                    <input type="text" value="" class="form-control" name="createTime">
                                                </div>
                                                <div class="col-md-12">
                                                    <label>sign</label>
                                                    <input type="text" value="" class="form-control" name="sign">
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <input type="submit" value="确&nbsp;&nbsp;认" class="btn btn-primary pull-right mb-xl">
                                                </div>
                                            </div>
                                        </div>

                                    </form>

                                </div>
                            </div>

                        </div>
                    </div>
                </div>

            </div>
        </div>
        <!-- END 页面内容-->

    </div>
</div>

<!-- R_foot -->
<%@include file="/include/R_foot.jsp" %>
</body>
</html>


