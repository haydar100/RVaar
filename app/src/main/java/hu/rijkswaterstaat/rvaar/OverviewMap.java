package hu.rijkswaterstaat.rvaar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OverviewMap extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final int DRAW_DISTANCE_MARKERS = 20000;
    public static final int NEAREST_MARKER_METER = 10000;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public ArrayList<MarkerOptions> markers;
    public MarkerOptions nearestMarkerLoc;
    public String provider;
    public LatLng currentLocation;
    public LatLng zoomLatLng;
    public Location getLastLocation;
    public LocationManager mLocManager;
    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public boolean AnimatedCameraOnce = true;
    protected Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;
    protected LocationRequest mLocationRequest;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    List<LatLng> positionToDrawLineOn = new ArrayList<LatLng>();
    FusedLocationProvider fusedService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_map);
        markers = new ArrayList<MarkerOptions>();
        buildGoogleApiClient();
        FusedLocationProvider test1 = new FusedLocationProvider(this);


        setUpMapIfNeeded();
    }




    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    /**
     * Sets up   the map if it\ is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been co
     * mpletely destroyed during this process (it is likely that it would only be
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
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            LatLng test = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            setZoomLatLng(test);


            mMap.clear();
            addMarkersToMap();


            findNearestMarker();
            if (AnimatedCameraOnce) { // tijdelijk
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(zoomLatLng)      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                AnimatedCameraOnce = false;
            }


        }
            if (mMap != null) {

                setUpMap();
            }


        }


        //  addMarkersToMap();


    public void findNearestMarker() {
        double minDist = 1E38;
        int minIndex = -1;
        for (int i = 0; i < markers.size(); i++) {
            Location currentIndexMarkerLoc = new Location("Marker");
            currentIndexMarkerLoc.setLatitude(markers.get(i).getPosition().latitude);
            currentIndexMarkerLoc.setLongitude(markers.get(i).getPosition().longitude);
            currentIndexMarkerLoc.setTime(new Date().getTime());
            float test = getLastLocation.distanceTo(currentIndexMarkerLoc);
            if (test < minDist) {
                minDist = test;
                minIndex = i;
            }
        }
        if (minIndex >= 0) {
            nearestMarkerLoc = markers.get(minIndex);
            nearestMarkerLoc.icon(null); // testen
            nearestMarkerLoc.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            // mMap.addMarker(nearestMarkerLoc);
            Log.d("nearestLocation name", "nearestLocation name" + nearestMarkerLoc.getTitle());
            notifyUser(nearestMarkerLoc);
        } else {
            nearestMarkerLoc.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
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


        // Causes Choreographer to skip frames, this can be fixed by seperating the addMarkerToMap methods to another class were ASyncTask is extended.

    }


    public void addMarkersToMap() {
        for (MarkerOptions m : markers) {
            Location loc = new Location("");
            loc.setLongitude(m.getPosition().longitude);
            loc.setLatitude(m.getPosition().latitude);
            if (getLastLocation.distanceTo(loc) < DRAW_DISTANCE_MARKERS) {
                if (m == nearestMarkerLoc) {
                    mMap.addMarker(m);
                } else {
                    m.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_iconkruispunt));
                    mMap.addMarker(m);
                }

            }

        }
    }

    public void notifyUser(MarkerOptions marker) {
        Location notifcationLoc = new Location("Marker");
        notifcationLoc.setLatitude(marker.getPosition().latitude);
        notifcationLoc.setLongitude(marker.getPosition().longitude);
        float distanceInMeters = getLastLocation.distanceTo(notifcationLoc);
        int notifyID = 1;
        if (distanceInMeters < NEAREST_MARKER_METER) { // in seconden te doen, in alle gevallen is het dan gelijk
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setContentTitle("rVaar")
                            .setContentText("Over " + distanceInMeters + " nadert u de kruispunt " + marker.getTitle())
                            .setSmallIcon(R.drawable.ic_rvaar);
            Intent resultIntent = new Intent(this, OverviewMap.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(OverviewMap.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notifyID, mBuilder.build()); // test on screen update/
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND); // http://developer.android.com/reference/android/app/Notification.html#DEFAULT_ALL


        }


    }

    public LatLng getZoomLatLng() {
        return zoomLatLng;
    }

    public void setZoomLatLng(LatLng zoomLatLng) {
        this.zoomLatLng = zoomLatLng;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("lastLocation", "not null");
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("rVaar", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("Rvaar", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Called when the location has changed.
     * <p/>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {

        location = mCurrentLocation;
        Log.d("Latitude", "Current Latitude " + location.getLatitude());
        Log.d("Longitude", "Current Longitude " + location.getLongitude());
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

}

