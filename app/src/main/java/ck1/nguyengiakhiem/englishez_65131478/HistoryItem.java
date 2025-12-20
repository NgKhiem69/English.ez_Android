package ck1.nguyengiakhiem.englishez_65131478;

public class HistoryItem {

    private int score;
    private int total;
    private int percent;
    private String time;
    private String date;

    public HistoryItem(int score, int total, int percent, String time, String date) {
        this.score = score;
        this.total = total;
        this.percent = percent;
        this.time = time;
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }

    public int getPercent() {
        return percent;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
