package ck1.nguyengiakhiem.englishez_65131478;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView tvTimeUsed;
    TextView tvScore, tvResult, tvStatus;
    Button btnRetry, btnHome;
    TextView tvDifficulty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScore = findViewById(R.id.tv_score);
        tvResult = findViewById(R.id.tv_result);
        tvStatus = findViewById(R.id.tv_status);
        tvTimeUsed = findViewById(R.id.tv_time_used);
        btnRetry = findViewById(R.id.btn_retry);
        btnHome = findViewById(R.id.btn_home);
        tvDifficulty = findViewById(R.id.tv_difficulty);


        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        int percent = (int) ((score * 100.0f) / total);

        tvScore.setText(score + " / " + total);
        tvResult.setText("Score: " + percent + "%");

        String timeUsed = getIntent().getStringExtra("time_used");
        if (timeUsed != null) {
            tvTimeUsed.setText("Time used: " + timeUsed);
        }

        if (percent >= 50) {
            tvStatus.setText("PASSED");
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvStatus.setText("FAILED");
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        String difficulty = getIntent().getStringExtra("difficulty");

        if (difficulty != null) {
            tvDifficulty.setText("Difficulty: " + difficulty.toUpperCase());
        }

        btnRetry.setOnClickListener(v -> {
            startActivity(new Intent(this, QuizActivity.class));
            finish();
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
