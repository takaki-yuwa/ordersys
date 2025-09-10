package servlet.order;

import java.io.IOException;
import java.util.List;

import constants.Constants;
import dao.product.ProductListDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.product.ProductInfo;
import util.ServletUtil;

@WebServlet("/OrderMenu")
public class OrderMenuServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// キャッシュ制御ヘッダーを設定
			ServletUtil.cacheControl(response);

			// 情報を取得
			HttpSession session = request.getSession();
			String sessionNumber = (String) session.getAttribute(ServletUtil.Param.SESSION_ID);
			String tableNumber = (String) session.getAttribute(ServletUtil.Param.TABLE_ID);

			// tableNumberまたはsessionNumberがnullの場合はデフォルト値0を設定（またはエラー処理を行う）
			if (ServletUtil.isNullOrEmpty(tableNumber) || ServletUtil.isNullOrEmpty(sessionNumber)) {
				ServletUtil.forwardError(request, response);
				return;
			}

			// 商品リストを取得
			ProductListDAO dao = new ProductListDAO();
			List<ProductInfo> productInfo = dao.selectProductList();

			// 商品リストをリクエスト属性にセット
			request.setAttribute(ServletUtil.Attr.PRODUCT, productInfo);
			request.setAttribute(ServletUtil.Attr.CATEGORY, Constants.CATEGORY_LIST);

			System.out.println("現在の画面：メニュー画面");
			// 次のページ (OrderMenu.jsp) へフォワード
			ServletUtil.forward(request, response, ServletUtil.Path.MENU);
		} catch (Exception e) {
			//ログに出力
			e.printStackTrace();

			//エラー画面に戻す
			ServletUtil.forwardError(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			// キャッシュ制御ヘッダーを設定
			ServletUtil.cacheControl(response);

			//情報の取得
			String sessionId = request.getParameter(ServletUtil.Param.SESSION_ID);
			String tableId = request.getParameter(ServletUtil.Param.TABLE_ID);
			String sessionStatus = request.getParameter(ServletUtil.Param.SESSION_STATUS);

			//セッションに保存
			HttpSession session = request.getSession();
			session.setAttribute(ServletUtil.Param.SESSION_ID, sessionId);
			session.setAttribute(ServletUtil.Param.TABLE_ID, tableId);
			session.setAttribute(ServletUtil.Param.SESSION_STATUS, sessionStatus);

			// 更新後は一覧画面にリダイレクト（PRGパターン推奨）
			response.sendRedirect(request.getContextPath() + "/OrderMenu");

		} catch (Exception e) {
			//ログに出力
			e.printStackTrace();

			//エラー画面に戻す
			ServletUtil.forwardError(request, response);
		}
	}
}