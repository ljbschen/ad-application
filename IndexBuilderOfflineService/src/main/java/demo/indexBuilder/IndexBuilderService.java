package demo.indexBuilder;

import demo.domain.Ad;
import demo.domain.AdRepository;
import demo.domain.Campaign;
import demo.domain.CampaignRepository;
import demo.utility.Utility;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class IndexBuilderService {
    private final static Logger LOGGER = Logger.getLogger(IndexBuilderService.class);
    private final static int EXP = 0; //0: never expire
    private static final String AdsDataFilePath = "ads_0502.txt";
    private static final String BudgetFilePath = "budget.txt";

    private AdRepository adRepository;
    private CampaignRepository campaignRepository;
    private MemcachedClient memcachedClient;

    @Autowired
    public IndexBuilderService(AdRepository adRepository, CampaignRepository campaignRepository) {
        this.adRepository = adRepository;
        this.campaignRepository = campaignRepository;
        try {
            memcachedClient = new MemcachedClient(
                    new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(),
                    AddrUtil.getAddresses("127.0.0.1:11211"));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    private boolean buildInvertIndex(Ad ad) {
        String keyWords = Utility.strJoin(new ArrayList<String>(Arrays.asList(ad.getKeyWords().split(","))), ",");
        List<String> tokens = Utility.cleanedTokenize(keyWords);
        for (String key : tokens) {
            if (memcachedClient.get(key) instanceof Set) {
                Set<Long> adIdList = (Set<Long>) memcachedClient.get(key);
                adIdList.add(ad.getAdId());
                memcachedClient.set(key, EXP, adIdList);
            } else {
                Set<Long> adIdList = new HashSet<Long>();
                adIdList.add(ad.getAdId());
                memcachedClient.set(key, EXP, adIdList);
            }
        }
        return true;
    }

    private boolean buildForwardIndex(Ad ad) {
        boolean result = false;
        try {
            this.adRepository.save(ad);
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    private boolean updateBudget(Campaign camp) {
        boolean result = false;
        try {
            this.campaignRepository.save(camp);
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    public void init() {
        try {
            BufferedReader brAd = new BufferedReader(new FileReader(AdsDataFilePath));
            String line;
            while ((line = brAd.readLine()) != null) {
                try {
                    System.out.println(line);
                    if (line.charAt(0) == '[') continue;
                    line = line.substring(0, line.lastIndexOf('}') + 1);
                    JSONObject adJson = new JSONObject(line);
                    Ad ad = new Ad();
                    if (adJson.isNull("adId") || adJson.isNull("campaignId")) {
                        continue;
                    }
                    ad.setAdId(adJson.getLong("adId"));
                    ad.setCampaignId(adJson.getLong("campaignId"));
                    ad.setBrand(adJson.isNull("brand") ? "" : adJson.getString("brand"));
                    ad.setPrice(adJson.isNull("price") ? 100.0 : adJson.getDouble("price"));
                    ad.setThumbnail(adJson.isNull("thumbnail") ? "" : adJson.getString("thumbnail"));
                    ad.setTitle(adJson.isNull("title") ? "" : adJson.getString("title"));
                    ad.setDetail_url(adJson.isNull("detail_url") ? "" : adJson.getString("detail_url"));
                    ad.setBidPrice(adJson.isNull("bidPrice") ? 1.0 : adJson.getDouble("bidPrice"));
                    ad.setPClick(adJson.isNull("pClick") ? 0.0 : adJson.getDouble("pClick"));
                    ad.setCategory(adJson.isNull("category") ? "" : adJson.getString("category"));
                    ad.setDescription(adJson.isNull("description") ? "" : adJson.getString("description"));
                    JSONArray keyWordsArray = adJson.isNull("keyWords") ? null : adJson.getJSONArray("keyWords");
                    StringBuilder keyWords = new StringBuilder();
                    if (keyWordsArray != null) {
                        for (int i = 0; i < keyWordsArray.length(); i++) {
                            keyWords.append(keyWordsArray.getString(i)).append(",");
                        }
                    }
                    ad.setKeyWords(keyWords.toString());
                    if (!buildInvertIndex(ad) || !buildForwardIndex(ad)) {
                        LOGGER.error("Can't build index on ad " + ad.getAdId());
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //load budget data
        try {
            BufferedReader brBudget = new BufferedReader(new FileReader(BudgetFilePath));
            String line;
            while ((line = brBudget.readLine()) != null) {
                System.out.println("budget line " + line);
                JSONObject campaignJson = new JSONObject(line);
                Long campaignId = campaignJson.getLong("campaignId");
                double budget = campaignJson.getDouble("budget");
                Campaign camp = new Campaign();
                camp.setCampaignId(campaignId);
                camp.setBudget(budget);
                if (!updateBudget(camp)) {
                    LOGGER.error("Can't build index on campaign " + camp.getCampaignId());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }
}
