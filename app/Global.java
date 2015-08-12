import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import models.CustomerSession;
import models.SessionProduct;
import models.WdCustomer;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import scala.concurrent.duration.Duration;


public class Global extends GlobalSettings {

	  public void onStart(Application app) {
	        Akka.system().scheduler().schedule(
	                Duration.create(0, TimeUnit.MILLISECONDS),
	                Duration.create(10, TimeUnit.MINUTES),
	                new Runnable() {
	                    public void run() {
							Calendar c = Calendar.getInstance();
							System.out.println("1");
							if(c.get(Calendar.HOUR_OF_DAY) == 0 && c.get(Calendar.MINUTE) <= 10){
								System.out.println("2");
								WdCustomer w = new WdCustomer();
								List<CustomerSession> cs = CustomerSession.getCustomerSessionByAllActiveCustomer();
								for(CustomerSession s: cs){
									s.setEnd(c.getTime());
									for(SessionProduct s1:s.getSessionProducts())
									{
										s1.setStatus("Wishlist");
										s1.update();
									}
									s.update();
								}
							}
	                    }
	                }, Akka.system().dispatcher()
	        );
	    }

	 
	
	
}
