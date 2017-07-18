package demo;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.List;

public class Main {
    private static final String FILENAME = "crawler/rawQuery2.txt";
    private static final String OUTPUT = "ad.json";
    private final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Start app...");
        try {
            File file = new File("ad.json");
            if (file.delete()) {
                logger.info(file.getName() + " is deleted!");
            } else {
                logger.warn("Delete operation is failed.");
            }
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        FeedsHandler feedsHandler = new FeedsHandler(FILENAME);
        List<Feed> feeds = feedsHandler.generateFeeds();
        Crawler crawler = new Crawler();
        writeToFile("[");
        for (Feed feed : feeds) {
            try {
                List<Ad> ads = crawler.getAmazonProds(feed);
                for (Ad ad : ads) {
                    if (ad.getAdId() != 0) writeToFile(",");
                    writeToFile(ad);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        writeToFile("]");

    }

    private static void writeToFile(Object ad) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            //Convert object to JSON string and save into file directly
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT, true))); // append mode file writer
            mapper.writeValue(out, ad);

            //Convert object to JSON string and pretty print
//            String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ads);
//            System.out.println(jsonInString);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (JsonMappingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private static void writeToFile(String s) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(OUTPUT, true));
            out.write(s);
            out.write("\n");
            out.close();
        } catch (IOException e) {
            System.out.println("Exception ");

        }
    }

}
