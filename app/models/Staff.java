package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class Staff extends Model{

	@Id
	public Long id;
	public String firstName;
	public String lastName;
	public String email;
	public String password;
	public StaffType staffType;
	
	public enum StaffType{
		SuperAdmin, Associate
	}
	
	@OneToMany
	public List<Retailer> retailers;
	
}
