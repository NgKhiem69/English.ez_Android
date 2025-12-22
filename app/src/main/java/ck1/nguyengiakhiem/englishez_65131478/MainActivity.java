package ck1.nguyengiakhiem.englishez_65131478;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardStart = findViewById(R.id.card_start);
        CardView cardHistory = findViewById(R.id.card_history);
        CardView cardSettings = findViewById(R.id.card_settings);
        CardView cardOut = findViewById(R.id.card_out);

        cardStart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class); startActivity(intent); });

        cardHistory.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        });

        cardSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
        cardOut.setOnClickListener(v -> {
            moveTaskToBack(true);
        });
    }
}
