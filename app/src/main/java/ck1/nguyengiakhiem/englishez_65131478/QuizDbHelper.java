package ck1.nguyengiakhiem.englishez_65131478;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Quiz.db";
    private static final int DB_VERSION = 1;

    // ================= QUESTIONS =================
    public static final String TABLE_QUESTIONS = "questions";

    private static final String CREATE_TABLE_QUESTIONS =
            "CREATE TABLE " + TABLE_QUESTIONS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "question TEXT, " +
                    "a TEXT, " +
                    "b TEXT, " +
                    "c TEXT, " +
                    "d TEXT, " +
                    "answer INTEGER)";

    // ================= HISTORY =================
    public static final String TABLE_HISTORY = "history";
    public static final String COL_ID = "id";
    public static final String COL_SCORE = "score";
    public static final String COL_TOTAL = "total";
    public static final String COL_PERCENT = "percent";
    public static final String COL_TIME = "time";
    public static final String COL_DATE = "date";

    private static final String CREATE_TABLE_HISTORY =
            "CREATE TABLE " + TABLE_HISTORY + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_SCORE + " INTEGER, " +
                    COL_TOTAL + " INTEGER, " +
                    COL_PERCENT + " INTEGER, " +
                    COL_TIME + " TEXT, " +
                    COL_DATE + " TEXT)";

    public QuizDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // ================= CREATE DB =================
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTIONS);
        db.execSQL(CREATE_TABLE_HISTORY);
        insertQuestions(db);
    }

    // ================= INSERT QUESTIONS =================
    private void insertQuestions(SQLiteDatabase db) {

        addQuestion(db, new Question(
                "What is the capital of England?",
                "Paris", "London", "Rome", "Berlin", 2
        ));

        addQuestion(db, new Question(
                "She ___ to school every day.",
                "go", "goes", "going", "gone", 2
        ));

        // 👉 Thêm tiếp đến 100 câu ở đây
    }

    private void addQuestion(SQLiteDatabase db, Question q) {
        ContentValues cv = new ContentValues();
        cv.put("question", q.getQuestion());
        cv.put("a", q.getOptionA());
        cv.put("b", q.getOptionB());
        cv.put("c", q.getOptionC());
        cv.put("d", q.getOptionD());
        cv.put("answer", q.getCorrectAnswer());
        db.insert(TABLE_QUESTIONS, null, cv);
    }

    // ================= GET QUESTIONS =================
    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS, null);

        if (c.moveToFirst()) {
            do {
                list.add(new Question(
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getInt(6)
                ));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    // ================= INSERT HISTORY =================
    public void insertHistory(int score, int total, int percent, String time, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_SCORE, score);
        cv.put(COL_TOTAL, total);
        cv.put(COL_PERCENT, percent);
        cv.put(COL_TIME, time);
        cv.put(COL_DATE, date);

        db.insert(TABLE_HISTORY, null, cv);
        db.close();
    }

    // ================= GET HISTORY =================
    public ArrayList<HistoryItem> getAllHistory() {
        ArrayList<HistoryItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_HISTORY + " ORDER BY " + COL_ID + " DESC",
                null
        );

        if (c.moveToFirst()) {
            do {
                list.add(new HistoryItem(
                        c.getInt(1),
                        c.getInt(2),
                        c.getInt(3),
                        c.getString(4),
                        c.getString(5)
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }
}
