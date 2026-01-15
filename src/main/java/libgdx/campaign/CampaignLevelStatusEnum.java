package libgdx.campaign;

public enum CampaignLevelStatusEnum {
    IN_PROGRESS(0),
    FINISHED(1),;

    private int status;

    CampaignLevelStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}