package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EItemCategory extends EBaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2582565467931211572L;

	@Id
	private String internalName;

	@Column(nullable = false)
	private String publicName;

	public EItemCategory() {
		super();
	}

	public EItemCategory(final String internalName) {
		this();
		this.internalName = internalName;
	}

	public void setInternalName(final String internalName) {
		this.internalName = internalName;
	}

	public void setPublicName(final String publicName) {
		this.publicName = publicName;
	}

	public String getPublicName() {
		return this.publicName;
	}

}
