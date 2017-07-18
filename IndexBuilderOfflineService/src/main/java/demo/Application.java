package demo;

import demo.indexBuilder.IndexBuilderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final String AdsDataFilePath = "";
    private static final String BudgetFilePath = "";
    private final static Logger LOGGER = Logger.getLogger(Application.class);

    @Autowired
    private IndexBuilderService indexBuilderService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
        LOGGER.info(this.indexBuilderService.getMessage());
//        init();
    }

//    private static void init() {
//        try {
//            BufferedReader brAd = new BufferedReader(new FileReader(AdsDataFilePath));
//            String line;
//            while ((line = brAd.readLine()) != null) {
//                JSONObject adJson = new JSONObject(line);
//                Ad ad = new Ad();
//                if (adJson.isNull("adId") || adJson.isNull("campaignId")) {
//                    continue;
//                }
//                ad.adId = adJson.getLong("adId");
//                ad.campaignId = adJson.getLong("campaignId");
//                ad.brand = adJson.isNull("brand") ? "" : adJson.getString("brand");
//                ad.price = adJson.isNull("price") ? 100.0 : adJson.getDouble("price");
//                ad.thumbnail = adJson.isNull("thumbnail") ? "" : adJson.getString("thumbnail");
//                ad.title = adJson.isNull("title") ? "" : adJson.getString("title");
//                ad.detail_url = adJson.isNull("detail_url") ? "" : adJson.getString("detail_url");
//                ad.bidPrice = adJson.isNull("bidPrice") ? 1.0 : adJson.getDouble("bidPrice");
//                ad.pClick = adJson.isNull("pClick") ? 0.0 : adJson.getDouble("pClick");
//                ad.category = adJson.isNull("category") ? "" : adJson.getString("category");
//                ad.description = adJson.isNull("description") ? "" : adJson.getString("description");
//                ad.keyWords = new ArrayList<String>();
//                JSONArray keyWords = adJson.isNull("keyWords") ? null : adJson.getJSONArray("keyWords");
//                for (int j = 0; j < keyWords.length(); j++) {
//                    ad.keyWords.add(keyWords.getString(j));
//                }
//
//                if (!indexBuilderService.buildInvertIndex(ad) || !indexBuilderService.buildForwardIndex(ad)) {
//                    LOGGER.error("Can't build index on ad " + ad.adId);
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //load budget data
//        try {
//            BufferedReader brBudget = new BufferedReader(new FileReader(BudgetFilePath));
//            String line;
//            while ((line = brBudget.readLine()) != null) {
//                JSONObject campaignJson = new JSONObject(line);
//                Long campaignId = campaignJson.getLong("campaignId");
//                double budget = campaignJson.getDouble("budget");
//                Campaign camp = new Campaign();
//                camp.campaignId = campaignId;
//                camp.budget = budget;
//                if (!indexBuilderService.updateBudget(camp)) {
//                    LOGGER.error("Can't build index on campaign " + camp.getCampaignId());
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            LOGGER.error(e.getMessage());
//        }
//    }
}
