package models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import utils.helpers.MD5HashHelper;

@Entity
public class EUser extends EBaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4072030810149295702L;

	static final String ACCOUNT_COLUMN_NAME = "account";
	private static final String PASSWORD_COLUMN_NAME = "password";

	@Id
	@Column(name = ACCOUNT_COLUMN_NAME)
	private String account;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, name = PASSWORD_COLUMN_NAME)
	private String password;

	@Column(nullable = false)
	private String fullName;

	@Column(nullable = false, unique = true)
	private String picture;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = EItem.OWNER_COLUMN)
	private Set<EItem> items;

	public EUser() {
		super();
	}

	public void setAccount(final String account) {
		this.account = account;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public String getAccount() {
		return this.account;
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getPicture() {
		return this.picture;
	}

	/**
	 * @param password
	 *            RAW
	 */
	public void setPassword(final String password) {
		this.password = MD5HashHelper.hash(password);
	}

	private static Finder<String, EUser> FINDER = new Finder<String, EUser>(String.class, EUser.class);

	/**
	 * 
	 * @param account
	 * @param password
	 *            RAW
	 * @return
	 */
	public static boolean exists(final String account, final String password) {
		return FINDER.where().eq(ACCOUNT_COLUMN_NAME, account).where().eq(PASSWORD_COLUMN_NAME, MD5HashHelper.hash(password)).findRowCount() > 0;
	}

	public static boolean exists(final String account) {
		return FINDER.where().eq(ACCOUNT_COLUMN_NAME, account).findRowCount() > 0;
	}

	public static EUser findByAccount(final String account) {
		return FINDER.where().eq(ACCOUNT_COLUMN_NAME, account).findUnique();
	}
}
