package viewmodels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductVM {

	public Long id;
	public Date approvedDate;
	public Date createdDate;
	public Date createdTime;
	public String description;
	public boolean isApproved;
	public String mfrSku;
	public String name;
	public Double qistPrice;
	public Date validFromDate;
	public Date validToDate;
	public String status;
	public String storeSku;
	public Date updatedDate;
	public String qistNo;
	public List<String> images = new ArrayList<>();
	
}
