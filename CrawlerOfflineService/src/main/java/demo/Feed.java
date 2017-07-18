package demo;

import lombok.Data;

@Data
public class Feed {
    private String query;
    private double bid;
    private int campaignID;
    private int queryGroupID;

    public Feed(String query, double bid, int campaignID, int queryGroupID) {
        this.query = query;
        this.bid = bid;
        this.campaignID = campaignID;
        this.queryGroupID = queryGroupID;
    }
}
