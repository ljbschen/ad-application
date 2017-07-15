package demo.indexBuilder;

import demo.domain.Ad;
import demo.domain.Campaign;

public interface IndexBuilderService  {

    boolean buildInvertIndex(Ad ad);

    boolean buildForwardIndex(Ad ad);

    boolean updateBudget(Campaign camp);
}
