package hu.rijkswaterstaat.rvaar;

import android.content.Context;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by PhenomII on 4-6-2015.
 */
public class AsyncPopup extends AsyncTask<Pair<Location, MarkerOptions>, Void, Void> {
    private MarkerOptions marker;
    private Location mCurrentLocation;
    private Context context;

    public AsyncPopup(Context c) {
        context = c;
    }

    @Override
    protected Void doInBackground(Pair<Location, MarkerOptions>... params) {
        marker = params[0].second;
        mCurrentLocation = params[0].first;
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ring = RingtoneManager.getRingtone(context, notification);
        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        Location loc = new Location("");
        loc.setLatitude(marker.getPosition().latitude);
        loc.setLongitude(marker.getPosition().longitude);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        ring.play();
        //vib.vibrate(750);

        ImageButton btnDismiss = (ImageButton) popupView.findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }
        });

    }
}
