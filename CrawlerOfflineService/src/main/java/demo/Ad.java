package demo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Ad implements Serializable {
    private static final long serialVersionUID = 1L;
    public int adId; // done
    public int campaignId; // done
    public List<String> keyWords;
    public double relevanceScore; // n/a
    public double pClick; // n/a
    public double bidPrice; // done
    public double rankScore; // n/a
    public double qualityScore; // n/a
    public double costPerClick; // n/a
    public int position;//1: top , 2: bottom
    public String title; // done
    public double price; // done
    public String thumbnail; // done
    public String description; // required
    public String brand; // done
    public String detail_url; // done
    public String query; //done
    public int query_group_id; // done
    public String category; // n/a
}
