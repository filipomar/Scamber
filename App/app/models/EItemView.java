package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class EItemView extends EBaseModel {

	static final String VIEWER_COLUMN = "viewer";

	static final String VIEWED_COLUMN = "viewed";

	/**
	 * 
	 */
	private static final long serialVersionUID = -5128302622252340701L;

	@Id
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = VIEWER_COLUMN, nullable = false)
	private EUser viewer;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = VIEWED_COLUMN, nullable = false)
	private EItem viewed;

	@Column
	private boolean liked;

	public void setLiked(final boolean liked) {
		this.liked = liked;
	}

	public void setViewed(final EItem viewed) {
		this.viewed = viewed;
	}

	public void setViewer(final EUser viewer) {
		this.viewer = viewer;
	}

	public EUser getViewer() {
		return this.viewer;
	}

	public EItemView() {
		super();
	}

	public EItemView(final EUser liker, final EItem item) {
		super();
		this.viewer = liker;
		this.viewed = item;
	}

}
