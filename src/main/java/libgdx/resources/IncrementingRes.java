package libgdx.resources;

public class IncrementingRes {

    private int beginIndex;
    private int endIndex;
    private String path;
    private String resName;

    public IncrementingRes(int beginIndex, int endIndex, String path, String resName) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.path = path;
        this.resName = resName;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public String getPath() {
        return path;
    }

    public String getResName() {
        return resName;
    }
}
