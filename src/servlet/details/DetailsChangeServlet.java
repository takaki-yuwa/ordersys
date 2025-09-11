package servlet.details;

import java.io.IOException;
import java.util.List;

import dao.topping.ToppingListDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.details.DetailsChangeInfo;
import model.topping.ToppingInfo;
import util.ServletUtil;

@WebServlet("/DetailsChange")
public class DetailsChangeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// キャッシュ制御ヘッダーを設定
			ServletUtil.cacheControl(response);

			//パラメータを取得
			String o_id = request.getParameter(ServletUtil.Param.ORDER_ID);
			String p_id = request.getParameter(ServletUtil.Param.PRODUCT_ID);
			String product_name = request.getParameter(ServletUtil.Param.PRODUCT_NAME);
			String category_name = request.getParameter(ServletUtil.Param.CATEGORY_NAME);
			String p_price = request.getParameter(ServletUtil.Param.PRODUCT_PRICE);
			String[] t_id = request.getParameterValues(ServletUtil.Param.TOPPING_ID_ATTR);
			String[] topping_name = request.getParameterValues(ServletUtil.Param.TOPPING_NAME_ATTR);
			String[] t_price = request.getParameterValues(ServletUtil.Param.TOPPING_PRICE_ATTR);
			String[] t_quantity = request.getParameterValues(ServletUtil.Param.TOPPING_QUANTITY_ATTR);
			String[] t_stock = request.getParameterValues(ServletUtil.Param.TOPPING_STOCK_ATTR);
			String subtotalstr = request.getParameter(ServletUtil.Param.SUBTOTAL);

			if (ServletUtil.isNullOrEmpty(o_id) || ServletUtil.isNullOrEmpty(p_id)) {
				ServletUtil.forwardError(request, response);
				return;
			}

			//int型への変換処理
			int order_id = ServletUtil.parseOrForward(o_id, request, response);
			int product_id = ServletUtil.parseOrForward(p_id, request, response);
			int product_price = ServletUtil.parseOrDefault(p_price, 0);
			int subtotal = ServletUtil.parseOrDefault(subtotalstr, 0);

			int[] topping_id;
			int[] topping_price;
			int[] topping_quantity;
			int[] topping_stock;
			//int型[]への変換処理
			topping_id = ServletUtil.safeParseIntArray(t_id);
			topping_price = ServletUtil.safeParseIntArray(t_price);
			topping_quantity = ServletUtil.safeParseIntArray(t_quantity);
			topping_stock=ServletUtil.safeParseIntArray(t_stock);

			//詳細変更リストオブジェクトの作成
			DetailsChangeInfo detailsChangeInfo = new DetailsChangeInfo(order_id, product_id, product_name, category_name, product_price,
					topping_id, topping_name, topping_price, topping_quantity, topping_stock, subtotal);

			// トッピング情報の取得
			ToppingListDAO dao = new ToppingListDAO();
			List<ToppingInfo> toppingInfo = dao.selectToppingList(product_id);

			System.out.println("現在の画面：商品詳細画面(変更)");
			request.setAttribute(ServletUtil.Attr.DETAILS_CHANGE, detailsChangeInfo);
			request.setAttribute(ServletUtil.Attr.TOPPING, toppingInfo);

			// JSPに転送
			ServletUtil.forward(request, response, ServletUtil.Path.DETAILS);
		} catch (Exception e) {
			//ログに出力
			e.printStackTrace();

			//エラー画面に戻す
			ServletUtil.forwardError(request, response);
		}
	}
}
