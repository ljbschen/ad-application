package demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "CAMPAIGNS")
public class Campaign {
    @Id
    @GeneratedValue
    private Long campaignId;
    private double budget;
}
