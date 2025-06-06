package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/OrderList")
public class OrderList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// リストの初期化
		List<Integer> order_id = List.of(1, 2, 3, 4, 5, 6, 7);
		List<String> product_name = List.of("納豆お好み焼き", "ベビースターお好み焼き", "豚玉お好み焼き", "イカお好み焼き", "桜エビお好み焼き", "イカ桜エビお好み焼き",
				"梅しそお好み焼き");
		List<String> topping_name = List.of("コーン", "カレー", "チーズ", "もち", "ツナ", "ベビースター");
		List<Integer> product_price = List.of(660, 660, 800, 740, 740, 770, 740);
		List<Integer> topping_price = List.of(110, 110, 110, 110, 110, 110);
		List<Integer> topping_quantity = List.of(1, 2, 3, 4, 5, 6);
		List<Integer> menu_quantity = List.of(1, 1, 1, 1, 1, 1, 1);
		List<Integer> menu_stock = List.of(10, 20, 21, 22, 23, 24, 25);
		List<Integer> menu_subtotal = List.of(0, 0, 0, 0, 0, 0, 0);
		int menu_total = 0;

		order_list orderList = new order_list(order_id, product_name, topping_name, product_price, topping_price,
				topping_quantity, menu_quantity, menu_stock, menu_subtotal, menu_total);

		HttpSession session = request.getSession();

		session.setAttribute("orderList", orderList);

		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/JSP/OrderList.jsp");
		dispatcher.forward(request, response);

	}

}