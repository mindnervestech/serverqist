package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

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
	@Lob
	public String image; 
	@Lob
	public String specifications;
	
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
	
	
	public static Finder<Long, Product> find = new Finder<>(Long.class, Product.class);

	public static Product findByQrCode(String qrCode) {
		return find.where().eq("qrCode", qrCode).findUnique();
	}
	
}
