package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class CustomerSession extends Model{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Date start;
	private Date end;
	
	@ManyToOne
	@JoinColumn(name="retailer_id")
	public WdRetailer wdRetailer;
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	public WdCustomer wdCustomer;
	
	@OneToMany(mappedBy="customerSession")
	public List<SessionProduct> sessionProducts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public WdRetailer getWdRetailer() {
		return wdRetailer;
	}

	public void setWdRetailer(WdRetailer wdRetailer) {
		this.wdRetailer = wdRetailer;
	}

	public WdCustomer getWdCustomer() {
		return wdCustomer;
	}

	public void setWdCustomer(WdCustomer wdCustomer) {
		this.wdCustomer = wdCustomer;
	}

	public List<SessionProduct> getSessionProducts() {
		return sessionProducts;
	}

	public void setSessionProducts(List<SessionProduct> sessionProducts) {
		this.sessionProducts = sessionProducts;
	}
	
	public SessionProduct addSessionProduct(SessionProduct sessionProduct) {
		getSessionProducts().add(sessionProduct);
		sessionProduct.setCustomerSession(this);

		return sessionProduct;
	}

	public SessionProduct removeSessionProduct(SessionProduct sessionProduct) {
		getSessionProducts().remove(sessionProduct);
		sessionProduct.setCustomerSession(null);

		return sessionProduct;
	}
	

	public static Finder<Long, CustomerSession> find = new Finder<>(Long.class, CustomerSession.class);

	
	
	public static CustomerSession  getCustomerSessionByRetailerAndDateAndCustomer(WdRetailer wdRetailer,Date d, WdCustomer c) {
		return find.where().eq("wdRetailer", wdRetailer).eq("start", d).eq("wdCustomer", c).findUnique();
	}
	
	public static List <CustomerSession>  getCustomerSessionByCustomerId(WdCustomer wdCustomer) {
		return find.where().eq("wdCustomer", wdCustomer).order().asc("start").setMaxRows(10).findList();
	}
	
   public static  CustomerSession getCustomerSessionByActiveCustomerId(WdCustomer wdCustomer)
   {
	   return find.where().eq("wdCustomer", wdCustomer).eq("end",null).findUnique();
   }
}
