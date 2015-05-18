package hu.rijkswaterstaat.rvaar.domain;

/**
 * Created by Haydar on 18-05-15.
 */
public class UserLocation {
    String id, boatname, boatType;
    double x, y;
    float direction;


    public UserLocation(String id, String boatname, String boatType, double x, double y, float direction) {

        this.id = id;
        this.boatname = boatname;
        this.boatType = boatType;

        this.x = x;
        this.y = y;
        this.direction = direction;
    }


    public UserLocation() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoatname() {
        return boatname;
    }

    public void setBoatname(String boatname) {
        this.boatname = boatname;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public String getBoatType() {
        return boatType;
    }

    public void setBoatType(String boatType) {
        this.boatType = boatType;
    }
}
