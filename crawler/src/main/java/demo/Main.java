package demo;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String FILENAME = "crawler/rawQuery2.txt";
    private static final String OUTPUT = "ad.json";
    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Start app...");

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
            mapper.writeValue(new File(OUTPUT), ads);

            //Convert object to JSON string and pretty print
            String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ads);
            System.out.println(jsonInString);
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
