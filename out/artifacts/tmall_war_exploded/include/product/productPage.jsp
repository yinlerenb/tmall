

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<title>模仿天猫官网 ${p.name}</title>
<div class="categoryPictureInProductPageDiv">
	<img class="categoryPictureInProductPage" src="img/category/${p.category.id}.jpg">
</div>

<div class="productPageDiv">
<%--显示单个图片和基本信息--%>
	<%@include file="imgAndInfo.jsp" %>
	<%--显示评价信息--%>
	<%@include file="productReview.jsp" %>
	<%--显示详情图片--%>
	<%@include file="productDetail.jsp" %>
</div>