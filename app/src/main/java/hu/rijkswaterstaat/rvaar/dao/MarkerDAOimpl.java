package hu.rijkswaterstaat.rvaar.dao;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import hu.rijkswaterstaat.rvaar.database.Connector;

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
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("loading markers failed", "getMarkers failed");
        }
        return allMarkers;
    }

    @Override
    public void saveLocationOfUser(String id, double x, double y) {
        Log.i("id", id.toString());
        try {
            Connection con = Connector.createConnection(Connector.driver, Connector.dbName, Connector.username, Connector.password, Connector.url);
            PreparedStatement ps = con.prepareStatement("INSERT INTO TRACKINGDATA VALUES (?, ?, ?)");
            ps.setString(1, id);
            ps.setDouble(2, x);
            ps.setDouble(3, y);
            ps.execute();
            con.close();
        } catch (SQLException e) {
            Connection con = Connector.createConnection(Connector.driver, Connector.dbName, Connector.username, Connector.password, Connector.url);
            try {
                PreparedStatement ps = con.prepareStatement("UPDATE TRACKINGDATA SET X = ?, Y = ? WHERE ID = ?");
                ps.setDouble(1, x);
                ps.setDouble(2, y);
                ps.setString(3, id);
                ps.execute();
                ps.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            Log.d("savinglocationUser", "saveLocationofuser");
        }

    }

    @Override
    public ArrayList<MarkerOptions> getUserLocations(String id) {
        ArrayList<MarkerOptions> allMarkers = new ArrayList<MarkerOptions>();
        try {
            Connection con = Connector.createConnection(Connector.driver, Connector.dbName, Connector.username, Connector.password, Connector.url);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM TRACKINGDATA WHERE ID != ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MarkerOptions option = new MarkerOptions().position(new LatLng(rs.getDouble("y"), rs.getDouble("x")));
                allMarkers.add(option);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("getUserLocations", "getUserLocations");
        }
        return allMarkers;
    }
}
