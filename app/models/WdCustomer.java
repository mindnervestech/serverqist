package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.ebean.Model;


/**
 * The persistent class for the wd_customers database table.
 * 
 */
@Entity
@Table(name="wd_customers")
@NamedQuery(name="WdCustomer.findAll", query="SELECT w FROM WdCustomer w")
public class WdCustomer extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	

	@Column(name="contact_no")
	private String contactNo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date")
	private Date createdDate;

	private String email;

	private String facebookid;

	private String firstname;

	private String googleplusid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_active")
	private Date lastActive;

	private String lastname;

	private String password;

	@Column(name="q_cart_mailing_list")
	private String qCartMailingList;

	@Column(name="qist_sku")
	private String qistSku;

	@Column(name="address_1")
	private String address1;
	
	@Column(name="address_2")
	private String address2;
	
	@Column(name="city")
	private String city;
	
	@Column(name="state")
	private String state;
	
	@Column(name="country")
	private String country;
	
	@Column(name="zip")
	private String zip;
	
	@Column(name="sku_postfix")
	private int skuPostfix;

	private String twitterid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date")
	private Date updatedDate;

	@Lob
	private String image;
	
	private String type;
	
	@OneToMany(mappedBy="wdCustomer")
	private List<WdCustomerOrders> wdCustomerOrders;
	
	
	
	@ManyToMany
	private List<Campaign> campaigns;
	
	@OneToMany(mappedBy="wdCustomer")
	private List<CustomerSession> customerSessions;
	
	public WdCustomer() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	

	public String getContactNo() {
		return this.contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFacebookid() {
		return this.facebookid;
	}

	public void setFacebookid(String facebookid) {
		this.facebookid = facebookid;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getGoogleplusid() {
		return this.googleplusid;
	}

	public void setGoogleplusid(String googleplusid) {
		this.googleplusid = googleplusid;
	}

	public Date getLastActive() {
		return this.lastActive;
	}

	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQCartMailingList() {
		/*if(this.qCartMailingList.equals("0")){
			this.qCartMailingList = "true";
		} else {
			this.qCartMailingList = "false";
		}*/
		return this.qCartMailingList;
	}

	public void setQCartMailingList(String qCartMailingList) {
		/*if(qCartMailingList.equals("true")){
			qCartMailingList = "0";
		} else {
			qCartMailingList = "1";
		}*/
		this.qCartMailingList = qCartMailingList;
	}

	public String getQistSku() {
		return this.qistSku;
	}

	public void setQistSku(String qistSku) {
		this.qistSku = qistSku;
	}

	public int getSkuPostfix() {
		return this.skuPostfix;
	}

	public void setSkuPostfix(int skuPostfix) {
		this.skuPostfix = skuPostfix;
	}

	public String getTwitterid() {
		return this.twitterid;
	}

	public void setTwitterid(String twitterid) {
		this.twitterid = twitterid;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<Campaign> getCampaigns() {
		return this.campaigns;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public List<CustomerSession> getCustomerSessions() {
		return this.customerSessions;
	}

	public void setCustomerSessions(List<CustomerSession> customerSessions) {
		this.customerSessions = customerSessions;
	}

	public CustomerSession addCustomerSession(CustomerSession customerSession) {
		getCustomerSessions().add(customerSession);
		customerSession.setWdCustomer(this);

		return customerSession;
	}

	public CustomerSession removeCustomerSession(CustomerSession customerSession) {
		getCustomerSessions().remove(customerSession);
		customerSession.setWdCustomer(null);

		return customerSession;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	




	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public List<WdCustomerOrders> getWdCustomerOrders() {
		return wdCustomerOrders;
	}

	public void setWdCustomerOrders(List<WdCustomerOrders> wdCustomerOrders) {
		this.wdCustomerOrders = wdCustomerOrders;
	}







	public static Finder<Long, WdCustomer> find = new Finder<>(Long.class, WdCustomer.class);

	public static WdCustomer findByEmailAndPassword(String email, String password) {
		return find.where().eq("email", email).eq("password", password).findUnique();
	}
	
	public static WdCustomer findByEmail(String email) {
		return find.where().eq("email", email).findUnique();
	}
	
	public static WdCustomer findByFbId(String FacebookId) {
		return find.where().eq("facebookId", FacebookId).findUnique();
	}
	
	public static WdCustomer findByGoogleID(String GooglePlusId) {
		return find.where().eq("googlePlusId", GooglePlusId).findUnique();
	}
	
	public static WdCustomer findByTwitterId(String TwitterId) {
		return find.where().eq("twitterId", TwitterId).findUnique();
	}

	public static WdCustomer findById(Long id) {
		return find.byId(id);
	}

}