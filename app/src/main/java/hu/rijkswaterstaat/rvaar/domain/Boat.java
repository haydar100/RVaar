package hu.rijkswaterstaat.rvaar.domain;

/**
 * Created by Haydar on 21-05-15.
 */
public class Boat {
    protected String boatname, boatType;
    protected double x, y;
    protected double bearing;
    protected User user;

    public Boat(String boatType, String boatname, double x, double y, double bearing, User user) {
        this.boatType = boatType;
        this.boatname = boatname;
        this.x = x;
        this.y = y;
        this.bearing = bearing;
        this.user = user;
    }


    public String getBoatname() {
        return boatname;
    }

    public void setBoatname(String boatname) {
        this.boatname = boatname;
    }

    public String getBoatType() {
        return boatType;
    }

    public void setBoatType(String boatType) {
        this.boatType = boatType;
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

    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

}
