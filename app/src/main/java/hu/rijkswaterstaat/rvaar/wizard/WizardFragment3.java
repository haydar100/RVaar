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
public class WizardFragment3 extends Fragment {
    public static final WizardFragment3 newInstance()
    {
        WizardFragment3 f = new WizardFragment3();
        return f;
    }

    public WizardFragment3(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.wizard_fragment3, container, false);
        return rootView;
    }
}
