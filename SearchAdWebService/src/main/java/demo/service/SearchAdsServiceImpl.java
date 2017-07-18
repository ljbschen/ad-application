package demo.service;

import demo.domain.Ad;
import demo.domain.AdRepository;
import demo.domain.CampaignRepository;
import demo.utility.AdsCampaignManager;
import demo.utility.AdsFilter;
import demo.utility.AdsSelector;
import demo.utility.QueryParser;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

@Service
public class SearchAdsServiceImpl implements SearchAdsService {
    private final AdRepository adRepository;
    private final CampaignRepository campaignRepository;
    private MemcachedClient memcachedClient;

    @Autowired
    public SearchAdsServiceImpl(AdRepository adRepository, CampaignRepository campaignRepository) {
        this.adRepository = adRepository;
        this.campaignRepository = campaignRepository;
        try {
            this.memcachedClient = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ad> selectAds(String query) {
        //query understanding
        List<String> queryTerms = QueryParser.getInstance().QueryUnderstand(query);
        //select ads candidates
        List<Ad> adsCandidates = AdsSelector.getInstance(this.memcachedClient, this.adRepository).selectAds(queryTerms);
//        //L0 filter by pClick, relevance score
        List<Ad> L0unfilteredAds = AdsFilter.getInstance().LevelZeroFilterAds(adsCandidates);
        System.out.println("L0unfilteredAds ads left = " + L0unfilteredAds.size());
//
        //L1 filter by relevance score : select top K ads
        int k = 20;
        List<Ad> unfilteredAds = AdsFilter.getInstance().LevelOneFilterAds(L0unfilteredAds,k);
        System.out.println("unfilteredAds ads left = " + unfilteredAds.size());

        //Dedupe ads per campaign
        List<Ad> dedupedAds = AdsCampaignManager.getInstance(this.campaignRepository).DedupeByCampaignId(unfilteredAds);
        System.out.println("dedupedAds ads left = " + dedupedAds.size());

        List<Ad> ads = AdsCampaignManager.getInstance(this.campaignRepository).ApplyBudget(dedupedAds);
        System.out.println("AdsCampaignManager ads left = " + ads.size());

        //allocation
        //AdsAllocation.getInstance().AllocateAds(ads);
        return ads;
    }
}
