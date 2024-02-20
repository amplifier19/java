// package gr.amplifier.optimization.booking;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author amplifier
 */
public class Helper {
    private static String df1 = new String ();
    private static String df2 = new String ();
    
    public static String checkDate (String date) {
        String [] formats = {"dd/MM/yyyy", "dd-MM-yyyy"};    
        for (String format : formats) {
            SimpleDateFormat sdf = new SimpleDateFormat (format);
            try {
                sdf.parse (date);
                return format;
            }
            catch (ParseException ex) {
                continue;
            }
        }
        return "";
    }
    
    public static String dateToString (Date date, String df) {
        SimpleDateFormat sdf = new SimpleDateFormat (df);
        String sdate = sdf.format(date);
        return sdate;
    }
    
    public static List<Booking> getBookingsFromInputStream (InputStream is) throws IOException, DateFormatException, ParseException {
        InputStreamReader isr = new InputStreamReader (is);
        BufferedReader br = new BufferedReader (isr);            
        String line = new String ();     
        List<Booking> list = new ArrayList<> ();
        String[] fields = new String [3];
        String separator = "\\s*[\\s,]++\\s*";
        

        int count = 1;
        while ( (line = br.readLine()) != null ) {  
            fields = line.split (separator);
            df1 = checkDate (fields[1]);
            df2 = checkDate (fields[2]);
            if (df1.isBlank () || df2.isBlank ()) {
                throw new DateFormatException ("Wrong date format at line " + count);
            }
            Booking booking = new Booking (fields[0], fields[1], fields[2], df1, df2);
            list.add (booking);
            count++;
        }
        list.sort(new ArrivalComparator ());
        String sarrival, sdeparture = new String ();
        for (Booking booking : list) {
            sarrival = Helper.dateToString (booking.getArrival (), df1);
            sdeparture = Helper.dateToString (booking.getDeparture (), df2);                
        }   
        return list;
    }
    
    public static List<Booking> getTestBookings () throws IOException, DateFormatException, ParseException {
        File file = new File ("/opt/dummy-bookings.txt");
        Scanner fsc = new Scanner (file);   
        String line = new String ();     
        List<Booking> list = new ArrayList<> ();
        String[] fields = new String [3];
        String separator = "\\s*[\\s,]++\\s*";        

        int count = 1;
        while ( fsc.hasNextLine () ) {
            line = fsc.nextLine ();
            fields = line.split (separator);
            df1 = checkDate (fields[1]);
            df2 = checkDate (fields[2]);
            if (df1.isBlank () || df2.isBlank ()) {
                throw new DateFormatException ("Wrong date format at line " + count);
            }
            Booking booking = new Booking (fields[0], fields[1], fields[2], df1, df2);
            list.add (booking);
            count++;
        }
        list.sort (new ArrivalComparator ());
        String sarrival, sdeparture = new String ();
        for (Booking booking : list) {
            sarrival = Helper.dateToString (booking.getArrival (), df1);
            sdeparture = Helper.dateToString (booking.getDeparture (), df2);                
        }   
        return list;
    }
    
    public static Map<String, Object>optimizeBookings (List<Booking> list) {
        Map<String, Object> results = new HashMap ();
        List<Booking> optimizedList = new ArrayList<> ();
        Integer days = 0;
        if (list.isEmpty ()) {
            results.put ("days", days);
            results.put ("optBookings", optimizedList);
            return results;
        }
        long msecDay = 24 * 60 * 60 * 1000;
        long [] L = new long [list.size ()];
        long departure, arrival;
        int i, j;
        int [] parent = new int [list.size ()];
        for (i=0; i<list.size (); i++) {
            departure = list.get (i).getDeparture ().getTime ();
            arrival = list.get (i).getArrival ().getTime ();
            L[i] = departure - arrival;
            L[i] = L[i] / msecDay;
        }
        Arrays.fill (parent, -1);
        Date arri, depi, depj;
        for (i=0; i<list.size (); i++) {
            arri = list.get (i).getArrival ();
            depi = list.get (i).getDeparture ();
            long durationi = (depi.getTime () - arri.getTime ()) / msecDay;
            for (j=0; j<i; j++) {
                depj = list.get (j).getDeparture ();
                if (arri.compareTo (depj) >= 0 && (L[i] < L[j]+durationi) ) {
                    L[i] = L[j] + durationi;
                    parent[i] = j;
                }
            }
        }
        days = (int) L[0];
        int index = 0;
        for (i=1; i<list.size (); i++) {
            if (L[i] > days) {
                days = (int) L[i];
                index = i;
            }
        }
        while (index != -1) {
            optimizedList.add (list.get (index));
            index = parent[index];
        }
        results.put ("days", days);
        results.put ("optBookings", optimizedList);
        return results;
    }
    
    public static String createJsonResponse (Integer days, List<Booking> optimizedList) {
        boolean first = true;
        String response = "{\"days\": " + "\"" + days + "\",\"bookings\": ["; 
        for (Booking booking : optimizedList) {
            if (first == true) {
                response += "{\"name\": " + "\"";
            }
            else {
                response += ",{\"name\": " + "\"";
            }
            response += booking.getName () + "\",\"arrival\": \"" + dateToString (booking.getArrival (), df1) + "\",\"departure\": \"" + dateToString (booking.getDeparture (), df2) + "\"}"; 
            first = false;
        }
        response += "]}";
        return response;
    }
}

class ArrivalComparator implements Comparator<Booking> {
    @Override
    public int compare (Booking b1, Booking b2) {
        return b1.getArrival ().compareTo (b2.getArrival ());
    }
}
    
