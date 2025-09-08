package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import dao.order.OrderCompletedDAO;
import util.DBUtil;

public class OrderService {

	private final OrderCompletedDAO dao = new OrderCompletedDAO();

	public boolean completedOrder(String[] productIds, String[] productQuantities, String[] productPrices, String[][] toppingIds,
			String[][] toppingQuantities, int sessionId) {
		Connection connection = null;
		try {
			connection = DBUtil.getConnection();
			connection.setAutoCommit(false); // トランザクション開始

			// 1. 商品詳細を登録
			List<Integer> orderIds = dao.insertProductDetails(connection, productIds);

			// 2. 注文詳細を登録
			boolean orderInserted = dao.insertOrderDetails(
					connection,
					orderIds.stream().map(String::valueOf).toArray(String[]::new),
					productQuantities,
					productPrices,
					sessionId);

			// 3. トッピング詳細を登録
			boolean toppingInserted = dao.insertToppingDetails(
					connection, toppingIds, toppingQuantities,
					orderIds.stream().map(String::valueOf).toArray(String[]::new));

			// 4. 在庫を更新
			dao.updateProductStock(connection, productIds, productQuantities);
			dao.updateToppingStock(
					connection,
					Arrays.stream(toppingIds).flatMap(Arrays::stream).toArray(String[]::new),
					Arrays.stream(toppingQuantities).flatMap(Arrays::stream).toArray(String[]::new));

			// すべて成功したら commit
			connection.commit();
			return orderInserted && toppingInserted;

		} catch (Exception e) {
			e.printStackTrace();
			if (connection != null) {
				try {
					connection.rollback(); // エラーならロールバック
				} catch (SQLException rollbackEx) {
					rollbackEx.printStackTrace();
				}
			}
			return false;
		} finally {
			if (connection != null) {
				try {
					connection.close(); // 必ず閉じる
				} catch (SQLException closeEx) {
					closeEx.printStackTrace();
				}
			}
		}
	}
}
