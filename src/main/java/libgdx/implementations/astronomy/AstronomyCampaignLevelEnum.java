package libgdx.implementations.astronomy;

import libgdx.campaign.CampaignLevel;

public enum AstronomyCampaignLevelEnum implements CampaignLevel {

    LEVEL_0_0,
    LEVEL_0_1,
    LEVEL_0_2,
    LEVEL_0_3,
    LEVEL_0_4,
    LEVEL_0_5,
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
