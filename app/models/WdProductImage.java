package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name="wd_product_image")
@NamedQuery(name="WdProductImage.findAll", query="SELECT w FROM WdProductImage w")
public class WdProductImage extends Model {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="product_image_name")
	private String productImageName;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private WdProduct wdProduct;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductImageName() {
		return productImageName;
	}

	public void setProductImageName(String productImageName) {
		this.productImageName = productImageName;
	}

	public WdProduct getWdProduct() {
		return wdProduct;
	}

	public void setWdProduct(WdProduct wdProduct) {
		this.wdProduct = wdProduct;
	}

}
