package vn.edu.eaut;

public class So {

    // Bài 1: Tính tổng số chẵn
    public int tongSoChan(int n) {
        int sum = 0;
        for (int i = 2; i <= n; i += 2) {
            sum += i;
        }
        return sum;
    }

    // Bài 2: Tính tổng nghịch đảo
    public double tongNghichDao(int n) {
        double sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += 1.0 / i;
        }
        return sum;
    }

    // Bài 3: Kiểm tra số nguyên tố
    public boolean laSoNguyenTo(int n) {
        if (n <= 1) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // Bài 4: Kiểm tra và phân loại tam giác
    public String loaiTamGiac(double a, double b, double c) {
        if (a + b <= c || a + c <= b || b + c <= a) {
            return "không phải tam giác";
        }
        if (a == b && b == c) {
            return "đều";
        }
        
        boolean isVuong = Math.abs(a * a + b * b - c * c) < 1e-9 ||
                          Math.abs(a * a + c * c - b * b) < 1e-9 ||
                          Math.abs(b * b + c * c - a * a) < 1e-9;
        
        if (a == b || b == c || a == c) {
            if (isVuong) {
                return "vuông cân";
            }
            return "cân";
        }
        if (isVuong) {
            return "vuông";
        }
        return "thường";
    }

    // Bài 5: Hiển thị dãy Fibonacci
    public void hienThiFibonacci(int n) {
        if (n <= 0) return;
        int t1 = 0, t2 = 1;
        System.out.print(t1);
        for (int i = 1; i < n; i++) {
            System.out.print(" " + t2);
            int sum = t1 + t2;
            t1 = t2;
            t2 = sum;
        }
        System.out.println();
    }
}
