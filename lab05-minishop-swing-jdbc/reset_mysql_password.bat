@echo off
echo ---------------------------------------------------------
echo     CHUONG TRINH RESET MAT KHAU MYSQL (QUYEN ADMIN)
echo ---------------------------------------------------------
echo.

echo 1. Dang tat dich vu MySQL84...
net stop MySQL84

echo.
echo 2. Tao file lenh de reset mat khau...
echo ALTER USER 'root'@'localhost' IDENTIFIED BY '123456'; > "C:\mysql_init.txt"

echo.
echo 3. Dang khoi dong MySQL de thuc thi lenh (Vui long doi 10 giay)...
start "Resetting MySQL" "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysqld.exe" --defaults-file="C:\ProgramData\MySQL\MySQL Server 8.4\my.ini" --init-file="C:\mysql_init.txt" --console

timeout /t 10 /nobreak >nul

echo.
echo 4. Tat tien trinh MySQL tam thoi...
taskkill /F /IM mysqld.exe >nul 2>&1

echo.
echo 5. Don dep file tam...
del "C:\mysql_init.txt" >nul 2>&1

echo.
echo 6. Khoi dong lai dich vu MySQL84 nhu binh thuong...
net start MySQL84

echo.
echo =========================================================
echo HOAN THANH! Mat khau cua ban da duoc doi thanh: 123456
echo Bay gio ban co the quay lai MySQL Workbench va nhap 123456
echo =========================================================
pause
