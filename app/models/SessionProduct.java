package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class SessionProduct extends Model{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String status;
	private boolean purchased;
	
	@ManyToOne
	@JoinColumn(name="session_id")
	private CustomerSession customerSession;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private WdProduct wdProduct;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isPurchased() {
		return purchased;
	}

	public void setPurchased(boolean purchased) {
		this.purchased = purchased;
	}

	public CustomerSession getCustomerSession() {
		return customerSession;
	}

	public void setCustomerSession(CustomerSession customerSession) {
		this.customerSession = customerSession;
	}

	public WdProduct getWdProduct() {
		return wdProduct;
	}

	public void setWdProduct(WdProduct wdProduct) {
		this.wdProduct = wdProduct;
	}
	

	public static Finder<Long, SessionProduct> find = new Finder<>(Long.class, SessionProduct.class);

	public static List <SessionProduct>  getSessionProductByCustomerIdAndPurchased(CustomerSession customerSession) {
		return find.where().eq("customerSession", customerSession).eq("purchased", true).findList();
	
	}

}
