package demo;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawler {
    final static Logger logger = Logger.getLogger(Crawler.class);
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
    private static final String AMAZON_QUERY_URL = "https://www.amazon.com/s/ref=nb_sb_noss?field-keywords=";

    private Feed feed;

    public Crawler(Feed feed) {
        this.feed = feed;
    }

    public List<Ad> getAmazonProds() {
        String query = this.feed.getQuery();
        List<Ad> ads = null;
        for (int i = 1; i < 2; i++) {
            String url = AMAZON_QUERY_URL + query + "&page=" + i;
            System.out.println("url" + url);
            try {
                Document doc = Jsoup.connect(url).maxBodySize(0).userAgent(USER_AGENT).timeout(10000).get();
                Integer docSize = doc.text().length();
                logger.info("page size: " + docSize);
                List<Element> prods = doc.getElementsByClass("s-result-item celwidget ");
                logger.info("number of prod: " + prods.size());
                ads = parseToAds(prods);

            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        return ads;
    }

    private List<Ad> parseToAds(List<Element> prods) {
        List<Ad> ads = new ArrayList<Ad>();
        for (Integer i = 0; i < prods.size(); i++) {
            String id = "result_" + i.toString();
            Element prodsById = prods.get(i).getElementById(id);
//            String asin = prodsById.attr("data-asin");
//            System.out.println("prod asin: " + asin);
            // get title
            String title = "", thumbnail = "", description = "", brand = "", detail_url = "";
            double price = 0;
            Elements titleEleList = prodsById.getElementsByAttribute("title");
            if (titleEleList.size() > 0) {
                title = titleEleList.get(0).attr("title");
            }

            // get thumbnail
            Elements imgEleList = prodsById.getElementsByClass("s-access-image cfMarker");
            if (imgEleList.size() > 0) {
                thumbnail = imgEleList.get(0).attr("src");
            }

//            #result_0 > div > div > div > div.a-fixed-left-grid-col.a-col-right > div:nth-child(4) > div.a-column.a-span5.a-span-last > div:nth-child(2) > span:nth-child(3)
//            #result_1 > div > div > div > div.a-fixed-left-grid-col.a-col-right > div:nth-child(4) > div.a-column.a-span5.a-span-last > div:nth-child(2) > span:nth-child(3)
//            #result_2 > div > div > div > div.a-fixed-left-grid-col.a-col-right > div:nth-child(3) > div.a-column.a-span5.a-span-last > div:nth-child(2) > span:nth-child(3)
//            #result_3 > div > div > div > div.a-fixed-left-grid-col.a-col-right > div:nth-child(2) > div.a-column.a-span5.a-span-last > div:nth-child(2) > span:nth-child(3)
//            #result_4 > div > div > div > div.a-fixed-left-grid-col.a-col-right > div:nth-child(3) > div.a-column.a-span5.a-span-last > div:nth-child(2) > span:nth-child(3)
//            #result_5 > div > div > div > div.a-fixed-left-grid-col.a-col-right > div:nth-child(2) > div.a-column.a-span5.a-span-last > div:nth-child(3) > span:nth-child(3)
//            #result_17 > div > div > div > div.a-fixed-left-grid-col.a-col-right > div:nth-child(2) > div.a-column.a-span5.a-span-last > div:nth-child(2)

            // get description
            Element dLevel1 = prodsById.getElementsByClass("a-column a-span5 a-span-last").get(0);
            Elements dLevel2List = dLevel1.select("div");
            Element dLevel2 = dLevel2List.get(dLevel2List.size() - 1);

//            System.out.println("description is " + dElement.text());

//            #result_0 > div > div > div > div.a-fixed-left-grid-col.a-col-right > div.a-row.a-spacing-small > div:nth-child(2) > span:nth-child(2)
//            #result_6 > div > div > div > div.a-fixed-left-grid-col.a-col-right > div.a-row.a-spacing-small > div:nth-child(2) > span:nth-child(2)
            // get brand
            brand = prodsById.select("#" + id + " > div > div > div > div.a-fixed-left-grid-col.a-col-right > div.a-row.a-spacing-small > div:nth-child(2) > span:nth-child(2)").text();
//            System.out.println("brand is " + brand);
            // get detail_url
            Elements detailUrlList = prodsById.getElementsByAttribute("href");
            if (detailUrlList.size() > 0) {
                detail_url = detailUrlList.get(0).attr("href");
            }

            // get price
            Elements priceWholeList = prodsById.getElementsByClass("sx-price-whole");
            Elements priceFragList = prodsById.getElementsByClass("sx-price-fractional");
            if (priceWholeList.size() > 0 && priceFragList.size() > 0) {
                price = Integer.parseInt(priceWholeList.get(0).text().replace(",", "")) + Double.parseDouble(priceFragList.get(0).text()) / 100;
            }
//            System.out.println("price is " + price);
        }
        return null;
    }

}
