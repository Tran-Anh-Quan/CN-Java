package vn.edu.eaut.lab2;

public class Student {
    private String id;
    private String name;
    private double attendanceScore;
    private double midtermScore;
    private double finalScore;
    private double totalScore;
    private String grade;

    public Student(String id, String name, double attendanceScore, double midtermScore, double finalScore) {
        this.id = id;
        this.name = name;
        this.attendanceScore = attendanceScore;
        this.midtermScore = midtermScore;
        this.finalScore = finalScore;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getAttendanceScore() { return attendanceScore; }
    public double getMidtermScore() { return midtermScore; }
    public double getFinalScore() { return finalScore; }

    public double getTotalScore() { return totalScore; }
    public void setTotalScore(double totalScore) { this.totalScore = totalScore; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
