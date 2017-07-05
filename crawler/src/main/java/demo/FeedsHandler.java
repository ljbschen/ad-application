package demo;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeedsHandler {
    private String fileName;
    final static Logger logger = Logger.getLogger(FeedsHandler.class);

    public FeedsHandler(String fileName) {
        this.fileName = fileName;
    }

    public List<Feed> generateFeeds() {
        List<Feed> feeds = new ArrayList<Feed>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            String sCurrentLine;
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            while ((sCurrentLine = br.readLine()) != null) {
                if (!sCurrentLine.contains(",")) continue;
                String[] fields = sCurrentLine.split(",");
                Feed feed = new Feed(fields[0], Double.parseDouble(fields[1].trim()), Integer.parseInt(fields[2].trim()), Integer.parseInt(fields[3].trim()));
                feeds.add(feed);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                logger.error(ex.getMessage());
            }

        }
        return feeds;
    }
}
