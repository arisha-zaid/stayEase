#!/bin/bash
# Mac/Linux run script
JAR=$(ls lib/mysql-connector*.jar 2>/dev/null | head -1)
if [ -z "$JAR" ]; then
    echo "[ERROR] MySQL connector JAR not found in lib/"
    exit 1
fi
java -cp "out:$JAR" main.Main
