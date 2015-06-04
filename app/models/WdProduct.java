package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.ebean.Model;


/**
 * The persistent class for the wd_product database table.
 * 
 */
@Entity
@Table(name="wd_product")
@NamedQuery(name="WdProduct.findAll", query="SELECT w FROM WdProduct w")
public class WdProduct extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="approved_date")
	private Date approvedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date")
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_time")
	private Date createdTime;

	private String description;

	@Lob
	private String image;

	@Column(name="is_approved")
	private String isApproved;

	@Column(name="mfr_sku")
	private String mfrSku;

	private String name;

	@Column(name="qist_sku")
	private String qistSku;

	@Column(name="qr_code")
	private String qrCode;

	@Column(name="sku_postfix")
	private int skuPostfix;

	@Lob
	private String specifications;

	private String status;

	@Column(name="store_sku")
	private String storeSku;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date")
	private Date updatedDate;

	//bi-directional many-to-many association to WdCustomer
	@ManyToMany
	@JoinTable(
		name="wd_customer_product"
		, joinColumns={
			@JoinColumn(name="product_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="customer_id")
			}
		)
	private List<WdCustomer> wdCustomers;

	//bi-directional many-to-one association to WdRetailer
	@ManyToOne
	@JoinColumn(name="retailer_id")
	private WdRetailer wdRetailer;

	//bi-directional many-to-many association to WdCategory
	@ManyToMany
	@JoinTable(
		name="wd_product_category"
		, joinColumns={
			@JoinColumn(name="product_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="category_id")
			}
		)
	private List<WdCategory> wdCategories;
	
	@OneToMany(mappedBy="wdProduct")
	private List<SessionProduct> sessionProducts;

	public WdProduct() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getIsApproved() {
		if(this.isApproved.equals("0")){
			this.isApproved = "true";
		} else {
			this.isApproved = "false";
		}
		return this.isApproved;
	}

	public void setIsApproved(String isApproved) {
		if(isApproved.equals("true")){
			isApproved = "0";
		} else {
			isApproved = "1";
		}
		this.isApproved = isApproved;
	}

	public String getMfrSku() {
		return this.mfrSku;
	}

	public void setMfrSku(String mfrSku) {
		this.mfrSku = mfrSku;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQistSku() {
		return this.qistSku;
	}

	public void setQistSku(String qistSku) {
		this.qistSku = qistSku;
	}

	public String getQrCode() {
		return this.qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public int getSkuPostfix() {
		return this.skuPostfix;
	}

	public void setSkuPostfix(int skuPostfix) {
		this.skuPostfix = skuPostfix;
	}

	public String getSpecifications() {
		return this.specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStoreSku() {
		return this.storeSku;
	}

	public void setStoreSku(String storeSku) {
		this.storeSku = storeSku;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<WdCustomer> getWdCustomers() {
		return this.wdCustomers;
	}

	public void setWdCustomers(List<WdCustomer> wdCustomers) {
		this.wdCustomers = wdCustomers;
	}

	public WdRetailer getWdRetailer() {
		return this.wdRetailer;
	}

	public void setWdRetailer(WdRetailer wdRetailer) {
		this.wdRetailer = wdRetailer;
	}

	public List<WdCategory> getWdCategories() {
		return this.wdCategories;
	}

	public void setWdCategories(List<WdCategory> wdCategories) {
		this.wdCategories = wdCategories;
	}
	
	public List<SessionProduct> getSessionProducts() {
		return sessionProducts;
	}

	public void setSessionProducts(List<SessionProduct> sessionProducts) {
		this.sessionProducts = sessionProducts;
	}
	
	public SessionProduct addSessionProduct(SessionProduct sessionProduct) {
		getSessionProducts().add(sessionProduct);
		sessionProduct.setWdProduct(this);

		return sessionProduct;
	}

	public SessionProduct removeSessionProduct(SessionProduct sessionProduct) {
		getSessionProducts().remove(sessionProduct);
		sessionProduct.setWdProduct(null);

		return sessionProduct;
	}
	
	
	
	public static Finder<Long, WdProduct> find = new Finder<>(Long.class, WdProduct.class);

	public static WdProduct findByQrCode(String qrcode) {
		return find.where().eq("qrCode", qrcode).findUnique();
	}
	
	public static List<WdProduct> findByRetailer(WdRetailer wdRetailer) {
		return find.where().eq("wdRetailer", wdRetailer).findList();
	}
	
	

}