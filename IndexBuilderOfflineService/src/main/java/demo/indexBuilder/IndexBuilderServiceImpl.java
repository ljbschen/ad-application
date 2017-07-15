package demo.indexBuilder;

import demo.domain.Ad;
import demo.domain.Campaign;
import org.springframework.stereotype.Service;

@Service
public class IndexBuilderServiceImpl implements IndexBuilderService {
    @Override
    public boolean buildInvertIndex(Ad ad) {
        return false;
    }

    @Override
    public boolean buildForwardIndex(Ad ad) {
        return false;
    }

    @Override
    public boolean updateBudget(Campaign camp) {
        return false;
    }
}
