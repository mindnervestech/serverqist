package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import models.CustomerSession;
import models.SessionProduct;
import models.WdCustomer;
import models.WdProduct;
import models.WdRetailer;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import viewmodels.CustomerSessionVM;
import viewmodels.CustomerVM;
import viewmodels.ProductVM;
import viewmodels.RetailerVM;
import views.html.index;

import com.fasterxml.jackson.databind.JsonNode;

public class Application extends Controller {

	//private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json";
	
	private static final double dist = 5.0;
	
	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result register(){
		HashMap<String, Object> map = new HashMap<>();
		DynamicForm users = Form.form().bindFromRequest();
		String fname = users.get("firstName");
		String lname = users.get("lastName");
		String email = users.get("email");
		String pass = users.get("password");
		String repass = users.get("retypePassword");
		String address = users.get("address");
		String contactNo = users.get("contactNo");
		Boolean qcart = Boolean.parseBoolean(users.get("qCartMailingList"));
		String FacebookId = users.get("FacebookId");
		String GooglePlusId = users.get("GooglePlusId");
		String TwitterId = users.get("TwitterId");
		String imageDataString = null ;
		WdCustomer c = new WdCustomer();
		MultipartFormData body = request().body().asMultipartFormData();
		if (body != null) {
			FilePart filePart = body.getFile("image");
			if (filePart != null) {
				File f = filePart.getFile();
				try {            
					FileInputStream imageInFile = new FileInputStream(f);
					byte imageData[] = new byte[(int) f.length()];
					imageInFile.read(imageData);
					imageInFile.close();
					imageDataString = Base64.encodeBase64URLSafeString(imageData);
				} catch (FileNotFoundException e) {
					System.out.println("Image not found" + e);
				} catch (IOException ioe) {
					System.out.println("Exception while reading the Image " + ioe);
				}
			}
		}
		if(fname.isEmpty() || fname == null ||
				lname.isEmpty() || lname == null){
			map.put("status", "500");
			map.put("message", "Please fill required data.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		if(!pass.equals(repass)){
			map.put("status", "500");
			map.put("message", "Passwords don't match.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		WdCustomer cus = WdCustomer.findByEmail(email);
		if(cus != null){
			map.put("status", "500");
			map.put("message", "User already exists.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}

		c.setFirstname(fname);
		c.setLastname(lname);
		c.setEmail(email);
		c.setPassword(pass);
		c.setAddress(address);
		c.setContactNo(contactNo);
		c.setImage(imageDataString);
		c.setType("M");
		c.setQCartMailingList(qcart.toString());
		Date d = new Date();
		c.setLastActive(d);
		c.setCreatedDate(d);
		c.setUpdatedDate(d);
		if(FacebookId != null && !FacebookId.isEmpty()){
			WdCustomer cc = WdCustomer.findByFbId(FacebookId);
			if(cc != null){
				HashMap<String, Object> map1 = new HashMap<>();
				map1.put("CTPYE", "S");
				map1.put("CustomerID", cc.getId().toString());
				map.put("status", "200");
				map.put("message", "OK.");
				map.put("data", map1);
				return ok(Json.toJson(map));
			}else{
				c.setFacebookid(FacebookId);
				c.setType("S");
			}

		} else if(GooglePlusId != null && !GooglePlusId.isEmpty()){
			WdCustomer cc = WdCustomer.findByGoogleID(GooglePlusId);
			if(cc != null){
				HashMap<String, Object> map1 = new HashMap<>();
				map1.put("CTPYE", "S");
				map1.put("CustomerID", cc.getId().toString());
				map.put("status", "200");
				map.put("message", "OK.");
				map.put("data", map1);
				return ok(Json.toJson(map));
			}else{
				c.setGoogleplusid(GooglePlusId);
				c.setType("S");
			}
		} else if(TwitterId != null && !TwitterId.isEmpty()){
			WdCustomer cc = WdCustomer.findByTwitterId(TwitterId);
			if(cc != null){
				HashMap<String, Object> map1 = new HashMap<>();
				map1.put("CTPYE", "S");
				map1.put("CustomerID", cc.getId().toString());
				map.put("status", "200");
				map.put("message", "OK.");
				map.put("data", map1);
				return ok(Json.toJson(map));
			}else{
				c.setTwitterid(TwitterId);
				c.setType("S");
			}	
		}
		c.save();
		
		
		CustomerVM vm = new CustomerVM();
		vm.id = c.getId();
		vm.firstName = c.getFirstname();
		vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		vm.password = c.getPassword();
		vm.address = c.getAddress();
		vm.image = c.getImage();
		vm.contactNo = c.getContactNo();
		vm.createdDate = c.getCreatedDate();
		vm.updatedDate = c.getUpdatedDate();
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+c.getSkuPostfix();
	
		
		
		HashMap<String, Object> map1 = new HashMap<>();
		map1.put("CTPYE", c.getType());
		map1.put("CustomerID", c.getId().toString());
		map1.put("UserData", vm);
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}

	public static Result login(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		String email = data.path("email").asText();
		String password = data.path("password").asText();
		if(email.isEmpty() || email == null ||
				password.isEmpty() || password == null){
			map.put("status", "500");
			map.put("message", "Please fill required data.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		WdCustomer c = WdCustomer.findByEmail(email);
		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		if(!c.getPassword().equals(password)){
			map.put("status", "500");
			map.put("message", "Invalid credentials.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		
		CustomerVM vm = new CustomerVM();
		vm.id = c.getId();
		vm.firstName = c.getFirstname();
		vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		vm.password = c.getPassword();
		vm.address = c.getAddress();
		vm.image = c.getImage();
		vm.contactNo = c.getContactNo();
		vm.createdDate = c.getCreatedDate();
		vm.updatedDate = c.getUpdatedDate();
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+c.getSkuPostfix();
	
		
		HashMap<String, Object> map1 = new HashMap<>();
		map1.put("CTPYE", c.getType());
		map1.put("CustomerID", c.getId().toString());
		map1.put("UserData",vm);
		map.put("status", "200");
		map.put("message", "Login successfull.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}

	public static Result forgotPassword(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		String email = data.path("email").asText();
		WdCustomer c = WdCustomer.findByEmail(email);
		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}else{
			sendPasswordMail(c.getEmail(),c.getPassword());
		}
		map.put("status", "200");
		map.put("message", "Email sent with new password.");
		map.put("data", null);
		return ok(Json.toJson(map));
	}
	
	public static Result getCustomerProfile(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long id = data.path("userId").asLong();
		WdCustomer c = WdCustomer.findById(id);
		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		CustomerVM vm = new CustomerVM();
		vm.id = c.getId();
		vm.firstName = c.getFirstname();
		vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		vm.password = c.getPassword();
		vm.address = c.getAddress();
		vm.image = c.getImage();
		vm.contactNo = c.getContactNo();
		vm.createdDate = c.getCreatedDate();
		vm.updatedDate = c.getUpdatedDate();
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+c.getSkuPostfix();
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", vm);
		return ok(Json.toJson(map));
	}
	
	public static Result changeCustomerName(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long id = data.path("userId").asLong();
		WdCustomer c = WdCustomer.findById(id); 
		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		c.setFirstname(data.path("firstName").asText());
		c.setLastname(data.path("lastName").asText());
		c.update();
		
		CustomerVM vm = new CustomerVM();
		vm.id = c.getId();
		vm.firstName = c.getFirstname();
		vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		vm.password = c.getPassword();
		vm.address = c.getAddress();
		vm.image = c.getImage();
		vm.contactNo = c.getContactNo();
		vm.createdDate = c.getCreatedDate();
		vm.updatedDate = c.getUpdatedDate();
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+c.getSkuPostfix();
		
		
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", vm);
		return ok(Json.toJson(map));
	}
	
	public static Result changeCustomerPassword(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long id = data.path("userId").asLong();
		WdCustomer c = WdCustomer.findById(id); 
		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		c.setPassword(data.path("password").asText());
		c.update();
		
		CustomerVM vm = new CustomerVM();
		vm.id = c.getId();
		vm.firstName = c.getFirstname();
		vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		vm.password = c.getPassword();
		vm.address = c.getAddress();
		vm.image = c.getImage();
		vm.contactNo = c.getContactNo();
		vm.createdDate = c.getCreatedDate();
		vm.updatedDate = c.getUpdatedDate();
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+c.getSkuPostfix();
		
		
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", vm);
		return ok(Json.toJson(map));
	}
	
	public static Result changeCustomerAddress(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long id = data.path("userId").asLong();
		WdCustomer c = WdCustomer.findById(id); 
		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		c.setAddress(data.path("address").asText());
		c.update();
		
		CustomerVM vm = new CustomerVM();
		vm.id = c.getId();
		vm.firstName = c.getFirstname();
		vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		vm.password = c.getPassword();
		vm.address = c.getAddress();
		vm.image = c.getImage();
		vm.contactNo = c.getContactNo();
		vm.createdDate = c.getCreatedDate();
		vm.updatedDate = c.getUpdatedDate();
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+c.getSkuPostfix();
		
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", vm);
		return ok(Json.toJson(map));
	}
	
	public static Result getMyOffers() throws Exception{
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		String lat = data.path("lat").asText();
		String lon = data.path("long").asText();
		double lon1 = Double.parseDouble(lon);
		double latn1 = Double.parseDouble(lat);
		long currentId = 0;
		double currDist = 0;
		List<WdRetailer> wd = WdRetailer.findAll();
		List<RetailerVM> vmList = new ArrayList<>();
		for(WdRetailer w:wd){
			String fullAddress = w.getStreetNo()+","+w.getStreetName()+","+w.getSuburb()+","+w.getCity();
			String[] latLongs = getLatLongPositions(fullAddress);
			double lon2 = Double.parseDouble(latLongs[1]);
			double lat2 = Double.parseDouble(latLongs[0]);
			double mydist = distance(latn1,lon1,lat2,lon2);
			if(mydist <= dist ){
				if(currentId == 0){
					currentId = w.getId();
					currDist = mydist;
				} else {
					if(mydist < currDist){
						currentId = w.getId();
						currDist = mydist;
					}
				}
				RetailerVM vm = new RetailerVM();
				vm.setBusinessName(w.getBusinessName());
				vm.setStreetName(w.getStreetName());
				vm.setStreetNo(w.getStreetNo());
				vm.setSuburb(w.getSuburb());
				vm.setTradingName(w.getTradingName());
				vm.setCity(w.getCity());
				vm.setContactPerson(w.getContactPerson());
				vm.setWorkEmail(w.getWorkEmail());
				vm.setQistNo(w.getQistSku()+w.getSkuPostfix());
				vmList.add(vm);
			}
		}
		List<ProductVM> prods = getProducts(currentId);
		HashMap<String, Object> map1 = new HashMap<>();
		map1.put("products", prods);
		map1.put("retailers", vmList);
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}
	
	private static List<ProductVM> getProducts(Long id) {
		WdRetailer wd = WdRetailer.findById(id);
		List<WdProduct> prod = WdProduct.findByRetailer(wd);
		List<ProductVM> plist = new ArrayList<>();
		for(WdProduct p : prod){
			ProductVM vm = new ProductVM();
			vm.id = p.getId();
			vm.name = p.getName();
			vm.description = p .getDescription();
			vm.image = p.getImage();
			vm.qrCode = p.getQrCode();
			vm.specifications = p.getSpecifications();
			vm.status = p .getStatus();
			vm.createdTime = p.getCreatedTime();
			vm.approvedDate = p.getApprovedDate();
			vm.createdDate = p.getCreatedDate();
			vm.updatedDate = p.getUpdatedDate();
			vm.isApproved = Boolean.parseBoolean(p.getIsApproved());
			vm.mfrSku = p.getMfrSku();
			vm.storeSku = p.getStoreSku();
			vm.qistNo = p.getQistSku() + p.getSkuPostfix();
			plist.add(vm);
		}
		return plist;
	}
	
	public static Result getRetailerProducts() {
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long id = data.path("retailerId").asLong();
		WdRetailer w = WdRetailer.findById(id);
		if(w == null){
			map.put("status", "500");
			map.put("message", "Retailer does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		RetailerVM vm = new RetailerVM();
		vm.setBusinessName(w.getBusinessName());
		vm.setStreetName(w.getStreetName());
		vm.setStreetNo(w.getStreetNo());
		vm.setSuburb(w.getSuburb());
		vm.setTradingName(w.getTradingName());
		vm.setCity(w.getCity());
		vm.setContactPerson(w.getContactPerson());
		vm.setWorkEmail(w.getWorkEmail());
		vm.setQistNo(w.getQistSku()+w.getSkuPostfix());
		List<ProductVM> prods = getProducts(id);
		HashMap<String, Object> map1 = new HashMap<>();
		map1.put("retailer", vm);
		map1.put("products", prods);
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}
	
	
	private static double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	private static String[] getLatLongPositions(String address) throws Exception
	{
		int responseCode = 0;
		String api = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=true";
		URL url = new URL(api);
		HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
		httpConnection.connect();
		responseCode = httpConnection.getResponseCode();
		if(responseCode == 200)
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();;
			Document document = builder.parse(httpConnection.getInputStream());
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/GeocodeResponse/status");
			String status = (String)expr.evaluate(document, XPathConstants.STRING);
			if(status.equals("OK"))
			{
				expr = xpath.compile("//geometry/location/lat");
				String latitude = (String)expr.evaluate(document, XPathConstants.STRING);
				expr = xpath.compile("//geometry/location/lng");
				String longitude = (String)expr.evaluate(document, XPathConstants.STRING);
				return new String[] {latitude, longitude};
			}
			else
			{
				throw new Exception("Error from the API - response status: "+status);
			}
		}
		return null;
	}


	public static Result getCustomerCart(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long id = data.path("userId").asLong();
		WdCustomer c = WdCustomer.findById(id); 
		ArrayList<ProductVM> VMs =  new ArrayList<ProductVM>();
		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}else{
			List <WdProduct> wdP  = c.getWdProducts();
			for(WdProduct p: wdP){
				ProductVM vm = new ProductVM();
				vm.id = p.getId();
				vm.name = p.getName();
				vm.description = p .getDescription();
				vm.image = p.getImage();
				vm.qrCode = p.getQrCode();
				vm.specifications = p.getSpecifications();
				vm.status = p .getStatus();
				vm.createdTime = p.getCreatedTime();
				vm.approvedDate = p.getApprovedDate();
				vm.createdDate = p.getCreatedDate();
				vm.updatedDate = p.getUpdatedDate();
				vm.isApproved = Boolean.parseBoolean(p.getIsApproved());
				vm.mfrSku = p.getMfrSku();
				vm.storeSku = p.getStoreSku();
				vm.qistNo = p.getQistSku()+p.getSkuPostfix();
				VMs.add(vm);
			}
		}
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", VMs);
		return ok(Json.toJson(map));
	}

	public static Result scanProduct() throws ParseException{
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long userId = data.path("userId").asLong();
		String qrcode = data.path("qrCode").asText();
	
		WdCustomer w = WdCustomer.findById(userId);
		WdProduct p = WdProduct.findByQrCode(qrcode);
		if(w == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		if(p == null){
			map.put("status", "500");
			map.put("message", "Product not found.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
	
		List<WdProduct> prods = w.getWdProducts();
		if(!prods.contains(p)){
			prods.add(p);
			w.setWdProducts(prods);
			w.save();
		}
		
		
		Date today = new Date();
		 
		 SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	     String date = DATE_FORMAT.format(today);
	     System.out.println("Today in dd/MM/yyyy format : " + date);
		
		CustomerSession cs = CustomerSession.getCustomerSessionByRetailerAndDate(p.getWdRetailer(),DATE_FORMAT.parse(date));
		
		if(cs == null){
			cs = new CustomerSession();
			cs.setWdRetailer(p.getWdRetailer());
			cs.setWdCustomer(w);
			cs.setStart(DATE_FORMAT.parse(date));
			cs.save();
		}
		
		SessionProduct sp  = new  SessionProduct() ;
		sp.setWdProduct(p);
		sp.setPurchased(false);
		sp.setCustomerSession(cs);
 		sp.save();
 
		
		ProductVM vm = new ProductVM();
		vm.id = p.getId();
		vm.name = p.getName();
		vm.description = p .getDescription();
		vm.image = p.getImage();
		vm.qrCode = p.getQrCode();
		vm.specifications = p.getSpecifications();
		vm.status = p .getStatus();
		vm.createdTime = p.getCreatedTime();
		vm.approvedDate = p.getApprovedDate();
		vm.createdDate = p.getCreatedDate();
		vm.updatedDate = p.getUpdatedDate();
		vm.isApproved = Boolean.parseBoolean(p.getIsApproved());
		vm.mfrSku = p.getMfrSku();
		vm.storeSku = p.getStoreSku();
		vm.qistNo = p.getQistSku()+p.getSkuPostfix();
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", vm);
		return ok(Json.toJson(map));
	}

	
	public static  Result  getCustomerPurchaseHistory(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		
		Long userId = data.path("userId").asLong();
		ArrayList<CustomerSessionVM> customerSessionVMs = new ArrayList<CustomerSessionVM>();
		WdCustomer w = WdCustomer.findById(userId);

		if(w == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}else{
			
			List<CustomerSession>  cs = CustomerSession.getCustomerSessionByCustomerId(w);
			for(CustomerSession c :  cs){
				
				CustomerSessionVM customerSession = new CustomerSessionVM();
				customerSession.id = c.getId();
				
				WdRetailer r =  c.getWdRetailer();
                RetailerVM rvm = new RetailerVM();
                rvm.setId(r.getId());
                rvm.setBillingInformation(r.getBillingInformation());
                rvm.setBusinessName(r.getBusinessName());
                rvm.setCity(r.getCity());
                rvm.setContactPerson(r.getContactPerson());
                rvm.setEftpos(r.getEftpos());
                rvm.setEftposProvider(r.getEftposProvider());
                rvm.setFbLink(r.getFbLink());
                rvm.setGoodsCategories(r.getGoodsCategories());
                rvm.setGoogleLink(r.getGoogleLink());
                rvm.setGstNo(r.getGstNo());
                rvm.setIrNo(r.getIrNo());
                rvm.setLogoImage(r.getLogoImage());
                rvm.setMerchantId(r.getMerchantId());
                rvm.setMobileNo(r.getMobileNo());
                rvm.setPaymark(r.getPaymark());
                rvm.setQistSku(r.getQistSku());
                rvm.setReferedBy(r.getReferedBy());
                rvm.setSkuPostfix(r.getSkuPostfix());
                rvm.setStreetName(r.getStreetName());
                rvm.setStreetNo(r.getStreetNo());
                rvm.setSuburb(r.getSuburb());
                rvm.setTitle(r.getTitle());
                rvm.setTradingName(r.getTradingName());
                rvm.setTwitterLink(r.getTwitterLink());
                rvm.setWorkEmail(r.getWorkEmail());
                rvm.setWorkPhone1(r.getWorkPhone1());
                rvm.setWorkPhone2(r.getWorkPhone2());
                rvm.setWorkUrl(r.getWorkUrl());
                rvm.setQistNo(r.getQistSku()+r.getSkuPostfix());
                
                customerSession.retailerVM = rvm;
				customerSession.start = c.getStart();
				
				List<SessionProduct> products = SessionProduct.getSessionProductByCustomerId(c);
				
				for(SessionProduct s: products){
					ProductVM pvm = new ProductVM();
					pvm.id = s.getWdProduct().getId();
					pvm.approvedDate = s.getWdProduct().getApprovedDate();
					pvm.createdDate = s.getWdProduct().getCreatedDate();
					pvm.createdTime = s.getWdProduct().getCreatedTime();
					pvm.description = s.getWdProduct().getDescription();
					pvm.image = s.getWdProduct().getImage();
					pvm.isApproved =Boolean.parseBoolean(s.getWdProduct().getIsApproved());
					pvm.mfrSku = s.getWdProduct().getMfrSku();
					pvm.name = s.getWdProduct().getName();
					pvm.qistNo = s.getWdProduct().getQistSku()+s.getWdProduct().getSkuPostfix();
					pvm.qrCode = s.getWdProduct().getQrCode();
					pvm.specifications = s.getWdProduct().getSpecifications();
					pvm.status = s.getWdProduct().getStatus();
					
					
					customerSession.products.add(pvm);	
				}
				
				customerSessionVMs.add(customerSession);
			}	
			map.put("status", "200");
			map.put("message", "OK.");
			map.put("data", customerSessionVMs);
			return ok(Json.toJson(map));
		}
		
	} 
	
	public static void sendPasswordMail(String email,String pass) {
		final String username = "mindnervesdemo@gmail.com";
		final String password = "mindnervesadmin";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try
		{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("mindnervesdemo@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject("Password Recovery");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			String template = "<p>Email : "+email+" <br>Password :"+pass+"</p>"; 
			messageBodyPart.setContent(template, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("Sent test message successfully....");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	} 

}
