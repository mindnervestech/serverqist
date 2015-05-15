package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class QistProduct extends Model{
	
	@Id
	public Long id;
	public Date start;
	public Date end;
	public Double qistPrice; 
	
	@ManyToOne
	public Product product;

}
