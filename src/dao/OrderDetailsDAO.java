package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import servlet.order_details_list;

public class OrderDetailsDAO {

    public List<order_details_list> findOrderHistory(int search_table_number, int search_order_price) {
        List<order_details_list> orderDetailsList = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT order_details_id, order_id, product_quantity, order_price, table_number, accounting_flag FROM order_details where table_number = " + search_table_number + " AND order_price = " + search_order_price))
            {

            while (rs.next()) {
            	int order_details_id = rs.getInt("order_details_id");
                int order_id = rs.getInt("order_id");
                int product_quantity = rs.getInt("product_quantity");
                int order_price = rs.getInt("order_price");
                int table_number = rs.getInt("table_number");
                int accounting_flag = rs.getInt("accounting_flag");

                // 商品詳細テーブル取得
                ResultSet rs_product_details = st.executeQuery("SELECT product_id FROM product_details where order_id = " + order_id);
                if (rs_product_details.next()) {
                    int product_id = rs_product_details.getInt("product_id");

                    // 商品テーブル取得
                    ResultSet rs_products = st.executeQuery("SELECT product_name, product_price FROM product where product_id = " + product_id);
                    if (rs_products.next()) {
                        String product_name = rs_products.getString("product_name");
                        int product_price = rs_products.getInt("product_price");

                        orderDetailsList.add(new order_details_list(order_details_id, order_id, product_quantity, order_price, table_number, accounting_flag,
                                product_name, product_price));
                    }
                }
            }
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetailsList;
    }
}
