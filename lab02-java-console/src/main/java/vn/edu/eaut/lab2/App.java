package vn.edu.eaut.lab2;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Nhập dữ liệu sinh viên:");
        System.out.print("Nhập mã sinh viên: ");
        String id = scanner.nextLine();

        System.out.print("Nhập họ tên: ");
        String name = scanner.nextLine();

        double attendanceScore = getValidScore(scanner, "điểm chuyên cần");
        double midtermScore = getValidScore(scanner, "điểm giữa kỳ");
        double finalScore = getValidScore(scanner, "điểm cuối kỳ");

        Student student = new Student(id, name, attendanceScore, midtermScore, finalScore);
        GradeCalculator.calculateAndSetGrade(student);

        System.out.println("\nKết quả:");
        System.out.printf("%s - %s - %.2f - %s\n", 
            student.getId(), 
            student.getName(), 
            student.getTotalScore(), 
            student.getGrade());
            
        scanner.close();
    }

    private static double getValidScore(Scanner scanner, String scoreName) {
        double score = -1;
        while (true) {
            System.out.print("Nhập " + scoreName + ": ");
            try {
                score = Double.parseDouble(scanner.nextLine());
                if (score >= 0 && score <= 10) {
                    break;
                } else {
                    System.out.println("Điểm không hợp lệ! Vui lòng nhập lại (0-10).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Đầu vào không hợp lệ! Vui lòng nhập một số (0-10).");
            }
        }
        return score;
    }
}
