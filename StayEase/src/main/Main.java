package main;

import ui.MainMenu;
import java.util.Scanner;

/**
 * Main.java
 *
 * Entry point for the StayEase Hotel Management System.
 *
 * HOW TO COMPILE (from the StayEase/ root folder):
 * ─────────────────────────────────────────────────
 *   Windows:
 *     javac -cp "lib\mysql-connector-j-x.x.xx.jar" -d out
 *           src\db\*.java src\models\*.java src\dao\*.java src\ui\*.java src\main\Main.java
 *
 *   Mac / Linux:
 *     javac -cp "lib/mysql-connector-j-x.x.xx.jar" -d out
 *           src/db/*.java src/models/*.java src/dao/*.java src/ui/*.java src/main/Main.java
 *
 * HOW TO RUN:
 * ─────────────────────────────────────────────────
 *   Windows:
 *     java -cp "out;lib\mysql-connector-j-x.x.xx.jar" main.Main
 *
 *   Mac / Linux:
 *     java -cp "out:lib/mysql-connector-j-x.x.xx.jar" main.Main
 *
 * BEFORE RUNNING:
 *   1. Run stayease.sql in MySQL Workbench (or via terminal)
 *   2. Update DB_PASSWORD in src/db/DBConnection.java
 *   3. Put the MySQL JDBC .jar file in the lib/ folder
 */
public class Main {

    public static void main(String[] args) {

        // One Scanner shared across all menu classes
        // Important: Do NOT create multiple Scanners on System.in
        Scanner scanner = new Scanner(System.in);

        MainMenu mainMenu = new MainMenu(scanner);
        mainMenu.show();

        scanner.close();
        System.out.println(" System exited cleanly. Bye!");
    }
}
