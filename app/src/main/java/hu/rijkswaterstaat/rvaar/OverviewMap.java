package hu.rijkswaterstaat.rvaar;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import hu.rijkswaterstaat.rvaar.domain.UserLocation;
import hu.rijkswaterstaat.rvaar.menu.MenuActivity;
import hu.rijkswaterstaat.rvaar.utils.MapHelper;
import hu.rijkswaterstaat.rvaar.webservice.WSConnector;

import static hu.rijkswaterstaat.rvaar.R.drawable.ic_iconkruispunt;
//import static hu.rijkswaterstaat.rvaar.R.drawable.ic_rvaar;


public class OverviewMap extends MenuActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private static final String TAG = "location-updates-sample";
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static String uniqueID = null;
    private final int DRAW_DISTANCE_MARKERS = 20000;
    private final int DRAW_DISTANCE_POPUP = 1000;
    private final int NEAREST_MARKER_METER = 10000;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private boolean gps_disabled;
    private boolean popupIsOpen = false;
    private boolean POPUP_SHOW = true;
    private boolean inactive = false;
    private GoogleMap mMap;
    private boolean AnimatedCameraOnce = true;
    private MarkerOptions nearestMarkerLoc;
    // Keys for storing activity state in the Bundle.
    private ArrayList<MarkerOptions> markers;
    /**
     * Provides the entry point to Google Play services.
     */
    private GoogleApiClient mGoogleApiClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;
    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;
    private ArrayList<MarkerOptions> userLocationMarker;
    private ProgressDialog dialog;
    private MarkerOptions last;
    private ArrayList<LatLng> points;

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.apply();
            }
        }
        return uniqueID;
    }

    private static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_map);
        setMenu();
        MapHelper.updateFromPreferences(this);
        markers = new ArrayList<>();
        points = new ArrayList<>();
        mLastUpdateTime = "";
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        uniqueID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gps_disabled = false;
            Toast.makeText(this, R.string.gps_enabled_message, Toast.LENGTH_SHORT).show();
        } else {
            gps_disabled = true;
            showGPSDisabledAlertToUser();

        }
        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
        setUpMapIfNeeded();

        ImageView myView = (ImageView)findViewById(R.id.imageView1);
        final Toast viewToast = Toast.makeText(this, R.string.cemt_plaatje_toelichting, Toast.LENGTH_SHORT);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewToast.show();
            }
        });
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);

            addMarkersToMap();


        }
        if (mMap != null) {
            setUpMap();
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null;
    }


    public void onClick(final View v) {

        if (mCurrentLocation != null) {


            LatLng target = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            //CameraPosition position = this.mMap.getCameraPosition();
            CameraPosition.Builder builder = new CameraPosition.Builder();
            builder.zoom(15);
            builder.target(target);

            this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
        }

    }

    private void setUpMap() {
        if (isNetworkAvailable()) {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    WSConnector ws = new WSConnector();
                    markers = ws.getMarkers();
                    ws.getMarkers().size();
                    markers.size();


                }
            });
            t1.start();
            try {
                t1.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            showNetworkdisabledAlertToUser();
        }


    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        Log.d("startLocationUpdate", "startLocationUpdate");
        displayProgressDialogGettingLoc();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        inactive = false;
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        MapHelper.updateFromPreferences(this);
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        inactive = true;
        cancelNotification(this, 1);

        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();

        }
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                WSConnector ws = new WSConnector();
                ws.removeUserLocation(uniqueID);

            }
        });
        try {
            t1.start();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        inactive = true;
        super.onStop();
        cancelNotification(this, 1);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                WSConnector ws = new WSConnector();
                ws.removeUserLocation(uniqueID);
            }
        });
        try {
            t1.start();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mGoogleApiClient.disconnect();

    }


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.

        displayProgressDialogGettingLoc();


        startLocationUpdates();
    }

    public void displayProgressDialogGettingLoc() {
        if (mCurrentLocation == null && !gps_disabled && isNetworkAvailable()) {
            dialog = ProgressDialog.show(this, "",
                    OverviewMap.this.getResources().getString(R.string.loading_message, true, true));
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());


        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        float zoom = mMap.getCameraPosition().zoom;
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String boatName = preferences.getString("BOAT_NAME", null);
        final String boatType = preferences.getString("boatType", null);


        mMap.clear();
        addUserLocToMap();
        addMarkersToMap();
        MarkerOptions k = new MarkerOptions();
        k.position(loc);
        k.title(getResources().getString(R.string.current_location));
        float myBearing = location.getBearing();
        Bitmap rotateBoatIcon = ((BitmapDrawable) MapHelper.rotateDrawable(this, myBearing, R.drawable.ic_markericon)).getBitmap();

        mMap.addMarker(k.icon(BitmapDescriptorFactory.fromBitmap(rotateBoatIcon)));
        findNearestMarker();

        if (AnimatedCameraOnce) {

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(MapHelper.lockCameraPosition(17, loc)));
            AnimatedCameraOnce = false;
        }
        else {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(MapHelper.lockCameraPosition(zoom, loc)));
        }
        drawPolyLineOnLocation(location);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                if (isNetworkAvailable()) {
                    WSConnector connector = new WSConnector();

                    LatLng lastPosition = points.get(points.size() - 1);
                    LatLng secondToLastPosition = points.get(points.size() - 2);


                    Location locLast = new Location("");
                    locLast.setLatitude(lastPosition.latitude);
                    locLast.setLongitude(lastPosition.longitude);

                    Location locSecondToLast = new Location("");
                    locSecondToLast.setLatitude(secondToLastPosition.latitude);
                    locSecondToLast.setLongitude(secondToLastPosition.longitude);
                    if (!inactive) {
                        connector.saveLocationOfUser(uniqueID, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), boatName, mCurrentLocation.getBearing(), boatType, "Beginner");
                    }


                    convertUserLocToMarkerOptions(connector.getUserLocations(uniqueID));

                }

            }
        });
        t1.start();
        try {
            t1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (dialog != null) {
            dialog.dismiss();
        }

    }

    private void drawPolyLineOnLocation(Location location) {

        if (location != null) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLngP = new LatLng(latitude, longitude);

            PolylineOptions polyLineOpt = new PolylineOptions()
                    .width(5)
                    .color(Color.RED)
                    .geodesic(true);

            points.add(latLngP);

            for (int z = 0; z < points.size(); z++) {
                LatLng point = points.get(z);
                polyLineOpt.add(point);
            }

            //Polyline line = mMap.addPolyline(polyLineOpt);

            points.add(latLngP);
        }
    }

    void convertUserLocToMarkerOptions(ArrayList<UserLocation> userLocations) {
        userLocationMarker = MapHelper.convertUserLocToMarkerOptions(this, userLocations);

    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /**
     * Stores activity data in the Bundle.
     */



    /*public BitmapDrawable rotateDrawable(float angle, int iconToRotate) {
        Bitmap arrowBitmap = BitmapFactory.decodeResource(this.getResources(),
                iconToRotate);
        // Create blank bitmap of equal size
        Bitmap canvasBitmap = arrowBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvasBitmap.eraseColor(0x00000000);

        // Create canvas
        Canvas canvas = new Canvas(canvasBitmap);

        // Create rotation matrix
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.setRotate(angle, canvas.getWidth() / 2, canvas.getHeight() / 2);

        // Draw bitmap onto canvas using matrix
        canvas.drawBitmap(arrowBitmap, rotateMatrix, null);

        return new BitmapDrawable(canvasBitmap);
    }*/
    public void findNearestMarker() {
        double minDist = 1E38;
        int minIndex = -1;
        for (int i = 0; i < markers.size(); i++) {
            Location currentIndexMarkerLoc = new Location("Marker");
            currentIndexMarkerLoc.setLatitude(markers.get(i).getPosition().latitude);
            currentIndexMarkerLoc.setLongitude(markers.get(i).getPosition().longitude);
            currentIndexMarkerLoc.setTime(new Date().getTime());
            float calculatedDistance = mCurrentLocation.distanceTo(currentIndexMarkerLoc);
            if (calculatedDistance < minDist) {
                minDist = calculatedDistance;
                minIndex = i;
            }
        }
        if (minIndex >= 0) {
            nearestMarkerLoc = markers.get(minIndex);
            nearestMarkerLoc.icon(null);
            nearestMarkerLoc.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            new Handler().postDelayed(cemtRunnable,15000);

            currentSpeedInKM();
            currentMarkerDistance(nearestMarkerLoc);
            if (POPUP_SHOW && last != nearestMarkerLoc && calcDistanceToMarker(nearestMarkerLoc) < DRAW_DISTANCE_POPUP) {

                new AsyncPopup(OverviewMap.this).execute(Pair.create(mCurrentLocation, nearestMarkerLoc));
                notifyUserNotificationBar(nearestMarkerLoc);
                last = nearestMarkerLoc;


            }
        } else {
            if (nearestMarkerLoc != null) {
                nearestMarkerLoc.visible(false);
                nearestMarkerLoc.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }

        }
    }
    MarkerOptions cemtMarker;
    Runnable cemtRunnable = new Runnable() {
        @Override
        public void run() {
            Location loc = new Location("");
            loc.setLatitude(nearestMarkerLoc.getPosition().latitude);
            loc.setLongitude(nearestMarkerLoc.getPosition().longitude);
            float distanceInMeters = mCurrentLocation.distanceTo(loc);
            if (distanceInMeters < 10000 && cemtMarker != nearestMarkerLoc) {
                ImageView iv = (ImageView)findViewById(R.id.imageView1);
                switch (nearestMarkerLoc.getSnippet()) {
                    case "I": {
                        iv.setImageResource(R.drawable.klasse1);
                        break;
                    }
                    case "II": {
                        iv.setImageResource(R.drawable.klasse2);
                        break;
                    }
                    case "III": {
                        iv.setImageResource(R.drawable.klasse3);
                        break;
                    }
                    case "IV": {
                        iv.setImageResource(R.drawable.klasse4);
                        break;
                    }
                    case "Va": {
                        iv.setImageResource(R.drawable.klasse5);
                        break;
                    }
                    case "Vb": {
                        iv.setImageResource(R.drawable.klasse6);
                        break;
                    }
                    case "VIa": {
                        iv.setImageResource(R.drawable.klasse7);
                        break;
                    }
                    case "VIb": {
                        iv.setImageResource(R.drawable.klasse8);
                        break;
                    }
                    case "VIc": {
                        iv.setImageResource(R.drawable.klasse9);
                        break;
                    }
                    case "VIIb": {
                        iv.setImageResource(R.drawable.klasse10);
                        break;
                    }
                }
            }
            cemtMarker = nearestMarkerLoc;
        }
    };


    void notifyUserNotificationBar(MarkerOptions marker) {
        Location notificationLoc = new Location("Marker");
        notificationLoc.setLatitude(marker.getPosition().latitude);
        notificationLoc.setLongitude(marker.getPosition().longitude);
        float distanceInMeters = mCurrentLocation.distanceTo(notificationLoc);
        int notifyID = 1;
        int x = Math.round(distanceInMeters);
        if (distanceInMeters < NEAREST_MARKER_METER) {

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setContentTitle(OverviewMap.this.getResources().getString(R.string.app_name))
                            .setContentText("U nadert het kruispunt " + marker.getTitle() + " (" + x + " Meter)")
                            .setSmallIcon(R.mipmap.ic_rvaar)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rvaar));

            Intent resultIntent = new Intent(this, OverviewMap.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(OverviewMap.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notifyID, mBuilder.build());

            mBuilder.setDefaults(-1);


        }
    }


    public void openSOS(View v) {
        Intent sos = new Intent(this, SOS.class);
        startActivity(sos);
    }

    public String currentSpeedInKM() {
        TextView textViewToChange = (TextView) findViewById(R.id.speed);
        Double currentSpeedCalc;
        int currentSpeedRound;
        int roundedSpeedKM;
        if (mCurrentLocation.getSpeed() > 0.0) {
            roundedSpeedKM = Math.round(mCurrentLocation.getSpeed());
            currentSpeedCalc = roundedSpeedKM * 3.60;
            currentSpeedRound = currentSpeedCalc.intValue();


        } else {
            textViewToChange.setText("Uw snelheid " + "0" + " Km/u");
            return getResources().getString(R.string.speed_unavailable);

        }
        textViewToChange.setText("Uw snelheid " + currentSpeedRound + " Km/u");
        return " Uw huidige snelheid : " + currentSpeedRound;

    }

    String currentMarkerDistance(MarkerOptions opt) {
        TextView textViewToChange = (TextView) findViewById(R.id.approaching);
        Location notifcationLoc = new Location("Marker");
        notifcationLoc.setLatitude(opt.getPosition().latitude);
        notifcationLoc.setLongitude(opt.getPosition().longitude);
        float distanceInMeters = mCurrentLocation.distanceTo(notifcationLoc);
        int x = Math.round(distanceInMeters);
        String s = x + "m";
        textViewToChange.setText(s);
        return s;

    }

    int calcDistanceToMarker(MarkerOptions opt){
        Location markerloc = new Location("Marker");
        markerloc.setLatitude(opt.getPosition().latitude);
        markerloc.setLongitude(opt.getPosition().longitude);
        float distanceBetweenUserLocation = mCurrentLocation.distanceTo(markerloc);
        int x = Math.round(distanceBetweenUserLocation);
        return x;
    }


    private void showNetworkdisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.network_connection_unavailable)
                .setCancelable(false);

        alertDialogBuilder.setNegativeButton(R.string.Cancel_button_message,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent Intent = new Intent(
                                OverviewMap.this, Home.class);
                        startActivity(Intent);
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.gps_disabled_message)
                .setCancelable(false)
                .setPositiveButton(R.string.Settings_message,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void addUserLocToMap() {
        if (userLocationMarker != null) {
            for (MarkerOptions m : userLocationMarker) {
                Location loc = new Location("");
                loc.setLongitude(m.getPosition().longitude);
                loc.setLatitude(m.getPosition().latitude);

                if (m == nearestMarkerLoc) {
                    mMap.addMarker(m);

                } else {
                    mMap.addMarker(m);
                }
            }
        }

    }

    void addMarkersToMap() {
        if (isNetworkAvailable()) {
            for (MarkerOptions m : markers) {
                Location loc = new Location("");
                loc.setLongitude(m.getPosition().longitude);
                loc.setLatitude(m.getPosition().latitude);
                if (mCurrentLocation.distanceTo(loc) < DRAW_DISTANCE_MARKERS) {
                    if (m == nearestMarkerLoc) {
                        mMap.addMarker(m);

                    } else {
                        m.icon(BitmapDescriptorFactory.fromResource(ic_iconkruispunt));
                        mMap.addMarker(m);
                    }

                }

            }
        }

    }
}