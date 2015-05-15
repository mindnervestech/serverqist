package controllers;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import models.Customer;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
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
    	JsonNode json = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		CustomerVM customer = null;
		try {
			customer = mapper.readValue(json.traverse(),CustomerVM.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(customer.firstName.isEmpty() || customer.firstName == null ||
				customer.lastName.isEmpty() || customer.lastName == null){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}
		if(!customer.password.equals(customer.retypePassword)){
			return ok(Json.toJson(new ErrorResponse(Error.E500.getCode(), Error.E500.getMessage())));
		}
		Customer c = new Customer();
		c.firstName = customer.firstName;
		c.lastName = customer.lastName;
		c.email = customer.email;
		c.password = customer.password;
		c.address = customer.address;
		c.qCartMailingList = customer.qCartMailingList;
		c.type = customer.type;
		if(customer.FacebookId != null && !customer.FacebookId.isEmpty()){
			c.FacebookId = customer.FacebookId;
		} else if(customer.GooglePlusId != null && !customer.GooglePlusId.isEmpty()){
			c.GooglePlusId = customer.GooglePlusId;
		} else if(customer.TwitterId != null && !customer.TwitterId.isEmpty()){
			c.TwitterId = customer.TwitterId;
		}
		c.lastActive = new Date();
		c.qistNo = UUID.randomUUID().toString();
		c.save();
		return ok(Json.toJson(new ErrorResponse(Error.E200.getCode(), Error.E200.getMessage())));
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
		return ok(Json.toJson(new ErrorResponse(Error.E200.getCode(), Error.E200.getMessage())));
    }
  
}
