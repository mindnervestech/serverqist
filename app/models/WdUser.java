package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.ebean.Model;


/**
 * The persistent class for the wd_users database table.
 * 
 */
@Entity
@Table(name="wd_users")
@NamedQuery(name="WdUser.findAll", query="SELECT w FROM WdUser w")
public class WdUser extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_on")
	private Date createdOn;

	private String email;

	@Lob
	private String password;

	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_on")
	private Date updateOn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_active")
	private Date lastActive;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="approved_date")
	private Date approvedDate;

	@Column(name="user_type")
	private String userType;

	//bi-directional many-to-one association to WdRetailer
	@OneToMany(mappedBy="wdUser")
	private List<WdRetailer> wdRetailers;

	//bi-directional many-to-one association to WdStaff
	@OneToMany(mappedBy="wdUser")
	private List<WdStaff> wdStaffs;

	public WdUser() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdateOn() {
		return this.updateOn;
	}

	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<WdRetailer> getWdRetailers() {
		return this.wdRetailers;
	}

	public void setWdRetailers(List<WdRetailer> wdRetailers) {
		this.wdRetailers = wdRetailers;
	}

	public WdRetailer addWdRetailer(WdRetailer wdRetailer) {
		getWdRetailers().add(wdRetailer);
		wdRetailer.setWdUser(this);

		return wdRetailer;
	}

	public WdRetailer removeWdRetailer(WdRetailer wdRetailer) {
		getWdRetailers().remove(wdRetailer);
		wdRetailer.setWdUser(null);

		return wdRetailer;
	}

	public List<WdStaff> getWdStaffs() {
		return this.wdStaffs;
	}

	public void setWdStaffs(List<WdStaff> wdStaffs) {
		this.wdStaffs = wdStaffs;
	}

	public WdStaff addWdStaff(WdStaff wdStaff) {
		getWdStaffs().add(wdStaff);
		wdStaff.setWdUser(this);

		return wdStaff;
	}

	public WdStaff removeWdStaff(WdStaff wdStaff) {
		getWdStaffs().remove(wdStaff);
		wdStaff.setWdUser(null);

		return wdStaff;
	}

	public Date getLastActive() {
		return lastActive;
	}

	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	
}