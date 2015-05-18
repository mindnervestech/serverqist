package viewmodels;

import java.util.Date;

public class ProductVM {

	public Long id;
	public String name;
	public String description;
	public Date createdTime;
	public String mfrSku;
	public String storeSku;
	public String qrCode;
	public String qistNo;
	public String status;
	public String image; 
	public String specifications;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getMfrSku() {
		return mfrSku;
	}
	public void setMfrSku(String mfrSku) {
		this.mfrSku = mfrSku;
	}
	public String getStoreSku() {
		return storeSku;
	}
	public void setStoreSku(String storeSku) {
		this.storeSku = storeSku;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getQistNo() {
		return qistNo;
	}
	public void setQistNo(String qistNo) {
		this.qistNo = qistNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getSpecifications() {
		return specifications;
	}
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
	
}
