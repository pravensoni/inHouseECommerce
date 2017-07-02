package com.praveen.ecommerce.models;

public class OrderStatus {

	public enum OrderStatusEnum {
		ORDER_PLACED, READY_TO_BE_SHIPPED, DISPATCHED, DELIVERED, RETURN_INITIATED, RETURNED
	}

	private OrderStatusEnum status;
	private String descInternal;
	private String descCustomer;

	public OrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(OrderStatusEnum status) {
		this.status = status;
	}

	public String getDescInternal() {
		return descInternal;
	}

	public void setDescInternal(String descInternal) {
		this.descInternal = descInternal;
	}

	public String getDescCustomer() {
		return descCustomer;
	}

	public void setDescCustomer(String descCustomer) {
		this.descCustomer = descCustomer;
	}

}
