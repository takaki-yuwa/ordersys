<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ page import="java.util.List, java.util.ArrayList"%>
<c:set var="Action" value="${applicationScope.Action}" scope="page" />
<c:set var="Param" value="${applicationScope.Param}" scope="page" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!--サイトのサイズ自動調整-->
<meta name="viewport" content="width=device-width,height=device-height,initial-scale=1.0">
<title>注文リスト画面</title>
<!--.cssの呼び出し-->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/popup.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/OrderList/orderlist.css">
<link rel="icon" href="data:," />
<script src="${pageContext.request.contextPath}/JavaScript/OrderList/orderlist.js"></script>
<script>
  const contextPath = '${pageContext.request.contextPath}';
</script>
</head>
<body>
	<!--ヘッダー(店の名前)-->
	<header class="header-storename">
		<div class="header-image-wrapper">
			<img src="${pageContext.request.contextPath}/image/木目3.jpg" alt="背景" class="header-background-image">
			<img src="${pageContext.request.contextPath}/image/biglogo.png" alt="店の名前" class="header-image">
		</div>
	</header>
	<main class="list-main">
		<div class="orderlist">
			<c:if test="${not empty orderListInfo}">
				<c:forEach var="orderlist" items="${orderListInfo}" varStatus="order_status">
					<c:set var="order_id" value="${orderlist.order_id}" />
					<c:set var="product_id" value="${orderlist.product_id}" />
					<c:set var="product_name" value="${orderlist.product_name}" />
					<c:set var="category_name" value="${orderlist.category_name}" />
					<c:set var="product_price" value="${orderlist.product_price}" />
					<c:set var="product_quantity" value="${orderlist.product_quantity}" />
					<c:set var="product_stock" value="${orderlist.product_stock}" />
					<c:set var="menu_subtotal" value="${product_price * product_quantity}" />
					<li>
						<div class="order-item">
							<div class="product-name"><c:out value="${product_name}" /></div>
							<div class="product-price"><c:out value="${product_price}" />円</div>
						</div> 
						
						<input type="hidden" id="productId-${order_id}" value="${product_id}">
						
						<!-- トッピングがある場合だけループ -->
						<c:if test="${not empty orderlist.topping_id}">
							<c:forEach var="topping_id" items="${orderlist.topping_id}" varStatus="topping">
								<c:set var="topping_name" value="${orderlist.topping_name[topping.index]}" />
								<c:set var="topping_price" value="${orderlist.topping_price[topping.index]}" />
								<c:set var="topping_quantity" value="${orderlist.topping_quantity[topping.index]}" />
								<c:if test="${topping_quantity>=1 }">
									<%--トッピング表示--%>
									<div class="order-item">
										<div class="topping-name">・<c:out value="${topping_name}" />✕<span><c:out value="${topping_quantity}" /></span></div>
										<div class="topping-price"><c:out value="${topping_price * topping_quantity}" />円</div>
									</div>
									<c:set var="menu_subtotal" value="${menu_subtotal+(topping_price * topping_quantity)}" />
								</c:if>
							</c:forEach>
						</c:if>
						<div class="order-item">
							<div class="subtotal-price">
								小計: <span id="subtotal-<c:out value='${order_id}' />" data-price="<c:out value='${menu_subtotal}' />"><c:out value="${menu_subtotal}" /></span>円
							</div>
						</div> 
						<c:set var="total" value="${total+menu_subtotal}" /> 
						<!-- ボタンを横並びに配置 -->
						<div class="order-item buttons-container">
							<!-- 変更ボタン -->
							<c:if test="${category_name == 'お好み焼き' || category_name == 'もんじゃ焼き'}">
							<form action="${pageContext.request.contextPath}/${Action.DETAILS_CHANGE}" method="post">
								<input type="hidden" name="from" value="OrderList.jsp">
								<input type="hidden" name="${Param.ORDER_ID}" value="<c:out value='${order_id}' />">
								<input type="hidden" name="${Param.PRODUCT_ID}" value="<c:out value='${product_id}' />">
								<input type="hidden" name="${Param.PRODUCT_NAME}" value="<c:out value='${product_name}' />">
								<input type="hidden" name="${Param.CATEGORY_NAME}" value="<c:out value='${category_name}' />"> 
								<input type="hidden" name="${Param.PRODUCT_PRICE}" value="<c:out value='${product_price}' />"> 
								<input type="hidden" name="${Param.SUBTOTAL}" value="<c:out value='${menu_subtotal}' />">
								<!-- トッピングがある場合だけループ -->
								<c:if test="${not empty orderlist.topping_id}">
									<c:forEach var="topping_id" items="${orderlist.topping_id}" varStatus="topping">
										<c:set var="topping_name" value="${orderlist.topping_name[topping.index]}" />
										<c:set var="topping_price" value="${orderlist.topping_price[topping.index]}" />
										<c:set var="topping_quantity" value="${orderlist.topping_quantity[topping.index]}" />
										<c:set var="topping_stock" value="${orderlist.topping_stock[topping.index]}" />
										<input type="hidden" name="${Param.TOPPING_ID_ATTR}" value="<c:out value='${topping_id}' />">
										<input type="hidden" name="${Param.TOPPING_NAME_ATTR}" value="<c:out value='${topping_name}' />">
										<input type="hidden" name="${Param.TOPPING_PRICE_ATTR}" value="<c:out value='${topping_price}' />">
										<input type="hidden" name="${Param.TOPPING_QUANTITY_ATTR}" value="<c:out value='${topping_quantity}' />">
										<input type="hidden" name="${Param.TOPPING_STOCK_ATTR}" value="<c:out value='${topping_stock}' />">
									</c:forEach>
								</c:if>
								<button class="change-btn">変更</button>
							</form>
							</c:if>
							<!-- 増減ボタンを追加（トッピングに関係なく表示） -->
							<div class="quantity-buttons">
								<button type="button" name="quantity" value="<c:out value='${product_quantity - 1}' />" class="decrease-btn" id="decrement-<c:out value='${order_id}' />"></button>
								<span class="quantity" id="counter-<c:out value='${order_id}' />"><c:out value="${product_quantity}" /></span>
								<button type="button" name="quantity" value="<c:out value='${product_quantity + 1}' />" class="increase-btn" id="increment-<c:out value='${order_id}' />">+</button>
							</div>
						</div>
					</li>
				</c:forEach>
			</c:if>
			<c:if test="${empty orderListInfo}">
				<div class="center-container">
					<div class="square-box">
					<img src="${pageContext.request.contextPath}/image/cart.png" alt="カート">
						<p class="center-text">注文リストに商品はありません</p>
					</div>
				</div>
			</c:if>
		</div>
	</main>
	<!--ポップアップの背景-->
	<div class="popup-overlay" id="popup-overlay"></div>
	<!--ポップアップの内容-->
	<div class="popup-content" id="popup-content">
		<p>この商品を削除します</p>
		<p>よろしいですか？</p>
		<button class="popup-close" id="close-popup">いいえ</button>
		<!-- 商品を削除 -->
		<form action="${pageContext.request.contextPath}/${Action.REMOVE}" method="post">
			<input type="hidden" name="${Param.ORDER_ID}" id="popup-order-id" />
			<button type="submit" class="popup-proceed" id="confirm-button">は　い</button>
		</form>
	</div>
	<footer class="footer-buttons">
		<div class="table-number"><c:out value="${sessionScope.tableNumber}" />卓</div>
		<div class="footer-wrapper">
			<!--ボタン-->
			<!--注文完了へ遷移-->
			<form action="${pageContext.request.contextPath}/${Action.COMPLETED}" method="post">
				<c:forEach var="orderlist" items="${orderListInfo}" varStatus="product">
					<input type="hidden" id="menu_id" name="${Param.ORDER_ID_ATTR}" value="${orderlist.order_id}">
					<input type="hidden" name="${Param.PRODUCT_ID_ATTR}" value="${orderlist.product_id}">
					<input type="hidden" id="countField-<c:out value='${orderlist.order_id}' />" name="${Param.PRODUCT_QUANTITY_ATTR}" value="${orderlist.product_quantity}">
					<input type="hidden" id="priceField-<c:out value='${orderlist.order_id}' />" name="${Param.SUBTOTAL_ATTR}" value="${orderlist.menu_subtotal}">
					<!-- トッピングがある場合にループ -->
					<c:if test="${not empty orderlist.topping_id}">
						<c:forEach var="topping_id" items="${orderlist.topping_id}" varStatus="topping">
							<input type="hidden" name="${Param.TOPPING_ID_}${product.index}" value="${topping_id}">
							<input type="hidden" id="toppingcountField-<c:out value='${orderlist.order_id}' />-<c:out value='${topping_id}' />" name="${Param.TOPPING_QUANTITY_}${product.index}" value="${orderlist.topping_quantity[topping.index]}">
						</c:forEach>
					</c:if>
				</c:forEach>
				<c:if test="${not empty orderListInfo}">
					<button class="fixed-right-button">
						<img src="${pageContext.request.contextPath}/image/Vector.png" alt="注文のボタン">注文する
					</button>
				</c:if>
			</form>
			<!--メニューへ遷移-->
			<a href="${pageContext.request.contextPath}/${Action.MENU}">
				<button class="fixed-left-button">
					<img src="${pageContext.request.contextPath}/image/menu.png" alt="メニューのボタン"> メニュー
				</button>
			</a>
		</div>
	</footer>
	<c:if test="${not empty orderListInfo}">
		<footer class="footer-subtotal">
			<div class="footer-subtotal-wrapper">
				<div class="subtotal-text" id="total">合計:<c:out value="${total}" />円(税込)</div>
			</div>
		</footer>
	</c:if>
