package servlet.accounting;

import java.io.IOException;

import dao.accounting.AccountingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.accounting.AccountingInfo;
import util.ServletUtil;

@WebServlet("/Accounting")
public class AccountingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// POSTリクエストを処理
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// セッションを取得
			HttpSession session = request.getSession();

			// パラメータの取得
			String sessionNumberStr = (String) session.getAttribute(ServletUtil.Param.SESSION_ID);
			String strTableNo = request.getParameter(ServletUtil.Param.TABLE_ID); // テーブル番号
			String strTotalPrice = request.getParameter(ServletUtil.Param.TOTAL); // 総額

			// 総額を整数に変換
			int sessionId = ServletUtil.parseOrDefault(sessionNumberStr, 0);
			int totalPrice = ServletUtil.parseOrDefault(strTotalPrice, 0);
			int tableNo = ServletUtil.parseOrForward(strTableNo, request, response);

			// AccountingDAOのインスタンスを作成
			AccountingDAO accountingDAO = new AccountingDAO();

			// 会計情報を挿入し、関連するorder_detailsを処理
			accountingDAO.processAccountingData(totalPrice, sessionId);

			// accounting_list オブジェクトを作成
			AccountingInfo accountingInfo = new AccountingInfo(strTableNo, strTotalPrice);

			// 卓番セッション
			accountingDAO.processSession(tableNo, sessionNumberStr);
			System.out.println("現在の画面：会計確定画面");

			// セッションの取得と情報削除
			request.getSession().invalidate();

			// JSPへフォワード
			request.setAttribute(ServletUtil.Attr.ACCOUNTING, accountingInfo);
			ServletUtil.forward(request, response, ServletUtil.Path.ACCOUNTING);
		} catch (Exception e) {
			//ログに出力
			e.printStackTrace();

			//エラー画面に戻す
			ServletUtil.forwardError(request, response);
		}
	}
}
