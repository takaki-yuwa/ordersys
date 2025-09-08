package model.accounting;

public class AccountingInfo {
	private String tableNo;
	private String totalPrice;

	public AccountingInfo(String strTableNo, String strTotalPrice) {
		this.setTableNo(strTableNo);
		this.setTotalPrice(strTotalPrice);
	}

	public String getTableNo() {
		return tableNo;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
}
