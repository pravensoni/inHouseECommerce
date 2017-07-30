package com.praveen.ecommerce.models;

public class PayInfo {
	private String key;
	private String hash;
	private String txnid;
	private String amount;
	private String firstname;
	private String email;
	private String phone;
	private String productinfo;
	private String surl;
	private String furl;
	private String serviceProvider;
	


	public PayInfo(String key, String hash, String txnid, String amount, String firstname, String email, String phone,
			String productinfo, String surl, String furl, String serviceProvider) {
		super();
		this.key = key;
		this.hash = hash;
		this.txnid = txnid;
		this.amount = amount;
		this.firstname = firstname;
		this.email = email;
		this.phone = phone;
		this.productinfo = productinfo;
		this.surl = surl;
		this.furl = furl;
		this.serviceProvider = serviceProvider;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public void setSurl(String surl) {
		this.surl = surl;
	}

	public String getFurl() {
		return furl;
	}

	public void setFurl(String furl) {
		this.furl = furl;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	@Override
	public String toString() {
		return "PayInfo [key=" + key + ", hash=" + hash + ", txnid=" + txnid + ", amount=" + amount + ", firstname="
				+ firstname + ", email=" + email + ", phone=" + phone + ", productinIo=" + productinfo + ", surl="
				+ surl + ", furl=" + furl + ", serviceProvider=" + serviceProvider + "]";
	}
	
	

}
