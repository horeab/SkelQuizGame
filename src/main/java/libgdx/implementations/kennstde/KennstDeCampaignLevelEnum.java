package libgdx.implementations.kennstde;

import libgdx.campaign.CampaignLevel;

public enum KennstDeCampaignLevelEnum implements CampaignLevel {

    //This order is displayed on the campaign screen
    LEVEL_0_2,
    LEVEL_0_3,
    LEVEL_0_0,
    LEVEL_0_4,
    LEVEL_0_1,
    ////
    ;

    @Override
    public int getIndex() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name();
    }

}
