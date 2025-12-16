package ck1.nguyengiakhiem.englishez_65131478;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    TextView tvQuestionNumber, tvQuestion;
    RadioGroup radioGroup;
    RadioButton rbA, rbB, rbC, rbD;
    Button btnNext;

    ArrayList<Question> questionList;
    int currentIndex = 0;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvQuestion = findViewById(R.id.tv_question);
        radioGroup = findViewById(R.id.radioGroupAnswers);
        rbA = findViewById(R.id.rb_a);
        rbB = findViewById(R.id.rb_b);
        rbC = findViewById(R.id.rb_c);
        rbD = findViewById(R.id.rb_d);
        btnNext = findViewById(R.id.btn_next);

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();

        showQuestion();

        btnNext.setOnClickListener(v -> checkAnswer());
    }

    private void showQuestion() {
        radioGroup.clearCheck();
        Question q = questionList.get(currentIndex);

        tvQuestionNumber.setText("Question " + (currentIndex + 1) + " / " + questionList.size());
        tvQuestion.setText(q.getQuestion());
        rbA.setText("A. " + q.getOptionA());
        rbB.setText("B. " + q.getOptionB());
        rbC.setText("C. " + q.getOptionC());
        rbD.setText("D. " + q.getOptionD());
    }

    private void checkAnswer() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) return;

        int answer = 0;
        if (selectedId == rbA.getId()) answer = 1;
        if (selectedId == rbB.getId()) answer = 2;
        if (selectedId == rbC.getId()) answer = 3;
        if (selectedId == rbD.getId()) answer = 4;

        if (answer == questionList.get(currentIndex).getCorrectAnswer()) {
            score++;
        }

        currentIndex++;
        if (currentIndex < questionList.size()) {
            showQuestion();
        } else {
            Toast.makeText(this, "Score: " + score, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
