package viewmodels;

import java.util.ArrayList;
import java.util.List;

public class ProductForNewsVM {

	public Long id;
	public String approvedDate;
	public String createdDate;
	public String createdTime;
	public String description;
	public boolean isApproved;
	public String mfrSku;
	public String name;
	public Double qistPrice;
	public String validFromDate;
	public String validToDate;
	public String status;
	public String storeSku;
	public String updatedDate;
	public String qistNo;
	public String offerStatus;
	
    public String storeName;
	
	public String city;
	
	public String streetName;

	public String streetNo;
	
	public List<String> categories = new ArrayList<>();
	public List<String> images = new ArrayList<>();
	
	public String availability;
}
