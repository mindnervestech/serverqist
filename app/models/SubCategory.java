package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class SubCategory extends Model{
	
	@Id
	public Long id;
	public String name;
	
	@ManyToOne
	public Category category;
	
	@ManyToMany(mappedBy = "subCategories")
	public List<Product> products;

}
