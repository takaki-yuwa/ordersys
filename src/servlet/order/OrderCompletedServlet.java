package servlet.order;

import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.OrderService;
import util.ServletUtil;

@WebServlet("/OrderCompleted")
public class OrderCompletedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final OrderService orderService = new OrderService();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			//キャッシュ制御
			ServletUtil.cacheControl(response);

			// パラメータの取得
			String[] order_ids = request.getParameterValues(ServletUtil.Param.ORDER_ID_ATTR);
			String[] product_id = request.getParameterValues(ServletUtil.Param.PRODUCT_ID_ATTR);
			String[] product_quantity = request.getParameterValues(ServletUtil.Param.PRODUCT_QUANTITY_ATTR);
			String[][] topping_ids = new String[order_ids.length][];
			String[][] topping_quantities = new String[order_ids.length][];
			String[] order_price = request.getParameterValues(ServletUtil.Param.SUBTOTAL_ATTR);
			for (int i = 0; i < order_ids.length; i++) {
				topping_ids[i] = request.getParameterValues(ServletUtil.Param.TOPPING_ID_ + i);
				String[] topping_quantity = request.getParameterValues(ServletUtil.Param.TOPPING_QUANTITY_ + i);

				if (topping_quantity != null) {
					topping_quantities[i] = Arrays.copyOf(topping_quantity, topping_quantity.length);
				} else {
					topping_quantities[i] = new String[0]; // トッピングなしの場合は空配列
				}

				if (topping_ids[i] == null) {
					topping_ids[i] = new String[0]; // トッピングが存在しない場合も空配列
				}
			}

			HttpSession session = request.getSession();

			String sessionNumber = (String) session.getAttribute(ServletUtil.Param.SESSION_ID);
			int sessionId = 0;
			if (!ServletUtil.isNullOrEmpty(sessionNumber)) {
				try {
					sessionId = Integer.parseInt(sessionNumber);
				} catch (NumberFormatException e) {
					System.out.println("無効な数値: " + sessionNumber);
					ServletUtil.forwardError(request, response, "無効な数値が入力されました");
					throw new IOException("数値変換エラー");
				}
			} else {
				ServletUtil.forwardError(request, response, "セッションIDが存在しません");
				return;
			}

			boolean success = orderService.completedOrder(product_id, product_quantity, order_price, topping_ids, topping_quantities, sessionId);

			if (success) {
				System.out.println("現在の画面：注文完了画面");
				ServletUtil.forward(request, response, ServletUtil.Path.COMPLETED);
			} else {
				ServletUtil.forwardError(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			ServletUtil.forwardError(request, response);
		}
	}
}
