@echo off
set JAR=
for %%f in (lib\mysql-connector*.jar) do set JAR=%%f
if "%JAR%"=="" (
    echo [ERROR] MySQL connector JAR not found in lib\
    pause
    exit /b 1
)
java -cp "out;%JAR%" main.Main
