package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.ebean.Model;


/**
 * The persistent class for the wd_staffs database table.
 * 
 */
@Entity
@Table(name="wd_staffs")
@NamedQuery(name="WdStaff.findAll", query="SELECT w FROM WdStaff w")
public class WdStaff extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String fullname;

	private int points;

	@Column(name="qist_sku")
	private String qistSku;

	@Column(name="sku_postfix")
	private int skuPostfix;

	//bi-directional many-to-one association to WdRetailer
	@OneToMany(mappedBy="wdStaff")
	private List<WdRetailer> wdRetailers;

	//bi-directional many-to-one association to WdUser
	@ManyToOne
	@JoinColumn(name="user_id")
	private WdUser wdUser;

	public WdStaff() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public int getPoints() {
		return this.points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getQistSku() {
		return this.qistSku;
	}

	public void setQistSku(String qistSku) {
		this.qistSku = qistSku;
	}

	public int getSkuPostfix() {
		return this.skuPostfix;
	}

	public void setSkuPostfix(int skuPostfix) {
		this.skuPostfix = skuPostfix;
	}

	public List<WdRetailer> getWdRetailers() {
		return this.wdRetailers;
	}

	public void setWdRetailers(List<WdRetailer> wdRetailers) {
		this.wdRetailers = wdRetailers;
	}

	public WdRetailer addWdRetailer(WdRetailer wdRetailer) {
		getWdRetailers().add(wdRetailer);
		wdRetailer.setWdStaff(this);

		return wdRetailer;
	}

	public WdRetailer removeWdRetailer(WdRetailer wdRetailer) {
		getWdRetailers().remove(wdRetailer);
		wdRetailer.setWdStaff(null);

		return wdRetailer;
	}

	public WdUser getWdUser() {
		return this.wdUser;
	}

	public void setWdUser(WdUser wdUser) {
		this.wdUser = wdUser;
	}

}