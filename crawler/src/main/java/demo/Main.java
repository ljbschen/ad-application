package demo;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String FILENAME = "crawler/rawQuery.txt";
    private static final String OUTPUT = "ad.json";
    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Start app...");
        try {
            File file = new File("ad.json");
            if (file.delete()) {
                logger.info(file.getName() + " is deleted!");
            } else {
                logger.warn("Delete operation is failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }


        FeedsHandler feedsHandler = new FeedsHandler(FILENAME);
        List<Feed> feeds = feedsHandler.generateFeeds();
        List<Ad> adsList = new ArrayList<Ad>();
        for (Feed feed : feeds) {
            try {
                Crawler crawler = new Crawler(feed);
                List<Ad> ads = crawler.getAmazonProds();
                adsList.addAll(ads);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
                writeToFile(adsList);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }

    }

    private static void writeToFile(List<Ad> ads) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            //Convert object to JSON string and save into file directly
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT, true))); // append mode file writer
            mapper.writeValue(out, ads);

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

}
