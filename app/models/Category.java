package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class Category extends Model{

	@Id
	public Long id;
	public String name;
	
	@OneToMany
	public List<SubCategory> subCategories;
	
	@ManyToMany(mappedBy = "categories")
	public List<Product> products;
	
}
