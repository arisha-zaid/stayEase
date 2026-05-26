#!/bin/bash
# Mac/Linux compile script
# Run from StayEase/ root folder

mkdir -p out

# Find the JAR (handles any version number)
JAR=$(ls lib/mysql-connector*.jar 2>/dev/null | head -1)

if [ -z "$JAR" ]; then
    echo "[ERROR] MySQL connector JAR not found in lib/"
    echo "Download from: https://dev.mysql.com/downloads/connector/j/"
    exit 1
fi

echo "Using: $JAR"
echo "Compiling..."

javac -cp "$JAR" -d out \
    src/db/DBConnection.java \
    src/models/Admin.java \
    src/models/Room.java \
    src/models/Customer.java \
    src/models/Booking.java \
    src/dao/AdminDAO.java \
    src/dao/RoomDAO.java \
    src/dao/CustomerDAO.java \
    src/dao/BookingDAO.java \
    src/ui/BillGenerator.java \
    src/ui/AdminMenu.java \
    src/ui/MainMenu.java \
    src/main/Main.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "To run:"
    echo "  java -cp \"out:$JAR\" main.Main"
else
    echo "Compilation failed. Check error messages above."
fi
