package com.praveen.ecommerce.models;

public class PayInfo {

	private static String key = "XUvj0LgD";
	private static String serviceProvider = "payu_paisa";
	private static String surl = "http://test.fidgetcube.in/webs/payment/success";
	private static String furl = "http://test.fidgetcube.in/webs/payment/failure";

	private String hash;
	private String txnid;
	private String amount;
	private String firstname;
	private String email;
	private String phone;
	private String productinfo;


	public PayInfo(String hash, String txnid, String amount, String firstname, String email, String phone,
			String productinfo) {
		super();
		this.hash = hash;
		this.txnid = txnid;
		this.amount = amount;
		this.firstname = firstname;
		this.email = email;
		this.phone = phone;
		this.productinfo = productinfo;
	}

	public String getKey() {
		return key;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getTxnid() {
		return txnid;
	}

	public void setTxnid(String txnid) {
		this.txnid = txnid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProductinfo() {
		return productinfo;
	}

	public void setProductinfo(String productinIo) {
		this.productinfo = productinIo;
	}

	public String getSurl() {
		return surl;
	}

	public String getFurl() {
		return furl;
	}


	@Override
	public String toString() {
		return "PayInfo [key=" + key + ", hash=" + hash + ", txnid=" + txnid + ", amount=" + amount + ", firstname="
				+ firstname + ", email=" + email + ", phone=" + phone + ", productinIo=" + productinfo + ", surl="
				+ surl + ", furl=" + furl + ", serviceProvider=" + serviceProvider + "]";
	}

}
