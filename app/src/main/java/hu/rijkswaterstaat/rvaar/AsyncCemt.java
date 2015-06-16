package hu.rijkswaterstaat.rvaar;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.ImageView;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by PhenomII on 4-6-2015.
 */
public class AsyncCemt extends AsyncTask<Pair<Location,MarkerOptions>,Void,Void> {
    private Location mCurrentLocation;
    private MarkerOptions marker;
    private Context context;
    private ImageView iv;
    public AsyncCemt(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(Pair<Location,MarkerOptions>... params) {
        marker = params[0].second;
        mCurrentLocation = params[0].first;


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Location loc = new Location("");
        loc.setLatitude(marker.getPosition().latitude);
        loc.setLongitude(marker.getPosition().longitude);
        float distanceInMeters = mCurrentLocation.distanceTo(loc);
        if (distanceInMeters < 10000) {
            iv = (ImageView) ((Activity) context).findViewById(R.id.imageView1);
            switch (marker.getSnippet()) {
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
    }
}
