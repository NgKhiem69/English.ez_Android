package ck1.nguyengiakhiem.englishez_65131478;

public class Question {
    private int id;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int correctAnswer; // 1-4
    private String difficulty;

    public Question() {}

    public Question(String question, String a, String b, String c, String d, int correctAnswer, String difficulty) {
        this.question = question;
        this.optionA = a;
        this.optionB = b;
        this.optionC = c;
        this.optionD = d;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;
    }

    // GETTER
    public String getQuestion() { return question; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public int getCorrectAnswer() { return correctAnswer; }
    public String getDifficulty() { return difficulty; }
}
