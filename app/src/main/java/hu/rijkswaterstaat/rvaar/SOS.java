package hu.rijkswaterstaat.rvaar;

/**
 * Created by ingo on 16-4-2015.
 */
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import hu.rijkswaterstaat.rvaar.menu.MenuActivity;

public class SOS extends MenuActivity implements OnClickListener
{
    private LinearLayout panel1;
    private LinearLayout panel2;
    private LinearLayout panel3;
    private LinearLayout panel4;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private View openLayout;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setMenu();

        panel1 = (LinearLayout) findViewById(R.id.panel1);
        panel2 = (LinearLayout) findViewById(R.id.panel2);
        panel3 = (LinearLayout) findViewById(R.id.panel3);
        panel4 = (LinearLayout) findViewById(R.id.panel4);


        //panel1.setVisibility(View.VISIBLE);

        //panel1.setVisibility(View.VISIBLE);

        //Log.v("CZ","height at first ..." + panel1.getMeasuredHeight());

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);


        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);


    }
    @Override
    public void onClick(View v)
    {
        hideOthers(v);
    }
    private void hideThemAll()
    {
        if(openLayout == null) return;
        if(openLayout == panel1)
            panel1.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panel1, true));
        if(openLayout == panel2)
            panel2.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panel2, true));
        if(openLayout == panel3)
        panel3.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panel3, true));
        if(openLayout == panel4)
            panel4.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panel4, true));
}

    private void hideOthers(View layoutView)
    {
        {
            int v;
            if(layoutView.getId() == R.id.text1)
            {
                v = panel1.getVisibility();
                if(v != View.VISIBLE)
                {
                    panel1.setVisibility(View.VISIBLE);
                    Log.v("CZ","height..." + panel1.getHeight());
                }

                //panel1.setVisibility(View.GONE);
                //Log.v("CZ","again height..." + panel1.getHeight());
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    panel1.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, panel1, true));
                }
            }
            else if(layoutView.getId() == R.id.text2)
            {
                v = panel2.getVisibility();
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    panel2.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, panel2, true));
                }
            }
            else if(layoutView.getId() == R.id.text3)
            {
                v = panel3.getVisibility();
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    panel3.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, panel3, true));
                }
            }
            else if(layoutView.getId() == R.id.text4)
            {
                v = panel4.getVisibility();
                hideThemAll();
                if(v != View.VISIBLE)
                {
                    panel4.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, panel4, true));
                }
            }


        }
    }

    public class ScaleAnimToHide extends ScaleAnimation
    {

        private final View mView;

        private final LayoutParams mLayoutParams;

        private final int mMarginBottomFromY;
        private final int mMarginBottomToY;

        private boolean mVanishAfter = false;

        public ScaleAnimToHide(float fromX, float toX, float fromY, float toY, int duration, View view,boolean vanishAfter)
        {
            super(fromX, toX, fromY, toY);
            setDuration(duration);
            openLayout = null;
            mView = view;
            mVanishAfter = vanishAfter;
            mLayoutParams = (LayoutParams) view.getLayoutParams();
            int height = mView.getHeight();
            mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin - height;
            mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) - height;

            Log.v("CZ","height..." + height + " , mMarginBottomFromY...." + mMarginBottomFromY  + " , mMarginBottomToY.." +mMarginBottomToY);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f)
            {
                int newMarginBottom = mMarginBottomFromY + (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime);
                mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,mLayoutParams.rightMargin, newMarginBottom);
                mView.getParent().requestLayout();
                //Log.v("CZ","newMarginBottom..." + newMarginBottom + " , mLayoutParams.topMargin..." + mLayoutParams.topMargin);
            }
            else if (mVanishAfter)
            {
                mView.setVisibility(View.GONE);
            }
        }
    }
    public class ScaleAnimToShow extends ScaleAnimation
    {

        private final View mView;

        private final LayoutParams mLayoutParams;

        private final int mMarginBottomFromY;
        private final int mMarginBottomToY;

        private boolean mVanishAfter = false;

        public ScaleAnimToShow(float toX, float fromX, float toY, float fromY, int duration, View view,boolean vanishAfter)
        {
            super(fromX, toX, fromY, toY);
            openLayout = view;
            setDuration(duration);
            mView = view;
            mVanishAfter = vanishAfter;
            mLayoutParams = (LayoutParams) view.getLayoutParams();
            mView.setVisibility(View.VISIBLE);
            int height = mView.getHeight();
            //mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin + height;
            //mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) + height;

            mMarginBottomFromY = 0;
            mMarginBottomToY = height;

            Log.v("CZ",".................height..." + height + " , mMarginBottomFromY...." + mMarginBottomFromY  + " , mMarginBottomToY.." +mMarginBottomToY);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f)
            {
                int newMarginBottom = (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime) - mMarginBottomToY;
                mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,mLayoutParams.rightMargin, newMarginBottom);
                mView.getParent().requestLayout();
                //Log.v("CZ","newMarginBottom..." + newMarginBottom + " , mLayoutParams.topMargin..." + mLayoutParams.topMargin);
            }
        }

    }
}

