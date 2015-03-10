package hu.rijkswaterstaat.rvaar;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Berkan on 10-3-2015.
 */
public class MarkerDAOimpl implements MarkerDAO {
    @Override
    public ArrayList<MarkerOptions> getMarkers() {
        ArrayList<MarkerOptions> allMarkers = new ArrayList<MarkerOptions>();
        try {
            Connection con = Connector.createConnection(Connector.driver, Connector.dbName, Connector.username, Connector.password, Connector.url);
            PreparedStatement ps = con.prepareStatement("select * from graph");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MarkerOptions option = new MarkerOptions().position(new LatLng(rs.getDouble("y1"), rs.getDouble("x1"))).title(rs.getString("lnaam"));

                allMarkers.add(option);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("loading markers failed", "getMarkers failed");
        }
        return allMarkers;
    }
}
