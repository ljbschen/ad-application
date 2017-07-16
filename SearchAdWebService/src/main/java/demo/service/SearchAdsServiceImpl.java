package demo.service;

import demo.domain.Ad;
import demo.domain.AdRepository;
import demo.domain.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchAdsServiceImpl implements SearchAdsService {
    private final AdRepository adRepository;

    private final CampaignRepository campaignRepository;

    @Autowired
    public SearchAdsServiceImpl(AdRepository adRepository, CampaignRepository campaignRepository) {
        this.adRepository = adRepository;
        this.campaignRepository = campaignRepository;
    }

    @Override
    public List<Ad> getAds(String query) {
        // process the query
        // send clean query to IndexBuilder
        return null;
    }
}
