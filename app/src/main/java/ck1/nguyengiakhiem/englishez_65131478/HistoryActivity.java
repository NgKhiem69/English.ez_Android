package ck1.nguyengiakhiem.englishez_65131478;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.list_history);

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        ArrayList<HistoryItem> historyList = dbHelper.getAllHistory();
        dbHelper.close();

        if (historyList.isEmpty()) {
            Toast.makeText(this, "No history yet", Toast.LENGTH_SHORT).show();
        }

        listView.setAdapter(new HistoryAdapter(this, historyList));
    }
}
