package com.praveen.ecommerce.models;

import java.util.List;

public class Product {

	private Long id;
	private String title;
	private double price;
	private double OrigPrice;
	private String description;
	List<String> imageLinks;
	List<Variant> variants;
	private int qty;
	private int inStock;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getOrigPrice() {
		return OrigPrice;
	}

	public void setOrigPrice(double origPrice) {
		OrigPrice = origPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getImageLinks() {
		return imageLinks;
	}

	public void setImageLinks(List<String> imageLinks) {
		this.imageLinks = imageLinks;
	}

	public List<Variant> getVariants() {
		return variants;
	}

	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", title=" + title + ", price=" + price + ", OrigPrice=" + OrigPrice
				+ ", description=" + description + ", imageLinks=" + imageLinks + ", variants=" + variants + ", qty="
				+ qty + ", inStock=" + inStock + "]";
	}

}
