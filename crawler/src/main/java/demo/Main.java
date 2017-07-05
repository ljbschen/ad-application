package demo;

import org.apache.log4j.Logger;

import java.util.List;

public class Main {
    private static final String FILENAME = "rawQuery.txt";
    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Start app...");
        FeedsHandler feedsHandler = new FeedsHandler(FILENAME);
        List<Feed> feeds = feedsHandler.generateFeeds();
        for (Feed feed : feeds) {
            Crawler crawler = new Crawler(feed);
            crawler.getAmazonProds();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
