package lk.ijse.techlacomputer.to;

public class Income {
    private String incomeId;
    private int date;
    private int month;
    private int year;
    private double total;

    public String getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(String incomeId) {
        this.incomeId = incomeId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Income(String incomeId, int date, int month, int year, double total) {
        this.incomeId = incomeId;
        this.date = date;
        this.month = month;
        this.year = year;
        this.total = total;
    }

    public Income() {
    }
}
