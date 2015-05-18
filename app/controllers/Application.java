package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import models.Customer;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import sun.misc.BASE64Encoder;
import viewmodels.CustomerVM;
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
    	 Boolean qcart = Boolean.parseBoolean(users.get("qCartMailingList"));
    	 String FacebookId = users.get("FacebookId");
    	 String GooglePlusId = users.get("GooglePlusId");
    	 String TwitterId = users.get("TwitterId");
    	 String img = users.get("image");
    	 String imageDataString = null ;
    	 Customer c = new Customer();
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
		
		c.firstName = fname;
		c.lastName = lname;
		c.email = email;
		c.password = pass;
		c.address = repass;
		c.image = imageDataString;
		c.qCartMailingList = qcart;
		c.type = "M";
		if(FacebookId != null && !FacebookId.isEmpty()){
			Customer cc = Customer.findByFbId(FacebookId);
			if(cc != null){
				HashMap<String, String> map = new HashMap<>();
				map.put("CTPYE", "S");
				map.put("CustomerID", cc.id.toString());
				return ok(Json.toJson(map));
			}else{
				c.FacebookId = FacebookId;
				c.type = "S";
			}
			
		} else if(GooglePlusId != null && !GooglePlusId.isEmpty()){
			Customer cc = Customer.findByGoogleID(GooglePlusId);
			if(cc != null){
				HashMap<String, String> map = new HashMap<>();
				map.put("CTPYE", "S");
				map.put("CustomerID", cc.id.toString());
				return ok(Json.toJson(map));
			}else{
				c.GooglePlusId = GooglePlusId;
				c.type = "S";
			}
		} else if(TwitterId != null && !TwitterId.isEmpty()){
			Customer cc = Customer.findByTwitterId(TwitterId);
					if(cc != null){
						HashMap<String, String> map = new HashMap<>();
						map.put("CTPYE", "S");
						map.put("CustomerID", cc.id.toString());
						return ok(Json.toJson(map));
					}else{
							c.TwitterId = TwitterId;
							c.type = "S";
					}	
		}
		c.lastActive = new Date();
		c.qistNo = UUID.randomUUID().toString();
		c.save();
		HashMap<String, String> map1 = new HashMap<>();
		map1.put("CTPYE", c.type);
		map1.put("CustomerID", c.id.toString());
		return ok(Json.toJson(map1));
		//return ok(Json.toJson(new ErrorResponse(Error.E200.getCode(), Error.E200.getMessage())));
    }
    
    public static Result login(){
    	JsonNode json = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		CustomerVM customer = null;
		try {
			customer = mapper.readValue(json.traverse(),CustomerVM.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(customer.email.isEmpty() || customer.email == null ||
				customer.password.isEmpty() || customer.password == null){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}
		Customer c = Customer.findByEmailAndPassword(customer.email, customer.password);
		if(c == null){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}
		HashMap<String, String> map1 = new HashMap<>();
		map1.put("CTPYE", c.type);
		map1.put("CustomerID", c.id.toString());
		return ok(Json.toJson(map1));
		//return ok(Json.toJson(new ErrorResponse(Error.E200.getCode(), Error.E200.getMessage())));
    }
    
    public static Result forgotPassword(){
    	
   	 DynamicForm users = Form.form().bindFromRequest();
   	 String email = users.get("email");
   	 Customer c = Customer.findByEmail(email);
   	if(c == null){
		return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
	}else{
		sendPasswordMail(c.email,c.password);
	}
   	 return ok(Json.toJson(new ErrorResponse(Error.E200.getCode(), Error.E200.getMessage())));
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
