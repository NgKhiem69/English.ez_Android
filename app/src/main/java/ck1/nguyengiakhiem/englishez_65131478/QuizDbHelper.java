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

    public QuizDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE questions (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "question TEXT," +
                        "a TEXT," +
                        "b TEXT," +
                        "c TEXT," +
                        "d TEXT," +
                        "answer INTEGER)"
        );

        insertQuestions(db);
    }

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
        db.insert("questions", null, cv);
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM questions", null);

        if (c.moveToFirst()) {
            do {
                Question q = new Question(
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getInt(6)
                );
                list.add(q);
            } while (c.moveToNext());
        }

        c.close();
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS questions");
        onCreate(db);
    }
}
