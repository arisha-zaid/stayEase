@echo off
REM Windows compile script
REM Run from StayEase\ root folder

mkdir out 2>nul

set JAR=
for %%f in (lib\mysql-connector*.jar) do set JAR=%%f

if "%JAR%"=="" (
    echo [ERROR] MySQL connector JAR not found in lib\
    echo Download from: https://dev.mysql.com/downloads/connector/j/
    pause
    exit /b 1
)

echo Using: %JAR%
echo Compiling...

javac -cp "%JAR%" -d out ^
    src\db\DBConnection.java ^
    src\models\Admin.java ^
    src\models\Room.java ^
    src\models\Customer.java ^
    src\models\Booking.java ^
    src\dao\AdminDAO.java ^
    src\dao\RoomDAO.java ^
    src\dao\CustomerDAO.java ^
    src\dao\BookingDAO.java ^
    src\ui\BillGenerator.java ^
    src\ui\AdminMenu.java ^
    src\ui\MainMenu.java ^
    src\main\Main.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo.
    echo To run:
    echo   java -cp "out;%JAR%" main.Main
) else (
    echo Compilation failed.
)
pause
