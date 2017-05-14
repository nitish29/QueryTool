/**
 * A Record object class.
 *
 * A record object is essentially an object representation of the input data provided.
 *
 * A record object has the following member variable fields
 *
 * 1) stb - a string value, denoting the set top box id.
 * 2) title - a string value, denoting the movie title.
 * 3) provider - a string value, denoting the movie provider.
 * 4) date - a LocalDate object, denoting the date at which this particular movie was watched.
 * 5) price - a float value, denoting the price the movie title.
 * 6) viewTime - a LocalTime object, denoting the total viewing time of the movie,
 *
 *
 */

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Record {

    public enum Attribute {
        STB, TITLE, PROVIDER, DATE, REV, VIEW_TIME
    }

    protected String stb;
    protected String title;
    protected String provider;
    protected LocalDate date;
    protected float price;
    protected LocalTime viewTime;


    Record ( String stb, String title, String provider, LocalDate date, float price, LocalTime viewTime ) {

        this.stb = stb;
        this.title = title;
        this.provider = provider;
        this.date = date;
        this.price = price;
        this.viewTime = viewTime;

    }

    Record () {}

    public String fetchAttributeAsString( Attribute att ) {

        switch (att) {

            case STB: return this.stb;

            case TITLE: return this.title;

            case PROVIDER: return this.provider;

            case DATE: return this.date.toString();

            case REV: return String.format("%.02f", this.price);

            case VIEW_TIME: return this.viewTime.toString().substring(1);

            default: return null;

        }

    }

    /**
     * This fucntion creates a new record object
     * @param data - a list containg string values pertaining to each record object member variable
     * @return - a newly instantiated record object
     */
    public static Record createNewRecord( List<String> data ) throws Exception{

        String stb = data.get(0);
        String title = data.get(1);
        String provider = data.get(2);

        String unsanitizedDate = data.get(3);
        String unsanitizedPrice = data.get(4);
        String unsanitizedViewTime = data.get(5);
        LocalDate sanitizedDate = sanitizeDate(unsanitizedDate);
        LocalTime sanitizedTime = sanitizeTime(unsanitizedViewTime);
        Float price = sanitizePrice(unsanitizedPrice);

        return new Record(stb, title, provider, sanitizedDate, price, sanitizedTime);


    }

    /**
     * This function converts String date into a LocalDate object.
     * @param unsanitizedDate - a date passed as a string
     * @return - a LocalDate object containing the mapping of the string into this particular object type.
     * @throws Exception
     */
    public static LocalDate sanitizeDate( String unsanitizedDate ) throws Exception{

        return LocalDate.parse(unsanitizedDate);

    }

    /**
     * This functions converts a string price into a Float object
     * @param unsanitizedPrice - a string value denoting price of a movie
     * @return a Float object, containing mapping of the string into this particular object type.
     * @throws Exception
     */
    public static Float sanitizePrice( String unsanitizedPrice ) throws Exception{

        return Float.valueOf(unsanitizedPrice);

    }

    /**
     * This function converts a String time into a LocalDate object.
     * @param unsanitizedTime - a string value denoting time
     * @return a LocalTime object, containing mapping of the string into this particular object type.
     * @throws Exception
     */
    public static LocalTime sanitizeTime( String unsanitizedTime ) throws Exception {

        String[] timeSplitString = unsanitizedTime.split(":");
        return LocalTime.of(Integer.parseInt(timeSplitString[0]), Integer.parseInt(timeSplitString[1]));

    }

}
