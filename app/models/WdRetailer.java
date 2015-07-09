package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;


/**
 * The persistent class for the wd_retailers database table.
 * 
 */
@Entity
@Table(name="wd_retailers")
@NamedQuery(name="WdRetailer.findAll", query="SELECT w FROM WdRetailer w")
public class WdRetailer extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Lob
	@Column(name="billing_information")
	private String billingInformation;

	@Lob
	@Column(name="business_name")
	private String businessName;

	private String city;

	@Column(name="contact_person")
	private String contactPerson;

	private String eftpos;

	@Column(name="eftpos_provider")
	private int eftposProvider;

	@Lob
	@Column(name="fb_link")
	private String fbLink;

	@Lob
	@Column(name="goods_categories")
	private String goodsCategories;

	@Lob
	@Column(name="google_link")
	private String googleLink;

	@Column(name="gst_no")
	private String gstNo;

	@Column(name="ir_no")
	private String irNo;

	@Lob
	@Column(name="logo_image")
	private String logoImage;

	@Column(name="merchant_id")
	private String merchantId;

	@Column(name="mobile_no")
	private String mobileNo;

	@Lob
	private String paymark;

	@Column(name="qist_sku")
	private String qistSku;

	@Column(name="refered_by")
	private String referedBy;

	@Column(name="sku_postfix")
	private int skuPostfix;

	@Lob
	@Column(name="street_name")
	private String streetName;

	@Lob
	@Column(name="street_no")
	private String streetNo;

	private String suburb;

	private String title;

	@Lob
	@Column(name="trading_name")
	private String tradingName;

	@Lob
	@Column(name="twitter_link")
	private String twitterLink;

	@Column(name="work_email")
	private String workEmail;

	@Column(name="work_phone_1")
	private String workPhone1;

	@Column(name="work_phone_2")
	private String workPhone2;

	@Column(name="work_url")
	private String workUrl;

	//bi-directional many-to-one association to WdProduct
	@OneToMany(mappedBy="wdRetailer")
	private List<WdProduct> wdProducts;

	//bi-directional many-to-one association to WdStaff
	@ManyToOne
	@JoinColumn(name="staff_id")
	private WdStaff wdStaff;

	//bi-directional many-to-one association to WdUser
	@ManyToOne
	@JoinColumn(name="user_id")
	private WdUser wdUser;
	
	@OneToMany(mappedBy="wdRetailer")
	private List<Campaign> campaigns;
	
	@OneToMany(mappedBy="wdRetailer")
	private List<CustomerSession> customerSessions;
	
	
 
	public WdRetailer() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBillingInformation() {
		return this.billingInformation;
	}

	public void setBillingInformation(String billingInformation) {
		this.billingInformation = billingInformation;
	}

	public String getBusinessName() {
		return this.businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getEftpos() {
		return this.eftpos;
	}

	public void setEftpos(String eftpos) {
		this.eftpos = eftpos;
	}

	public int getEftposProvider() {
		return this.eftposProvider;
	}

	public void setEftposProvider(int eftposProvider) {
		this.eftposProvider = eftposProvider;
	}

	public String getFbLink() {
		return this.fbLink;
	}

	public void setFbLink(String fbLink) {
		this.fbLink = fbLink;
	}

	public String getGoodsCategories() {
		return this.goodsCategories;
	}

	public void setGoodsCategories(String goodsCategories) {
		this.goodsCategories = goodsCategories;
	}

	public String getGoogleLink() {
		return this.googleLink;
	}

	public void setGoogleLink(String googleLink) {
		this.googleLink = googleLink;
	}

	public String getGstNo() {
		return this.gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getIrNo() {
		return this.irNo;
	}

	public void setIrNo(String irNo) {
		this.irNo = irNo;
	}

	public String getLogoImage() {
		return this.logoImage;
	}

	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}

	public String getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPaymark() {
		return this.paymark;
	}

	public void setPaymark(String paymark) {
		this.paymark = paymark;
	}

	public String getQistSku() {
		return this.qistSku;
	}

	public void setQistSku(String qistSku) {
		this.qistSku = qistSku;
	}

	public String getReferedBy() {
		return this.referedBy;
	}

	public void setReferedBy(String referedBy) {
		this.referedBy = referedBy;
	}

	public int getSkuPostfix() {
		return this.skuPostfix;
	}

	public void setSkuPostfix(int skuPostfix) {
		this.skuPostfix = skuPostfix;
	}

	public String getStreetName() {
		return this.streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetNo() {
		return this.streetNo;
	}

	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}

	public String getSuburb() {
		return this.suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTradingName() {
		return this.tradingName;
	}

	public void setTradingName(String tradingName) {
		this.tradingName = tradingName;
	}

	public String getTwitterLink() {
		return this.twitterLink;
	}

	public void setTwitterLink(String twitterLink) {
		this.twitterLink = twitterLink;
	}

	public String getWorkEmail() {
		return this.workEmail;
	}

	public void setWorkEmail(String workEmail) {
		this.workEmail = workEmail;
	}

	public String getWorkPhone1() {
		return this.workPhone1;
	}

	public void setWorkPhone1(String workPhone1) {
		this.workPhone1 = workPhone1;
	}

	public String getWorkPhone2() {
		return this.workPhone2;
	}

	public void setWorkPhone2(String workPhone2) {
		this.workPhone2 = workPhone2;
	}

	public String getWorkUrl() {
		return this.workUrl;
	}

	public void setWorkUrl(String workUrl) {
		this.workUrl = workUrl;
	}

	public List<WdProduct> getWdProducts() {
		return this.wdProducts;
	}

	public void setWdProducts(List<WdProduct> wdProducts) {
		this.wdProducts = wdProducts;
	}

	public WdProduct addWdProduct(WdProduct wdProduct) {
		getWdProducts().add(wdProduct);
		wdProduct.setWdRetailer(this);

		return wdProduct;
	}

	public WdProduct removeWdProduct(WdProduct wdProduct) {
		getWdProducts().remove(wdProduct);
		wdProduct.setWdRetailer(null);

		return wdProduct;
	}

	public WdStaff getWdStaff() {
		return this.wdStaff;
	}

	public void setWdStaff(WdStaff wdStaff) {
		this.wdStaff = wdStaff;
	}

	public WdUser getWdUser() {
		return this.wdUser;
	}

	public void setWdUser(WdUser wdUser) {
		this.wdUser = wdUser;
	}
	
	public List<CustomerSession> getCustomerSessions() {
		return this.customerSessions;
	}

	public void setCustomerSessions(List<CustomerSession> customerSessions) {
		this.customerSessions = customerSessions;
	}

	public CustomerSession addCustomerSession(CustomerSession customerSession) {
		getCustomerSessions().add(customerSession);
		customerSession.setWdRetailer(this);

		return customerSession;
	}

	public CustomerSession removeCustomerSession(CustomerSession customerSession) {
		getCustomerSessions().remove(customerSession);
		customerSession.setWdRetailer(null);

		return customerSession;
	}
	
	public List<Campaign> getCampaigns() {
		return this.campaigns;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public Campaign addCampaign(Campaign campaign) {
		getCampaigns().add(campaign);
		campaign.setWdRetailer(this);

		return campaign;
	}

	public Campaign removeCampaign(Campaign campaign) {
		getCampaigns().remove(campaign);
		campaign.setWdRetailer(null);

		return campaign;
	}
	
	public static Finder<Long, WdRetailer> find = new Finder<>(Long.class, WdRetailer.class);

	public static List<WdRetailer> findAll() {
		return find.all();
	}

	public static WdRetailer findById(Long id) {
		return find.where().eq("id", id).findUnique();
	}
	
    public static WdRetailer findByUser(WdUser u1)
    {
    	return find.where().eq("wdUser", u1).findUnique();
    }
	
	
	
}