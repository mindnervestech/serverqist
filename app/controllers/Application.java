package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
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

import models.WdCustomer;
import models.WdProduct;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import sun.misc.BASE64Encoder;
import viewmodels.CustomerVM;
import viewmodels.ProductVM;
import views.html.index;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static enum Error {
		E200("200", "OK"), E500("500", "ERROR");
		Error(String code, String message) {
			this.code = code;
			this.message = message;
		}
		private String code;
		private String message;
		public String getCode() {
			return code;
		}
		public String getMessage() {
			return message;
		}
	}

	public static class ErrorResponse {
		public String code;
		public String message;
		public ErrorResponse(String code,String message) {
			this.code = code;
			this.message = message;
		}
	}

	public static Result register(){

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
				System.out.println("imageee file===="+f);

				try {            
					FileInputStream imageInFile = new FileInputStream(f);
					byte imageData[] = new byte[(int) f.length()];
					imageInFile.read(imageData);
					BASE64Encoder encoder = new BASE64Encoder();
					imageDataString = encoder.encode(imageData);
					System.out.println("imageee===="+imageDataString);
					imageInFile.close();

				} catch (FileNotFoundException e) {
					System.out.println("Image not found" + e);
				} catch (IOException ioe) {
					System.out.println("Exception while reading the Image " + ioe);
				}

			}
		}
		if(fname.isEmpty() || fname == null ||
				lname.isEmpty() || lname == null){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}
		if(!pass.equals(repass)){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
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
				HashMap<String, String> map = new HashMap<>();
				map.put("CTPYE", "S");
				map.put("CustomerID", cc.getId().toString());
				return ok(Json.toJson(map));
			}else{
				c.setFacebookid(FacebookId);
				c.setType("S");
			}

		} else if(GooglePlusId != null && !GooglePlusId.isEmpty()){
			WdCustomer cc = WdCustomer.findByGoogleID(GooglePlusId);
			if(cc != null){
				HashMap<String, String> map = new HashMap<>();
				map.put("CTPYE", "S");
				map.put("CustomerID", cc.getId().toString());
				return ok(Json.toJson(map));
			}else{
				c.setGoogleplusid(GooglePlusId);
				c.setType("S");
			}
		} else if(TwitterId != null && !TwitterId.isEmpty()){
			WdCustomer cc = WdCustomer.findByTwitterId(TwitterId);
			if(cc != null){
				HashMap<String, String> map = new HashMap<>();
				map.put("CTPYE", "S");
				map.put("CustomerID", cc.getId().toString());
				return ok(Json.toJson(map));
			}else{
				c.setTwitterid(TwitterId);
				c.setType("S");
			}	
		}
		c.save();
		HashMap<String, String> map1 = new HashMap<>();
		map1.put("CTPYE", c.getType());
		map1.put("CustomerID", c.getId().toString());
		return ok(Json.toJson(map1));
		//return ok(Json.toJson(new ErrorResponse(Error.E200.getCode(), Error.E200.getMessage())));
	}

	public static Result login(){
		DynamicForm users = Form.form().bindFromRequest();
		if(users.get("email").isEmpty() || users.get("email") == null ||
				users.get("password").isEmpty() || users.get("password") == null){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}
		WdCustomer c = WdCustomer.findByEmailAndPassword(users.get("email"), users.get("password"));
		if(c == null){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}
		HashMap<String, String> map1 = new HashMap<>();
		map1.put("CTPYE", c.getType());
		map1.put("CustomerID", c.getId().toString());
		return ok(Json.toJson(map1));
		//return ok(Json.toJson(new ErrorResponse(Error.E200.getCode(), Error.E200.getMessage())));
	}

	public static Result forgotPassword(){

		DynamicForm users = Form.form().bindFromRequest();
		String email = users.get("email");
		WdCustomer c = WdCustomer.findByEmail(email);
		if(c == null){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}else{
			sendPasswordMail(c.getEmail(),c.getPassword());
		}
		return ok(Json.toJson(new ErrorResponse(Error.E200.getCode(), Error.E200.getMessage())));
	}
	
	public static Result getCustomerProfile(){
		DynamicForm users = Form.form().bindFromRequest();
		Long id = Long.parseLong(users.get("userId"));
		WdCustomer c = WdCustomer.findById(id); 
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
		return ok(Json.toJson(vm));
	}
	
	public static Result changeCustomerName(){
		DynamicForm users = Form.form().bindFromRequest();
		Long id = Long.parseLong(users.get("userId"));
		WdCustomer c = WdCustomer.findById(id); 
		if(c == null){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}
		c.setFirstname(users.get("firstName"));
		c.setLastname(users.get("lastName"));
		c.update();
		return ok(Json.toJson(new ErrorResponse(Error.E200.getCode(), Error.E200.getMessage())));
	}
	
	public static Result changeCustomerPassword(){
		DynamicForm users = Form.form().bindFromRequest();
		Long id = Long.parseLong(users.get("userId"));
		WdCustomer c = WdCustomer.findById(id); 
		if(c == null){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}
		c.setPassword(users.get("password"));
		c.update();
		return ok(Json.toJson(new ErrorResponse(Error.E200.getCode(), Error.E200.getMessage())));
	}
	
	public static Result changeCustomerAddress(){
		DynamicForm users = Form.form().bindFromRequest();
		Long id = Long.parseLong(users.get("userId"));
		WdCustomer c = WdCustomer.findById(id); 
		if(c == null){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}
		c.setAddress(users.get("address"));
		c.update();
		return ok(Json.toJson(new ErrorResponse(Error.E200.getCode(), Error.E200.getMessage())));
	}


	public static Result scanProduct(){
		DynamicForm users = Form.form().bindFromRequest();
		String qrcode = users.get("qrCode");
		WdProduct p = WdProduct.findByQrCode(qrcode);
		if(p == null){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}
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
		return ok(Json.toJson(vm));
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
