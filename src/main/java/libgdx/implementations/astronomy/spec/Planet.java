package libgdx.implementations.astronomy.spec;

public class Planet {

    private int id;
    private String name;
    private int orbitalPeriodInDays;
    private int lightFromSunInSec;
    private int meanTempInC;
    private float radius;
    private float massInRelationToEarth;
    private float gravityInRelationToEarth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrbitalPeriodInDays() {
        return orbitalPeriodInDays;
    }

    public void setOrbitalPeriodInDays(int orbitalPeriodInDays) {
        this.orbitalPeriodInDays = orbitalPeriodInDays;
    }

    public int getLightFromSunInSec() {
        return lightFromSunInSec;
    }

    public void setLightFromSunInSec(int lightFromSunInSec) {
        this.lightFromSunInSec = lightFromSunInSec;
    }

    public int getMeanTempInC() {
        return meanTempInC;
    }

    public void setMeanTempInC(int meanTempInC) {
        this.meanTempInC = meanTempInC;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getMassInRelationToEarth() {
        return massInRelationToEarth;
    }

    public void setMassInRelationToEarth(float massInRelationToEarth) {
        this.massInRelationToEarth = massInRelationToEarth;
    }

    public float getGravityInRelationToEarth() {
        return gravityInRelationToEarth;
    }

    public void setGravityInRelationToEarth(float gravityInRelationToEarth) {
        this.gravityInRelationToEarth = gravityInRelationToEarth;
    }
}

