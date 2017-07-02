package com.praveen.ecommerce.models;

public class Variant {

	public enum VariantType {
		COLOUR, SIZE
	}

	private VariantType variantType;
	private String variantName;
	private String imageLink;

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

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

}
