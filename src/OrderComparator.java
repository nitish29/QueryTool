/**
 * This is a comparator class to sort record object based on different attributes
 */

import java.util.*;


public class OrderComparator implements Comparator<Record> {

    Record.Attribute recordAttribute = null;

    public OrderComparator(Record.Attribute attribute) {

        this.recordAttribute = attribute;

    }

    public int compare(Record record1, Record record2) {

        switch (recordAttribute) {

            case STB:
                return record1.stb.compareTo(record2.stb);

            case TITLE:
                return record1.title.compareTo(record2.title);

            case PROVIDER:
                return record1.provider.compareTo(record2.provider);

            case DATE:
                return record1.date.toString().compareTo(record2.date.toString());

            case REV:
                return Float.compare(record1.price, record2.price);

            case VIEW_TIME:
                return record1.viewTime.compareTo(record2.viewTime);

            default:
                return 0;

        }

    }


}
