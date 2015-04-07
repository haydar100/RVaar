package hu.rijkswaterstaat.rvaar;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
    import android.os.Bundle;
    import android.app.Activity;
    import android.view.Menu;
import android.view.View;
import android.widget.Button;
    import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import hu.rijkswaterstaat.rvaar.domain.Question;

public class Quiz extends Activity {
    ArrayList<Question> quesList = new ArrayList<Question>();
    int score = 0;
    int qid = 0;
    Question currentQ;
    TextView txtQuestion;
    RadioButton rda, rdb, rdc;
    Button butNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Question q1 = new Question("Wat is links op een schip" +
                " ", "Bakboord", "Stuurboord", "Motorboord", "Bakboord");

        Question q2 = new Question("Wat is links op een schip" +
                " ", "Bakboord", "Stuurboord", "Motorboord", "Bakboord");

        Question q3 = new Question("Wat is links op een schip" +
                " ", "Bakboord", "Stuurboord", "Motorboord", "Bakboord");

        Question q4 = new Question("Wat is links op een schip" +
                " ", "Bakboord", "Stuurboord", "Motorboord", "Bakboord");

        Question q5 = new Question("Wat is links op een schip" +
                " ", "Bakboord", "Stuurboord", "Motorboord", "Bakboord");
        quesList.add(q1);
        quesList.add(q2);
        quesList.add(q3);
        quesList.add(q4);
        quesList.add(q5);
        currentQ = quesList.get(qid);
        txtQuestion = (TextView) findViewById(R.id.textView1);
        rda = (RadioButton) findViewById(R.id.radio0);
        rdb = (RadioButton) findViewById(R.id.radio1);
        rdc = (RadioButton) findViewById(R.id.radio2);
        butNext = (Button) findViewById(R.id.button1);

        setQuestionView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    private void setQuestionView() {
        txtQuestion.setText(currentQ.getQUESTION());
        rda.setText(currentQ.getOPTA());
        rdb.setText(currentQ.getOPTB());
        rdc.setText(currentQ.getOPTC());
        qid++;
    }
    public void onClick_QuizNext(View v){
        RadioGroup grp=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton answer=(RadioButton)findViewById(grp.getCheckedRadioButtonId());
        Log.d("yourans", currentQ.getANSWER() + " " + answer.getText());
        if(currentQ.getANSWER().equals(answer.getText()))
        {
            score++;
            Log.d("score", "Your score "+score);
        }
        if(qid<5){
            currentQ=quesList.get(qid);
            setQuestionView();
        }else{
            Intent intent = new Intent(Quiz.this, Result.class);
            Bundle b = new Bundle();
            Log.i("" , ""+ score);
            b.putInt("score", score); //Your score
            intent.putExtras(b); //Put your score to your next Intent
            startActivity(intent);
            finish();
    }
/**/
    }}



