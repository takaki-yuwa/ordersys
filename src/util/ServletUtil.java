package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletUtil {

	// ロガー
	private static final Logger logger = Logger.getLogger(ServletUtil.class.getName());

	// 共通JSPパス
	public static class Path {
		public static final String ERROR = "/jsp/Error.jsp";
		public static final String STATE = "/jsp/OrderState.jsp";
		public static final String SKIP_STATE = "/jsp/SkipOrderState.jsp";
		public static final String MENU = "/jsp/OrderMenu.jsp";
		public static final String DETAILS = "/jsp/ProductDetails.jsp";
		public static final String LIST = "/jsp/OrderList.jsp";
		public static final String REMOVE = "OrderList.jsp";
		public static final String COMPLETED = "/jsp/OrderCompleted.jsp";
		public static final String HISTORY = "/jsp/OrderHistory.jsp";
		public static final String ACCOUNTING = "/jsp/Accounting.jsp";
	}

	//共通WebServlet名
	public static class Action {
		public static final String STATE = "OrderState";
		public static final String MENU = "OrderMenu";
		public static final String DETAILS_ADD = "DetailsAdd";
		public static final String DETAILS_CHANGE = "DetailsChange";
		public static final String LIST = "OrderList";
		public static final String REMOVE = "OrderRemove";
		public static final String COMPLETED = "OrderCompleted";
		public static final String HISTORY = "OrderHistory";
		public static final String ACCOUNTING = "Accounting";
	}

	// 共通定数名
	public static class Attr {
		public static final String TABLE = "tableInfo";
		public static final String CATEGORY = "categoryList";
		public static final String PRODUCT = "productInfo";
		public static final String TOPPING = "toppingInfo";
		public static final String DETAILS_ADD = "addProductInfo";
		public static final String DETAILS_CHANGE = "changeProductInfo";
		public static final String LIST = "orderListInfo";
		public static final String HISTORY = "orderHistoryInfo";
		public static final String ACCOUNTING = "accountingInfo";
	}

	//共通パラメータ
	public static class Param {
		public static final String FORM = "form";
		public static final String SESSION_ID = "sessionNumber";
		public static final String TABLE_ID = "tableNumber";
		public static final String SESSION_STATUS = "sessionStatus";
		public static final String GUEST_COUNT = "gest_count";
		public static final String ORDER_ID = "order_id";
		public static final String ORDER_ID_ATTR = "order_id[]";
		public static final String PRODUCT_ID = "product_id";
		public static final String PRODUCT_NAME = "product_name";
		public static final String CATEGORY_NAME = "category_name";
		public static final String PRODUCT_PRICE = "product_price";
		public static final String PRODUCT_STOCK = "product_stock";
		public static final String PRODUCT_ID_ATTR = "product_id[]";
		public static final String PRODUCT_QUANTITY_ATTR = "product_quantity[]";
		public static final String TOPPING_ID = "topping_id";
		public static final String TOPPING_NAME = "topping_name";
		public static final String TOPPING_PRICE = "topping_price";
		public static final String TOPPING_QUANTITY = "topping_quantity";
		public static final String TOPPING_STOCK = "topping_stock";
		public static final String TOPPING_ID_ATTR = "topping_id[]";
		public static final String TOPPING_NAME_ATTR = "topping_name[]";
		public static final String TOPPING_PRICE_ATTR = "topping_price[]";
		public static final String TOPPING_QUANTITY_ATTR = "topping_quantity[]";
		public static final String TOPPING_STOCK_ATTR = "topping_stock[]";
		public static final String TOPPING_ID_ = "topping_id_";
		public static final String TOPPING_QUANTITY_ = "topping_quantity_";
		public static final String SUBTOTAL = "subtotal";
		public static final String SUBTOTAL_ATTR = "subtotal[]";
		public static final String TOTAL = "total";
	}

	public static class Value {
		public static final String STATE = "State";
		public static final String SKIP_STATE = "SkipState";
	}

	public static class Status {
		public static final String ACTIVE = "active";
		public static final String INACTIVE = "inactive";
		public static final String CLOSED = "closed";
	}

	// キャッシュ制御ヘッダーを設定
	public static void cacheControl(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP/1.1
		response.setHeader("Pragma", "no-cache"); // HTTP/1.0
		response.setDateHeader("Expires", 0); // プロキシ／Expiresヘッダー用
	}

	// nullかEmptyならtrueを返す
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}

	// nullか空白のみならtrueを返す
	public static boolean isNullOrBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	// 遷移先画面に飛ばす
	public static void forward(HttpServletRequest request, HttpServletResponse response, String path)
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}

	// エラー画面に飛ばす
	public static void forwardError(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("現在の画面：エラー画面");
		request.getRequestDispatcher(Path.ERROR).forward(request, response);
	}

	// エラー画面にメッセージ付きで飛ばす
	public static void forwardError(HttpServletRequest request, HttpServletResponse response, String message)
			throws ServletException, IOException {
		request.setAttribute("errorMessage", message);
		request.getRequestDispatcher(Path.ERROR).forward(request, response);
	}

	// 必須パラメータの数値変換（失敗したらエラー画面）
	public static int parseOrForward(String param, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			return Integer.parseInt(param);
		} catch (NumberFormatException e) {
			logger.warning("無効な数値: " + param);
			forwardError(request, response, "無効な数値が入力されました");
			throw new IOException("数値変換エラー");
		}
	}

	// 任意パラメータの数値変換（失敗したらデフォルト値）
	public static int parseOrDefault(String param, int defaultValue) {
		if (isNullOrEmpty(param))
			return defaultValue;
		try {
			return Integer.parseInt(param);
		} catch (NumberFormatException e) {
			logger.warning("無効な数値: " + param);
			return defaultValue;
		}
	}

	// 配列をint型に変換（nullまたは空の場合は空配列）
	public static int[] safeParseIntArray(String[] arr) {
		if (arr == null || arr.length == 0)
			return new int[0];
		return java.util.Arrays.stream(arr)
				.mapToInt(s -> parseOrDefault(s, 0))
				.toArray();
	}

	// int[] を List<Integer> に変換
	public static List<Integer> toList(int[] arr) {
		List<Integer> list = new ArrayList<>();
		for (int v : arr)
			list.add(v);
		return list;
	}

	// String[] を List<String> に変換
	public static List<String> toList(String[] arr) {
		List<String> list = new ArrayList<>();
		if (arr != null) {
			for (String s : arr)
				list.add(s);
		}
		return list;
	}
}