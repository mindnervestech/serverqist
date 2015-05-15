package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class Customer extends Model{

	@Id
	public Long id;
	public String firstName;
	public String lastName;
	public String email;
	public String password;
	public String address;
	@Lob
	public String image;
	public String type;
	public String FacebookId;
	public String GooglePlusId;
	public String TwitterId;
	public boolean qCartMailingList;
	public String qistNo;
	public Date lastActive;
	
	@ManyToMany
	public List<Product> cart;
	
	@OneToMany
	public List<CustomerSession> sessions;
	
	@ManyToMany
	public List<Campaign> campaigns;
	
	public static Finder<Long, Customer> find = new Finder<>(Long.class, Customer.class);

	public static Customer findByEmailAndPassword(String email, String password) {
		return find.where().eq("email", email).eq("password", password).findUnique();
	}
}
