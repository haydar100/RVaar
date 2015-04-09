package hu.rijkswaterstaat.rvaar.dao;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Berkan on 10-3-2015.
 */
public interface MarkerDAO {
    public ArrayList<MarkerOptions> getMarkers();

    public ArrayList<String> getTipsTricks();
    public void saveLocationOfUser(String id, double x, double y);

    public ArrayList<MarkerOptions> getUserLocations(String id);
//

}
