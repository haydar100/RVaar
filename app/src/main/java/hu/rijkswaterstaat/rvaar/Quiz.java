package hu.rijkswaterstaat.rvaar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hu.rijkswaterstaat.rvaar.domain.Question;

public class Quiz extends ActionBarActivity {
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
        Question q1 = new Question("Een vaarwater is zó smal, dat u met uw schip niet in één keer kunt draaien (er staat geen stroom of wind). Uw schip heeft een rechtse schroef. U draait het gemakkelijkst over:" +
                " ", "Stuurboord", "Bakboord", "beide boegen", "Stuurboord");

        Question q2 = new Question("Hoeveel reddingsvesten moeten aan boord van een snelle motorboot zijn?" +
                " ", "één voor elke opvarende binnen handbereik", "minimaal één aantal opvarende maakt niet uit", "geen", "één voor elke opvarende binnen handbereik");

        Question q3 = new Question("'s Nachts ziet u een rood en een groen boordlicht. Dit is het vooraanzicht van een:" +
                " ", "klein zeilschip", "vissend vissersschip", "vrachtschip", "klein zeilschip");

        Question q4 = new Question("U vaart 's nachts een haven aan. U ziet aan uw stuurboordzijde :" +
                " ", "groen vast licht of groen flikkerlicht", "wit vast licht of wit flikkerlicht", "rood vast licht of rood flikkerlicht", "groen vast licht of groen flikkerlicht");

        Question q5 = new Question("U meet windkracht 5 op de schaal van Beaufort. Dit is een :" +
                " ", "vrij krachtige wind", "zwakke wind", "stormachtige wind", "vrij krachtige wind");
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



