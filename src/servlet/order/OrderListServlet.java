package servlet.order;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.order.OrderListInfo;
import util.ServletUtil;

@WebServlet("/OrderList")
public class OrderListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// キャッシュ制御
		ServletUtil.cacheControl(response);

		// セッションから orderList を取得（なければ新規作成）
		HttpSession session = request.getSession();
		List<OrderListInfo> orderList = (List<OrderListInfo>) session.getAttribute(ServletUtil.Attr.LIST);
		if (orderList == null) {
			orderList = new ArrayList<>();
		}

		// 遷移元ページ名を取得
		String pageName = "";
		String url = request.getHeader("REFERER");
		if (url != null) {
			try {
				URL parsedUrl = new URL(url);
				String path = parsedUrl.getPath();
				pageName = path.substring(path.lastIndexOf('/') + 1);
				System.out.println("ページ名: " + pageName);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		// 「追加ページ」の場合
		if (ServletUtil.Action.DETAILS_ADD.equals(pageName)) {

			int maxOrderId = orderList.stream().mapToInt(OrderListInfo::getOrder_id).max().orElse(0);
			int order_id = maxOrderId + 1;

			String product_name = request.getParameter(ServletUtil.Param.PRODUCT_NAME);
			String category_name = request.getParameter(ServletUtil.Param.CATEGORY_NAME);
			int product_price = ServletUtil.parseOrDefault(request.getParameter(ServletUtil.Param.PRODUCT_PRICE), 0);
			int menu_quantity = 1;
			int menu_stock = 20;

			// トッピング情報
			int[] topping_id_arr = ServletUtil.safeParseIntArray(request.getParameterValues(ServletUtil.Param.TOPPING_ID));
			String[] topping_name_arr = request.getParameterValues(ServletUtil.Param.TOPPING_NAME);
			int[] topping_price_arr = ServletUtil.safeParseIntArray(request.getParameterValues(ServletUtil.Param.TOPPING_PRICE));
			int[] topping_quantity_arr = ServletUtil.safeParseIntArray(request.getParameterValues(ServletUtil.Param.TOPPING_QUANTITY));

			// menu_subtotal 計算（商品価格 + トッピング合計）
			int subtotal = product_price;
			if (topping_id_arr != null) {
				for (int i = 0; i < topping_id_arr.length; i++) {
					System.out.println("トッピング名：" + topping_name_arr[i]);
					System.out.println("トッピング個数：" + topping_quantity_arr[i]);
					subtotal += topping_price_arr[i] * topping_quantity_arr[i];
				}
			}

			// OrderListInfo 作成して追加
			orderList.add(new OrderListInfo(order_id,
					ServletUtil.parseOrDefault(request.getParameter(ServletUtil.Param.PRODUCT_ID), 0),
					product_name,
					category_name,
					product_price,
					menu_quantity,
					menu_stock,
					ServletUtil.toList(topping_id_arr),
					ServletUtil.toList(topping_name_arr),
					ServletUtil.toList(topping_price_arr),
					ServletUtil.toList(topping_quantity_arr),
					subtotal // menu_subtotal に設定
			));

		} else if (ServletUtil.Action.DETAILS_CHANGE.equals(pageName)) {
			// 「変更ページ」の場合
			int order_id = ServletUtil.parseOrDefault(request.getParameter(ServletUtil.Param.ORDER_ID), 0);
			int product_id = ServletUtil.parseOrDefault(request.getParameter(ServletUtil.Param.PRODUCT_ID), 0);
			int product_price = ServletUtil.parseOrDefault(request.getParameter(ServletUtil.Param.PRODUCT_PRICE), 0);

			int[] topping_id_arr = ServletUtil.safeParseIntArray(request.getParameterValues(ServletUtil.Param.TOPPING_ID_ATTR));
			String[] topping_name_arr = request.getParameterValues(ServletUtil.Param.TOPPING_NAME_ATTR);
			int[] topping_price_arr = ServletUtil.safeParseIntArray(request.getParameterValues(ServletUtil.Param.TOPPING_PRICE_ATTR));
			int[] topping_quantity_arr = ServletUtil.safeParseIntArray(request.getParameterValues(ServletUtil.Param.TOPPING_QUANTITY_ATTR));

			for (OrderListInfo order : orderList) {
				if (order.getOrder_id() == order_id && order.getProduct_id() == product_id) {
					order.setTopping_id(ServletUtil.toList(topping_id_arr));
					order.setTopping_name(ServletUtil.toList(topping_name_arr));
					order.setTopping_price(ServletUtil.toList(topping_price_arr));
					order.setTopping_quantity(ServletUtil.toList(topping_quantity_arr));

					// menu_subtotal 更新
					int subtotal = product_price;
					if (topping_id_arr != null) {
						for (int i = 0; i < topping_id_arr.length; i++) {
							System.out.println("トッピング名：" + topping_name_arr[i]);
							System.out.println("トッピング個数：" + topping_quantity_arr[i]);
							subtotal += topping_price_arr[i] * topping_quantity_arr[i];
						}
					}
					order.setMenu_subtotal(subtotal);
				}
			}
		}

		// セッションに保存
		session.setAttribute(ServletUtil.Attr.LIST, orderList);
		System.out.println("現在の画面：注文リスト画面");
		// JSP にフォワード
		ServletUtil.forward(request, response, ServletUtil.Path.LIST);
	}
}
