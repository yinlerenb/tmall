<%--
  Created by IntelliJ IDEA.
  User: E7450
  Date: 2020/6/19
  Time: 19:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="../include/admin/adminHeader.jsp" %>
<%@include file="../include/admin/adminNavigator.jsp" %>

<script>
    $(function () {
        $("#addForm").submit(function () {
            if (!checkEmpty("name", "产品名称"))
                return false;
            if (!checkNumber("originalPrice", "原价格"))
                return false;
            if (!checkNumber("promotePrice", "优惠价格"))
                return false;
            if (!checkInt("stock", "库存"))
                return false;
            return true;
        });
    });
</script>

<title>产品管理</title>

<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a></li>
        <li><a href="admin_product_list?cid=${c.id}">${c.name}</a></li>
        <li class="active">产品管理</li>
    </ol>

    <div class="listDataTableDiv">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
            <tr class="success">
                <th>ID</th>
                <th>图片</th>
                <th>产品名称</th>
                <th>产品小标题</th>
                <th width="53px">原价格</th>
                <td width="80px">优惠价格</td>
                <td width="80px">库存数量</td>
                <td width="80px">图片管理</td>
                <td width="80px">设置属性</td>
                <th width="42px">编辑</th>
                <th width="42px">删除</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${ps}" var="p">
                <tr>
                    <td>${p.id}</td>
                    <td>
                        <c:if test="${!empty p.firstProductImage}">
                            <img width="40px" src="img/productSingle/${p.firfirstProductImage.id}.jpg">
                        </c:if>
                    </td>
                    <td>${p.name}</td>
                    <td>${p.subTitle}</td>
                    <td>${p.originalPrice}</td>
                    <td>${p.promotePrice}</td>
                    <td>${p.stock}</td>


                    <td>
                        <a href="admin_productImage_list?pid=${p.id}">
                            <span class="glyphicon glyphicon-picture"></span>
                        </a>
                    </td>
                    <td>
                        <a href="admin_product_editPropertyValue?id=${p.id}">
                            <span class="glyphicon glyphicon-th-list"></span>
                        </a>
                    </td>
                    <td>
                        <a href="admin_product_edit?id=${p.id}">
                            <span class="glyphicon glyphicon-edit"></span>
                        </a>
                    </td>
                    <td>
                        <a deleteLink="true" href="admin_product_delete?id=${p.id}">
                            <span class="glyphicon glyphicon-trash"></span>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="pageDiv">
        <%@include file="../include/admin/adminPage.jsp" %>
    </div>

    <div class="panel panel-warning addDiv">
        <div class="panel-heading">新增产品</div>
        <div class="panel-body">
            <form method="post" id="addForm" action="admin_product_add" enctype="multipart/form-data">
                <table class="addTable">
                    <tr>
                        <td>产品名称</td>
                        <td><input id="name" name="name" type="text" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>产品小标题</td>
                        <td>
                            <input id="subTitle" type="text" name="subTitle" class="form-control">
                        </td>
                    </tr>
                    <tr>
                        <td>原价格</td>
                        <td>
                            <input id="originalPrice" type="text" name="originalPrice" class="form-control" value="99.98">
                        </td>
                    </tr>
                    <tr>
                        <td>优惠价格</td>
                        <td>
                            <input id="promotePrice" type="text" name="promotePrice" class="form-control" value="19.98">
                        </td>
                    </tr>
                    <tr>
                        <td>库存</td>
                        <td>
                            <input id="stock" type="text" name="ostock" class="form-control" value="99">
                        </td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <input type="hidden" name="cid" value="${c.id}">
                            <button type="submit" class="btn btn-success">提交</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<%@include file="../include/admin/adminFooter.jsp" %>
