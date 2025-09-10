package servlet.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.order.OrderListInfo;
import util.ServletUtil;

@WebServlet("/OrderRemove")
public class OrderRemoveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// キャッシュ制御ヘッダーを設定
			ServletUtil.cacheControl(response);

			HttpSession session = request.getSession();
			List<OrderListInfo> orderList = (List<OrderListInfo>) session.getAttribute(ServletUtil.Attr.LIST);
			int order_id = Integer.valueOf(request.getParameter(ServletUtil.Param.ORDER_ID));
			//削除対象のデバッグ確認
			System.out.println("削除対象：" + order_id + "目の注文");
			if (null == orderList) {
				orderList = new ArrayList<>();
			}
			//セッションから対象のorder_idを削除する
			if (orderList != null) {

				Iterator<OrderListInfo> iterator = orderList.iterator();
				while (iterator.hasNext()) {
					OrderListInfo order = iterator.next();
					if (order.getOrder_id() == order_id) {
						iterator.remove();
						break;
					}
				}

				session.setAttribute(ServletUtil.Attr.LIST, orderList);
			}

			response.sendRedirect(request.getContextPath() + ServletUtil.Path.LIST);
		} catch (Exception e) {
			e.printStackTrace();
			ServletUtil.forwardError(request, response);
		}
	}
}
