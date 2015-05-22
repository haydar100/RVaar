package hu.rijkswaterstaat.rvaar.domain;

/**
 * Created by Haydar on 21-05-15.
 */
public class User {
    protected String deviceID;
    protected Niveau niveau;

    public User(String deviceID, Niveau niveau) {
        this.deviceID = deviceID;
        this.niveau = niveau;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

}
