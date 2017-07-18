package demo.rest;

import demo.domain.Ad;
import demo.service.SearchAdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchAdRestController {
    private SearchAdsService searchAdsService;

    @Autowired
    public SearchAdRestController(SearchAdsService searchAdsService) {
        this.searchAdsService = searchAdsService;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public List<Ad> getAds(@RequestParam(name = "query") String query) {
        return this.searchAdsService.selectAds(query);
    }
}
