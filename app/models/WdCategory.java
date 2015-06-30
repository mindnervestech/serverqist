package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import scala.collection.parallel.ParIterableLike.Find;


/**
 * The persistent class for the wd_categories database table.
 * 
 */
@Entity
@Table(name="wd_categories")
@NamedQuery(name="WdCategory.findAll", query="SELECT w FROM WdCategory w")
public class WdCategory extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date")
	private Date createdDate;

	private String name;

	@Column(name="parent_id")
	private Long parentId;

	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date")
	private Date updatedDate;

	//bi-directional many-to-many association to WdProduct
	@ManyToMany(mappedBy="wdCategories")
	private List<WdProduct> wdProducts;

	public WdCategory() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<WdProduct> getWdProducts() {
		return this.wdProducts;
	}

	public void setWdProducts(List<WdProduct> wdProducts) {
		this.wdProducts = wdProducts;
	} 
	
	public static Finder<Long, WdCategory> find = new Finder<>(Long.class, WdCategory.class);
	
	public static List<WdCategory> getByMainParent()
	{
		return find.where().eq("parentId",0L).findList();
	}
	

}