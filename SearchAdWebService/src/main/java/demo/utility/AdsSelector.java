package demo.utility;

import demo.domain.Ad;
import demo.domain.AdRepository;
import net.spy.memcached.MemcachedClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AdsSelector {
    private static AdsSelector instance = null;
    //private int EXP = 7200;
    private AdRepository adRepository;
    private MemcachedClient memcachedClient;

    private AdsSelector(MemcachedClient memcachedClient, AdRepository adRepository) {
        this.memcachedClient = memcachedClient;
        this.adRepository = adRepository;
    }

    public static AdsSelector getInstance(MemcachedClient memcachedClient, AdRepository adRepository) {
        if (instance == null) {
            instance = new AdsSelector(memcachedClient, adRepository);
        }
        return instance;
    }


    public List<Ad> selectAds(List<String> queryTerms) {
        List<Ad> adList = new ArrayList<Ad>();
        //key: adID, value : count of adId which match current query
        HashMap<Long, Integer> matchedAds = new HashMap<Long, Integer>();
        try {
            for (String queryTerm : queryTerms) {
                System.out.println("selectAds queryTerm = " + queryTerm);
                Set<Long> adIdList = (Set<Long>) this.memcachedClient.get(queryTerm);
                if (adIdList != null && adIdList.size() > 0) {
                    for (Object adId : adIdList) {
                        Long key = (Long) adId;
                        if (matchedAds.containsKey(key)) {
                            int count = matchedAds.get(key) + 1;
                            matchedAds.put(key, count);
                        } else {
                            matchedAds.put(key, 1);
                        }
                    }
                }
            }
            for (Long adId : matchedAds.keySet()) {
                System.out.println("selectAds adId = " + adId);
                Ad ad = adRepository.getAdByAdId(adId);
                if (ad == null) {
                    continue;
                }
                //number of word match query / total number of words in key words
                double relevanceScore = (matchedAds.get(adId) * 1.0 / ad.getKeyWords().split(",").length);
                ad.setRelevanceScore(relevanceScore);
                System.out.println("selectAds pClick = " + ad.getPClick());
                System.out.println("selectAds relevanceScore = " + ad.getRelevanceScore());
                adList.add(ad);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return adList;
    }
}
