package ck1.nguyengiakhiem.englishez_65131478;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Quiz.db";
    private static final int DB_VERSION = 3; // tăng version khi test

    private Context context;

    public static final String COL_DIFFICULTY = "difficulty";


    // ================= QUESTIONS =================
    public static final String TABLE_QUESTIONS = "questions";

    private static final String CREATE_TABLE_QUESTIONS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "question TEXT, " +
                    "a TEXT, " +
                    "b TEXT, " +
                    "c TEXT, " +
                    "d TEXT, " +
                    "answer INTEGER, " +
                    "difficulty TEXT)";

    // ================= HISTORY =================
    public static final String TABLE_HISTORY = "history";

    private static final String CREATE_TABLE_HISTORY =
            "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "score INTEGER, " +
                    "total INTEGER, " +
                    "percent INTEGER, " +
                    "total_time TEXT, " +
                    "used_time TEXT, " +
                    "difficulty TEXT, " +
                    "date TEXT)";


    public QuizDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    // ================= CREATE DB =================
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTIONS);
        db.execSQL(CREATE_TABLE_HISTORY);

        if (isQuestionsEmpty(db)) {
            insertQuestionsFromJson(db);
        }
    }

    // ================= CHECK EMPTY =================
    private boolean isQuestionsEmpty(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_QUESTIONS, null);
        c.moveToFirst();
        boolean empty = c.getInt(0) == 0;
        c.close();
        return empty;
    }

    // ================= LOAD JSON =================
    private String loadJSONFromAsset() {
        try {
            InputStream is = context.getAssets().open("questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ================= INSERT FROM JSON =================
    private void insertQuestionsFromJson(SQLiteDatabase db) {
        try {
            String json = loadJSONFromAsset();
            if (json == null) return;

            JSONObject root = new JSONObject(json);
            JSONArray arr = root.getJSONArray("questions");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject q = arr.getJSONObject(i);

                ContentValues cv = new ContentValues();
                cv.put("question", q.getString("question"));
                cv.put("a", q.getString("a"));
                cv.put("b", q.getString("b"));
                cv.put("c", q.getString("c"));
                cv.put("d", q.getString("d"));
                cv.put("answer", q.getInt("answer"));
                cv.put("difficulty", q.getString("difficulty"));

                db.insert(TABLE_QUESTIONS, null, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= GET QUESTIONS BY DIFFICULTY =================
    public ArrayList<Question> getQuestionsByDifficulty(String difficulty) {
        ArrayList<Question> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_QUESTIONS + " WHERE difficulty = ?",
                new String[]{difficulty}
        );

        if (c.moveToFirst()) {
            do {
                list.add(new Question(
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getInt(6),
                        c.getString(7)
                ));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    // ================= HISTORY =================
    public void insertHistory(int score, int total, int percent, String totalTime, String usedTime, String difficulty, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("score", score);
        cv.put("total", total);
        cv.put("percent", percent);
        cv.put("total_time", totalTime);
        cv.put("used_time", usedTime);
        cv.put("difficulty", difficulty);
        cv.put("date", date);

        db.insert("history", null, cv);
        db.close();
    }



    // ================= GET HISTORY =================
    public ArrayList<HistoryItem> getAllHistory() {
        ArrayList<HistoryItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_HISTORY + " ORDER BY id DESC",
                null
        );

        if (c.moveToFirst()) {
            do {
                list.add(new HistoryItem(
                        c.getInt(1),    // score
                        c.getInt(2),    // total
                        c.getInt(3),    // percent
                        c.getString(4), // total_time
                        c.getString(5), // used_time
                        c.getString(6), // difficulty
                        c.getString(7)  // date
                ));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }
    // ================= UPGRADE =================
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_TABLE_QUESTIONS);
        db.execSQL(CREATE_TABLE_HISTORY);
    }
    public void clearHistory() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("history", null, null);
        db.close();
    }

}
