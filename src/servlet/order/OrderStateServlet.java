package servlet.order;

import java.io.IOException;

import dao.order.OrderStateDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.table.TableInfo;
import util.ServletUtil;

@WebServlet("/OrderState")
public class OrderStateServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// キャッシュ制御ヘッダーを設定
			ServletUtil.cacheControl(response);

			//卓番パラメータを取得
			String table_token = request.getParameter("tt");

			System.out.println("トークンの値：" + table_token);

			//状態の更新と情報の取得
			OrderStateDAO dao = new OrderStateDAO();
			dao.updateSession(table_token);
			TableInfo tableInfo = dao.selectTableInfo(table_token);

			System.out.println("セッションID：" + tableInfo.getSession_id());
			System.out.println("卓番ID：" + tableInfo.getTable_id() + "卓");
			System.out.println("セッションの状態：" + tableInfo.getSession_status());
			System.out.println("現在の画面：注文開始画面");

			//リクエスト属性にセット
			request.setAttribute(ServletUtil.Attr.TABLE, tableInfo);

			//次のページへフォワード
			ServletUtil.forward(request, response, ServletUtil.Path.STATE);

		} catch (Exception e) {

			System.err.println("セッションの状態がclosed、または接続中にエラーが発生しました: " + e.getMessage());
			//ログに出力
			e.printStackTrace();

			//エラー画面に戻す
			ServletUtil.forwardError(request, response);
		}
	}
}
