package dao.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.product.ProductInfo;
import util.DBUtil;

public class ProductListDAO {
	public List<ProductInfo> selectProductList() {
		List<ProductInfo> productInfoList = new ArrayList<>();
		String selectProductSql = "SELECT * FROM product WHERE product_delete_flag = 1";
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement selectStmt = connection.prepareStatement(selectProductSql)) {
			try (ResultSet resultSet = selectStmt.executeQuery()) {
				while (resultSet.next()) {
					int product_id = resultSet.getInt("product_id");
					String product_name = resultSet.getString("product_name");
					String category_name = resultSet.getString("category_name");
					int product_price = resultSet.getInt("product_price");
					int product_stock = resultSet.getInt("product_stock");
					int product_display_flag = resultSet.getInt("product_display_flag");
					productInfoList.add(new ProductInfo(product_id, product_name, category_name, product_price, product_stock, product_display_flag));
				}
			}

		} catch (SQLException e) {
			System.err.println("データベースから商品情報の取得中にエラーが発生しました。");
			System.err.println("商品情報取得中にSQLエラーが発生しました: " + e.getMessage());
			System.err.println("SQL状態コード: " + e.getSQLState());
			System.err.println("エラーコード: " + e.getErrorCode());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("商品情報取得中に予期しないエラーが発生しました。");
			e.printStackTrace();
		}

		return productInfoList;
	}
}
