package libgdx.resources;

public class ResImpl implements Res{

    private String path;
    private Class<?> classType;
    private String name;

    public ResImpl(String path, Class<?> classType, String name) {
        this.path = path;
        this.classType = classType;
        this.name = name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Class<?> getClassType() {
        return classType;
    }

    @Override
    public String name() {
        return name;
    }
}
