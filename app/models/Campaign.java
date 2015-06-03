package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class Campaign extends Model{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String message;
	
	@ManyToOne
	@JoinColumn(name="retailer_id")
	private WdRetailer wdRetailer;
	
	@ManyToMany(mappedBy="campaigns")
	private List<WdCustomer> wdCustomers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public WdRetailer getWdRetailer() {
		return wdRetailer;
	}

	public void setWdRetailer(WdRetailer wdRetailer) {
		this.wdRetailer = wdRetailer;
	}

	public List<WdCustomer> getWdCustomers() {
		return wdCustomers;
	}

	public void setWdCustomers(List<WdCustomer> wdCustomers) {
		this.wdCustomers = wdCustomers;
	}


}
