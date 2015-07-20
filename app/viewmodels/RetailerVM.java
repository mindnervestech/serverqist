package viewmodels;

import java.util.ArrayList;
import java.util.List;

import models.CustomerSession;
import models.WdStaff;
import models.WdUser;

public class RetailerVM {

	private Long id;

	private String billingInformation;

	private String businessName;

	private String city;

	private String contactPerson;

	private String eftpos;

	private int eftposProvider;

	private String fbLink;

	private String goodsCategories;

	private String googleLink;

	private String gstNo;

	private String irNo;

	private String logoImage;

	private String merchantId;

	private String mobileNo;

	private String paymark;

	private String qistSku;

	private String referedBy;

	private int skuPostfix;

	private String streetName;

	private String streetNo;

	private String suburb;

	private String title;

	private String tradingName;

	private String twitterLink;

	private String workEmail;

	private String workPhone1;

	private String workPhone2;

	private String workUrl;

	private List<ProductVM> products = new ArrayList<>();

	private WdStaff wdStaff;

	private WdUser wdUser;
	
	private String qistNo;
	
	private List<CustomerSession> customerSessions = new ArrayList<>();


	public List<ProductVM> getProducts() {
		return products;
	}


	public void setProducts(List<ProductVM> products) {
		this.products = products;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getBillingInformation() {
		return billingInformation;
	}


	public void setBillingInformation(String billingInformation) {
		this.billingInformation = billingInformation;
	}


	public String getBusinessName() {
		return businessName;
	}


	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getContactPerson() {
		return contactPerson;
	}


	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}


	public String getEftpos() {
		return eftpos;
	}


	public void setEftpos(String eftpos) {
		this.eftpos = eftpos;
	}


	public int getEftposProvider() {
		return eftposProvider;
	}


	public void setEftposProvider(int eftposProvider) {
		this.eftposProvider = eftposProvider;
	}


	public String getFbLink() {
		return fbLink;
	}


	public void setFbLink(String fbLink) {
		this.fbLink = fbLink;
	}


	public String getGoodsCategories() {
		return goodsCategories;
	}


	public void setGoodsCategories(String goodsCategories) {
		this.goodsCategories = goodsCategories;
	}


	public String getGoogleLink() {
		return googleLink;
	}


	public void setGoogleLink(String googleLink) {
		this.googleLink = googleLink;
	}


	public String getGstNo() {
		return gstNo;
	}


	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}


	public String getIrNo() {
		return irNo;
	}


	public void setIrNo(String irNo) {
		this.irNo = irNo;
	}


	public String getLogoImage() {
		return logoImage;
	}


	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}


	public String getMerchantId() {
		return merchantId;
	}


	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}


	public String getQistNo() {
		return qistNo;
	}


	public void setQistNo(String qistNo) {
		this.qistNo = qistNo;
	}


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public String getPaymark() {
		return paymark;
	}


	public void setPaymark(String paymark) {
		this.paymark = paymark;
	}


	public String getQistSku() {
		return qistSku;
	}


	public void setQistSku(String qistSku) {
		this.qistSku = qistSku;
	}


	public String getReferedBy() {
		return referedBy;
	}


	public void setReferedBy(String referedBy) {
		this.referedBy = referedBy;
	}


	public int getSkuPostfix() {
		return skuPostfix;
	}


	public void setSkuPostfix(int skuPostfix) {
		this.skuPostfix = skuPostfix;
	}


	public String getStreetName() {
		return streetName;
	}


	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}


	public String getStreetNo() {
		return streetNo;
	}


	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}


	public String getSuburb() {
		return suburb;
	}


	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getTradingName() {
		return tradingName;
	}


	public void setTradingName(String tradingName) {
		this.tradingName = tradingName;
	}


	public String getTwitterLink() {
		return twitterLink;
	}


	public void setTwitterLink(String twitterLink) {
		this.twitterLink = twitterLink;
	}


	public String getWorkEmail() {
		return workEmail;
	}


	public void setWorkEmail(String workEmail) {
		this.workEmail = workEmail;
	}


	public String getWorkPhone1() {
		return workPhone1;
	}


	public void setWorkPhone1(String workPhone1) {
		this.workPhone1 = workPhone1;
	}


	public String getWorkPhone2() {
		return workPhone2;
	}


	public void setWorkPhone2(String workPhone2) {
		this.workPhone2 = workPhone2;
	}


	public String getWorkUrl() {
		return workUrl;
	}


	public void setWorkUrl(String workUrl) {
		this.workUrl = workUrl;
	}


	public WdStaff getWdStaff() {
		return wdStaff;
	}


	public void setWdStaff(WdStaff wdStaff) {
		this.wdStaff = wdStaff;
	}


	public WdUser getWdUser() {
		return wdUser;
	}


	public void setWdUser(WdUser wdUser) {
		this.wdUser = wdUser;
	}


	public List<CustomerSession> getCustomerSessions() {
		return customerSessions;
	}


	public void setCustomerSessions(List<CustomerSession> customerSessions) {
		this.customerSessions = customerSessions;
	}
	
	
}
