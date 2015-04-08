package hu.rijkswaterstaat.rvaar;

import
        android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.RatingBar;
import android.widget.TextView;


public class Result extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

//get text view
        TextView t=(TextView)findViewById(R.id.textResult);
//get score
        Bundle b = getIntent().getExtras();
        int score= b.getInt("score");

        switch (score)
        {
            case 0:t.setText("Beginner");
            case 1:t.setText("Beginner");
            case 2: t.setText("Beginner");
                break;
            case 3:t.setText("Gemiddeld");
            case 4:t.setText("Gemiddeld");
                break;
            case 5:t.setText("Expert");
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }
}