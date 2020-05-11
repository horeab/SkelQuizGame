package libgdx.implementations.animals;

import libgdx.campaign.CampaignLevel;

public enum AnimalsCampaignLevelEnum implements CampaignLevel {

    //This order is displayed on the campaign screen
    LEVEL_0_0,
    ////
    LEVEL_1_0,
    ////
    LEVEL_2_0,
    ////
    LEVEL_3_0,
    ////
    LEVEL_4_0,
    ////
    LEVEL_5_0,
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
