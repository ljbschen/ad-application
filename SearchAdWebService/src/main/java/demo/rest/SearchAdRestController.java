package demo.rest;

import demo.domain.Ad;
import demo.service.SearchAdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchAdRestController {
    private final SearchAdsService searchAdsService;

    @Autowired
    public SearchAdRestController(SearchAdsService searchAdsService) {
        this.searchAdsService = searchAdsService;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public List<Ad> getAds(@PathVariable String query) {
        return null;
    }
}
