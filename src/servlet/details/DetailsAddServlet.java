package servlet.details;

import java.io.IOException;
import java.util.List;

import dao.topping.ToppingListDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.details.DetailsAddInfo;
import model.topping.ToppingInfo;
import util.ServletUtil;

@WebServlet("/DetailsAdd")
public class DetailsAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// キャッシュ制御ヘッダーを設定
			ServletUtil.cacheControl(response);

			// 製品情報のパラメータを取得
			String p_id = request.getParameter(ServletUtil.Param.PRODUCT_ID);
			String name = request.getParameter(ServletUtil.Param.PRODUCT_NAME);
			String category = request.getParameter(ServletUtil.Param.CATEGORY_NAME);
			String p_price = request.getParameter(ServletUtil.Param.PRODUCT_PRICE);
			String p_stock = request.getParameter(ServletUtil.Param.PRODUCT_STOCK);

			if (ServletUtil.isNullOrEmpty(p_id)) {
				ServletUtil.forwardError(request, response);
				return;
			}

			//それぞれの情報をint型に変換する
			int id = ServletUtil.parseOrForward(p_id, request, response);
			int price = ServletUtil.parseOrDefault(p_price, 0);
			int stock = ServletUtil.parseOrDefault(p_stock, 0);

			// 製品リストオブジェクトの作成
			DetailsAddInfo detailsAddInfo = new DetailsAddInfo(id, name, category, price, stock);

			// トッピング情報の取得
			ToppingListDAO dao = new ToppingListDAO();
			List<ToppingInfo> toppingInfo = dao.selectToppingList(id);

			System.out.println("現在の画面：商品詳細画面(追加)");
			request.setAttribute(ServletUtil.Attr.DETAILS_ADD, detailsAddInfo);
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
