package filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.ServletUtil;

@WebFilter(urlPatterns = { "/jsp/*", "/servlet/*" })
public class SessionFilter implements Filter {

	private static final Logger logger = Logger.getLogger(SessionFilter.class.getName());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		try {
			HttpSession session = req.getSession(false);

			// 注文開始画面とメニュー画面のURIを取得
			String orderStartURI = req.getContextPath() + "/" + ServletUtil.Action.STATE;
			String orderMenuURI = req.getContextPath() + "/" + ServletUtil.Action.MENU;

			// セッションにSESSION_IDが存在するか
			boolean sessionValid = (session != null && session.getAttribute(ServletUtil.Param.SESSION_ID) != null);

			// 注文開始画面、メニュー画面、または静的リソースの場合は通過
			boolean allowedPage = req.getRequestURI().equals(orderStartURI)
					|| req.getRequestURI().equals(orderMenuURI);

			boolean isStaticResource = req.getRequestURI().matches(".*(\\.css|\\.js|\\.png|\\.jpg|\\.gif|\\.svg|\\.ico|\\.woff2?)$");

			if (sessionValid || allowedPage || isStaticResource) {
				chain.doFilter(request, response); // 通過
			} else {
				// セッションなしでアクセス不可ページの場合はエラー画面へ
				logger.info("セッションが無効です。アクセス拒否: " + req.getRequestURI());
				ServletUtil.forwardError(req, res);
			}

		} catch (Exception e) {
			logger.severe("SessionFilterで予期しない例外が発生しました: " + e.getMessage());
			e.printStackTrace();
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "内部エラーが発生しました");
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		// 初期化処理が必要な場合はここに記述
	}

	@Override
	public void destroy() {
		// 終了処理が必要な場合はここに記述
	}
}