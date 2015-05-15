package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class Product extends Model{

	@Id
	public Long id;
	public String name;
	public String description;
	public Date createdTime;
	public String mfrSku;
	public String storeSku;
	public String qrCode;
	public String qistNo;
	public String status;
	
	@OneToMany
	public List<QistProduct> offers;
	
	@ManyToMany(mappedBy = "cart")
	public List<Customer> customers;
	
	@OneToMany
	public List<SessionProduct> sessions;
	
	@ManyToOne
	public Retailer retailer;
	
	@ManyToMany
	public List<Category> categories;
	
	@ManyToMany
	public List<SubCategory> subCategories;
}
