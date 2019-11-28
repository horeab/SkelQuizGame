package libgdx.implementations.anatomy;

import libgdx.campaign.CampaignLevel;

public enum AnatomyCampaignLevelEnum implements CampaignLevel {

    //This order is displayed on the campaign screen
    LEVEL_0_0,
    LEVEL_0_1,
    LEVEL_0_2,
    LEVEL_0_3,
    LEVEL_0_4,
    LEVEL_0_5,
    LEVEL_0_6,
    LEVEL_0_7,
    LEVEL_0_8,
    LEVEL_0_9,
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
