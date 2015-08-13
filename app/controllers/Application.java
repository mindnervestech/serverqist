package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import models.WdCategory;
import models.WdCustomer;
import models.WdCustomerOrderDetail;
import models.WdCustomerOrders;
import models.WdProduct;
import models.WdProductImage;
import models.WdRetailer;
import models.WdUser;

import org.w3c.dom.Document;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import viewmodels.CategoriesVM;
import viewmodels.CustomerOrderVM;
import viewmodels.CustomerSessionVM;
import viewmodels.CustomerVM;
import viewmodels.NewsVM;
import viewmodels.ProductForNewsVM;
import viewmodels.ProductVM;
import viewmodels.RetailerInfoVM;
import viewmodels.RetailerVM;
import views.html.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class Application extends Controller {

	public static final String lCertificate = Play.application().path().getAbsolutePath()+"/conf/NewFile.p12";

	//private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json";

	private static final double dist = 5.0;
	final static String FILEPATH = Play.application().configuration().getString("filePath");
	final static String DOMAIN_URL = Play.application().configuration().getString("domainUrl");
	final static String PRODUCT_IMAGE = Play.application().configuration().getString("productImage");
	final static String RETAILER_IMAGE =Play.application().configuration().getString("retailerImage");
	final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	final static DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result register(){
		File folder = new File(FILEPATH);
		if(!folder.exists()){
			folder.mkdir();
		}
		File subFolder = new File(FILEPATH + "/customers");
		if(!subFolder.exists()){
			subFolder.mkdir();
		}

		HashMap<String, Object> map = new HashMap<>();
		DynamicForm users = Form.form().bindFromRequest();

		String fname = users.get("firstName");
		String lname = users.get("lastName");
		String email = users.get("email");
		String pass = users.get("password");
		String repass = users.get("retypePassword");
		/*String address = users.get("address");*/
		String address1 = users.get("address1");
		String address2 = users.get("address2");
		String city = users.get("city");
		String state = users.get("state");
		String country = users.get("country");
		String zip = users.get("zip");
		String contactNo = users.get("contactNo");
		Boolean qcart = Boolean.parseBoolean(users.get("qCartMailingList"));
		String FacebookId = users.get("FacebookId");
		String GooglePlusId = users.get("GooglePlusId");
		String TwitterId = users.get("TwitterId");
		String imageUrl = users.get("imageUrl");
		Double lat = Double.parseDouble(users.get("lat"));
		Double lng = Double.parseDouble(users.get("lng"));
		//Double lat = 0D;
		//Double lng = 0D;
		String deviceType = users.get("deviceType");
		String deviceToken = users.get("deviceToken");
		String imageDataString = null ;

		WdCustomer c = new WdCustomer();

		if((FacebookId != null && !FacebookId.isEmpty()) || (GooglePlusId != null && !GooglePlusId.isEmpty()) 
				|| (TwitterId != null && !TwitterId.isEmpty())){

			if(FacebookId != null && !FacebookId.isEmpty()){
				WdCustomer cc = WdCustomer.findByFbId(FacebookId);
				if(cc != null){
					CustomerVM vm = new CustomerVM();
					vm.id = cc.getId();
					vm.name = (cc.getFirstname());
					//vm.lastName = c.getLastname();
					vm.email = cc.getEmail();
					//vm.password = cc.getPassword();
					/*vm.address = cc.getAddress();*/
					vm.address1 = cc.getAddress1();
					vm.address2 = cc.getAddress2();
					vm.city = cc.getCity();
					vm.state = cc.getState();
					vm.country = cc.getCountry();
					vm.zip = cc.getZip();
					vm.image = DOMAIN_URL+"getCustomerProfileImage/"+cc.getId();
					vm.contactNo = cc.getContactNo();
					vm.createdDate = df.format(cc.getCreatedDate());
					vm.updatedDate = df.format(cc.getUpdatedDate());
					vm.lastActive = df.format(cc.getLastActive());
					vm.qCartMailingList = Boolean.parseBoolean(cc.getQCartMailingList());
					vm.qistNo = cc.getQistSku()+String.format("%07d", cc.getSkuPostfix());

					cc.setLastActive(new Date());
					cc.update();

					HashMap<String, Object> map1 = new HashMap<>();
					map1.put("CTPYE", "S");
					map1.put("CustomerID", cc.getId().toString());
					map1.put("UserData",vm);
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
					CustomerVM vm = new CustomerVM();
					vm.id = cc.getId();
					vm.name = (cc.getFirstname());
					//vm.lastName = c.getLastname();
					vm.email = cc.getEmail();
					//vm.password = cc.getPassword();
					/*vm.address = cc.getAddress();*/
					vm.address1 = cc.getAddress1();
					vm.address2 = cc.getAddress2();
					vm.city = cc.getCity();
					vm.state = cc.getState();
					vm.country = cc.getCountry();
					vm.zip = cc.getZip();
					vm.image = DOMAIN_URL+"getCustomerProfileImage/"+cc.getId();
					vm.contactNo = cc.getContactNo();
					vm.createdDate = df.format(cc.getCreatedDate());
					vm.updatedDate = df.format(cc.getUpdatedDate());
					vm.lastActive = df.format(cc.getLastActive());
					vm.qCartMailingList = Boolean.parseBoolean(cc.getQCartMailingList());
					vm.qistNo = cc.getQistSku()+String.format("%07d", cc.getSkuPostfix());

					cc.setLastActive(new Date());
					cc.update();

					HashMap<String, Object> map1 = new HashMap<>();
					map1.put("CTPYE", "S");
					map1.put("CustomerID", cc.getId().toString());
					map1.put("UserData",vm);
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
					CustomerVM vm = new CustomerVM();
					vm.id = cc.getId();
					vm.name = (cc.getFirstname());
					//vm.lastName = c.getLastname();
					vm.email = cc.getEmail();
					//vm.password = cc.getPassword();
					/*vm.address = cc.getAddress();*/
					vm.address1 = cc.getAddress1();
					vm.address2 = cc.getAddress2();
					vm.city = cc.getCity();
					vm.state = cc.getState();
					vm.country = cc.getCountry();
					vm.zip = cc.getZip();
					vm.image = DOMAIN_URL+"getCustomerProfileImage/"+cc.getId();
					vm.contactNo = cc.getContactNo();
					vm.createdDate = df.format(cc.getCreatedDate());
					vm.updatedDate = df.format(cc.getUpdatedDate());
					vm.lastActive = df.format(cc.getLastActive());
					vm.qCartMailingList = Boolean.parseBoolean(cc.getQCartMailingList());
					vm.qistNo = cc.getQistSku()+String.format("%07d", cc.getSkuPostfix());

					cc.setLastActive(new Date());
					cc.update();

					HashMap<String, Object> map1 = new HashMap<>();
					map1.put("CTPYE", "S");
					map1.put("CustomerID", cc.getId().toString());
					map1.put("UserData",vm);
					map.put("status", "200");
					map.put("message", "OK.");
					map.put("data", map1);
					return ok(Json.toJson(map));
				}else{
					c.setTwitterid(TwitterId);
					c.setType("S");
				}	
			}
		} else {
			if(fname == null || fname.isEmpty() ||
					lname == null || lname.isEmpty()){
				map.put("status", "500");
				map.put("message", "Please fill required data.");
				map.put("data", null);
				return ok(Json.toJson(map));
			}
			if(pass != null){
				if(!pass.equals(repass)){
					map.put("status", "500");
					map.put("message", "Passwords don't match.");
					map.put("data", null);
					return ok(Json.toJson(map));
				}
			}
			WdCustomer cus = WdCustomer.findByEmail(email);
			if(cus != null){
				map.put("status", "500");
				map.put("message", "User already exists.");
				map.put("data", null);
				return ok(Json.toJson(map));
			}
			c.setType("M");
		}

		c.setFirstname(fname  +" "+  lname);
		//c.setLastname(lname);
		c.setEmail(email);
		c.setPassword(pass);
		/*c.setAddress(address);*/
		c.setAddress1(address1);
		c.setAddress2(address2);
		c.setCity(city);
		c.setState(state);
		c.setCountry(country);
		c.setZip(zip);
		c.setContactNo(contactNo);
		c.setImage(imageDataString);
		if(qcart.toString().equals("true")){
			c.setQCartMailingList("0");
		} else {
			c.setQCartMailingList("1");
		}
		Date d = new Date();
		c.setLastActive(d);
		c.setCreatedDate(d);
		c.setUpdatedDate(d);
		c.setQistSku("QCS");
		c.setLat(lat);
		c.setLng(lng);
		c.setDeviceType(deviceType);
		c.setDeviceToken(deviceToken);
		c.save();

		MultipartFormData body = request().body().asMultipartFormData();
		if (body != null) {
			FilePart filePart = body.getFile("image");
			if (filePart != null) {
				File f = filePart.getFile();
				try {      
					FileInputStream is = new FileInputStream(f);
					File cusFolder = new File(FILEPATH+"/customers/"+c.getId()+"/");
					if(!cusFolder.exists()){
						cusFolder.mkdir();
					}
					File file = new File(FILEPATH+"/customers/"+c.getId()+"/"+filePart.getFilename());
					int read = 0;
					byte[] bytes = new byte[1024];
					FileOutputStream os = new FileOutputStream(file);
					while ((read = is.read(bytes)) != -1) {
						os.write(bytes, 0, read);
					}
					os.close();
					imageDataString = filePart.getFilename();
					//FileInputStream imageInFile = new FileInputStream(f);
					//byte imageData[] = new byte[(int) f.length()];
					//imageInFile.read(imageData);
					//imageInFile.close();
					//imageDataString = Base64.encodeBase64URLSafeString(imageData);
				} catch (FileNotFoundException e) {
					System.out.println("Image not found" + e);
				} catch (IOException ioe) {
					System.out.println("Exception while reading the Image " + ioe);
				}
			}
		}  
		if(imageUrl != null && !imageUrl.isEmpty()){
			try {
				URL url = new URL(imageUrl);
				String fileName = url.getFile();

				File cusFolder = new File(FILEPATH+"/customers/"+c.getId()+"/");
				if(!cusFolder.exists()){
					cusFolder.mkdir();
				}

				String destName = FILEPATH+"/customers/"+c.getId()+"/"+fileName.substring(fileName.lastIndexOf("/")+1);

				InputStream is = url.openStream();
				OutputStream os = new FileOutputStream(destName);

				byte[] b = new byte[2048];
				int length;

				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}

				is.close();
				os.close();
				imageDataString = fileName.substring(fileName.lastIndexOf("/")+1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		c.setImage(imageDataString);
		c.setSkuPostfix((int)(long)c.getId());
		c.update();

		CustomerVM vm = new CustomerVM();
		vm.id = c.getId();
		vm.name = (c.getFirstname());
		//vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		//vm.password = c.getPassword();
		/*vm.address = c.getAddress();*/
		vm.address1 = c.getAddress1();
		vm.address2 = c.getAddress2();
		vm.city = c.getCity();
		vm.state = c.getState();
		vm.country = c.getCountry();
		vm.zip = c.getZip();
		vm.image = DOMAIN_URL+"getCustomerProfileImage/"+c.getId();
		vm.contactNo = c.getContactNo();
		vm.createdDate = df.format(c.getCreatedDate());
		vm.updatedDate = df.format(c.getUpdatedDate());
		vm.lastActive = df.format(c.getLastActive());
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+String.format("%07d", c.getSkuPostfix());

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
		Double lat = data.path("lat").asDouble();
		Double lng = data.path("lng").asDouble();
		String deviceType = data.path("deviceType").asText();
		String deviceToken = data.path("deviceToken").asText();
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
		vm.name = (c.getFirstname());
		//vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		//vm.password = c.getPassword();
		/*vm.address = c.getAddress();*/
		vm.address1 = c.getAddress1();
		vm.address2 = c.getAddress2();
		vm.city = c.getCity();
		vm.state = c.getState();
		vm.country = c.getCountry();
		vm.zip = c.getZip();
		vm.image = DOMAIN_URL+"getCustomerProfileImage/"+c.getId();
		vm.contactNo = c.getContactNo();
		vm.createdDate = df.format(c.getCreatedDate());
		vm.updatedDate = df.format(c.getUpdatedDate());
		vm.lastActive = df.format(c.getLastActive());
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+String.format("%07d", c.getSkuPostfix());

		c.setLastActive(new Date());
		c.setLat(lat);
		c.setLng(lng);
		c.setDeviceType(deviceType);
		c.setDeviceToken(deviceToken);
		c.update();

		HashMap<String, Object> map1 = new HashMap<>();
		map1.put("CTPYE", c.getType());
		map1.put("CustomerID", c.getId().toString());
		map1.put("UserData",vm);
		map.put("status", "200");
		map.put("message", "Login successfull.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}
	
	public static Result updateLocation(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long id = data.path("userId").asLong();
		Double lat = data.path("lat").asDouble();
		Double lng = data.path("lng").asDouble();
		WdCustomer c = WdCustomer.findById(id);
		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		c.setLat(lat);
		c.setLng(lng);
		c.update();
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", null);
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
			c.setUpdatedDate(new Date());
			c.update();
		}
		map.put("status", "200");
		map.put("message", "Email sent with new password.");
		map.put("data", null);
		return ok(Json.toJson(map));
	}

	public static Result getCustomerProfile(){
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> map1 = new HashMap<>();
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
		vm.name = (c.getFirstname());
		//vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		vm.password = c.getPassword();
		/*		vm.address = c.getAddress();*/
		vm.address1 = c.getAddress1();
		vm.address2 = c.getAddress2();
		vm.city = c.getCity();
		vm.state = c.getState();
		vm.country = c.getCountry();
		vm.zip = c.getZip();
		vm.image = DOMAIN_URL+"getCustomerProfileImage/"+c.getId();
		vm.contactNo = c.getContactNo();
		vm.createdDate = df.format(c.getCreatedDate());
		vm.updatedDate = df.format(c.getUpdatedDate());
		vm.lastActive = df.format(c.getLastActive());
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+String.format("%07d", c.getSkuPostfix());
		map1.put("CTPYE", c.getType());
		map1.put("CustomerID", c.getId().toString());
		map1.put("UserData",vm);
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}

	public static Result changeCustomerName(){
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> map1 = new HashMap<>();
		JsonNode data = request().body().asJson();

		Long id = data.path("userId").asLong();
		WdCustomer c = WdCustomer.findById(id); 
		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		c.setFirstname(data.path("name").asText());
		//c.setLastname(data.path("lastName").asText());
		c.setUpdatedDate(new Date());
		c.update();

		CustomerVM vm = new CustomerVM();
		vm.id = c.getId();
		vm.name = (c.getFirstname());
		//vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		vm.password = c.getPassword();
		/*vm.address = c.getAddress();*/
		vm.address1 = c.getAddress1();
		vm.address2 = c.getAddress2();
		vm.city = c.getCity();
		vm.state = c.getState();
		vm.country = c.getCountry();
		vm.zip = c.getZip();
		vm.image = DOMAIN_URL+"getCustomerProfileImage/"+c.getId();
		vm.contactNo = c.getContactNo();
		vm.createdDate = df.format(c.getCreatedDate());
		vm.updatedDate = df.format(c.getUpdatedDate());
		vm.lastActive = df.format(c.getLastActive());
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+String.format("%07d", c.getSkuPostfix());
		
		map1.put("CTPYE", c.getType());
		map1.put("CustomerID", c.getId().toString());
		map1.put("UserData",vm);
		
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}

	public static Result changeCustomerPassword(){
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> map1 = new HashMap<>();
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
		c.setUpdatedDate(new Date());
		c.update();

		CustomerVM vm = new CustomerVM();
		vm.id = c.getId();
		vm.name =(c.getFirstname());
		//vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		vm.password = c.getPassword();
		/*vm.address = c.getAddress();*/
		vm.address1 = c.getAddress1();
		vm.address2 = c.getAddress2();
		vm.city = c.getCity();
		vm.state = c.getState();
		vm.country = c.getCountry();
		vm.zip = c.getZip();
		vm.image = DOMAIN_URL+"getCustomerProfileImage/"+c.getId();
		vm.contactNo = c.getContactNo();
		vm.createdDate = df.format(c.getCreatedDate());
		vm.updatedDate = df.format(c.getUpdatedDate());
		vm.lastActive = df.format(c.getLastActive());
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+String.format("%07d", c.getSkuPostfix());

		map1.put("CTPYE", c.getType());
		map1.put("CustomerID", c.getId().toString());
		map1.put("UserData",vm);
		
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}

	public static Result changeCustomerAddress(){
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> map1 = new HashMap<>();

		JsonNode data = request().body().asJson();
		Long id = data.path("userId").asLong();
		WdCustomer c = WdCustomer.findById(id); 
		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		c.setAddress1(data.path("address1").asText());
		c.setAddress2(data.path("address2").asText());
		c.setCity(data.path("city").asText());
		c.setState(data.path("state").asText());
		c.setCountry(data.path("country").asText());
		c.setZip(data.path("zip").asText());
		c.setUpdatedDate(new Date());
		c.update();

		CustomerVM vm = new CustomerVM();
		vm.id = c.getId();
		vm.name =(c.getFirstname());
		//vm.lastName = c.getLastname();
		vm.email = c.getEmail();
		vm.password = c.getPassword();
		/*vm.address = c.getAddress();*/
		vm.address1 = c.getAddress1();
		vm.address2 = c.getAddress2();
		vm.city = c.getCity();
		vm.state = c.getState();
		vm.country = c.getCountry();
		vm.zip = c.getZip();
		vm.image = DOMAIN_URL+"getCustomerProfileImage/"+c.getId();
		vm.contactNo = c.getContactNo();
		vm.createdDate = df.format(c.getCreatedDate());
		vm.updatedDate = df.format(c.getUpdatedDate());
		vm.lastActive = df.format(c.getLastActive());
		vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
		vm.qistNo = c.getQistSku()+String.format("%07d", c.getSkuPostfix());

		map1.put("CTPYE", c.getType());
		map1.put("CustomerID", c.getId().toString());
		map1.put("UserData",vm);
		
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}


	public static Result getNews(){
		HashMap<String,Object> map = new HashMap<>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		Date todate1 = cal.getTime();    
		String fromdate = dateFormat.format(todate1);

		List<WdUser> user = WdUser.findBydate(fromdate);

		List<NewsVM> VMn = new ArrayList<NewsVM>();
		for(WdUser s:user){
			System.out.println("User Id "+s.getId());
			NewsVM vmn = new NewsVM();
			WdRetailer w=WdRetailer.findByUser(s);
			RetailerVM vm = new RetailerVM();
			vm.setId(w.getId());
			vm.setBusinessName(w.getBusinessName());
			vm.setStreetName(w.getStreetName());
			vm.setStreetNo(w.getStreetNo());
			vm.setSuburb(w.getSuburb());
			vm.setLogoImage(RETAILER_IMAGE + w.getLogoImage());
			vm.setTradingName(w.getTradingName());
			vm.setCity(w.getCity());
			vm.setContactPerson(w.getContactPerson());
			vm.setWorkEmail(w.getWorkEmail());
			vm.setQistNo(w.getQistSku()+String.format("%07d", w.getSkuPostfix()));
			vmn.news="New store \'"+w.getBusinessName()+"\' added.";
			vmn.retailerData=vm;
			VMn.add(vmn);
		}

		List<WdProduct> product = WdProduct.findByProductDate(fromdate);

		for(WdProduct p : product)
		{
			NewsVM vmn = new NewsVM();
			ProductForNewsVM vm = new ProductForNewsVM();
			vm.id = p.getId();
			vm.name = p.getName();
			vm.description = p .getDescription();
			vm.status = p .getStatus();
			vm.isApproved = Boolean.parseBoolean(p.getIsApproved());
			vm.mfrSku = p.getMfrSku();
			vm.storeSku = p.getStoreSku();
			vm.qistNo = p.getQistSku() + String.format("%07d", p.getSkuPostfix());
			vm.qistPrice = p.getQistPrice();
			if(p.getApprovedDate() != null){
				vm.approvedDate = df.format(p.getApprovedDate());
			}
			vm.createdDate = df.format(p.getCreatedDate());
			vm.updatedDate = df.format(p.getUpdatedDate());
			vm.validFromDate = df.format(p.getValidFromDate());
			vm.validToDate = df.format(p.getValidToDate());
			for(WdProductImage i:p.getProductImages()){
				String url = PRODUCT_IMAGE + i.getProductImageName();
				vm.images.add(url);
			}
            vm.storeName = p.getWdRetailer().getBusinessName();
            vm.city = p.getWdRetailer().getCity();
            vm.streetName = p.getWdRetailer().getStreetName();
            vm.streetNo = p.getWdRetailer().getStreetNo();
			vmn.news="New prouct \'"+p.getName()+"\' added in store \'"+p.getWdRetailer().getBusinessName()+"\'";
			vmn.productData=vm;
			VMn.add(vmn);
			
			
		}


		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", VMn);
		return ok(Json.toJson(map));
	}


	public static Result sendPushNotification() {
		HashMap<String,Object> map = new HashMap<>();
		System.out.println("sendPushNotification " + lCertificate);
		String password = "123456";
		ApnsService service = APNS.newService().withCert(lCertificate, password)
				.withSandboxDestination()
				.build();
		System.out.println("sendPushNotification");
		String payload = APNS.newPayload().alertBody("hello..........").build();
		com.notnoop.apns.ApnsNotification notification = service.push("cc3a0cda5875545f5c86372418834d1dc80425df34fa73df5f030052ccf51a8a", payload);
		System.out.println("Sending notification message!");
		map.put("status", "200");
		return ok(Json.toJson(map));
	}

	private static final String AUTHORIZATION_KEY = "AIzaSyBLKOwfgUvHxVy6MxDS57XjXSnJWgwR_rI";

	//senderID = 495831502323

	public static Result sendPushNotification1() {
		HashMap<String,Object> map = new HashMap<>();
		try {
			com.google.android.gcm.server.Sender sender = new com.google.android.gcm.server.Sender(AUTHORIZATION_KEY);
			com.google.android.gcm.server.Message message = new com.google.android.gcm.server.Message.Builder().timeToLive(30).collapseKey("message")
					.delayWhileIdle(true)
					.addData("message", "HELLO...").build();
			com.google.android.gcm.server.Result result = sender.send(message, "APA91bEK8D6FEe5bC6NYnbN_VvIwh3xk6dmrqrAiqQKQ4zZHi2JM2LDJDuW-RNvhRxn9DK6gzQDKkCh-uW3aSi8dDnaBCU87b1SkfdpHzo1-0oyrI4QwsmfGeuE9HTNO9bXKT3-CvROQ", 1);
			System.out.println(result);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		map.put("status", "200");
		return ok(Json.toJson(map));
	}

	/*public static Result getMyOffers() throws Exception{
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		String lat = data.path("lat").asText();
		String lon = data.path("lng").asText();
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
				vm.setId(w.getId());
				vm.setBusinessName(w.getBusinessName());
				vm.setStreetName(w.getStreetName());
				vm.setStreetNo(w.getStreetNo());
				vm.setSuburb(w.getSuburb());
				vm.setLogoImage(RETAILER_IMAGE + w.getLogoImage());
				vm.setTradingName(w.getTradingName());
				vm.setCity(w.getCity());
				vm.setContactPerson(w.getContactPerson());
				vm.setWorkEmail(w.getWorkEmail());
				vm.setQistNo(w.getQistSku()+String.format("%07d", w.getSkuPostfix()));
				vmList.add(vm);
			}
		}
		String URL = "http://maps.googleapis.com/maps/api/geocode/json";
		URL url = new URL(URL + "?latlng="
				+ lat+","+lon + "&sensor=true");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output = "",full="";
		output = br.readLine();
		while (output != null) {

			if(output.indexOf("formatted_address")>-1){
				System.out.println(output);

				full=output.substring(32);
				if(full.indexOf(",")>-1)
				{
					full=full.substring(0,full.lastIndexOf(',')-1);
				}
				break;
			}
			output = br.readLine();  
		}
		List<ProductVM> prods = getProducts(currentId);
		HashMap<String, Object> map1 = new HashMap<>();
		map1.put("products", prods);
		map1.put("retailers", vmList);
		map1.put("location",full);
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}*/

	public static Result getMyOffers() throws Exception{
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		String lat = data.path("lat").asText();
		String lon = data.path("lng").asText();
		Long customerId = data.path("userId").asLong();
		
		if(customerId==0){
			map.put("status", "500");
			map.put("message", "Internal sever error");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		
		double lon1 = Double.parseDouble(lon);
		double latn1 = Double.parseDouble(lat);
		System.out.println("retailer enter");
		List<WdRetailer> wd = WdRetailer.findAll();
		System.out.println("find all retailer size"+wd.size());
		List<RetailerVM> vmList = new ArrayList<>();
		for(WdRetailer w:wd){
			String fullAddress = w.getStreetNo()+","+w.getStreetName()+","+w.getSuburb()+","+w.getCity();
			String[] latLongs = getLatLongPositions(fullAddress);
			if(latLongs != null){
				double lon2 = Double.parseDouble(latLongs[1]);
				double lat2 = Double.parseDouble(latLongs[0]);
				double mydist = distance(latn1,lon1,lat2,lon2);
				if(mydist <= dist ){
					RetailerVM vm = new RetailerVM();
					vm.setId(w.getId());
					vm.setBusinessName(w.getBusinessName());
					vm.setStreetName(w.getStreetName());
					vm.setStreetNo(w.getStreetNo());
					vm.setSuburb(w.getSuburb());
					vm.setLogoImage(RETAILER_IMAGE + w.getLogoImage());
					vm.setTradingName(w.getTradingName());
					vm.setCity(w.getCity());
					vm.setContactPerson(w.getContactPerson());
					vm.setWorkEmail(w.getWorkEmail());
					vm.setQistNo(w.getQistSku()+String.format("%07d", w.getSkuPostfix()));
					vm.setProducts(getProducts1(w.getId(),customerId));
					vmList.add(vm);
				}
			}
		}
		String URL = "http://maps.googleapis.com/maps/api/geocode/json";
		URL url = new URL(URL + "?latlng="
				+ lat+","+lon + "&sensor=true");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output = "",full="";
		output = br.readLine();
		while (output != null) {
			if(output.indexOf("formatted_address")>-1){
				System.out.println(output);
				full=output.substring(32);
				if(full.indexOf(",")>-1)
				{
					full=full.substring(0,full.lastIndexOf(',')-1);
				}
				break;
			}
			output = br.readLine();  
		}
		HashMap<String, Object> map1 = new HashMap<>();
		map1.put("retailers", vmList);
		map1.put("location",full);
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}

	private static List<ProductVM> getProducts1(Long id,Long userId) {
		System.out.println("wwwwwwwwwwwww"+id);
		System.out.println("qqqqqqqqqqqq"+userId);
	    WdRetailer wd = WdRetailer.findById(id);
	    System.out.println("ididid"+wd.getId());
		WdCustomer wdc = WdCustomer.findById(userId);
		System.out.println("eeeeeeeeeeee"+wdc.getId());
		List<WdProduct> prod = WdProduct.findByRetailer(wd);
		
		
		
		List<ProductVM> plist = new ArrayList<>();
		for(WdProduct p : prod){
			ProductVM vm = new ProductVM();
			vm.id = p.getId();
			vm.name = p.getName();
			vm.description = p .getDescription();
			vm.status = p .getStatus();
			vm.isApproved = Boolean.parseBoolean(p.getIsApproved());
			vm.mfrSku = p.getMfrSku();
			vm.storeSku = p.getStoreSku();
			vm.qistNo = p.getQistSku() + String.format("%07d", p.getSkuPostfix());
			vm.qistPrice = p.getQistPrice();
			vm.originalPrice = p.getOriginalPrice();
			if(p.getApprovedDate() != null){
				vm.approvedDate = df.format(p.getApprovedDate());
			}
			vm.createdDate = df.format(p.getCreatedDate());
			vm.updatedDate = df.format(p.getUpdatedDate());
			vm.validFromDate = df.format(p.getValidFromDate());
			vm.validToDate = df.format(p.getValidToDate());
			for(WdProductImage i:p.getProductImages()){
				String url = PRODUCT_IMAGE + i.getProductImageName();
				vm.images.add(url);
			}
			List<CustomerSession> cs = CustomerSession.getRetailerCustomerSession(wd,wdc);
			if(cs.size()>0){
				for(CustomerSession c : cs){
				  System.out.println("enter");
					List<SessionProduct> p1 = SessionProduct.getProductStatus(c,p);
				  System.out.println("exit");
				  
				  if(p1.size()>0){
					  for(SessionProduct pp :p1){
				  System.out.println("in session"+pp.getStatus());
				  if(pp.getStatus().equals("None")){
					  vm.offerStatus="Wishlist";
				  }else{
				  vm.offerStatus = pp.getStatus();
				  }
					  }
				  }else{
					  if(p.getStatus().equals("active")){
							vm.offerStatus = "Available";
							}else{
								vm.offerStatus = "Not Available";
							}
				  }
				  
				}
			}else{
				if(p.getStatus().equals("active")){
				vm.offerStatus = "Available";
				}else{
					vm.offerStatus = "Not Available";
				}
				
			}
			plist.add(vm);
		}
		return plist;
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
			vm.status = p .getStatus();
			vm.isApproved = Boolean.parseBoolean(p.getIsApproved());
			vm.mfrSku = p.getMfrSku();
			vm.storeSku = p.getStoreSku();
			vm.qistNo = p.getQistSku() + String.format("%07d", p.getSkuPostfix());
			vm.qistPrice = p.getQistPrice();
			vm.originalPrice = p.getOriginalPrice();
			if(p.getApprovedDate() != null){
				vm.approvedDate = df.format(p.getApprovedDate());
			}
			vm.createdDate = df.format(p.getCreatedDate());
			vm.updatedDate = df.format(p.getUpdatedDate());
			vm.validFromDate = df.format(p.getValidFromDate());
			vm.validToDate = df.format(p.getValidToDate());
			for(WdProductImage i:p.getProductImages()){
				String url = PRODUCT_IMAGE + i.getProductImageName();
				vm.images.add(url);
			}
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
		vm.setId(w.getId());
		vm.setBusinessName(w.getBusinessName());
		vm.setStreetName(w.getStreetName());
		vm.setStreetNo(w.getStreetNo());
		vm.setSuburb(w.getSuburb());
		vm.setTradingName(w.getTradingName());
		vm.setCity(w.getCity());
		vm.setLogoImage(RETAILER_IMAGE + w.getLogoImage());
		vm.setContactPerson(w.getContactPerson());
		vm.setWorkEmail(w.getWorkEmail());
		vm.setQistNo(w.getQistSku()+String.format("%07d", w.getSkuPostfix()));
		vm.setProducts(getProducts(id));
		HashMap<String, Object> map1 = new HashMap<>();
		map1.put("retailer", vm);
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
				//throw new Exception("Error from the API - response status: "+status);
			}
		}
		return null;
	}

	public static Result confirmPurchaseOrder(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long userId = data.path("userId").asLong();
		String address1 = data.path("address1").asText();
		String address2 = data.path("address2").asText();
		String city = data.path("city").asText();
		String state = data.path("state").asText();
		String country = data.path("country").asText();
		String zip = data.path("zip").asText();
		WdCustomer c = WdCustomer.findById(userId);
		Date date = new Date();
		if(c==null)
		{
			map.put("status", "500");
			map.put("message", "User does not exist.");
			return ok(Json.toJson(map));	
		}else{
			CustomerSession cs1 = CustomerSession.getCustomerSessionByActiveCustomerId(c);
			if(cs1!=null){
				WdCustomerOrders o = new WdCustomerOrders();
				o.setWdCustomer(cs1.getWdCustomer());
				o.setWdRetailer(cs1.getWdRetailer());
				if(cs1.getWdCustomer().getAddress1() != null){
					o.setBillingAddress1(cs1.getWdCustomer().getAddress1());
					o.setBillingAddress2(cs1.getWdCustomer().getAddress2());
					o.setBillingCity(cs1.getWdCustomer().getCity());
					o.setBillingState(cs1.getWdCustomer().getState());
					o.setBillingCountry(cs1.getWdCustomer().getCountry());
					o.setBillingZip(cs1.getWdCustomer().getZip());
				} else {
					o.setBillingAddress1(address1);
					o.setBillingAddress2(address2);
					o.setBillingCity(city);
					o.setBillingState(state);
					o.setBillingCountry(country);
					o.setBillingZip(zip);
				}
				o.setShippingAddress1(address1);
				o.setShippingAddress2(address2);
				o.setShippingCity(city);
				o.setShippingState(state);
				o.setShippingCountry(country);
				o.setShippingZip(zip);
				o.setStatus("pending");
				o.setCreatedDate(date);
				o.setUpdatedDate(date);
				o.setSession(cs1);
				o.save();
				//create and save customer order with all details
				for(SessionProduct p : cs1.getSessionProducts()){
					if(p.getStatus().equals("Cart")){
						//create and save customer order details for each session product in cart
						WdCustomerOrderDetail od = new WdCustomerOrderDetail();
						od.setWdCustomerOrder(o);
						od.setProductId(p.getWdProduct().getId());
						od.setCreatedDate(date);
						od.setUpdatedDate(date);
						od.setQuantity(1L);
						od.setProductPrice(p.getWdProduct().getQistPrice());
						od.save();
						p.setStatus("Purchased");
						p.update();	
					}
				}
				cs1.setEnd(date);
				cs1.update();
			}
		}
		map.put("status","200");
		map.put("message","OK.");
		map.put("data",null);
		return ok(Json.toJson(map));
	}


	public static Result getCustomerCart(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long id = data.path("userId").asLong();
		WdCustomer c = WdCustomer.findById(id); 
		ArrayList<ProductVM> VMs =  new ArrayList<ProductVM>();
		ArrayList<RetailerVM> VMr = new ArrayList<RetailerVM>();
		Map<String , Object> map1 = new HashMap<>();

		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}else{

			CustomerSession cs1 = CustomerSession.getCustomerSessionByActiveCustomerId(c);

			if(cs1 != null){
				CustomerSessionVM cvm = new CustomerSessionVM();
				for(SessionProduct p:cs1.getSessionProducts()){
					if(p.getStatus().equals("Cart"))
					{
						ProductVM vm = new ProductVM();
						vm.id = p.getWdProduct().getId();
						vm.name = p.getWdProduct().getName();
						vm.description = p .getWdProduct().getDescription();
						vm.status = p .getStatus();
						vm.isApproved = Boolean.parseBoolean(p.getWdProduct().getIsApproved());
						vm.mfrSku = p.getWdProduct().getMfrSku();
						vm.storeSku = p.getWdProduct().getStoreSku();
						vm.qistNo = p.getWdProduct().getQistSku() + String.format("%07d", p.getWdProduct().getSkuPostfix());
						vm.qistPrice = p.getWdProduct().getQistPrice();
						vm.originalPrice = p.getWdProduct().getOriginalPrice();
						if(p.getWdProduct().getApprovedDate() != null){
							vm.approvedDate = df.format(p.getWdProduct().getApprovedDate());
						}
						vm.createdDate = df.format(p.getWdProduct().getCreatedDate());
						vm.updatedDate = df.format(p.getWdProduct().getUpdatedDate());
						vm.validFromDate = df.format(p.getWdProduct().getValidFromDate());
						vm.validToDate = df.format(p.getWdProduct().getValidToDate());
						for(WdProductImage i:p.getWdProduct().getProductImages()){
							String url = PRODUCT_IMAGE + i.getProductImageName();
							vm.images.add(url);
						}
						VMs.add(vm);
					}
				}
				WdRetailer r = cs1.getWdRetailer();
				RetailerVM rvm = new RetailerVM();
				rvm.setId(r.getId());
				rvm.setBusinessName(r.getBusinessName());
				rvm.setStreetName(r.getStreetName());
				rvm.setStreetNo(r.getStreetNo());
				rvm.setSuburb(r.getSuburb());
				rvm.setLogoImage(RETAILER_IMAGE + r.getLogoImage());
				rvm.setTradingName(r.getTradingName());
				rvm.setCity(r.getCity());
				rvm.setContactPerson(r.getContactPerson());
				rvm.setWorkEmail(r.getWorkEmail());
				rvm.setQistNo(r.getQistSku()+String.format("%07d", r.getSkuPostfix()));
				rvm.setProducts(VMs);
				cvm.retailerVM = rvm;
				cvm.start = df1.format(cs1.getStart());
				map.put("status", "200");
				map.put("message", "OK.");
				map.put("data", cvm);
			} else {
				map.put("status","200");
				map.put("message","OK.");
				map.put("data",null);
			}
		}
		return ok(Json.toJson(map));
	}

	public static String timecal(String databasetime){
		Date d = new Date();
		Date d1=null;
		try {
			d1=df1.parse(databasetime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time=d.getTime()-d1.getTime();
		long diffMinutes = time / (60 * 1000) % 60;
		long diffHours = time / (60 * 60 * 1000) % 24;
		long min=(diffHours *60)+diffMinutes;
		return (min+"");
	}

	public static Result getCustomerWishlist(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long id = data.path("userId").asLong();
		WdCustomer c = WdCustomer.findById(id); 

		ArrayList<CustomerSessionVM> VMcs = new ArrayList<CustomerSessionVM>();
		ArrayList<CategoriesVM> VMc = new ArrayList<CategoriesVM>();
		Map<String , Object> map1 = new HashMap<>();

		if(c == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}else{

			List<CustomerSession> cs1 = CustomerSession.getCustomerSessionByAllCustomerId(c);

			if(cs1 != null){
				for(CustomerSession cs:cs1)
				{
					ArrayList<ProductVM> VMs =  new ArrayList<ProductVM>();
					CustomerSessionVM csvm = new CustomerSessionVM();
					for(SessionProduct p:cs.getSessionProducts()){
						if(p.getStatus().equals("Wishlist"))
						{
							ProductVM vm = new ProductVM();
							vm.id = p.getWdProduct().getId();
							vm.name = p.getWdProduct().getName();
							vm.description = p .getWdProduct().getDescription();
							vm.status = p .getStatus();
							vm.isApproved = Boolean.parseBoolean(p.getWdProduct().getIsApproved());
							vm.mfrSku = p.getWdProduct().getMfrSku();
							vm.storeSku = p.getWdProduct().getStoreSku();
							vm.qistNo = p.getWdProduct().getQistSku() + String.format("%07d", p.getWdProduct().getSkuPostfix());
							vm.qistPrice = p.getWdProduct().getQistPrice();
							vm.originalPrice = p.getWdProduct().getOriginalPrice();
							if(p.getWdProduct().getApprovedDate() != null){
								vm.approvedDate = df.format(p.getWdProduct().getApprovedDate());
							}
							vm.createdDate = df.format(p.getWdProduct().getCreatedDate());
							vm.updatedDate = df.format(p.getWdProduct().getUpdatedDate());
							vm.validFromDate = df.format(p.getWdProduct().getValidFromDate());
							vm.validToDate = df.format(p.getWdProduct().getValidToDate());
							for(WdProductImage i:p.getWdProduct().getProductImages()){
								String url = PRODUCT_IMAGE + i.getProductImageName();
								vm.images.add(url);
							}
							for(WdCategory cat:p.getWdProduct().getWdCategories())
							{
								vm.categories.add(cat.getName());
							}
							VMs.add(vm);
						}
					}
					if(VMs.size()>0)
					{
						//map1.put("products",VMs);
						WdRetailer r = cs.getWdRetailer();
						RetailerVM rvm = new RetailerVM();
						rvm.setId(r.getId());
						rvm.setBusinessName(r.getBusinessName());
						rvm.setStreetName(r.getStreetName());
						rvm.setStreetNo(r.getStreetNo());
						rvm.setSuburb(r.getSuburb());
						rvm.setLogoImage(RETAILER_IMAGE + r.getLogoImage());
						rvm.setTradingName(r.getTradingName());
						rvm.setCity(r.getCity());
						rvm.setContactPerson(r.getContactPerson());
						rvm.setWorkEmail(r.getWorkEmail());
						rvm.setQistNo(r.getQistSku()+String.format("%07d", r.getSkuPostfix()));
						rvm.setProducts(VMs);
						//map1.put("store",rvm);
						csvm.retailerVM = rvm;
						VMcs.add(csvm);
					}
				}
				map1.put("wishlist",VMcs);
				List<WdCategory> wc = WdCategory.getByMainParent();

				for(WdCategory w : wc)
				{
					CategoriesVM cate = new CategoriesVM();
					cate.id = w.getId();
					cate.name = w.getName();
					VMc.add(cate);
				}
				map1.put("categories",VMc);



			}
		}
		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", map1);
		return ok(Json.toJson(map));
	}


	public static Result addToCart() throws ParseException{
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long userId = data.path("userId").asLong();
		Long productId = data.path("productId").asLong();

		WdCustomer w = WdCustomer.findById(userId);
		WdProduct p = WdProduct.findByProductId(productId);
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

		Date today = new Date();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
		String date = DATE_FORMAT.format(today);

		CustomerSession cs1 = CustomerSession.getCustomerSessionByActiveCustomerId(w);
		System.out.println(cs1);
		if(cs1 != null)
		{
			if(cs1.getWdRetailer().getId() == p.getWdRetailer().getId()){
				for(SessionProduct sp1 : cs1.getSessionProducts())
				{
					if(sp1.getWdProduct().getId()==p.getId())
					{
						map.put("status", "500");
						map.put("message", "Already added in cart.");
						map.put("data", null);
						return ok(Json.toJson(map));
					}
				}
				SessionProduct sp  = new  SessionProduct() ;
				sp.setWdProduct(p);
				/*sp.setPurchased(false);*/
				sp.setCustomerSession(cs1);
				sp.setStatus("Cart");
				sp.save();

			}else{
				for(SessionProduct sp:cs1.getSessionProducts()){
					sp.setStatus("Wishlist");
					sp.update();
				}
				cs1.setEnd(today);
				cs1.update();
				cs1 = new CustomerSession();
				cs1.setWdRetailer(p.getWdRetailer());
				cs1.setWdCustomer(w);
				cs1.setStart(today);
				cs1.save();
				SessionProduct sp  = new  SessionProduct() ;
				sp.setWdProduct(p);
				/*sp.setPurchased(false);*/
				sp.setCustomerSession(cs1);
				sp.setStatus("Cart");
				sp.save();

			}
		}else{
			cs1 = new CustomerSession();
			cs1.setWdRetailer(p.getWdRetailer());
			cs1.setWdCustomer(w);
			cs1.setStart(today);
			cs1.save();
			SessionProduct sp  = new  SessionProduct() ;
			sp.setWdProduct(p);
			/*sp.setPurchased(false);*/
			sp.setCustomerSession(cs1);
			sp.setStatus("Cart");
			sp.save();

		}

		Map<String, Object> res = new HashMap<>();

		ProductVM vm = new ProductVM();
		vm.id = p.getId();
		vm.name = p.getName();
		vm.description = p .getDescription();
		vm.status = p .getStatus();
		vm.isApproved = Boolean.parseBoolean(p.getIsApproved());
		vm.mfrSku = p.getMfrSku();
		vm.storeSku = p.getStoreSku();
		vm.qistNo = p.getQistSku() + String.format("%07d", p.getSkuPostfix());
		vm.qistPrice = p.getQistPrice();
		vm.originalPrice = p.getOriginalPrice();
		if(p.getApprovedDate() != null){
			vm.approvedDate = df.format(p.getApprovedDate());
		}
		vm.createdDate = df.format(p.getCreatedDate());
		vm.updatedDate = df.format(p.getUpdatedDate());
		vm.validFromDate = df.format(p.getValidFromDate());
		vm.validToDate = df.format(p.getValidToDate());
		for(WdProductImage i:p.getProductImages()){
			String url = PRODUCT_IMAGE + i.getProductImageName();
			vm.images.add(url);
		}

		res.put("product", vm);

		WdRetailer r = p.getWdRetailer();
		RetailerVM rvm = new RetailerVM();
		rvm.setId(r.getId());
		rvm.setBusinessName(r.getBusinessName());
		rvm.setStreetName(r.getStreetName());
		rvm.setStreetNo(r.getStreetNo());
		rvm.setLogoImage(RETAILER_IMAGE + r.getLogoImage());
		rvm.setSuburb(r.getSuburb());
		rvm.setTradingName(r.getTradingName());
		rvm.setCity(r.getCity());
		rvm.setContactPerson(r.getContactPerson());
		rvm.setWorkEmail(r.getWorkEmail());
		rvm.setQistNo(r.getQistSku()+String.format("%07d", r.getSkuPostfix()));

		res.put("store", rvm);

		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", res);
		return ok(Json.toJson(map));
	}

	public static Result addToWishlist(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long userId = data.path("userId").asLong();
		Long productId = data.path("productId").asLong();

		WdCustomer w = WdCustomer.findById(userId);
		if(w == null){
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
		
		WdProduct p = WdProduct.findByProductId(productId);
		if(p == null){
			map.put("status", "500");
			map.put("message", "Product not found.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}

		Date today = new Date();

		CustomerSession cs1 = new CustomerSession();
		cs1.setWdRetailer(p.getWdRetailer());
		cs1.setWdCustomer(w);
		cs1.setStart(today);
		cs1.setEnd(today);
		cs1.save();
		SessionProduct sp  = new  SessionProduct() ;
		sp.setWdProduct(p);
		sp.setCustomerSession(cs1);
		sp.setStatus("Wishlist");
		sp.save();

		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data", null);
		return ok(Json.toJson(map));
	}



	public static Result scanProduct() throws ParseException{
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long userId = data.path("userId").asLong();
		String qrcode = data.path("qrCode").asText();
		String showpopup="NO";

		WdCustomer w = WdCustomer.findById(userId);
		WdProduct p = WdProduct.findByQistSkuAndSkuPostfix(qrcode);
      
		Map<String, Object> res = new HashMap<>();

		if(w == null){
			if(p == null){
				map.put("status", "500");
				map.put("message", "Product not found.");
				map.put("data", null);
				return ok(Json.toJson(map));
			}
			ProductVM vm = new ProductVM();
			vm.id = p.getId();
			vm.name = p.getName();
			vm.description = p .getDescription();
			vm.status = p .getStatus();
			vm.isApproved = Boolean.parseBoolean(p.getIsApproved());
			vm.mfrSku = p.getMfrSku();
			vm.storeSku = p.getStoreSku();
			vm.qistNo = p.getQistSku() + String.format("%07d", p.getSkuPostfix());
			vm.qistPrice = p.getQistPrice();
			vm.originalPrice = p.getOriginalPrice();
			if(p.getApprovedDate() != null){
				vm.approvedDate = df.format(p.getApprovedDate());
			}
			vm.createdDate = df.format(p.getCreatedDate());
			vm.updatedDate = df.format(p.getUpdatedDate());
			vm.validFromDate = df.format(p.getValidFromDate());
			vm.validToDate = df.format(p.getValidToDate());
			for(WdProductImage i:p.getProductImages()){
				String url = PRODUCT_IMAGE + i.getProductImageName();
				vm.images.add(url);
			}

			res.put("product", vm);

			WdRetailer r = p.getWdRetailer();
			RetailerVM rvm = new RetailerVM();
			rvm.setId(r.getId());
			rvm.setBusinessName(r.getBusinessName());
			rvm.setStreetName(r.getStreetName());
			rvm.setStreetNo(r.getStreetNo());
			rvm.setLogoImage(RETAILER_IMAGE + r.getLogoImage());
			rvm.setSuburb(r.getSuburb());
			rvm.setTradingName(r.getTradingName());
			rvm.setCity(r.getCity());
			rvm.setContactPerson(r.getContactPerson());
			rvm.setWorkEmail(r.getWorkEmail());
			rvm.setQistNo(r.getQistSku()+String.format("%07d", r.getSkuPostfix()));

			res.put("store", rvm);
			res.put("showpopup",showpopup);
			map.put("status", "200");
			map.put("message", "OK.");
			map.put("data", res);
			return ok(Json.toJson(map));
		}
		if(p == null){
			map.put("status", "500");
			map.put("message", "Product not found.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}

		Date today = new Date();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
		String date = DATE_FORMAT.format(today);
		
		List<CustomerSession> d = CustomerSession.getRetailerCustomerSession(p.getWdRetailer(),w);
		
		if(d!=null){
		for(CustomerSession d1:d){
			CustomerSession cs1 = CustomerSession.getCustomerSessionByActiveCustomerId(w);
			System.out.println(cs1);
			if(cs1 != null)
			{
			   if(cs1.getWdRetailer().getId() == p.getWdRetailer().getId()){
					for(SessionProduct sp1 : cs1.getSessionProducts())
					{
						if(sp1.getWdProduct().getId()==p.getId())
						{
							map.put("status", "500");
							map.put("message", "Already added in cart.");
							map.put("data", null);
							return ok(Json.toJson(map));
						}
					}
					SessionProduct sp  = new  SessionProduct() ;
					sp.setWdProduct(p);
					
				//	sp.setPurchased(false);
					sp.setCustomerSession(cs1);
					sp.setStatus("Cart");
					sp.save();

				}else{
					for(SessionProduct sp:cs1.getSessionProducts()){
						sp.setStatus("Wishlist");
						sp.update();
					}
					cs1.setEnd(today);
					cs1.update();
					cs1 = new CustomerSession();
					cs1.setWdRetailer(p.getWdRetailer());
					cs1.setWdCustomer(w);
					cs1.setStart(today);
					cs1.save();
					SessionProduct sp  = new  SessionProduct() ;
					sp.setWdProduct(p);
					//	sp.setPurchased(false);
					sp.setCustomerSession(cs1);
					sp.setStatus("Cart");
					sp.save();

				}
			}else{
				cs1 = new CustomerSession();
				cs1.setWdRetailer(p.getWdRetailer());
				cs1.setWdCustomer(w);
				cs1.setStart(today);
				cs1.save();
				SessionProduct sp  = new  SessionProduct() ;
				sp.setWdProduct(p);
				//sp.setPurchased(false);
				sp.setCustomerSession(cs1);
				sp.setStatus("Cart");
				sp.save();

			}

			ProductVM vm = new ProductVM();
			vm.id = p.getId();
			vm.name = p.getName();
			vm.description = p .getDescription();
			vm.status = p .getStatus();
			vm.isApproved = Boolean.parseBoolean(p.getIsApproved());
			vm.mfrSku = p.getMfrSku();
			vm.storeSku = p.getStoreSku();
			vm.qistNo = p.getQistSku() + String.format("%07d", p.getSkuPostfix());
			vm.qistPrice = p.getQistPrice();
			vm.originalPrice = p.getOriginalPrice();
			if(p.getApprovedDate() != null){
				vm.approvedDate = df.format(p.getApprovedDate());
			}
			vm.createdDate = df.format(p.getCreatedDate());
			vm.updatedDate = df.format(p.getUpdatedDate());
			vm.validFromDate = df.format(p.getValidFromDate());
			vm.validToDate = df.format(p.getValidToDate());
			for(WdProductImage i:p.getProductImages()){
				String url = PRODUCT_IMAGE + i.getProductImageName();
				vm.images.add(url);
			}

			res.put("product", vm);

			WdRetailer r = p.getWdRetailer();
			RetailerVM rvm = new RetailerVM();
			rvm.setId(r.getId());
			rvm.setBusinessName(r.getBusinessName());
			rvm.setStreetName(r.getStreetName());
			rvm.setStreetNo(r.getStreetNo());
			rvm.setLogoImage(RETAILER_IMAGE + r.getLogoImage());
			rvm.setSuburb(r.getSuburb());
			rvm.setTradingName(r.getTradingName());
			rvm.setCity(r.getCity());
			rvm.setContactPerson(r.getContactPerson());
			rvm.setWorkEmail(r.getWorkEmail());
			rvm.setQistNo(r.getQistSku()+String.format("%07d", r.getSkuPostfix()));

			res.put("store", rvm);
			
			res.put("showpopup",showpopup);
			

			map.put("status", "200");
			map.put("message", "OK.");
			map.put("data", res);
			return ok(Json.toJson(map));
					
			}
		}
			showpopup="YES";
			
			res.put("showpopup",showpopup);
			res.put("product",null);
			res.put("store",null);
			map.put("status", "200");
			map.put("message", "OK.");
			map.put("data", res);
			return ok(Json.toJson(map));
		
	}
	
	public static Result getOrder(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		String popStatus = data.path("popStatus").asText();
		if(popStatus.equals("YES")){
			Long userId = data.path("userId").asLong();
			String qrcode = data.path("qrCode").asText();
			WdCustomer w = WdCustomer.findById(userId);
			WdProduct p = WdProduct.findByQistSkuAndSkuPostfix(qrcode);
	      
			Map<String, Object> res = new HashMap<>();

			if(w == null){
				if(p == null){
					map.put("status", "500");
					map.put("message", "Product not found.");
					map.put("data", null);
					return ok(Json.toJson(map));
				}
				ProductVM vm = new ProductVM();
				vm.id = p.getId();
				vm.name = p.getName();
				vm.description = p .getDescription();
				vm.status = p .getStatus();
				vm.isApproved = Boolean.parseBoolean(p.getIsApproved());
				vm.mfrSku = p.getMfrSku();
				vm.storeSku = p.getStoreSku();
				vm.qistNo = p.getQistSku() + String.format("%07d", p.getSkuPostfix());
				vm.qistPrice = p.getQistPrice();
				vm.originalPrice = p.getOriginalPrice();
				if(p.getApprovedDate() != null){
					vm.approvedDate = df.format(p.getApprovedDate());
				}
				vm.createdDate = df.format(p.getCreatedDate());
				vm.updatedDate = df.format(p.getUpdatedDate());
				vm.validFromDate = df.format(p.getValidFromDate());
				vm.validToDate = df.format(p.getValidToDate());
				for(WdProductImage i:p.getProductImages()){
					String url = PRODUCT_IMAGE + i.getProductImageName();
					vm.images.add(url);
				}

				res.put("product", vm);

				WdRetailer r = p.getWdRetailer();
				RetailerVM rvm = new RetailerVM();
				rvm.setId(r.getId());
				rvm.setBusinessName(r.getBusinessName());
				rvm.setStreetName(r.getStreetName());
				rvm.setStreetNo(r.getStreetNo());
				rvm.setLogoImage(RETAILER_IMAGE + r.getLogoImage());
				rvm.setSuburb(r.getSuburb());
				rvm.setTradingName(r.getTradingName());
				rvm.setCity(r.getCity());
				rvm.setContactPerson(r.getContactPerson());
				rvm.setWorkEmail(r.getWorkEmail());
				rvm.setQistNo(r.getQistSku()+String.format("%07d", r.getSkuPostfix()));

				res.put("store", rvm);
				map.put("status", "200");
				map.put("message", "OK.");
				map.put("data", res);
				return ok(Json.toJson(map));
			}
			if(p == null){
				map.put("status", "500");
				map.put("message", "Product not found.");
				map.put("data", null);
				return ok(Json.toJson(map));
			}

			Date today = new Date();
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
			String date = DATE_FORMAT.format(today);
			
			
	         CustomerSession cs1 = CustomerSession.getCustomerSessionByActiveCustomerId(w);
			System.out.println(cs1);
			if(cs1 != null)
			{
			   if(cs1.getWdRetailer().getId() == p.getWdRetailer().getId()){
					for(SessionProduct sp1 : cs1.getSessionProducts())
					{
						if(sp1.getWdProduct().getId()==p.getId())
						{
							map.put("status", "500");
							map.put("message", "Already added in cart.");
							map.put("data", null);
							return ok(Json.toJson(map));
						}
					}
					SessionProduct sp  = new  SessionProduct() ;
					sp.setWdProduct(p);
				//	sp.setPurchased(false);
					sp.setCustomerSession(cs1);
					sp.setStatus("Cart");
					sp.save();

				}else{
					for(SessionProduct sp:cs1.getSessionProducts()){
						sp.setStatus("Wishlist");
						sp.update();
					}
					cs1.setEnd(today);
					cs1.update();
					cs1 = new CustomerSession();
					cs1.setWdRetailer(p.getWdRetailer());
					cs1.setWdCustomer(w);
					cs1.setStart(today);
					cs1.save();
					SessionProduct sp  = new  SessionProduct() ;
					sp.setWdProduct(p);
					//	sp.setPurchased(false);
					sp.setCustomerSession(cs1);
					sp.setStatus("Cart");
					sp.save();

				}
			}else{
				cs1 = new CustomerSession();
				cs1.setWdRetailer(p.getWdRetailer());
				cs1.setWdCustomer(w);
				cs1.setStart(today);
				cs1.save();
				SessionProduct sp  = new  SessionProduct() ;
				sp.setWdProduct(p);
				//sp.setPurchased(false);
				sp.setCustomerSession(cs1);
				sp.setStatus("Cart");
				sp.save();

			}

			ProductVM vm = new ProductVM();
			vm.id = p.getId();
			vm.name = p.getName();
			vm.description = p .getDescription();
			vm.status = p .getStatus();
			vm.isApproved = Boolean.parseBoolean(p.getIsApproved());
			vm.mfrSku = p.getMfrSku();
			vm.storeSku = p.getStoreSku();
			vm.qistNo = p.getQistSku() + String.format("%07d", p.getSkuPostfix());
			vm.qistPrice = p.getQistPrice();
			vm.originalPrice = p.getOriginalPrice();
			if(p.getApprovedDate() != null){
				vm.approvedDate = df.format(p.getApprovedDate());
			}
			vm.createdDate = df.format(p.getCreatedDate());
			vm.updatedDate = df.format(p.getUpdatedDate());
			vm.validFromDate = df.format(p.getValidFromDate());
			vm.validToDate = df.format(p.getValidToDate());
			for(WdProductImage i:p.getProductImages()){
				String url = PRODUCT_IMAGE + i.getProductImageName();
				vm.images.add(url);
			}

			res.put("product", vm);

			WdRetailer r = p.getWdRetailer();
			RetailerVM rvm = new RetailerVM();
			rvm.setId(r.getId());
			rvm.setBusinessName(r.getBusinessName());
			rvm.setStreetName(r.getStreetName());
			rvm.setStreetNo(r.getStreetNo());
			rvm.setLogoImage(RETAILER_IMAGE + r.getLogoImage());
			rvm.setSuburb(r.getSuburb());
			rvm.setTradingName(r.getTradingName());
			rvm.setCity(r.getCity());
			rvm.setContactPerson(r.getContactPerson());
			rvm.setWorkEmail(r.getWorkEmail());
			rvm.setQistNo(r.getQistSku()+String.format("%07d", r.getSkuPostfix()));

			res.put("store", rvm);
			
			map.put("status", "200");
			map.put("message", "OK.");
			map.put("data", res);
			return ok(Json.toJson(map));
				
		}
		return ok();
	}

	public static Result getRecentlyVisitedStores(){
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
				rvm.setLogoImage(RETAILER_IMAGE + r.getLogoImage());
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
				rvm.setQistNo(r.getQistSku()+String.format("%07d", r.getSkuPostfix()));

				customerSession.start = df1.format(c.getStart());
				if(c.getEnd()!=null)
				{
					customerSession.end = df1.format(c.getEnd());
					customerSession.beforeTime=timecal(customerSession.end);
				}

				List<SessionProduct> products = c.getSessionProducts();
				List<ProductVM> prods = new ArrayList<>();
				for(SessionProduct s: products){
					ProductVM pvm = new ProductVM();
					pvm.id = s.getWdProduct().getId();
					pvm.description = s.getWdProduct().getDescription();
					pvm.isApproved =Boolean.parseBoolean(s.getWdProduct().getIsApproved());
					pvm.mfrSku = s.getWdProduct().getMfrSku();
					pvm.name = s.getWdProduct().getName();
					pvm.qistNo = s.getWdProduct().getQistSku()+String.format("%07d", s.getWdProduct().getSkuPostfix());
					pvm.status = s.getWdProduct().getStatus();
					pvm.storeSku = s.getWdProduct().getStoreSku();
					pvm.qistPrice = s.getWdProduct().getQistPrice();
					pvm.originalPrice = s.getWdProduct().getOriginalPrice();
					if(s.getWdProduct().getApprovedDate() != null){
						pvm.approvedDate = df.format(s.getWdProduct().getApprovedDate());
					}
					pvm.createdDate = df.format(s.getWdProduct().getCreatedDate());
					pvm.updatedDate = df.format(s.getWdProduct().getUpdatedDate());
					pvm.validFromDate = df.format(s.getWdProduct().getValidFromDate());
					pvm.validToDate = df.format(s.getWdProduct().getValidToDate());
					for(WdProductImage i:s.getWdProduct().getProductImages()){
						String url = PRODUCT_IMAGE + i.getProductImageName();
						pvm.images.add(url);
					}
					prods.add(pvm);	
				}
				rvm.setProducts(prods);
				customerSession.retailerVM = rvm;
				customerSessionVMs.add(customerSession);
			}	
			map.put("status", "200");
			map.put("message", "OK.");
			map.put("data", customerSessionVMs);
			return ok(Json.toJson(map));
		}
	}
	public static  Result  getCustomerPurchaseHistory(){
		HashMap<String, Object> map = new HashMap<>();
		JsonNode data = request().body().asJson();
		Long userId = data.path("userId").asLong();
		List<CustomerOrderVM> VMc = new ArrayList<>();
		WdCustomer w = WdCustomer.findById(userId); 

		if(w == null)
		{
			map.put("status","500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}else{
			List<WdCustomerOrders> wco = WdCustomerOrders.findById(w);
			for(WdCustomerOrders o :wco){
				CustomerOrderVM co = new CustomerOrderVM();

				RetailerVM rvm = new RetailerVM();
				rvm.setId(o.getWdRetailer().getId());
				rvm.setBillingInformation(o.getWdRetailer().getBillingInformation());
				rvm.setBusinessName(o.getWdRetailer().getBusinessName());
				rvm.setCity(o.getWdRetailer().getCity());
				rvm.setContactPerson(o.getWdRetailer().getContactPerson());
				rvm.setEftpos(o.getWdRetailer().getEftpos());
				rvm.setEftposProvider(o.getWdRetailer().getEftposProvider());
				rvm.setFbLink(o.getWdRetailer().getFbLink());
				rvm.setGoodsCategories(o.getWdRetailer().getGoodsCategories());
				rvm.setGoogleLink(o.getWdRetailer().getGoogleLink());
				rvm.setGstNo(o.getWdRetailer().getGstNo());
				rvm.setIrNo(o.getWdRetailer().getIrNo());
				rvm.setLogoImage(RETAILER_IMAGE + o.getWdRetailer().getLogoImage());
				rvm.setMerchantId(o.getWdRetailer().getMerchantId());
				rvm.setMobileNo(o.getWdRetailer().getMobileNo());
				rvm.setPaymark(o.getWdRetailer().getPaymark());
				rvm.setQistSku(o.getWdRetailer().getQistSku());
				rvm.setReferedBy(o.getWdRetailer().getReferedBy());
				rvm.setSkuPostfix(o.getWdRetailer().getSkuPostfix());
				rvm.setStreetName(o.getWdRetailer().getStreetName());
				rvm.setStreetNo(o.getWdRetailer().getStreetNo());
				rvm.setSuburb(o.getWdRetailer().getSuburb());
				rvm.setTitle(o.getWdRetailer().getTitle());
				rvm.setTradingName(o.getWdRetailer().getTradingName());
				rvm.setTwitterLink(o.getWdRetailer().getTwitterLink());
				rvm.setWorkEmail(o.getWdRetailer().getWorkEmail());
				rvm.setWorkPhone1(o.getWdRetailer().getWorkPhone1());
				rvm.setWorkPhone2(o.getWdRetailer().getWorkPhone2());
				rvm.setWorkUrl(o.getWdRetailer().getWorkUrl());
				rvm.setQistNo(o.getWdRetailer().getQistSku()+String.format("%07d", o.getWdRetailer().getSkuPostfix()));

				List<WdCustomerOrderDetail> wcod = WdCustomerOrderDetail.findByOrderId(o);
				List<ProductVM> prods = new ArrayList<>();
				for(WdCustomerOrderDetail od : wcod){
					WdProduct p = WdProduct.findByProductId(od.getProductId());

					ProductVM pvm = new ProductVM();
					pvm.id = p.getId();
					pvm.description = p.getDescription();
					pvm.isApproved =Boolean.parseBoolean(p.getIsApproved());
					pvm.mfrSku =p.getMfrSku();
					pvm.name = p.getName();
					pvm.qistNo = p.getQistSku()+String.format("%07d", p.getSkuPostfix());
					pvm.status = p.getStatus();
					pvm.storeSku = p.getStoreSku();
					pvm.qistPrice = p.getQistPrice();
					pvm.originalPrice = p.getOriginalPrice();
					if(p.getApprovedDate() != null){
						pvm.approvedDate = df.format(p.getApprovedDate());
					}
					pvm.createdDate = df.format(p.getCreatedDate());
					pvm.updatedDate = df.format(p.getUpdatedDate());
					pvm.validFromDate = df.format(p.getValidFromDate());
					pvm.validToDate = df.format(p.getValidToDate());
					for(WdProductImage i:p.getProductImages()){
						String url = PRODUCT_IMAGE + i.getProductImageName();
						pvm.images.add(url);
					}
					prods.add(pvm);
				}
				rvm.setProducts(prods);
				co.retailer = rvm;
				co.createDate = df1.format(o.getCreatedDate());
				co.beforeTime = timecal(co.createDate);
				VMc.add(co);
			}
		}

		map.put("status", "200");
		map.put("message", "OK.");
		map.put("data",VMc);
		return ok(Json.toJson(map));
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

	public static Result  changeCustomerProfileImage() throws IOException{
		HashMap<String, Object> map = new HashMap<>();
		DynamicForm users = Form.form().bindFromRequest();

		Long id = Long.parseLong(users.get("userId"));
		WdCustomer c = WdCustomer.findById(id); 

		String imageDataString = new String();
		if(c != null){
			MultipartFormData body = request().body().asMultipartFormData();
			if (body != null) {
				FilePart filePart = body.getFile("image");
				if (filePart != null) {
					File f = filePart.getFile();
					try {         
						FileInputStream is = new FileInputStream(f);
						File cusFolder = new File(FILEPATH+"/customers/"+c.getId()+"/");
						if(!cusFolder.exists()){
							cusFolder.mkdir();
						}
						File file = new File(FILEPATH+"/customers/"+c.getId()+"/"+filePart.getFilename());
						int read = 0;
						byte[] bytes = new byte[1024];
						FileOutputStream os = new FileOutputStream(file);
						while ((read = is.read(bytes)) != -1) {
							os.write(bytes, 0, read);
						}
						os.close();
						imageDataString = filePart.getFilename();
						//FileInputStream imageInFile = new FileInputStream(f);
						//byte imageData[] = new byte[(int) f.length()];
						//imageInFile.read(imageData);
						//imageInFile.close();
						//imageDataString = Base64.encodeBase64URLSafeString(imageData);
					} catch (FileNotFoundException e) {
						System.out.println("Image not found" + e);
					} catch (IOException ioe) {
						System.out.println("Exception while reading the Image " + ioe);
					}
				}
			}
			c.setImage(imageDataString);
			c.setUpdatedDate(new Date());
			c.update();
			CustomerVM vm = new CustomerVM();
			vm.id = c.getId();
			vm.name =(c.getFirstname());
			//vm.lastName = c.getLastname();
			vm.email = c.getEmail();
			vm.password = c.getPassword();
			/*vm.address = c.getAddress();*/
			vm.address1 = c.getAddress1();
			vm.address2 = c.getAddress2();
			vm.city = c.getCity();
			vm.state = c.getState();
			vm.country = c.getCountry();
			vm.zip = c.getZip();
			vm.image = DOMAIN_URL+"getCustomerProfileImage/"+c.getId();
			vm.contactNo = c.getContactNo();
			vm.createdDate = df.format(c.getCreatedDate());
			vm.updatedDate = df.format(c.getUpdatedDate());
			vm.lastActive = df.format(c.getLastActive());
			vm.qCartMailingList = Boolean.parseBoolean(c.getQCartMailingList());
			vm.qistNo = c.getQistSku()+String.format("%07d", c.getSkuPostfix());

			HashMap<String, Object> map1 = new HashMap<>();
			map1.put("CTPYE", c.getType());
			map1.put("CustomerID", c.getId().toString());
			map1.put("UserData", vm);
			map.put("status", "200");
			map.put("message", "OK.");
			map.put("data", map1);
			return ok(Json.toJson(map));
		}else{
			map.put("status", "500");
			map.put("message", "User does not exist.");
			map.put("data", null);
			return ok(Json.toJson(map));
		}
	}


	public static Result getCustomerProfileImage(Long id) throws IOException{
		WdCustomer c = WdCustomer.findById(id); 
		if(c == null){
			return ok();
		}
		File f = new File(FILEPATH+"/customers/"+c.getId()+"/"+c.getImage());
		return ok(f);
	}

}
