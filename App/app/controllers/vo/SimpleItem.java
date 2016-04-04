package controllers.vo;

import models.EItem;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleItem {

	@JsonProperty
	private final String ownerAccount;

	@JsonProperty
	private final String fullName;

	@JsonProperty
	private final String category;

	public SimpleItem(final EItem item) {
		this.ownerAccount = item.getOwner().getAccount();
		this.fullName = item.getOwner().getFullName();
		this.category = item.getCategory().getPublicName();
	}
}
