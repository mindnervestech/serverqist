package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class CustomerSession extends Model{
	
	@Id
	public Long id;
	public Date start;
	public Date end;
	
	@ManyToOne
	public Retailer store;
	
	@ManyToOne
	public Customer customer;
	
	@OneToMany
	public List<SessionProduct> products;

}
