package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="wd_customer_orders")
@NamedQuery(name="WdCustomerOrders.findAll", query="SELECT w FROM WdCustomerOrders w")
public class WdCustomerOrders extends Model{

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="status")
	private String status;
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	public WdCustomer wdCustomer;
	
	@ManyToOne
	@JoinColumn(name="retailer_id")
	private WdRetailer wdRetailer;
	
	@Column(name="shipping_address_1")
	private String shippingAddress1;
	
	@Column(name="shipping_address_2")
	private String shippingAddress2;
	
	@Column(name="shipping_city")
	private String shippingCity;
	
	@Column(name="shipping_state")
	private String shippingState;
	
	@Column(name="shipping_country")
	private String shippingCountry;
	
	@Column(name="shipping_zip")
	private String shippingZip;
	
	@Column(name="billing_address_1")
	private String billingAddress1;
	
	@Column(name="billing_address_2")
	private String billingAddress2;
	
	@Column(name="billing_city")
	private String billingCity;
	
	@Column(name="billing_state")
	private String billingState;
	
	@Column(name="billing_country")
	private String billingCountry;
	
	@Column(name="billing_zip")
	private String billingZip;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@OneToMany(mappedBy="wdCustomerOrder")
	private List<WdCustomerOrderDetail> wdCustomerOrderDetail;

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

	public String getShippingAddress1() {
		return shippingAddress1;
	}

	public void setShippingAddress1(String shippingAddress1) {
		this.shippingAddress1 = shippingAddress1;
	}

	public String getShippingAddress2() {
		return shippingAddress2;
	}

	public void setShippingAddress2(String shippingAddress2) {
		this.shippingAddress2 = shippingAddress2;
	}

	public String getShippingCity() {
		return shippingCity;
	}

	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}

	public String getShippingState() {
		return shippingState;
	}

	public void setShippingState(String shippingState) {
		this.shippingState = shippingState;
	}

	public String getShippingCountry() {
		return shippingCountry;
	}

	public void setShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry;
	}

	public String getShippingZip() {
		return shippingZip;
	}

	public void setShippingZip(String shippingZip) {
		this.shippingZip = shippingZip;
	}

	public String getBillingAddress1() {
		return billingAddress1;
	}

	public void setBillingAddress1(String billingAddress1) {
		this.billingAddress1 = billingAddress1;
	}

	public String getBillingAddress2() {
		return billingAddress2;
	}

	public void setBillingAddress2(String billingAddress2) {
		this.billingAddress2 = billingAddress2;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	public String getBillingZip() {
		return billingZip;
	}

	public void setBillingZip(String billingZip) {
		this.billingZip = billingZip;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public WdCustomer getWdCustomer() {
		return wdCustomer;
	}

	public void setWdCustomer(WdCustomer wdCustomer) {
		this.wdCustomer = wdCustomer;
	}

	public WdRetailer getWdRetailer() {
		return wdRetailer;
	}

	public void setWdRetailer(WdRetailer wdRetailer) {
		this.wdRetailer = wdRetailer;
	}

	public List<WdCustomerOrderDetail> getWdCustomerOrderDetail() {
		return wdCustomerOrderDetail;
	}

	public void setWdCustomerOrderDetail(
			List<WdCustomerOrderDetail> wdCustomerOrderDetail) {
		this.wdCustomerOrderDetail = wdCustomerOrderDetail;
	}
	
	public static Finder<Long, WdCustomerOrders> find = new Finder<>(Long.class, WdCustomerOrders.class);
	
	public static List<WdCustomerOrders> findById(WdCustomer id){
		return find.where().eq("wdCustomer", id).findList();
	}
	
	
}
