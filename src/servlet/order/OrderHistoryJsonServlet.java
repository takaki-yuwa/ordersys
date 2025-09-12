package servlet.order;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;

import dao.order.OrderHistoryDAO;
import model.order.OrderHistoryInfo;
import util.ServletUtil;

@WebServlet("/api/historylist")
public class OrderHistoryJsonServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			response.setContentType("application/json; charset=UTF-8");

			// セッション取得
			HttpSession session = request.getSession();
			String sessionNumberStr = (String) session.getAttribute(ServletUtil.Param.SESSION_ID);

			if (ServletUtil.isNullOrEmpty(sessionNumberStr)) {
				ServletUtil.forwardError(request, response);
				return;
			}

			int sessionId = ServletUtil.parseOrForward(sessionNumberStr, request, response);

			OrderHistoryDAO dao = new OrderHistoryDAO();
			List<OrderHistoryInfo> orderHistoryInfo = dao.selectOrderHistory(sessionId);

			String json = new Gson().toJson(orderHistoryInfo);

			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher("/jsp/Error.jsp").forward(request, response);
		}
	}
}
