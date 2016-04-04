package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class EMessage extends EBaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5128302622252340701L;

	@Id
	private long id;

	@Column(columnDefinition = "TINYTEXT", nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "from_user", nullable = false)
	private EUser from;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "to_user", nullable = false)
	private EUser to;

	public EMessage() {
		super();
	}

	public EMessage(final String content, final EUser from, final EUser to) {
		this();
		this.content = content;
		this.from = from;
		this.to = to;
	}

	public void setFrom(final EUser from) {
		this.from = from;
	}

	public void setTo(final EUser to) {
		this.to = to;
	}

	public void setContent(final String content) {
		this.content = content;
	}

}
