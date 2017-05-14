/**
 * This class contains methods that perform data import.
 */
import java.io.*;

public class Importer {


    public static final String baseDirectoryOutputDataStore = "dataStore/";


    /**
     * This function accepts the path of a file as a string value.
     * It parses the data from the file line by line, and generates
     * unique text files based on stb, title and date.
     *
     * @param path
     * @throws Exception
     */
    protected static void importDataFromFile(String path) throws Exception {

        if ( path.length() == 0 || path == null ) {

            throw new Exception("File Path Cannot be Null");

        }


        String line;

        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        int count = 0;

        while ((line = bufferedReader.readLine()) != null) {

            if ( count != 0 ) {

                String[] parts = line.trim().split("\\|"); //split the line based on "|"
                indexNewData(parts);

            }

            count++;

        }

        bufferedReader.close();

    }


    /**
     * This is a helper function for generating a new file based on STB, TITLE and DATE. This function ensures that if there's an existing
     * file with same STB, TITILE and DATE then that file is overwritten with the new data.
     * For each line, it fetches the data enclosed within '|' separator, and generates a file with a name like (STB)_(DATE)_(TITLE).txt
     * The new file contains new-line separated stb, title, movie, provider ,rev and view time attribute fields respectively .
     * @param data - A string array containg the data to be written to the datastore file.
     * @throws IOException
     */
    protected static void indexNewData( String[] data ) throws IOException {

        String unsanitizedSTBID = data[0];
        String title = data[1];
        String provider = data[2];
        String unSanizedDate = data[3];
        String unSanitizedPrice = data[4];
        String unSanitizedViewingTime = data[5];

        StringBuilder filePath = new StringBuilder(baseDirectoryOutputDataStore).append("/").append(unsanitizedSTBID).append("_").append(unSanizedDate).append("_").append(title).append(".txt");

        File file = new File(filePath.toString());

        file.getParentFile().mkdirs(); // Will create parent directories if they don't exist

        if ( !file.exists() ) {

            file.createNewFile();

        }

        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.append(unsanitizedSTBID).append("\n");
        writer.append(title).append("\n");
        writer.append(provider).append("\n");
        writer.append(unSanizedDate).append("\n");
        writer.append(unSanitizedPrice).append("\n");
        writer.append(unSanitizedViewingTime);
        writer.close();

    }

}
