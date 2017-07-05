package demo;

import java.io.Serializable;
import java.util.List;

public class Ad implements Serializable {
    private static final long serialVersionUID = 1L;
    public int adId; // done
    public int campaignId; // done
    public List<String> keyWords;
    public double relevanceScore;
    public double pClick;
    public double bidPrice;
    public double rankScore;
    public double qualityScore;
    public double costPerClick;
    public int position;//1: top , 2: bottom
    public String title; // done
    public double price; // done
    public String thumbnail; // done
    public String description; // required
    public String brand; // done
    public String detail_url; // done
    public String query; //done
    public int query_group_id;
    public String category;
}
