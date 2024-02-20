// package gr.amplifier.optimization.booking;

import java.io.IOException;  
import java.io.InputStream;  
import jakarta.ws.rs.Consumes;  
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;  
import jakarta.ws.rs.Path;  
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;  
import jakarta.ws.rs.core.Response;  
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;  
import org.glassfish.jersey.media.multipart.FormDataParam;  

/**
 *
 * @author amplifier
 */
@Path("/v1")  
public class BookingService {
    @POST  
    @Path ("/optimize")  
    @Consumes (MediaType.MULTIPART_FORM_DATA)  
    @Produces (MediaType.APPLICATION_JSON)
    public Response optimizeBookings (
        @FormDataParam ("file") InputStream is,  
        @FormDataParam ("file") FormDataContentDisposition fileDetail
    ) { 
        Integer days = 0; 
        String response = new String ();
        try {
            List<Booking> list = Helper.getBookingsFromInputStream(is);
            
            Map<String, Object> results = Helper.optimizeBookings(list);
            response = Helper.createJsonResponse ((Integer) results.get("days"), (List<Booking>) results.get("optBookings"));
        } catch (DateFormatException e) {
            return Response.status (400).entity ("{\"error\": \"" + e.getMessage() + "\"}").build ();
        } catch (ParseException e) {
            e.printStackTrace ();
            return Response.status (400).entity ("{\"error\": \"Date parsing error\"}").build ();
        }
        catch (IOException e) {
            e.printStackTrace ();
            return Response.status (400).entity ("{\"error\": \"Error on file reading\"}").build ();
        }
        return Response.status (200).entity (response).build ();
    }
    
    @GET
    @Path ("/test")
    @Produces (MediaType.APPLICATION_JSON)
    public Response test () {
        Integer days = 0; 
        String response = new String ();
        try {
            List<Booking> list = Helper.getTestBookings();
            
            Map<String, Object> results = Helper.optimizeBookings(list);
            response = Helper.createJsonResponse ((Integer) results.get("days"), (List<Booking>) results.get("optBookings"));
        } catch (DateFormatException e) {
            return Response.status (400).entity ("{\"error\": \"" + e.getMessage() + "\"}").build ();
        } catch (ParseException e) {
            e.printStackTrace ();
            return Response.status (400).entity ("{\"error\": \"Date parsing error\"}").build ();
        }
        catch (IOException e) {
            e.printStackTrace ();
            return Response.status (400).entity ("{\"error\": \"Error on file reading\"}").build ();
        }
        return Response.status (200).entity (response).build ();
    }
    
} 
