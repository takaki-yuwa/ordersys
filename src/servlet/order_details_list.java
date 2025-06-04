package servlet;

import java.util.ArrayList;
import java.util.List;

public class order_details_list {
	// 注文詳細
    private int order_details_id;
    private int order_id;
    private int product_quantity;
    private int order_price;
    private int table_number;
    private int accounting_flag;
    // 商品詳細
    private String product_name;
    private int product_price;
    private int product_id;
    // トッピング
    private List<Integer> topping_id;
    private List<Integer> topping_quantity;
    private List<String> topping_name;
    private List<Integer> topping_price;

    public order_details_list(int order_details_id, int order_id, int product_quantity, int order_price, int table_number, int accounting_flag,
    		String name, Integer price) {
    	this.order_details_id = order_details_id;
    	this.order_id = order_id;
        this.product_quantity = product_quantity;
        this.order_price = order_price;
        this.table_number = table_number;
        this.accounting_flag = accounting_flag;
    	this.product_name = name;
        this.product_price = price;
    }
    public void topping_list(List<String> name, List<Integer> price, List<Integer> topping_quantity) {
    	this.topping_name = new ArrayList<>(name);
        this.topping_price = new ArrayList<>(price);
        this.topping_quantity = new ArrayList<>(topping_quantity);
    }
    public int getorder_details_id() {
        return order_details_id;
    }
    
    public int getorder_id() {
        return order_id;
    }

    public int getproduct_quantity() {
        return product_quantity;
    }
    
    public int getorder_price() {
        return order_price;
    }

    public int gettable_number() {
        return table_number;
    }

    public String getProduct_name() {
        return product_name;
    }

    public Integer getProduct_price() {
        return product_price;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public List<String> getTopping_name() {
        return topping_name;
    }

    public List<Integer> getTopping_price() {
        return topping_price;
    }

    public List<Integer> getTopping_id() {
        return topping_id;
    }

    public List<Integer> getTopping_quantity() {
        return topping_quantity;
    }
}
