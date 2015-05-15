package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class Campaign extends Model{
	
	@Id
	public Long id;
	public String message;
	
	@ManyToOne
	public Retailer retailer;
	
	@ManyToMany(mappedBy = "campaigns")
	public List<Customer> customers;
	

}
