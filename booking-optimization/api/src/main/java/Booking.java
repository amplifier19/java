// package gr.amplifier.optimization.booking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author amplifier
 */
public class Booking {
    private String name;
    private Date arrival;
    private Date departure;
    
    public Booking (String n, String a, String d, String df1, String df2) throws ParseException {
        name = n;
        
        SimpleDateFormat sdf1 = new SimpleDateFormat (df1);
        SimpleDateFormat sdf2 = new SimpleDateFormat (df2);
        arrival = sdf1.parse (a);
        departure = sdf2.parse (d);

    }
    
    public String getName () {
        return name;
    }
    
    public Date getArrival () {
        return arrival;
    }
    
    public Date getDeparture () {
        return departure;
    }
}
