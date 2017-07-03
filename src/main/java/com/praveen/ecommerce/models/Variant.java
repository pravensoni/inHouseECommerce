package com.praveen.ecommerce.models;

import java.util.List;

public class Variant {

	public enum VariantType {
		COLOR, SIZE
	}

	private long id;
	private VariantType variantType;
	private String variantName;
	private List<String> imageLink;

	public VariantType getVariantType() {
		return variantType;
	}

	public void setVariantType(VariantType variantType) {
		this.variantType = variantType;
	}

	public String getVariantName() {
		return variantName;
	}

	public void setVariantName(String variantName) {
		this.variantName = variantName;
	}

	public List<String> getImageLink() {
		return imageLink;
	}

	public void setImageLink(List<String> imageLink) {
		this.imageLink = imageLink;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
