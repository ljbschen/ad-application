package demo;

import demo.domain.Ad;
import demo.domain.Campaign;
import demo.indexBuilder.IndexBuilder;
import demo.indexBuilder.IndexBuilderService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootApplication
public class Application {
    private static final String AdsDataFilePath = "";
    private static final String BudgetFilePath = "";

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        init();
    }

    private static void init() {
        try {
            BufferedReader brAd = new BufferedReader(new FileReader(AdsDataFilePath));
            String line;
            while ((line = brAd.readLine()) != null) {
                JSONObject adJson = new JSONObject(line);
                Ad ad = new Ad();
                if (adJson.isNull("adId") || adJson.isNull("campaignId")) {
                    continue;
                }
                ad.adId = adJson.getLong("adId");
                ad.campaignId = adJson.getLong("campaignId");
                ad.brand = adJson.isNull("brand") ? "" : adJson.getString("brand");
                ad.price = adJson.isNull("price") ? 100.0 : adJson.getDouble("price");
                ad.thumbnail = adJson.isNull("thumbnail") ? "" : adJson.getString("thumbnail");
                ad.title = adJson.isNull("title") ? "" : adJson.getString("title");
                ad.detail_url = adJson.isNull("detail_url") ? "" : adJson.getString("detail_url");
                ad.bidPrice = adJson.isNull("bidPrice") ? 1.0 : adJson.getDouble("bidPrice");
                ad.pClick = adJson.isNull("pClick") ? 0.0 : adJson.getDouble("pClick");
                ad.category = adJson.isNull("category") ? "" : adJson.getString("category");
                ad.description = adJson.isNull("description") ? "" : adJson.getString("description");
                ad.keyWords = new ArrayList<String>();
                JSONArray keyWords = adJson.isNull("keyWords") ? null : adJson.getJSONArray("keyWords");
                for (int j = 0; j < keyWords.length(); j++) {
                    ad.keyWords.add(keyWords.getString(j));
                }

                if (!indexBuilderService.buildInvertIndex(ad) || !indexBuilderService.buildForwardIndex(ad)) {
                    //log
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //load budget data
        try {
            BufferedReader brBudget = new BufferedReader(new FileReader(BudgetFilePath))
            String line;
            while ((line = brBudget.readLine()) != null) {
                JSONObject campaignJson = new JSONObject(line);
                Long campaignId = campaignJson.getLong("campaignId");
                double budget = campaignJson.getDouble("budget");
                Campaign camp = new Campaign();
                camp.campaignId = campaignId;
                camp.budget = budget;
                if (!indexBuilder.updateBudget(camp)) {
                    //log
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        indexBuilder.Close();
    }
}
