package viewmodels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerSessionVM {
	public Long id;
	public RetailerVM retailerVM;
	public List<ProductVM> products = new  ArrayList<>();
    public Date start;
    
}
