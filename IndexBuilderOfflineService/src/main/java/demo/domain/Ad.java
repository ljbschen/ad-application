package demo.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "ADS")
public class Ad {
    @Id
    @GeneratedValue
    private Long id;
    private Long adId;
    private Long campaignId;
    @Column(columnDefinition = "LONGBLOB")
    private String keyWords;
    private double relevanceScore;
    private double pClick;
    private double bidPrice;
    private double rankScore;
    private double qualityScore;
    private double costPerClick;
    private int position;//1: top , 2: bottom
    @Column(columnDefinition = "LONGBLOB")
    private String title; // required
    private double price; // required
    private String thumbnail; // required
    @Column(columnDefinition = "LONGBLOB")
    private String description; // required
    private String brand; // required
    private String detail_url; // required
    private String query; //required
    private String category;
}