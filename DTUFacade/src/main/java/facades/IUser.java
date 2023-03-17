package facades;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

public interface IUser {
    
    @POST
    Response register();
    
    @GET
    Response generateReport();
}
