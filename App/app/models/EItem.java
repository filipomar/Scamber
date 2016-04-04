package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Expr;

@Entity
public class EItem extends EBaseModel {
	private static final String CATEGORY_COLUMN = "category";
	static final String OWNER_COLUMN = "owner";
	private static final String USER_EMAIL_COLUMN = OWNER_COLUMN + "." + EUser.ACCOUNT_COLUMN_NAME;
	private static final String VIEWS_COLUMN = "views";
	private static final String VIEWS_VIEWER_PATH = VIEWS_COLUMN + "." + EItemView.VIEWER_COLUMN;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4568247072164226506L;

	@Id
	private long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, columnDefinition = "TINYTEXT")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = OWNER_COLUMN, nullable = false)
	private EUser owner;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = CATEGORY_COLUMN, nullable = false)
	private EItemCategory category;

	@Column(nullable = false)
	private String photoOne;

	@Column(nullable = true)
	private String photoTwo;

	@Column(nullable = true)
	private String photoThree;

	@Column(nullable = true)
	private String photoFour;

	@Column(nullable = true)
	private String photoFive;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = EItemView.VIEWED_COLUMN)
	private Set<EItemView> views;

	public EItem(final String name, final String description, final EUser owner, final EItemCategory category, final String photoOne) {
		this();
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.category = category;
		this.photoOne = photoOne;
	}

	public EItem() {
		super();
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setOwner(final EUser owner) {
		this.owner = owner;
	}

	public void setCategory(final EItemCategory category) {
		this.category = category;
	}

	public void setPhotoOne(final String photoOne) {
		this.photoOne = photoOne;
	}

	public void setPhotoTwo(final String photoTwo) {
		this.photoTwo = photoTwo;
	}

	public void setPhotoThree(final String photoThree) {
		this.photoThree = photoThree;
	}

	public void setPhotoFour(final String photoFour) {
		this.photoFour = photoFour;
	}

	public void setPhotoFive(final String photoFive) {
		this.photoFive = photoFive;
	}

	public String getDescription() {
		return this.description;
	}

	public EItemCategory getCategory() {
		return this.category;
	}

	public EUser getOwner() {
		return this.owner;
	}

	private static Finder<String, EItem> FINDER = new Finder<String, EItem>(String.class, EItem.class);

	/**
	 * Products that have never been seen, Products that are not from the user
	 * 
	 * @param account
	 * @return
	 */
	public static List<EItem> findNewItems(final String account) {
		final List<EItem> items = FINDER.fetch(VIEWS_COLUMN).fetch(VIEWS_VIEWER_PATH).fetch(CATEGORY_COLUMN).where().not(Expr.eq(USER_EMAIL_COLUMN, account))
				.findList();
		final List<EItem> nonViewed = new ArrayList<EItem>();

		for (final EItem item : items) {
			if (!item.wasViewedBy(account)) {
				nonViewed.add(item);
			}
		}

		return nonViewed;
	}

	private boolean wasViewedBy(final String account) {
		for (final EItemView view : this.views) {
			if (view.getViewer().getAccount().equals(account)) {
				return true;
			}
		}

		return false;
	}

}
