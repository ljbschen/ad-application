package demo.service;

import demo.domain.Ad;

import java.util.List;

public interface SearchAdsService {
    List<Ad> getAds(String query);
}
