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
    LEVEL_0_10,
    LEVEL_0_11,

    LEVEL_0_12,
    LEVEL_0_13,
    LEVEL_0_14,
    LEVEL_0_15,
    LEVEL_0_16,
    LEVEL_0_17,
    LEVEL_0_18,
    LEVEL_0_19,
    LEVEL_0_20,
    LEVEL_0_21,
    LEVEL_0_22,
    LEVEL_0_23,
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
