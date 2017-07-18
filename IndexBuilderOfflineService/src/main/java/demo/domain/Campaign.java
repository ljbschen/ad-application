package demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "ads")
public class Campaign {
    @Id
    @GeneratedValue
    public Long campaignId;
    public double budget;
}
