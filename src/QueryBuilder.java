/**
 * This is our main driver class, that parses command line arguments, provided support for data import and querying.
 */

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;


public class QueryBuilder {

    public static final String baseDirectoryOutputDataStore = "dataStore/";

    public static void main(String args[]) throws Exception {

        commandLineParser(args);

    }

    /**
     * This is the main helper function to parse arguments passed from the command line
     * @param args - a string array of command line arguments
     * @throws Exception
     */

    private static void commandLineParser(String[] args) throws Exception {

        Options options = new Options();
        options.addOption("s", true, "display selected fields");
        options.addOption("f", true, "filter query by attributes");
        options.addOption("o", true, "order query by attributes");
        options.addOption("i", true, "import data from file");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if ( cmd.hasOption("i") ) {

            Importer.importDataFromFile(String.valueOf(cmd.getOptionValue("i")));

        }

        String filterOptions = cmd.getOptionValue("f");

        List<Record> records = null;

        if (filterOptions == null) {

            records = QueryBuilder.fetchAllRecords();

        }

        if (filterOptions != null) {

            records = QueryBuilder.fetchFilteredRecords(filterOptions);

        }

        if (cmd.hasOption("o")) {

            String orderByOptions = cmd.getOptionValue("o");
            records = QueryBuilder.orderRecordsByAttributes(orderByOptions, records);

        }

        if (cmd.hasOption("s")) {

            String selectAttributes = cmd.getOptionValue("s");
            QueryBuilder.displayRecordsBySelectedAttributes(records, selectAttributes);


        } else {

            QueryBuilder.displayRecordsBySelectedAttributes(records, null);

        }

    }

    /**
     * This function iterates over all the files in the datastore and convert the file data into a record object, and adds that record to a list
     * @return a list of all the records that are stored in the datastore
     * @throws Exception
     */
    public static List<Record> fetchAllRecords() throws Exception {

        List<Record> recordList = new ArrayList<>();

        File dir = new File(baseDirectoryOutputDataStore);

        File[] files = dir.listFiles();

        for (File file : files) {

            HashSet<String> set = new HashSet<String>();
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> data = bufferedReader.lines().collect(Collectors.toList());
            bufferedReader.close();

            Record record = Record.createNewRecord(data);
            recordList.add(record);


        }

        return recordList;

    }


    /**
     * This function iterates over all the files in the datastore and for each file it checks whether the data contains the all filter values passed.
     * If a file contains all the provided filters, it converts the file data into a record object, and adds that record to a list
     *
     * Example query: java comScore.jar  -f DATE=2014-01-03 [this query would retrieve all records that have DATE as 2014-01-03]
     *
     * @param filterOptions - a comma separated string of key=value pairs eg -f DATE=2014-01-03,REV=4.00
     * @return
     * @throws Exception
     */
    public static List<Record> fetchFilteredRecords(String filterOptions) throws Exception {


        String[] filters = filterOptions.split(",");
        Map<Record.Attribute, String> filtersMap = new LinkedHashMap<Record.Attribute, String>();


        //Assumption: A valid filter must be provided, and the filters will not be empty and filter values must be an exact match

        for (int i = 0; i < filters.length; i++) {

            String[] attributeFilter = filters[i].split("=");

            if (!QueryBuilder.isValidAttribute(attributeFilter[0])) {

                continue;

            }

            filtersMap.put(Record.Attribute.valueOf(attributeFilter[0]), attributeFilter[1]);

        }

        // iterate over each value in the filter, and every file in the database => if value is found in the file, make a new record and insert into list

        List<Record> recordList = new ArrayList<>();
        File dir = new File(baseDirectoryOutputDataStore);
        File[] files = dir.listFiles();

        for (File file : files) {

            HashSet<String> set = new HashSet<String>();

            boolean containsAllFilters = true;

            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            List<String> data = bufferedReader.lines().collect(Collectors.toList());

            for (String word : data) {

                set.add(word);

            }

            bufferedReader.close();

            //check whether exact value match is found

            for (String value : filtersMap.values()) {

                if (!set.contains(value)) {
                    containsAllFilters = false;
                    break;
                }

            }

            if (containsAllFilters) {

                Record record = Record.createNewRecord(data);
                recordList.add(record);

            }


        }

        return recordList;

    }


    /**
     * This function orders the fetched records based on the order attribute string provided.
     * The order string can be provided by specifying -o parameter followed by the order when making the query.
     *
     *
     * Example query: java comScore.jar  -o TITLE, DATE [this would order the records first by DATE and then by TITLE]
     *
     * @param order - a comma separated string of attributes
     * @param records - the records that need to be ordered, passed as a List
     * @return an ordered list of records
     */
    public static List<Record> orderRecordsByAttributes(String order, List<Record> records) {

        String[] orders = order.split(",");
        List<String> orderList = Arrays.asList(orders);
        Collections.reverse(orderList);

        for (String orderValue : orderList) {

            if (isValidAttribute(orderValue)) {

                Collections.sort(records, new OrderComparator(Record.Attribute.valueOf(orderValue)));

            }

        }

        return records;

    }

    /**
     * This function displays the results based on the -s (select query) and only displays attributes that are requested when making a query.
     * If -s option is not provided, all the attributes pertaining to a record are displayed.
     *
     * Example query - java comScore.jar -s TITLE, DATE [display only the title and date of the records in a list].
     *
     * Example query - java comScore.jar   [display all attributes of the records in a list].
     *
     * @param records - the records that need to be displayed, passed as a list
     * @param selectAttributes - a comma separated string of attributes
     */

    public static void displayRecordsBySelectedAttributes(List<Record> records, String selectAttributes) {

        boolean displayAllAttributes = false;

        if ( selectAttributes == null ) {

            displayAllAttributes = true;

        }

        List<Record.Attribute> selectList = new ArrayList<>();

        if ( !displayAllAttributes ) {

            String[] attributes = selectAttributes.split(",");

            for ( int i = 0; i < attributes.length; i++ ) {

                if ( isValidAttribute(attributes[i]) ) {

                    selectList.add(i,Record.Attribute.valueOf(attributes[i]));

                }

            }

        } else {

            int i = 0;
            for ( Record.Attribute att : Record.Attribute.values() ) {

                selectList.add(i,att);
                i++;

            }

        }


        for ( Record singleRecord : records ) {

            List<String> printValues = new ArrayList<>();

            for ( Record.Attribute att : selectList ) {

                printValues.add(singleRecord.fetchAttributeAsString(att));

            }

            System.out.println(String.join(",", printValues));

        }

    }

    /**
     * This is a helper function which checks whether an argument provided in the query is a valid attribute of a record object or not
     * @param s - An attribute value passed as a string
     * @return a boolean value indicating whether a valid attribute was passed or not in the query
     *
     * Acceptable Attribute types: STB, TITLE, DATE, REV, PROVIDER, VIEW_TIME
     *
     */
    protected static boolean isValidAttribute(String s) {

        try {

            Record.Attribute attribute = Record.Attribute.valueOf(s);

            if (attribute != null) {

                return true;

            }

        } catch (IllegalArgumentException ex) {

            System.out.println("Error: Attribute " + s + " is not a valid attribute for filtering/Ordering Records");
            return false;

        }

        return false;
    }

}
