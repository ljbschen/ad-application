package demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long>{
    Campaign getCampaignByCampaignId(Long id);
}
