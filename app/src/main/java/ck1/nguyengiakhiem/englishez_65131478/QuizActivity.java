package ck1.nguyengiakhiem.englishez_65131478;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    // UI
    TextView tvTimer, tvQuestionNumber, tvQuestion, tvDifficulty;
    TextView tvA, tvB, tvC, tvD;
    Button btnNext, btnPrev;
    CardView cardA, cardB, cardC, cardD;

    // Settings
    String difficulty;
    String totalTimeString;

    // Colors
    int colorSelected = android.graphics.Color.parseColor("#86FF7E");
    int colorDefault = android.graphics.Color.WHITE;

    // Data
    ArrayList<Question> questionList;
    int currentIndex = 0;
    HashMap<Integer, Integer> userAnswers = new HashMap<>();

    // Timer
    CountDownTimer countDownTimer;
    long totalTime;
    long remainingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // ===== LOAD SETTINGS =====
        SharedPreferences prefs = getSharedPreferences("quiz_settings", MODE_PRIVATE);
        int timeFromSettings = prefs.getInt("time", 25);
        difficulty = prefs.getString("difficulty", "easy");

        totalTime = timeFromSettings * 60 * 1000L;
        remainingTime = totalTime;
        totalTimeString = String.format(Locale.getDefault(), "%02d:00", timeFromSettings);

        // ===== UI =====
        tvTimer = findViewById(R.id.tv_timer);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvQuestion = findViewById(R.id.tv_question);
        tvDifficulty = findViewById(R.id.tv_difficulty);

        tvA = findViewById(R.id.tv_a);
        tvB = findViewById(R.id.tv_b);
        tvC = findViewById(R.id.tv_c);
        tvD = findViewById(R.id.tv_d);

        btnNext = findViewById(R.id.btn_next);
        btnPrev = findViewById(R.id.btn_prev);

        cardA = findViewById(R.id.card_rb_a);
        cardB = findViewById(R.id.card_rb_b);
        cardC = findViewById(R.id.card_rb_c);
        cardD = findViewById(R.id.card_rb_d);

        tvDifficulty.setText("Difficulty: " + difficulty.toUpperCase());

        btnNext.setEnabled(false);
        btnNext.setAlpha(0.5f);

        // ===== LOAD QUESTIONS =====
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getQuestionsByDifficulty(difficulty);

        if (questionList.isEmpty()) {
            Toast.makeText(this, "No questions found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        startTimer();
        loadQuestion();

        // ===== CLICK ANSWERS =====
        cardA.setOnClickListener(v -> handleSelect(1, cardA));
        cardB.setOnClickListener(v -> handleSelect(2, cardB));
        cardC.setOnClickListener(v -> handleSelect(3, cardC));
        cardD.setOnClickListener(v -> handleSelect(4, cardD));

        btnNext.setOnClickListener(v -> {
            if (!userAnswers.containsKey(currentIndex)) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentIndex < questionList.size() - 1) {
                currentIndex++;
                loadQuestion();
            } else {
                finishQuiz();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                loadQuestion();
            }
        });
    }

    // ===== ANSWER SELECT =====
    private void handleSelect(int option, CardView selectedCard) {
        userAnswers.put(currentIndex, option);

        resetCardColors();
        selectedCard.setCardBackgroundColor(colorSelected);

        btnNext.setEnabled(true);
        btnNext.setAlpha(1f);
    }

    private void resetCardColors() {
        cardA.setCardBackgroundColor(colorDefault);
        cardB.setCardBackgroundColor(colorDefault);
        cardC.setCardBackgroundColor(colorDefault);
        cardD.setCardBackgroundColor(colorDefault);
    }

    // ===== TIMER =====
    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                int min = (int) (millisUntilFinished / 1000) / 60;
                int sec = (int) (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format(Locale.getDefault(),
                        "Time: %02d:%02d", min, sec));
            }

            @Override
            public void onFinish() {
                finishQuiz();
            }
        }.start();
    }

    // ===== LOAD QUESTION =====
    private void loadQuestion() {
        Question q = questionList.get(currentIndex);

        tvQuestionNumber.setText(
                "Question " + (currentIndex + 1) + " / " + questionList.size()
        );

        tvQuestion.setText(q.getQuestion());
        tvA.setText("A. " + q.getOptionA());
        tvB.setText("B. " + q.getOptionB());
        tvC.setText("C. " + q.getOptionC());
        tvD.setText("D. " + q.getOptionD());

        resetCardColors();

        if (userAnswers.containsKey(currentIndex)) {
            int opt = userAnswers.get(currentIndex);
            if (opt == 1) cardA.setCardBackgroundColor(colorSelected);
            else if (opt == 2) cardB.setCardBackgroundColor(colorSelected);
            else if (opt == 3) cardC.setCardBackgroundColor(colorSelected);
            else cardD.setCardBackgroundColor(colorSelected);

            btnNext.setEnabled(true);
            btnNext.setAlpha(1f);
        } else {
            btnNext.setEnabled(false);
            btnNext.setAlpha(0.5f);
        }

        btnPrev.setEnabled(currentIndex != 0);
    }

    // ===== FINISH =====
    private void finishQuiz() {
        if (countDownTimer != null) countDownTimer.cancel();

        int score = 0;
        for (int i = 0; i < questionList.size(); i++) {
            if (userAnswers.containsKey(i)
                    && userAnswers.get(i) == questionList.get(i).getCorrectAnswer()) {
                score++;
            }
        }

        int percent = (int) ((score * 100f) / questionList.size());

        int usedSeconds = (int) ((totalTime - remainingTime) / 1000);
        String timeUsed = String.format(Locale.getDefault(),
                "%02d:%02d", usedSeconds / 60, usedSeconds % 60);

        String date = new java.text.SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
        ).format(new java.util.Date());

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        dbHelper.insertHistory(
                score,
                questionList.size(),
                percent,
                totalTimeString,
                timeUsed,
                difficulty,
                date
        );

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", questionList.size());
        intent.putExtra("percent", percent);
        intent.putExtra("time_used", timeUsed);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }
}
