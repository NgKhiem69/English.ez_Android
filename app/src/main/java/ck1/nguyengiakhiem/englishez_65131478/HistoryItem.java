package ck1.nguyengiakhiem.englishez_65131478;

public class HistoryItem {

    int score, total, percent;
    String totalTime;
    String usedTime;
    String difficulty;
    String date;

    public HistoryItem(
            int score,
            int total,
            int percent,
            String totalTime,
            String usedTime,
            String difficulty,
            String date
    ) {
        this.score = score;
        this.total = total;
        this.percent = percent;
        this.totalTime = totalTime;
        this.usedTime = usedTime;
        this.difficulty = difficulty;
        this.date = date;
    }

    public int getScore() { return score; }
    public int getTotal() { return total; }
    public int getPercent() { return percent; }
    public String getTotalTime() { return totalTime; }
    public String getUsedTime() { return usedTime; }
    public String getDifficulty() { return difficulty; }
    public String getDate() { return date; }
}

