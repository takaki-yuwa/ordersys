<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="Action" value="${applicationScope.Action}" scope="page" />
<c:set var="Param" value="${applicationScope.Param}" scope="page" />
<c:set var="Value" value="${applicationScope.Value}" scope="page" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!--サイトのサイズ自動調整-->
<meta name="viewport"
	content="width=device-width,height=device-height,initial-scale=1.0">
<title>注文開始画面</title>
<!--.cssの呼び出し-->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/OrderState/orderstate.css">
<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
<script src="${pageContext.request.contextPath}/JavaScript/OrderState/orderstate.js"></script>
</head>
<body>
	<!--ヘッダー(店の名前)-->
	<header class="header-storename">
		<div class="header-image-wrapper">
			<img src="${pageContext.request.contextPath}/image/木目3.jpg" alt="背景" class="header-background-image">
			<img src="${pageContext.request.contextPath}/image/biglogo.png" alt="店の名前" class="header-image">
		</div>
	</header>
	<main>
		<div class="center-container">
			<div class="square-box">
				<div class="table-number">
					<c:out value="${tableInfo.table_id}" />
					卓
				</div>
			</div>
		</div>
		<p class="center-text">いらっしゃいませ！</p>
		<p class="center-text">人数を設定してください</p>

		<!-- 人数入力 -->
		<div class="people-counter">
			<button type="button" id="decrement-btn">-</button>
			<span id="guest-count">1</span>
			<button type="button" id="increment-btn">+</button>
		</div>

		<!-- 送信ボタン -->
		<div class="center-container">
			<form action="${Action.MENU}" method="post">
				<input type="hidden" name="${Param.FORM}" value="${Value.STATE}">
				<input type="hidden" name="${Param.SESSION_ID}" value="<c:out value='${tableInfo.session_id}' />">
				<input type="hidden" name="${Param.TABLE_ID}" value="<c:out value='${tableInfo.table_id}' />">
				<input type="hidden" name="${Param.SESSION_STATUS}" value="<c:out value='${tableInfo.session_status}' />">
				<input type="hidden" name="${Param.GUEST_COUNT}" id="guest-input" value="1">
				<button class="state-btn">注文開始</button>
			</form>
		</div>
	</main>
</body>
</html>