package demo.service;

import demo.domain.Ad;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchAdsServiceImpl implements SearchAdsService {

    @Override
    public List<Ad> getAds(String query) {
        // process the query
        // send clean query to IndexBuilder
        return null;
    }
}
