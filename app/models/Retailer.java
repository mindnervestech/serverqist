package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class Retailer extends Model{

	@Id
	public Long id;
	public String companyName;
	public String username;
	public String lcoation;
	public String qistNo;
	public String status;
	public Date createTime;
	public Date approvedDate;
	public Date lastActive;
	
	@OneToMany
	public List<Product> products;
	
	@OneToMany
	public List<Campaign> campaigns;
	
	@OneToMany
	public List<CustomerSession> sessions;
	
	@ManyToOne
	public Staff staff;
}
