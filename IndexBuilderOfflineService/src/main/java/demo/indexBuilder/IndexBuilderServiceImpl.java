package demo.indexBuilder;

import demo.domain.Ad;
import demo.domain.AdRepository;
import demo.domain.Campaign;
import demo.domain.CampaignRepository;
import demo.utility.Utility;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class IndexBuilderServiceImpl implements IndexBuilderService {
    private final static Logger LOGGER = Logger.getLogger(IndexBuilderServiceImpl.class);
    private final static int EXP = 0; //0: never expire

    private final AdRepository adRepository;
    private final CampaignRepository campaignRepository;
    private MemcachedClient memcachedClient;

    @Autowired
    public IndexBuilderServiceImpl(AdRepository adRepository, CampaignRepository campaignRepository) {
        this.adRepository = adRepository;
        this.campaignRepository = campaignRepository;
        try {
            memcachedClient = new MemcachedClient(
                    new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(),
                    AddrUtil.getAddresses("127.0.0.1:11211"));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public boolean buildInvertIndex(Ad ad) {
        String keyWords = Utility.strJoin(ad.keyWords, ",");
        List<String> tokens = Utility.cleanedTokenize(keyWords);
        for (int i = 0; i < tokens.size(); i++) {
            String key = tokens.get(i);
            if (memcachedClient.get(key) instanceof Set) {
                Set<Long> adIdList = (Set<Long>) memcachedClient.get(key);
                adIdList.add(ad.adId);
                memcachedClient.set(key, EXP, adIdList);
            } else {
                Set<Long> adIdList = new HashSet<Long>();
                adIdList.add(ad.adId);
                memcachedClient.set(key, EXP, adIdList);
            }
        }
        return true;
    }

    @Override
    public boolean buildForwardIndex(Ad ad) {
        boolean result = false;
        try {
            this.adRepository.save(ad);
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    @Override
    public boolean updateBudget(Campaign camp) {
        boolean result = false;
        try {
            this.campaignRepository.save(camp);
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }
}
