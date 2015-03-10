package hu.rijkswaterstaat.rvaar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

public class OverviewMap extends FragmentActivity {

    public ArrayList<MarkerOptions> markers;
    public MarkerOptions test2;
    public String provider;
    public LatLng currentLocation;
    private LocationManager mLocManager;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_map);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            mMap.setMyLocationEnabled(true);
            GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    findNearestMarker();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10.0f));
                    Log.d("Latitude", "Current Latitude " + location.getLatitude());
                    Log.d("Longitude", "Current Longitude " + location.getLongitude());
                }
            };
            mMap.setOnMyLocationChangeListener(myLocationChangeListener);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                setUpMap();
            }
        }
    }


    public void findNearestMarker() {
        double minDist = 1E38;
        int minIndex = -1;
        for (int i = 0; i < markers.size(); i++) {
            Location kaas = new Location("Marker");
            Location currentLoc = mLocManager.getLastKnownLocation(provider);

            kaas.setLatitude(markers.get(i).getPosition().latitude);
            kaas.setLongitude(markers.get(i).getPosition().longitude);
            kaas.setTime(new Date().getTime());
            float test = currentLoc.distanceTo(kaas);
            if (test < minDist) {
                minDist = test;
                minIndex = i;
            }
        }
        if (minIndex >= 0) {
            test2 = markers.get(minIndex);
            test2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(test2);
            Log.d("Test2", "Test2 " + test2.getTitle());
            notificationTest(test2);
        } else {
            test2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
    }

    private void setUpMap() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                MarkerDAOimpl positionDao = new MarkerDAOimpl();
                markers = positionDao.getMarkers();
            }
        });
        t1.start();
        try {
            t1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addMarkersToMap();


        // Causes Choreographer to skip frames, this can be fixed by seperating the addMarkerToMap methods to another class were ASyncTask is extended.

    }


    public void addMarkersToMap() {
        for (MarkerOptions m : markers) {
            mMap.addMarker(m);
        }
    }

    public void notificationTest(MarkerOptions test) {
        Location kaas = new Location("Marker");
        kaas.setLatitude(test.getPosition().latitude);
        kaas.setLongitude(test.getPosition().longitude);
        Location currentLoc = mLocManager.getLastKnownLocation(provider);
        float distanceInMeters = currentLoc.distanceTo(kaas);
        if (distanceInMeters < 500) { // in seconden te doen, in alle gevallen is het dan gelijk
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setContentTitle("Notifcation test ")
                            .setContentText(test.getTitle() + distanceInMeters)
                            .setSmallIcon(R.drawable.ic_rvaar);
            Intent resultIntent = new Intent(this, OverviewMap.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(OverviewMap.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }


    }


}
