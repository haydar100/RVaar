package hu.rijkswaterstaat.rvaar.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import hu.rijkswaterstaat.rvaar.R;
import hu.rijkswaterstaat.rvaar.domain.UserLocation;

/**
 * Created by Haydar on 21-05-15.
 */
public class MapHelper {
    protected static int DRAW_DISTANCE_MARKERS = 20000;
    protected static int DRAW_DISTANCE_POPUP = 1000;
    protected static int NEAREST_MARKER_METER = 10000;
    protected static boolean POPUP_SHOW = true;
    protected static long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    public static CameraPosition lockCameraPosition(float zoomLevel, LatLng loc) {


            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(loc)      // Sets the center of the map to Mountain View
                    .zoom(zoomLevel)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            return cameraPosition;

    }


    public static ArrayList<MarkerOptions> convertUserLocToMarkerOptions(Context con, ArrayList<UserLocation> userLocations) {
        ArrayList<MarkerOptions> userLocationMarker = new ArrayList<MarkerOptions>();
        for (UserLocation ul : userLocations) {
            MarkerOptions convertedMarkerOption = new MarkerOptions();
            convertedMarkerOption.position(new LatLng(ul.getX(), ul.getY()));
            convertedMarkerOption.title(ul.getBoatname());

            switch (ul.getBoatType().toLowerCase()) {
                case "kano":
                    convertedMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(rotateDrawable(con, ul.getDirection(), R.drawable.ic_markericonkanoandere).getBitmap()));
                    break;
                case "roeiboot":
                    convertedMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(rotateDrawable(con, ul.getDirection(), R.drawable.ic_markericonroeiboatandere).getBitmap()));
                    break;
                case "speedboot":
                    convertedMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(rotateDrawable(con, ul.getDirection(), R.drawable.ic_markericonspeedboatandere).getBitmap()));
                    break;
                case "zeilboot":
                    convertedMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(rotateDrawable(con, ul.getDirection(), R.drawable.ic_markericonzeilboatandere).getBitmap()));
                    break;
                case "sloep":
                    convertedMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(rotateDrawable(con, ul.getDirection(), R.drawable.ic_markericonanderesloep).getBitmap()));
                    break;
                default:
                    convertedMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(rotateDrawable(con, ul.getDirection(), R.drawable.ic_markericonandere).getBitmap()));
                    break;
            }
            userLocationMarker.add(convertedMarkerOption);
        }
        return userLocationMarker;
    }

    public static void updateFromPreferences(Context con) {

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(con);
        String valueOfDRAW_DISTANCE_MARKERS = SP.getString("DRAW_DISTANCE_MARKERS", "20000");
        String valueOfDRAW_DISTANCE_POPUP = SP.getString("DRAW_DISTANCE_POPUP", "1000");
        String valueOfNEAREST_MARKER_METER = SP.getString("NEAREST_MARKER_METER", "10000");
        String valueOfUPDATE_INTERVAL_IN_MILLISECONDS = SP.getString("UPDATE_INTERVAL_IN_MILLISECONDS", "10000");
        POPUP_SHOW = SP.getBoolean("POPUP_SHOW", true);
        DRAW_DISTANCE_MARKERS = Integer.valueOf(valueOfDRAW_DISTANCE_MARKERS);
        DRAW_DISTANCE_POPUP = Integer.valueOf(valueOfDRAW_DISTANCE_POPUP);
        NEAREST_MARKER_METER = Integer.valueOf(valueOfNEAREST_MARKER_METER);
        UPDATE_INTERVAL_IN_MILLISECONDS = Long.valueOf(valueOfUPDATE_INTERVAL_IN_MILLISECONDS);
    }

    public static BitmapDrawable rotateDrawable(Context con, float angle, int iconToRotate) {
        Bitmap arrowBitmap = BitmapFactory.decodeResource(con.getResources(),
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
    }
}
