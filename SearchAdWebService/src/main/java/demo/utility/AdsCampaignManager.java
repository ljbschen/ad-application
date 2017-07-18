package demo.utility;

import demo.domain.Ad;
import demo.domain.AdRepository;
import demo.domain.Campaign;
import demo.domain.CampaignRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AdsCampaignManager {
    private static AdsCampaignManager instance = null;
    private static final double minPriceThreshold = 0.0;
//    private final AdRepository adRepository;
    private final CampaignRepository campaignRepository;

    private AdsCampaignManager(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public static AdsCampaignManager getInstance(CampaignRepository campaignRepository) {
        if (instance == null) {
            instance = new AdsCampaignManager(campaignRepository);
        }
        return instance;
    }

    public List<Ad> DedupeByCampaignId(List<Ad> adsCandidates) {
        List<Ad> dedupedAds = new ArrayList<Ad>();
        HashSet<Long> campaignIdSet = new HashSet<Long>();
        for (Ad ad : adsCandidates) {
            if (!campaignIdSet.contains(ad.getCampaignId())) {
                dedupedAds.add(ad);
                campaignIdSet.add(ad.getCampaignId());
            }
        }
        return dedupedAds;
    }

    public List<Ad> ApplyBudget(List<Ad> adsCandidates) {
        List<Ad> ads = new ArrayList<Ad>();
        try {
            for (int i = 0; i < adsCandidates.size() - 1; i++) {
                Ad ad = adsCandidates.get(i);
                Long campaignId = ad.getCampaignId();
                double budget = this.campaignRepository.getCampaignByCampaignId(campaignId).getBudget();
                System.out.println("AdsCampaignManager ad.costPerClick= " + ad.getCostPerClick());
                System.out.println("AdsCampaignManager campaignId= " + campaignId);
                System.out.println("AdsCampaignManager budget left = " + budget);
                if (ad.getCostPerClick() <= budget && ad.getCostPerClick() >= minPriceThreshold) {
                    ads.add(ad);
                    budget = budget - ad.getCostPerClick();
                    this.campaignRepository.saveAndFlush(new Campaign(campaignId, budget));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ads;
    }
}
