package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class SessionProduct extends Model{

	@Id
	public Long id;
	public String status;
	public boolean purchased;
	
	@ManyToOne
	public CustomerSession session;
	
	@ManyToOne
	public Product product;
}
