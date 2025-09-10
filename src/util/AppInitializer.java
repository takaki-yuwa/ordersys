package util;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// Action を Map 化
		Map<String, String> actionMap = new HashMap<>();
		actionMap.put("STATE", ServletUtil.Action.STATE);
		actionMap.put("MENU", ServletUtil.Action.MENU);
		actionMap.put("DETAILS_ADD", ServletUtil.Action.DETAILS_ADD);
		actionMap.put("DETAILS_CHANGE", ServletUtil.Action.DETAILS_CHANGE);
		actionMap.put("LIST", ServletUtil.Action.LIST);
		actionMap.put("REMOVE", ServletUtil.Action.REMOVE);
		actionMap.put("COMPLETED", ServletUtil.Action.COMPLETED);
		actionMap.put("HISTORY", ServletUtil.Action.HISTORY);
		actionMap.put("ACCOUNTING", ServletUtil.Action.ACCOUNTING);

		// Param を Map 化
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("FORM", ServletUtil.Param.FORM);
		paramMap.put("SESSION_ID", ServletUtil.Param.SESSION_ID);
		paramMap.put("TABLE_ID", ServletUtil.Param.TABLE_ID);
		paramMap.put("SESSION_STATUS", ServletUtil.Param.SESSION_STATUS);
		paramMap.put("GUEST_COUNT", ServletUtil.Param.GUEST_COUNT);
		paramMap.put("ORDER_ID", ServletUtil.Param.ORDER_ID);
		paramMap.put("ORDER_ID_ATTR", ServletUtil.Param.ORDER_ID_ATTR);
		paramMap.put("PRODUCT_ID", ServletUtil.Param.PRODUCT_ID);
		paramMap.put("PRODUCT_NAME", ServletUtil.Param.PRODUCT_NAME);
		paramMap.put("CATEGORY_NAME", ServletUtil.Param.CATEGORY_NAME);
		paramMap.put("PRODUCT_PRICE", ServletUtil.Param.PRODUCT_PRICE);
		paramMap.put("PRODUCT_STOCK", ServletUtil.Param.PRODUCT_STOCK);
		paramMap.put("PRODUCT_ID_ATTR", ServletUtil.Param.PRODUCT_ID_ATTR);
		paramMap.put("PRODUCT_QUANTITY_ATTR", ServletUtil.Param.PRODUCT_QUANTITY_ATTR);
		paramMap.put("TOPPING_ID", ServletUtil.Param.TOPPING_ID);
		paramMap.put("TOPPING_NAME", ServletUtil.Param.TOPPING_NAME);
		paramMap.put("TOPPING_PRICE", ServletUtil.Param.TOPPING_PRICE);
		paramMap.put("TOPPING_QUANTITY", ServletUtil.Param.TOPPING_QUANTITY);
		paramMap.put("TOPPING_ID_ATTR", ServletUtil.Param.TOPPING_ID_ATTR);
		paramMap.put("TOPPING_NAME_ATTR", ServletUtil.Param.TOPPING_NAME_ATTR);
		paramMap.put("TOPPING_PRICE_ATTR", ServletUtil.Param.TOPPING_PRICE_ATTR);
		paramMap.put("TOPPING_QUANTITY_ATTR", ServletUtil.Param.TOPPING_QUANTITY_ATTR);
		paramMap.put("TOPPING_ID_", ServletUtil.Param.TOPPING_ID_);
		paramMap.put("TOPPING_QUANTITY_", ServletUtil.Param.TOPPING_QUANTITY_);
		paramMap.put("SUBTOTAL", ServletUtil.Param.SUBTOTAL);
		paramMap.put("SUBTOTAL_ATTR", ServletUtil.Param.SUBTOTAL_ATTR);
		paramMap.put("TOTAL", ServletUtil.Param.TOTAL);

		Map<String, String> valueMap = new HashMap<>();
		valueMap.put("STATE", ServletUtil.Value.STATE);
		valueMap.put("SKIP_STATE", ServletUtil.Value.SKIP_STATE);

		Map<String, String> statusMap = new HashMap<>();
		statusMap.put("ACTIVE", ServletUtil.Status.ACTIVE);
		statusMap.put("INACTIVE", ServletUtil.Status.INACTIVE);
		statusMap.put("CLOSED", ServletUtil.Status.CLOSED);

		// ApplicationScope に登録
		sce.getServletContext().setAttribute("Action", actionMap);
		sce.getServletContext().setAttribute("Param", paramMap);
		sce.getServletContext().setAttribute("Value", valueMap);
		sce.getServletContext().setAttribute("Status", statusMap);
	}
}
