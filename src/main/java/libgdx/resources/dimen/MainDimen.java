package libgdx.resources.dimen;

public enum MainDimen implements Dimen {

    horizontal_general_margin(2),
    vertical_general_margin(2),
    ///////////////////////////
    height_default_btn(8),
    width_default_btn(62),
    side_btn_image(8),
    side_btn_back(8),
    side_btn_unlock_content(7),
    //////////////////////////
    side_notification_popup_icon(8),;

    private float dimen;

    MainDimen(float dimen) {
        this.dimen = dimen;
    }

    @Override
    public float getDimen() {
        return DimenUtils.getDimen(this);
    }

    @Override
    public int getIntegerValueOfDimen() {
        return DimenUtils.getIntegerValueOfDimen(this);
    }

    @Override
    public float getRawDimen() {
        return dimen;
    }
}
