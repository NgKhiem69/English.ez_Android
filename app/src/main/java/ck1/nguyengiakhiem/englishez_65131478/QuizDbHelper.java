package ck1.nguyengiakhiem.englishez_65131478;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EnglishQuiz.db";
    private static final int DATABASE_VERSION = 1;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE questions (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "question TEXT, option1 TEXT, option2 TEXT, " +
                "option3 TEXT, option4 TEXT, answer_nr INTEGER)";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable(db); // Thêm dữ liệu mẫu
    }

    private void fillQuestionsTable(SQLiteDatabase db) {
        Question q1 = new Question("What is the past tense of 'Go'?", "Goed", "Went", "Gone", "Going", 2);
        addQuestion(db, q1);
        // Thêm nhiều câu hỏi khác ở đây...
    }

    private void addQuestion(SQLiteDatabase db, Question question) {
        ContentValues cv = new ContentValues();
        cv.put("question", question.getQuestion()); // Bây giờ getQuestion() đã hoạt động
        cv.put("option1", question.getOption1());
        cv.put("option2", question.getOption2());
        cv.put("option3", question.getOption3());
        cv.put("option4", question.getOption4());
        cv.put("answer_nr", question.getAnswerNr());
        db.insert("questions", null, cv);
    }

    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM questions", null);
        if (c.moveToFirst()) {
            do {
                // Đọc dữ liệu từ Cursor và add vào list
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
