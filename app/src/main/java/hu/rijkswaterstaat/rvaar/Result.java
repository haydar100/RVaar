package hu.rijkswaterstaat.rvaar;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class Result extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView t=(TextView)findViewById(R.id.textResult);
        int score = Integer.parseInt(getIntent().getStringExtra("score"));
        Log.d("score", Integer.toString(score));
        ImageView iv = (ImageView) findViewById(R.id.ImageResult);
        switch (score)
        {
            case 0:t.setText("U bent een beginner !");
                iv.setImageResource(R.drawable.icon_beginner);
            case 1:t.setText("U bent een beginner !");
                iv.setImageResource(R.drawable.icon_beginner);
            case 2: t.setText("U bent een beginner !");
                iv.setImageResource(R.drawable.icon_beginner);
                break;
            case 3:t.setText("U bent Gemiddeld !");
                iv.setImageResource(R.drawable.icon_gemiddeld);
            case 4:t.setText("U bent Gemiddeld !");
                iv.setImageResource(R.drawable.icon_gemiddeld);
                break;
            case 5:t.setText("U bent een Expert !");
                iv.setImageResource(R.drawable.icon_expert);
                break;
        }
    }
    public void terughome(View v){
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }
}