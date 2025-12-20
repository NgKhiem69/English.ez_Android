package ck1.nguyengiakhiem.englishez_65131478;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    RadioGroup rgDifficulty;
    RadioButton rbEasy, rbMedium, rbHard;
    Spinner spTime;
    Button btnSave, btnClearHistory, btnHome;
    SharedPreferences prefs;
    boolean isChanged = false;
    String tempDifficulty;
    int tempTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("quiz_settings", MODE_PRIVATE);

        rgDifficulty = findViewById(R.id.rg_difficulty);
        rbEasy = findViewById(R.id.rb_easy);
        rbMedium = findViewById(R.id.rb_medium);
        rbHard = findViewById(R.id.rb_hard);
        spTime = findViewById(R.id.sp_time);
        btnSave = findViewById(R.id.btn_save);
        btnClearHistory = findViewById(R.id.btn_clear_history);
        btnHome = findViewById(R.id.btn_home);

        setupSpinner();
        loadSettings();


        rgDifficulty.setOnCheckedChangeListener((group, checkedId) -> {
            isChanged = true;
            if (checkedId == R.id.rb_easy) tempDifficulty = "easy";
            else if (checkedId == R.id.rb_medium) tempDifficulty = "medium";
            else tempDifficulty = "hard";
        });

        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                isChanged = true;
                tempTime = Integer.parseInt(parent.getItemAtPosition(pos).toString());
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSave.setOnClickListener(v -> saveSettings());

        btnHome.setOnClickListener(v -> checkBeforeExit());

        btnClearHistory.setOnClickListener(v -> clearHistory());

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        checkBeforeExit();
                    }
                });
    }

    // ================= LOAD =================
    private void loadSettings() {
        tempDifficulty = prefs.getString("difficulty", "easy");
        tempTime = prefs.getInt("time", 25);

        if (tempDifficulty.equals("easy")) rbEasy.setChecked(true);
        else if (tempDifficulty.equals("medium")) rbMedium.setChecked(true);
        else rbHard.setChecked(true);

        String[] times = {"5", "10", "15", "25"};
        for (int i = 0; i < times.length; i++) {
            if (Integer.parseInt(times[i]) == tempTime) {
                spTime.setSelection(i);
                break;
            }
        }
    }

    // ================= SAVE =================
    private void saveSettings() {
        prefs.edit()
                .putString("difficulty", tempDifficulty)
                .putInt("time", tempTime)
                .apply();

        isChanged = false;
        Toast.makeText(this, "Đã lưu cài đặt", Toast.LENGTH_SHORT).show();
    }

    // ================= EXIT CHECK =================
    private void checkBeforeExit() {
        if (!isChanged) {
            finish();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Lưu thay đổi?")
                .setMessage("Bạn có muốn lưu các thay đổi trước khi thoát?")
                .setPositiveButton("Lưu", (d, w) -> {
                    saveSettings();
                    finish();
                })
                .setNegativeButton("Không lưu", (d, w) -> finish())
                .setNeutralButton("Hủy", null)
                .show();
    }



    // ================= CLEAR HISTORY =================
    private void clearHistory() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa toàn bộ lịch sử làm bài?")
                .setPositiveButton("Xóa", (d, w) -> {
                    QuizDbHelper dbHelper = new QuizDbHelper(this);
                    dbHelper.clearHistory();
                    Toast.makeText(this, "Đã xóa lịch sử", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ================= SPINNER =================
    private void setupSpinner() {
        String[] times = {"5", "10", "15", "25"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, times
        );
        spTime.setAdapter(adapter);
    }
}
