package com.praveen.ecommerce.models;

import java.util.List;

public class Order {

	private int id;
	private String Disp_order_id;
	List<Product> product;
	CustomerDetail customerDetail;
	OrderStatus orderStatus;
	PaymentType paymentType;
	PaymentStatus paymentStatus;
	
	public enum PaymentType{
		ONLINE,COD
	}
	public enum PaymentStatus{
		PENDING,IN_PROCESS,COMPLETED
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDisp_order_id() {
		return Disp_order_id;
	}

	public void setDisp_order_id(String disp_order_id) {
		Disp_order_id = disp_order_id;
	}

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}

	public CustomerDetail getCustomerDetail() {
		return customerDetail;
	}

	public void setCustomerDetail(CustomerDetail customerDetail) {
		this.customerDetail = customerDetail;
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

	
}
