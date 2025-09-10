<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="Action" value="${applicationScope.Action}" scope="page" />
<c:set var="Param" value="${applicationScope.Param}" scope="page" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,height=device-height,initial-scale=1.0">
<title>商品詳細画面</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/Details/details.css">
<link rel="icon" href="data:," />
</head>
<body>
	<%
	String fromPage = request.getParameter("from");
	%>
	<!-- ヘッダー(店の名前) -->
	<header class="header-storename">
		<div class="header-image-wrapper">
			<img src="${pageContext.request.contextPath}/image/木目3.jpg" alt="背景" class="header-background-image"> 
			<img src="${pageContext.request.contextPath}/image/biglogo.png" alt="店の名前" class="header-image">
		</div>
	</header>
	<header class="header-product">
		<div class="header-product-wrapper">
			<!-- EL式を使用して商品名を表示 -->
			<%
			//メニュー画面から遷移してきた場合
			if ("OrderMenu.jsp".equals(fromPage)) {
			%>
			<c:if test="${not empty addProductInfo}">
				<div class="product-text">
					<c:out value="${addProductInfo.product_name}" />
				</div>
				<div class="price-text">
					<c:out value="${addProductInfo.product_price}" />
					円(税込)
				</div>
			</c:if>
			<%
			//注文リスト画面から遷移してきた場合
			} else if ("OrderList.jsp".equals(fromPage)) {
			%>
			<c:if test="${not empty changeProductInfo}">
				<div class="product-text">
					<c:out value="${changeProductInfo.product_name}" />
				</div>
				<div class="price-text">
					<c:out value="${changeProductInfo.product_price}" />
					円(税込)
				</div>
			</c:if>
			<%
			}
			%>
		</div>
	</header>
	<main class="details-main">
		<%
		//メニュー画面から遷移してきた場合
		if ("OrderMenu.jsp".equals(fromPage)) {
		%>
		<!-- セッションからカテゴリー名を取ってくる -->
		<c:if test="${not empty addProductInfo.category_name}">
			<c:choose>
				<c:when
					test="${addProductInfo.category_name == 'お好み焼き' || addProductInfo.category_name == 'もんじゃ焼き'}">
					<c:if test="${not empty toppingInfo}">
						<p>トッピング</p>
						<c:forEach var="topping" items="${toppingInfo}" varStatus="status">
							<li class="topping-row">
								<c:if test="${topping.topping_display_flag == 1}">
									<div class="break-topping">
										<c:out value="${topping.topping_name}" />
										：
										<c:out value="${topping.topping_price}" />
										円
									</div>
									<c:if test="${topping.topping_stock > 0}">
										<button class="counter-button minus" data-id="<c:out value='${topping.topping_id}' />" data-index="<c:out value='${status.index}' />">-</button>
										<input type="text" value="0" class="counter-input" readonly data-id="<c:out value='${topping.topping_id}' />">
										<button class="counter-button plus" data-index="<c:out value='${status.index}' />" data-id="<c:out value='${topping.topping_id}' />" data-max="<c:out value='${topping.topping_stock}' />">+</button>
									</c:if>
									<c:if test="${topping.topping_stock <= 0}">
										<img src="${pageContext.request.contextPath}/image/soldout.png" alt="Sold Out" class="soldout-img" />
									</c:if>
								</c:if></li>
						</c:forEach>
					</c:if>
					<c:if test="${empty toppingInfo}">
						<div>トッピングはありません。</div>
					</c:if>
				</c:when>
			</c:choose>
		</c:if>
		<%
		//注文リスト画面から遷移してきた場合
		} else if ("OrderList.jsp".equals(fromPage)) {
		%>
		<!-- セッションからカテゴリー名を取ってくる -->
		<c:if test="${not empty changeProductInfo.category_name}">
			<c:choose>
				<c:when test="${changeProductInfo.category_name == 'お好み焼き' || changeProductInfo.category_name == 'もんじゃ焼き'}">
					<p>トッピング</p>
					<c:if test="${not empty toppingInfo}">
						<c:forEach var="topping" items="${toppingInfo}" varStatus="status">
							<c:set var="topping_quantity" value="${changeProductInfo.topping_quantity[status.index]}" />
							<li class="topping-row">
								<c:if test="${topping.topping_display_flag == 1}">
									<div class="break-topping">
										<c:out value="${topping.topping_name}" />
										：
										<c:out value="${topping.topping_price}" />
										円
									</div>
									<c:if test="${topping.topping_stock > 0}">
										<button class="counter-button minus" data-id="<c:out value='${topping.topping_id}' />" data-index="<c:out value='${status.index}' />">-</button>
										<input type="text" value="<c:out value='${topping_quantity}' />" class="counter-input" readonly data-id="<c:out value='${topping.topping_id}' />">
										<button class="counter-button plus" data-index="<c:out value='${status.index}' />" data-id="<c:out value='${topping.topping_id}' />" data-max="<c:out value='${topping.topping_stock}' />">+</button>
									</c:if>
									<c:if test="${topping.topping_stock <= 0}">
										<img src="${pageContext.request.contextPath}/image/soldout.png" alt="Sold Out" class="soldout-img" />
									</c:if>
								</c:if></li>
						</c:forEach>
					</c:if>
					<c:if test="${empty toppingInfo}">
						<div>トッピングはありません。</div>
					</c:if>
				</c:when>
			</c:choose>
		</c:if>
		<%
		}
		%>
	</main>
	<footer class="footer-buttons">
		<div class="table-number">
			<c:out value="${sessionScope.tableNumber}" />
			卓
		</div>
		<div class="footer-wrapper">
			<!-- ボタン -->
			<%
			//メニュー画面から遷移してきた場合
			if ("OrderMenu.jsp".equals(fromPage)) {
			%>
			<form action="${Action.LIST}" method="post">
				<button class="fixed-right-button">
					<input type="hidden" name="${Param.PRODUCT_ID}" value="<c:out value='${addProductInfo.product_id}' />"> 
					<input type="hidden" name="${Param.PRODUCT_NAME}" value="<c:out value='${addProductInfo.product_name}' />">
					<input type="hidden" name="${Param.CATEGORY_NAME}" value="<c:out value='${addProductInfo.category_name}' />">
					<input type="hidden" name="${Param.PRODUCT_PRICE}" value="<c:out value='${addProductInfo.product_price}' />">
					<input type="hidden" name="${Param.PRODUCT_STOCK}" value="<c:out value='${addProductInfo.product_stock}' />">
					<c:forEach var="topping" items="${toppingInfo}" varStatus="status">
						<input type="hidden" name="${Param.TOPPING_ID}" value="<c:out value='${topping.topping_id}' />">
						<input type="hidden" name="${Param.TOPPING_NAME}" value="<c:out value='${topping.topping_name}' />">
						<input type="hidden" name="${Param.TOPPING_PRICE}" value="<c:out value='${topping.topping_price}' />">
						<input type="hidden" name="${Param.TOPPING_QUANTITY}" id="topping-<c:out value='${topping.topping_id}' />" value="0">
					</c:forEach>
					<input type="hidden" name="${Param.TOTAL}" id="input-total" value=""> 
					<img src="${pageContext.request.contextPath}/image/addCart.png"alt="追加のボタン"> 追加
				</button>
			</form>
			<%
			//注文リスト画面から遷移してきた場合
			} else if ("OrderList.jsp".equals(fromPage)) {
			%>
			<form action="${Action.LIST}" method="post">
				<button class="fixed-right-button">
					<input type="hidden" name="${Param.ORDER_ID}"value="<c:out value='${changeProductInfo.order_id}' />"> 
					<input type="hidden" name="${Param.PRODUCT_ID}" value="<c:out value='${changeProductInfo.product_id}' />">
					<input type="hidden" name="${Param.PRODUCT_NAME}" value="<c:out value='${changeProductInfo.product_name}' />">
					<input type="hidden" name="${Param.CATEGORY_NAME}" value="<c:out value='${changeProductInfo.category_name}' />">
					<input type="hidden" name="${Param.PRODUCT_PRICE}" value="<c:out value='${changeProductInfo.product_price}' />">
					<input type="hidden" name="${Param.SUBTOTAL}" value="<c:out value='${changeProductInfo.subtotal}' />">
					<c:forEach var="topping_id" items="${changeProductInfo.topping_id}" varStatus="status">
						<c:set var="topping_name" value="${changeProductInfo.topping_name[status.index]}" />
						<c:set var="topping_price" value="${changeProductInfo.topping_price[status.index]}" />
						<c:set var="topping_quantity" value="${changeProductInfo.topping_quantity[status.index]}" />
						<input type="hidden" name="${Param.TOPPING_ID_ATTR}" value="<c:out value='${topping_id}' />">
						<input type="hidden" name="${Param.TOPPING_NAME_ATTR}" value="<c:out value='${topping_name}' />">
						<input type="hidden" name="${Param.TOPPING_PRICE_ATTR}" value="<c:out value='${topping_price}' />">
						<%--valueに変更していない個数がリセットされないように取得した${topping_quantity}を入れる--%>
						<input type="hidden" name="${Param.TOPPING_QUANTITY_ATTR}" id="topping-<c:out value='${topping_id}' />" value="<c:out value='${topping_quantity}' />">
					</c:forEach>
					<input type="hidden" name="${Param.TOTAL}" id="input-total" value=""> 
					<img src="${pageContext.request.contextPath}/image/changeCart.png" alt="変更のボタン"> 変更
				</button>
			</form>
			<%
			}
			%>
			<a href="${Action.MENU}">
				<button class="fixed-left-button">
					<img src="${pageContext.request.contextPath}/image/menu.png" alt="メニューのボタン"> メニュー
				</button>
			</a>
		</div>
	</footer>
	<footer class="footer-subtotal">
		<div class="footer-subtotal-wrapper">
			<div class="subtotal-text">
				小計:<span id="total">0</span>円(税込)
			</div>
		</div>
	</footer>

	<script>
    // 商品本体価格
    const basePrice = <c:out value="${empty addProductInfo ? (empty changeProductInfo ? 0 : changeProductInfo.product_price) : addProductInfo.product_price}" />;
    const initialTotal = <c:out value="${empty addProductInfo ? (empty changeProductInfo ? 0 : changeProductInfo.subtotal) : addProductInfo.product_price}" />;

    // トッピングの価格を JS オブジェクトで渡す
    const toppingPrices = {
	<c:if test="${not empty toppingInfo}">
    	<c:forEach var="topping" items="${toppingInfo}" varStatus="status">
        	"${topping.topping_id}": ${topping.topping_price}<c:if test="${!status.last}">,</c:if>
    	</c:forEach>
	</c:if>
	};
	</script>
	<script src="${pageContext.request.contextPath}/JavaScript/Details/details.js"></script>
</body>
</html>