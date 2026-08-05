package vn.edu.eaut.lab2;

public class GradeCalculator {

    public static void calculateAndSetGrade(Student student) {
        double total = student.getAttendanceScore() * 0.1 
                     + student.getMidtermScore() * 0.3 
                     + student.getFinalScore() * 0.6;
        student.setTotalScore(total);

        String grade;
        if (total >= 8.5) {
            grade = "A";
        } else if (total >= 7.0) {
            grade = "B";
        } else if (total >= 5.5) {
            grade = "C";
        } else if (total >= 4.0) {
            grade = "D";
        } else {
            grade = "F";
        }
        student.setGrade(grade);
    }
}
