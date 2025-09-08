package servlet.order;

import java.io.IOException;
import java.util.List;

import dao.order.OrderHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.order.OrderHistoryInfo;
import util.ServletUtil;

@WebServlet("/OrderHistory")
public class OrderHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// キャッシュ制御
			ServletUtil.cacheControl(response);

			// セッション取得
			HttpSession session = request.getSession();
			String sessionNumberStr = (String) session.getAttribute(ServletUtil.Param.SESSION_ID);

			if (ServletUtil.isNullOrEmpty(sessionNumberStr)) {
				ServletUtil.forwardError(request, response);
				return;
			}

			int sessionId = ServletUtil.parseOrForward(sessionNumberStr, request, response);

			// 注文履歴取得
			OrderHistoryDAO dao = new OrderHistoryDAO();
			List<OrderHistoryInfo> orderHistoryInfo = dao.selectOrderHistory(sessionId);

			if (orderHistoryInfo == null || orderHistoryInfo.isEmpty()) {
				System.out.println("注文履歴が見つかりませんでした。(sessionId=" + sessionId + ")");
			}

			// JSPへフォワード
			request.setAttribute(ServletUtil.Attr.HISTORY, orderHistoryInfo);
			System.out.println("現在の画面：注文履歴画面");
			ServletUtil.forward(request, response, ServletUtil.Path.HISTORY);

		} catch (Exception e) {
			e.printStackTrace();
			ServletUtil.forwardError(request, response);
		}
	}
}