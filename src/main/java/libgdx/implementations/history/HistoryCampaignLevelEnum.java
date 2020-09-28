package libgdx.implementations.history;

import libgdx.campaign.CampaignLevel;

public enum HistoryCampaignLevelEnum implements CampaignLevel {

    LEVEL_0_0,
    LEVEL_0_1,
    ;
    ////;

    @Override
    public int getIndex() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name();
    }

}
