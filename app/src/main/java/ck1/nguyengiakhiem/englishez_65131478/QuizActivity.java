package ck1.nguyengiakhiem.englishez_65131478;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity {

    // UI
    TextView tvTimer, tvQuestionNumber, tvQuestion;
    TextView tvA, tvB, tvC, tvD;
    Button btnNext, btnPrev;
    CardView cardA, cardB, cardC, cardD;

    // Màu sắc
    int colorSelected = android.graphics.Color.parseColor("#86FF7E");
    int colorDefault = android.graphics.Color.WHITE;

    // Data
    ArrayList<Question> questionList;
    int currentIndex = 0;
    HashMap<Integer, Integer> userAnswers = new HashMap<>();

    // Timer
    CountDownTimer countDownTimer;
    long totalTime = 75 * 60 * 1000; // 75 phút
    long remainingTime = totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Ánh xạ
        tvTimer = findViewById(R.id.tv_timer);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvQuestion = findViewById(R.id.tv_question);

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

        // Load DB
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();

        startTimer();
        loadQuestion();

        // Click chọn đáp án
        cardA.setOnClickListener(v -> handleSelect(1, cardA));
        cardB.setOnClickListener(v -> handleSelect(2, cardB));
        cardC.setOnClickListener(v -> handleSelect(3, cardC));
        cardD.setOnClickListener(v -> handleSelect(4, cardD));

        btnNext.setOnClickListener(v -> {
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

    // ================= CHỌN ĐÁP ÁN =================
    private void handleSelect(int optionNumber, CardView selectedCard) {
        Integer lastAnswer = userAnswers.get(currentIndex);

        if (lastAnswer != null && lastAnswer == optionNumber) {
            // Nhấn lần 2 -> bỏ chọn
            userAnswers.remove(currentIndex);
            resetCardColors();
        } else {
            userAnswers.put(currentIndex, optionNumber);
            resetCardColors();
            selectedCard.setCardBackgroundColor(colorSelected);
        }
    }

    private void resetCardColors() {
        cardA.setCardBackgroundColor(colorDefault);
        cardB.setCardBackgroundColor(colorDefault);
        cardC.setCardBackgroundColor(colorDefault);
        cardD.setCardBackgroundColor(colorDefault);
    }

    // ================= TIMER =================
    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;

                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                tvTimer.setText(String.format("Time: %02d:%02d", minutes, seconds));
            }


            @Override
            public void onFinish() {
                finishQuiz();
            }
        }.start();
    }

    // ================= LOAD QUESTION =================
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

        // Khôi phục đáp án đã chọn
        if (userAnswers.containsKey(currentIndex)) {
            int savedOption = userAnswers.get(currentIndex);
            if (savedOption == 1) cardA.setCardBackgroundColor(colorSelected);
            else if (savedOption == 2) cardB.setCardBackgroundColor(colorSelected);
            else if (savedOption == 3) cardC.setCardBackgroundColor(colorSelected);
            else if (savedOption == 4) cardD.setCardBackgroundColor(colorSelected);
        }

        btnPrev.setEnabled(currentIndex != 0);
    }

    // ================= FINISH QUIZ =================
    private void finishQuiz() {

        if (countDownTimer != null) countDownTimer.cancel();

        if (questionList == null || questionList.isEmpty()) {
            Toast.makeText(this, "No questions found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int score = 0;
        for (int i = 0; i < questionList.size(); i++) {
            if (userAnswers.containsKey(i)) {
                if (userAnswers.get(i) == questionList.get(i).getCorrectAnswer()) {
                    score++;
                }
            }
        }

        int percent = (int) ((score * 100.0f) / questionList.size());

        // ===== TÍNH THỜI GIAN ĐÃ LÀM =====
        int usedSeconds = (int) ((totalTime - remainingTime) / 1000);
        int usedMinutes = usedSeconds / 60;
        int usedRemainSeconds = usedSeconds % 60;

        String timeUsed = String.format(
                "%02d:%02d",
                usedMinutes,
                usedRemainSeconds
        );

        String date = new java.text.SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                java.util.Locale.getDefault()
        ).format(new java.util.Date());

        // ===== LƯU HISTORY =====
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        dbHelper.insertHistory(
                score,
                questionList.size(),
                percent,
                timeUsed,
                date
        );

        // ===== GỬI SANG RESULT =====
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", questionList.size());
        intent.putExtra("percent", percent);
        intent.putExtra("time_used", timeUsed);
        startActivity(intent);
        finish();
    }

}
