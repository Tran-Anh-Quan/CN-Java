package vn.edu.eaut;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        So so = new So();

        System.out.println("--- CÁC BÀI TOÁN ---");
        System.out.println("1. Tính tổng số chẵn");
        System.out.println("2. Tính tổng nghịch đảo");
        System.out.println("3. Kiểm tra số nguyên tố");
        System.out.println("4. Kiểm tra và phân loại tam giác");
        System.out.println("5. Hiển thị dãy Fibonacci");
        System.out.print("Chọn bài toán (1-5): ");
        
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.print("Nhập n nguyên dương: ");
                int n1 = scanner.nextInt();
                System.out.println("Tổng s = " + so.tongSoChan(n1));
                break;
            case 2:
                System.out.print("Nhập n nguyên dương: ");
                int n2 = scanner.nextInt();
                System.out.printf("Tổng s = %.4f\n", so.tongNghichDao(n2));
                break;
            case 3:
                System.out.print("Nhập số nguyên n: ");
                int n3 = scanner.nextInt();
                if (so.laSoNguyenTo(n3)) {
                    System.out.println("nguyên tố");
                } else {
                    System.out.println("không nguyên tố");
                }
                break;
            case 4:
                System.out.print("Nhập 3 số a, b, c: ");
                double a = scanner.nextDouble();
                double b = scanner.nextDouble();
                double c = scanner.nextDouble();
                System.out.println(so.loaiTamGiac(a, b, c));
                break;
            case 5:
                System.out.print("Nhập n nguyên dương: ");
                int n5 = scanner.nextInt();
                so.hienThiFibonacci(n5);
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
        
        scanner.close();
    }
}
