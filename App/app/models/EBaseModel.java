package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import play.db.ebean.Model;

@MappedSuperclass
public class EBaseModel extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2563677980099255897L;

	@Column(nullable = false)
	private Date lastUpdatedOn;

	@Column(nullable = false)
	private Date createdOn;

	private void refreshLastUpdated() {
		this.lastUpdatedOn = new Date();
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public Date getLastUpdatedOn() {
		return this.lastUpdatedOn;
	}

	@Override
	public void save() {
		this.createdOn = new Date();
		this.refreshLastUpdated();

		super.save();
	}

	@Override
	public void update() {
		this.refreshLastUpdated();
		super.update();
	}

	protected EBaseModel() {
		super();
	}
}
