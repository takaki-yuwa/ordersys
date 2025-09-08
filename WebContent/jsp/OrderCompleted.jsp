<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="Action" value="${applicationScope.Action}" scope="page" />
<c:set var="Param" value="${applicationScope.Param}" scope="page" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!--サイトのサイズ自動調整-->
<meta name="viewport"
	content="width=device-width,height=device-height,initial-scale=1.0">
<title>注文完了画面</title>
<!--.cssの呼び出し-->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/OrderCompleted/ordercompleted.css">
<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
<script src="${pageContext.request.contextPath}/JavaScript/OrderCompleted/ordercompleted.js"></script>
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
				<p class="center-text">ご注文いただき</p>
				<p class="center-text">ありがとうございます</p>
			</div>
		</div>
		<p class="center-text">お料理を準備いたしますので</p>
		<p class="center-text">しばらくお待ち下さい</p>

	</main>
	<footer class="footer-buttons">
		<div class="table-number"><c:out value="${sessionScope.tableNumber}" />卓</div>
		<div class="footer-wrapper">
			<!--ボタン-->
			<!--メニューへ遷移-->
			<a href="${Action.MENU}">
				<button class="fixed-left-button">
					<img src="${pageContext.request.contextPath}/image/menu.png" alt="メニューのボタン"> メニュー
				</button>
			</a>
		</div>
	</footer>
</body>
</html>