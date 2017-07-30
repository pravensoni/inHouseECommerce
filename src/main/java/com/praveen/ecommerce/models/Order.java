package com.praveen.ecommerce.models;

import java.util.List;

public class Order {

	private int id;
	private String dispOrderId;
	private List<Product> products;
	private CustomerInfo customerInfo;
	private OrderStatus orderStatus;
	private PaymentType paymentType;
	private PaymentStatus paymentStatus;
	private int subTotal;
	private PayInfo payInfo;

	public PayInfo getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(PayInfo payInfo) {
		this.payInfo = payInfo;
	}

	public enum PaymentType {
		ONLINE, COD
	}

	public enum PaymentStatus {
		PENDING, IN_PROCESS, COMPLETED
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> product) {
		this.products = product;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}

	public int getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(int subTotal) {
		this.subTotal = subTotal;
	}

	public String getDispOrderId() {
		return dispOrderId;
	}

	public void setDispOrderId(String dispOrderId) {
		this.dispOrderId = dispOrderId;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", dispOrderId=" + dispOrderId + ", products=" + products + ", customerInfo="
				+ customerInfo + ", orderStatus=" + orderStatus + ", paymentType=" + paymentType + ", paymentStatus="
				+ paymentStatus + ", subTotal=" + subTotal + ", payInfo=" + payInfo + "]";
	}
	
	

}
