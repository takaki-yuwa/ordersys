package servlet.order;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.product.ProductListDAO;
import model.product.ProductInfo;

@WebServlet("/api/productlist")
public class OrderMenuJsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("application/json; charset=UTF-8");

            ProductListDAO dao = new ProductListDAO();
            List<ProductInfo> productInfo = dao.selectProductList();

            String json = new Gson().toJson(productInfo);

            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();

        } catch (Exception e) {
        	e.printStackTrace();
			request.getRequestDispatcher("/jsp/Error.jsp").forward(request, response);
        }
    }
}