</body>

<!-- 小計・合計の更新 -->
<c:forEach var="orderlist" items="${orderListInfo}" varStatus="product">
	<c:set var="order_id" value="${orderlist.order_id}" />
	<c:set var="product_price" value="${orderlist.product_price}" />
	<c:set var="product_quantity" value="${orderlist.product_quantity}" />
	<c:set var="product_stock" value="${orderlist.product_stock}" />
	<c:set var="menu_subtotal" value="${product_price * product_quantity}" />
	<!-- トッピングがある場合だけ処理 -->
	<c:if test="${not empty orderlist.topping_id}">
		<c:forEach var="topping_id" items="${orderlist.topping_id}" varStatus="topping">
			<c:set var="topping_price" value="${orderlist.topping_price[topping.index]}" />
			<c:set var="topping_quantity" value="${orderlist.topping_quantity[topping.index]}" />
			<c:set var="menu_subtotal" value="${menu_subtotal+(topping_price * topping_quantity)}" />
		</c:forEach>
	</c:if>
	<!-- JavaScriptで埋め込む -->
	<script>
	(function() {
		  const toppings = [
		    <c:if test="${not empty orderlist.topping_id}">
			    <c:forEach var="i" begin="0" end="${fn:length(orderlist.topping_id) - 1}" varStatus="loop">
			      {
			        id: "<c:out value='${orderlist.topping_id[i]}' />",
			        quantity: <c:out value='${orderlist.topping_quantity[i]}' />,
			        stock: <c:out value='${orderlist.topping_stock[i]}' />
			      }<c:if test="${!loop.last}">,</c:if>
			    </c:forEach>
			</c:if>
		  ];
		  updateOrderState(<c:out value='${order_id}' />, <c:out value='${product_quantity}' />, <c:out value='${product_stock}' />, <c:out value='${menu_subtotal}' />, toppings);
	})();
	</script>
</c:forEach>
</html>