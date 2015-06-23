package hu.rijkswaterstaat.rvaar.wizard;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.rijkswaterstaat.rvaar.R;

/**
 * Created by BunyamiN on 23-6-2015.
 */
public class WizardFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final WizardFragment newInstance(String message)
    {
        WizardFragment f = new WizardFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }


    public WizardFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.wizard_fragment1, container, false);

        return rootView;
    }
}
