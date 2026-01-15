package libgdx.campaign;

public class CampaignStoreLevel {

    private int level;
    private int status;
    private long score;
    private String name;

    public CampaignStoreLevel(CampaignLevel campaignLevel) {
        level = campaignLevel.getIndex();
        score = 0;
        status = CampaignLevelStatusEnum.IN_PROGRESS.getStatus();
        name = campaignLevel.getName();
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
}
